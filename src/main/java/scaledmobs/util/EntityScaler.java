/*
 * Scaled Mobs Mod
 * Copyright (c) 2020 Ryan Sammons
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package scaledmobs.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scaledmobs.ScaledMobsMod;
import scaledmobs.capabilities.IScalableMobCapability;
import scaledmobs.capabilities.ScalableMobCapabilityProvider;
import scaledmobs.config.ConfigManager;
import scaledmobs.config.IEntityScaleSettings;
import scaledmobs.config.ShouldScaleEnum;
import scaledmobs.network.PacketHandler;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.UUID;

public class EntityScaler {
    private static final UUID MAX_HEALTH_MODIFIER_UUID = UUID.fromString("F5512A48-917F-C286-5E3F-A8249E5E4806");
    private static final UUID ATTACK_DAMAGE_MODIFIER_UUID = UUID.fromString("EF4C4D02-4290-4D13-BA86-1FFA6BED9E17");
    private static final UUID FOLLOW_RANGE_MODIFIER_UUID = UUID.fromString("ECF06227-862A-D3CE-97EC-9A7645FB368C");
    private static final Attribute MAX_HEALTH_ATTRIBUTE = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("minecraft", "generic.max_health"));
    private static final Attribute ATTACK_DAMAGE_ATTRIBUTE = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("minecraft", "generic.attack_damage"));
    private static final Attribute FOLLOW_RANGE_ATTRIBUTE = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("minecraft", "generic.follow_range"));
    private static final String ENTITY_SIZE_FIELD_SRG = "field_213325_aI";                   // net.minecraft.entity.Entity.size
    private static final String ENTITY_EYE_HEIGHT_FIELD_SRG = "field_213326_aJ";             // net.minecraft.entity.Entity.eyeHeight
    private static final String CREEPER_ENTITY_EXPLOSION_RADIUS_FIELD_SRG = "field_82226_g"; // net.minecraft.entity.monster.CreeperEntity.explosionRadius
    private static final Field ENTITY_SIZE_FIELD = ObfuscationReflectionHelper.findField(Entity.class, ENTITY_SIZE_FIELD_SRG);
    private static final Field ENTITY_EYE_HEIGHT_FIELD = ObfuscationReflectionHelper.findField(Entity.class, ENTITY_EYE_HEIGHT_FIELD_SRG);
    private static final Field CREEPER_ENTITY_EXPLOSION_RADIUS_FIELD = ObfuscationReflectionHelper.findField(CreeperEntity.class, CREEPER_ENTITY_EXPLOSION_RADIUS_FIELD_SRG);
    private static final Logger LOGGER = LogManager.getLogger();
    private static final HashMap<EntityType<?>, EnumMap<Pose, EntityBaseSize>> BASE_SIZE_CACHE = new HashMap<>();

    private static class EntityBaseSize {
        public final EntitySize baseSize;
        public final float baseEyeHeight;

        public EntityBaseSize(EntitySize baseSize, float baseEyeHeight) {
            this.baseSize = baseSize;
            this.baseEyeHeight = baseEyeHeight;
        }
    }

    public static boolean difficultyChanged = false;
    public static int difficultyChangedTimeout = 0;

    public static float getEntityScale(@Nonnull LivingEntity entity) {
        IScalableMobCapability scalableMobCapability = entity.getCapability(ScalableMobCapabilityProvider.scalableMobCapability).orElse(null);
        if (scalableMobCapability != null) {
            return scalableMobCapability.getScale();
        }
        return 1.0f;
    }

    public static void setEntityScale(@Nonnull LivingEntity entity, float scale) {
        IScalableMobCapability scalableMobCapability = entity.getCapability(ScalableMobCapabilityProvider.scalableMobCapability).orElse(null);
        if (scalableMobCapability != null) {
            scalableMobCapability.setScale(scale);
            recalculateEntitySize(entity, scale);
            updateEntityAttributes(entity, scale);
        }
    }

    public static void updateIfIncorrect(@Nonnull LivingEntity entity) {
        IScalableMobCapability scalableMobCapability = entity.getCapability(ScalableMobCapabilityProvider.scalableMobCapability).orElse(null);
        if (scalableMobCapability != null) {
            // Check to make sure the entity's current scale is correct
            EntitySize currentEntitySize;
            try {
                currentEntitySize = ((EntitySize) ENTITY_SIZE_FIELD.get(entity));
            }
            catch (IllegalAccessException illegalAccessException) {
                RuntimeException exception = new RuntimeException("Failed to get fields on " + entity + ": " + illegalAccessException.getMessage());
                exception.setStackTrace(illegalAccessException.getStackTrace());
                throw exception;
            }
            EntityBaseSize entityBaseSize = getBaseSize(entity);
            float scale = scalableMobCapability.getScale();
            EntitySize correctEntitySize = entityBaseSize.baseSize.scale(scale);
            if (currentEntitySize.width != correctEntitySize.width || currentEntitySize.height != correctEntitySize.height) {
                recalculateEntitySize(entity, scale);
                updateEntityAttributes(entity, scale);
            }
            else if (difficultyChanged) {
                updateEntityAttributes(entity, scale); // Apply attribute settings for new difficulty
            }
        }
    }

    private static EntityBaseSize getBaseSize(@Nonnull LivingEntity entity) {
        // Get the base size for this entity type and pose
        // Find a cached entity type, or create one if it doesn't exist
        EntityType<?> entityType = entity.getType();
        EnumMap<Pose, EntityBaseSize> entityBaseSizeMap = BASE_SIZE_CACHE.get(entityType);
        if (entityBaseSizeMap == null) {
            entityBaseSizeMap = new EnumMap<>(Pose.class);
            BASE_SIZE_CACHE.put(entityType, entityBaseSizeMap);
        }
        // Find a cached pose, or create one if it doesn't exist
        Pose entityPose = entity.getPose();
        boolean isChild = entity.isChild();
        EntityBaseSize entityBaseSize = entityBaseSizeMap.get(entityPose);
        if (entityBaseSize == null) {
            if (entityPose != Pose.SLEEPING) {
                entityBaseSize = new EntityBaseSize(
                        !isChild ? entity.getSize(entity.getPose()) : entity.getSize(entity.getPose()).scale(2.0f), // Children are x0.5 the size of adults
                        !isChild ? entity.getEyeHeight() : entity.getEyeHeight() * 2.0f // Same thing here
                );
            }
            else {
                entityBaseSize = new EntityBaseSize(entity.getSize(entity.getPose()), entity.getEyeHeight()); // Sleeping size is the same regardless of adult or child
            }
            entityBaseSizeMap.put(entityPose, entityBaseSize);
        }

        return entityBaseSize;
    }

    private static void recalculateEntitySize(@Nonnull LivingEntity entity, float scale) {
        // Scale the base
        EntityBaseSize entityBaseSize = getBaseSize(entity);
        Pose entityPose = entity.getPose();
        boolean isChild = entity.isChild();
        EntitySize entityScaledSize = entityPose != Pose.SLEEPING ?                                             // Don't scale size when sleeping since it might break sleeping mobs
                !isChild ? entityBaseSize.baseSize.scale(scale) : entityBaseSize.baseSize.scale(scale * 0.5f) : // Children are x0.5 the size of adults
                entityBaseSize.baseSize;                                                                        // Children don't scale down when sleeping
        float entityScaledEyeHeight = entityPose != Pose.SLEEPING ?
                !isChild ? entityBaseSize.baseEyeHeight * scale : entityBaseSize.baseEyeHeight * scale * 0.5f :
                entityBaseSize.baseEyeHeight;

        // Set the size fields and bounding box for this entity
        try {
            ENTITY_SIZE_FIELD.set(entity, entityScaledSize);
            ENTITY_EYE_HEIGHT_FIELD.set(entity, entityScaledEyeHeight);
        }
        catch (IllegalAccessException illegalAccessException) {
            RuntimeException exception = new RuntimeException("Failed to set fields on " + entity + ": " + illegalAccessException.getMessage());
            exception.setStackTrace(illegalAccessException.getStackTrace());
            throw exception;
        }
        double centerWidthOffset = (double)entity.getWidth() / 2.0,
               entityX = entity.getPosX(),
               entityY = entity.getPosY(),
               entityZ = entity.getPosZ();
        entity.setBoundingBox(new AxisAlignedBB(entityX - centerWidthOffset, entityY, entityZ - centerWidthOffset,
                entityX + centerWidthOffset, entityY + (double)entity.getHeight(), entityZ + centerWidthOffset));

        // Notify clients of the change (Server Only)
        if (entity.getEntityWorld() instanceof ServerWorld)
            PacketHandler.notifyScaleChange(entity, scale);
    }

    private static void updateEntityAttributes(@Nonnull LivingEntity entity, float scale) {
        // Get this entity's attribute settings
        IEntityScaleSettings entityScaleSettings = ConfigManager.getConfig(entity.getEntityWorld()).getEntityScaleSettings(entity);
        IWorld world = entity.getEntityWorld();
        if (entityScaleSettings.getShouldScale(world) != ShouldScaleEnum.FALSE) {
            ShouldScaleEnum shouldScale = entityScaleSettings.getShouldScale(world);
            boolean scaleHealth = entityScaleSettings.getShouldScaleHealth(world);
            boolean scaleAttack = entityScaleSettings.getShouldScaleAttack(world);
            boolean scaleView = entityScaleSettings.getShouldScaleView(world);

            // Apply attribute modifiers
            switch (shouldScale) {
            case RANDOM:
                // Calculate multipliers
                float healthMultiplier = 1.0f;
                float attackMultiplier = 1.0f;
                float viewMultiplier = 1.0f;
                if (scale >= 1.0f) {
                    float maxScale = entityScaleSettings.getMaxGrow(world);
                    float multiplyPercent = scale / maxScale;
                    if (scaleHealth) {
                        float maxHealthMultiplier = entityScaleSettings.getMaxHealthMultiplier(world);
                        healthMultiplier = multiplyPercent * maxHealthMultiplier;
                    }
                    if (scaleAttack) {
                        float maxAttackMultiplier = entityScaleSettings.getMaxAttackMultiplier(world);
                        attackMultiplier = multiplyPercent * maxAttackMultiplier;
                    }
                    if (scaleView) {
                        float maxViewMultiplier = entityScaleSettings.getMaxViewMultiplier(world);
                        viewMultiplier = multiplyPercent * maxViewMultiplier;
                    }
                }
                else {
                    float minScale = 1 / entityScaleSettings.getMaxShrink(world);
                    float multiplyPercent =  scale / (1 - minScale);
                    if (scaleHealth) {
                        float minHealthMultiplier = entityScaleSettings.getMinHealthMultiplier(world);
                        healthMultiplier = multiplyPercent * (1 - minHealthMultiplier);
                    }
                    if (scaleAttack) {
                        float minAttackMultiplier = entityScaleSettings.getMinAttackMultiplier(world);
                        attackMultiplier = multiplyPercent * (1 - minAttackMultiplier);
                    }
                    if (scaleView) {
                        float minViewMultiplier = entityScaleSettings.getMinViewMultiplier(world);
                        viewMultiplier = multiplyPercent * (1 - minViewMultiplier);
                    }
                }

                // Apply multipliers
                if (scaleHealth) {
                    assert MAX_HEALTH_ATTRIBUTE != null;
                    ModifiableAttributeInstance maxHealthAttribute = entity.getAttribute(MAX_HEALTH_ATTRIBUTE);
                    if (maxHealthAttribute != null) {
                        // Get the current health value of the mob
                        float health = entity.getHealth() / entity.getMaxHealth();

                        // Scale the mob's max health
                        maxHealthAttribute.removeModifier(MAX_HEALTH_MODIFIER_UUID);
                        maxHealthAttribute.func_233767_b_( // Apply the attribute modifier with visibility to NBT data (to preserve health value)
                                new AttributeModifier(
                                    MAX_HEALTH_MODIFIER_UUID,
                                    "Scaled Mobs Mod Max Health Multiplier",
                                    // Minecraft multiplies the attribute by this then adds
                                    // that to the total value, so we need to subtract 1
                                    healthMultiplier - 1.0f,
                                    AttributeModifier.Operation.MULTIPLY_BASE
                                )
                        );

                        // Scale the mob's current health value
                        entity.setHealth(health * entity.getMaxHealth());
                    }
                    else
                        LOGGER.warn(ScaledMobsMod.SCALEDMOBS_MARKER, "Entity " + entity.getType().getName().getString() + " doesn't seem to have a minecraft:generic.max_health attribute, which is extremely unusual and almost certainly an error");
                }
                if (scaleAttack) {
                    // The meaning of scaleAttack is context-dependent
                    if (entity instanceof CreeperEntity) {
                        // Scale the creeper's blast radius. We could use NBT, but
                        // it's faster to directly modify fields than process strings
                        try {
                            CREEPER_ENTITY_EXPLOSION_RADIUS_FIELD.set(entity, Math.round(attackMultiplier * 3.0f));
                        } catch (IllegalAccessException illegalAccessException) {
                            RuntimeException exception = new RuntimeException("Failed to set fields on " + entity + ": " + illegalAccessException.getMessage());
                            exception.setStackTrace(illegalAccessException.getStackTrace());
                            throw exception;
                        }
                    } else {
                        assert ATTACK_DAMAGE_ATTRIBUTE != null;
                        ModifiableAttributeInstance attackDamageAttribute = entity.getAttribute(ATTACK_DAMAGE_ATTRIBUTE);
                        if (attackDamageAttribute != null) {
                            attackDamageAttribute.removeModifier(ATTACK_DAMAGE_MODIFIER_UUID);
                            attackDamageAttribute.func_233767_b_( // Apply the attribute modifier with invisibility to NBT data
                                    new AttributeModifier(
                                            ATTACK_DAMAGE_MODIFIER_UUID,
                                            "Scaled Mobs Mod Attack Damage Multiplier",
                                            // Minecraft multiplies the attribute by this then adds
                                            // that to the total value, so we need to subtract 1
                                            attackMultiplier - 1.0f,
                                            AttributeModifier.Operation.MULTIPLY_BASE
                                    )
                            );
                        }
                    }
                }
                if (scaleView) {
                    assert FOLLOW_RANGE_ATTRIBUTE != null;
                    ModifiableAttributeInstance followRangeAttribute = entity.getAttribute(FOLLOW_RANGE_ATTRIBUTE);
                    if (followRangeAttribute != null) {
                        followRangeAttribute.removeModifier(FOLLOW_RANGE_MODIFIER_UUID);
                        followRangeAttribute.func_233767_b_( // Apply the attribute modifier with invisibility to NBT data
                                new AttributeModifier(
                                        FOLLOW_RANGE_MODIFIER_UUID,
                                        "Scaled Mobs Mod View Distance Multiplier",
                                        // Minecraft multiplies the attribute by this then adds
                                        // that to the total value, so we need to subtract 1
                                        viewMultiplier - 1.0f,
                                        AttributeModifier.Operation.MULTIPLY_BASE
                                )
                        );
                    }
                }
                break;
            case FIXED:
                // The multiplier will just be the scale in this case
                if (scaleHealth) {
                    assert MAX_HEALTH_ATTRIBUTE != null;
                    ModifiableAttributeInstance maxHealthAttribute = entity.getAttribute(MAX_HEALTH_ATTRIBUTE);
                    if (maxHealthAttribute != null) {
                        // Get the current health value of the mob
                        float health = entity.getHealth() / entity.getMaxHealth();

                        // Scale the mob's max health
                        maxHealthAttribute.removeModifier(MAX_HEALTH_MODIFIER_UUID);
                        maxHealthAttribute.func_233767_b_( // Apply the attribute modifier with visibility to NBT data (to preserve health value)
                                new AttributeModifier(
                                        MAX_HEALTH_MODIFIER_UUID,
                                        "Scaled Mobs Mod Max Health Multiplier",
                                        // Minecraft multiplies the attribute by this then adds
                                        // that to the total value, so we need to subtract 1
                                        scale - 1.0f,
                                        AttributeModifier.Operation.MULTIPLY_BASE
                                )
                        );

                        // Scale the mob's current health value
                        entity.setHealth(health * entity.getMaxHealth());
                    }
                    else
                        LOGGER.warn(ScaledMobsMod.SCALEDMOBS_MARKER, "Entity " + entity.getType().getName().getString() + " doesn't seem to have a minecraft:generic.max_health attribute, which is extremely unusual and almost certainly an error");
                }
                if (scaleAttack) {
                    // The meaning of scaleAttack is context-dependent
                    if (entity instanceof CreeperEntity) {
                        // Scale the creeper's blast radius. We could use NBT, but
                        // it's faster to directly modify fields than process strings
                        try {
                            CREEPER_ENTITY_EXPLOSION_RADIUS_FIELD.set(entity, Math.round(scale * 3.0f));
                        } catch (IllegalAccessException illegalAccessException) {
                            RuntimeException exception = new RuntimeException("Failed to set fields on " + entity + ": " + illegalAccessException.getMessage());
                            exception.setStackTrace(illegalAccessException.getStackTrace());
                            throw exception;
                        }
                    } else {
                        assert ATTACK_DAMAGE_ATTRIBUTE != null;
                        ModifiableAttributeInstance attackDamageAttribute = entity.getAttribute(ATTACK_DAMAGE_ATTRIBUTE);
                        if (attackDamageAttribute != null) {
                            attackDamageAttribute.removeModifier(ATTACK_DAMAGE_MODIFIER_UUID);
                            attackDamageAttribute.func_233767_b_( // Apply the attribute modifier with invisibility to NBT data
                                    new AttributeModifier(
                                            ATTACK_DAMAGE_MODIFIER_UUID,
                                            "Scaled Mobs Mod Attack Damage Multiplier",
                                            // Minecraft multiplies the attribute by this then adds
                                            // that to the total value, so we need to subtract 1
                                            scale - 1.0f,
                                            AttributeModifier.Operation.MULTIPLY_BASE
                                    )
                            );
                        }
                    }
                }
                if (scaleView) {
                    assert FOLLOW_RANGE_ATTRIBUTE != null;
                    ModifiableAttributeInstance followRangeAttribute = entity.getAttribute(FOLLOW_RANGE_ATTRIBUTE);
                    if (followRangeAttribute != null) {
                        followRangeAttribute.removeModifier(FOLLOW_RANGE_MODIFIER_UUID);
                        followRangeAttribute.func_233767_b_( // Apply the attribute modifier with invisibility to NBT data
                                new AttributeModifier(
                                        FOLLOW_RANGE_MODIFIER_UUID,
                                        "Scaled Mobs Mod View Distance Multiplier",
                                        // Minecraft multiplies the attribute by this then adds
                                        // that to the total value, so we need to subtract 1
                                        scale - 1.0f,
                                        AttributeModifier.Operation.MULTIPLY_BASE
                                )
                        );
                    }
                }
                break;
            }
        }
    }
}

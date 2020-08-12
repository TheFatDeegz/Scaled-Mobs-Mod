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

package scaledmobs.config;

import com.google.gson.Gson;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scaledmobs.ScaledMobsMod;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;

public class Config implements EntityClassConstants {
    public static class SpawnConfig {
        public final boolean scaleNaturallySpawned;
        public final boolean scaleTriggerSpawned;
        public final boolean scaleArtificiallySpawned;
        public final boolean scaleManuallySpawned;
        public final boolean scalePhantoms;

        public SpawnConfig(
                boolean scaleNaturallySpawned,
                boolean scaleTriggerSpawned,
                boolean scaleArtificiallySpawned,
                boolean scaleManuallySpawned,
                boolean scalePhantoms
        ) {
            this.scaleNaturallySpawned = scaleNaturallySpawned;
            this.scaleTriggerSpawned = scaleTriggerSpawned;
            this.scaleArtificiallySpawned = scaleArtificiallySpawned;
            this.scaleManuallySpawned = scaleManuallySpawned;
            this.scalePhantoms = scalePhantoms;
        }
    }

    private static final Logger LOGGER = LogManager.getLogger();

    private final char[] serializedConfig;
    private final EntityType<?>[] blacklistedEntities;
    private final HashMap<EntityType<?>, IEntityScaleSettings> entitySettings = new HashMap<>();

    public final SpawnConfig spawnConfig;
    public final IEntityScaleSettings defaultSettings;

    public Config(char[] configFile) {
        serializedConfig = configFile.clone();

        // Deserialize the config file
        Gson gson = new Gson();
        ConfigJson configObject = gson.fromJson(new String(configFile), ConfigJson.class);

        // Load spawnConfig
        if (configObject.spawnConfig != null) {
            spawnConfig = new SpawnConfig(
                    configObject.spawnConfig.scaleNaturallySpawned,
                    configObject.spawnConfig.scaleTriggerSpawned,
                    configObject.spawnConfig.scaleArtificiallySpawned,
                    configObject.spawnConfig.scaleManuallySpawned,
                    configObject.spawnConfig.scalePhantoms
            );
        }
        else {
            throw new NullPointerException("spawnConfig was not provided in config file");
        }

        // Load blacklistedEntities
        IForgeRegistry<EntityType<?>> entityRegistry = RegistryManager.ACTIVE.getRegistry(EntityType.class);
        if (configObject.blacklistedEntities != null) {
            ArrayList<EntityType<?>> blacklistedEntities = new ArrayList<>();
            for (String blacklistedEntityId : configObject.blacklistedEntities) {
                EntityType<?> blacklistedEntity = entityRegistry.getValue(new ResourceLocation(blacklistedEntityId));
                if (blacklistedEntity != null)
                    blacklistedEntities.add(blacklistedEntity);
                else
                    LOGGER.warn(ScaledMobsMod.SCALEDMOBS_MARKER, "Couldn't find entity \"{}\". Make sure that it's spelled correctly and the mod it's a part of is loaded if it's from a mod.", blacklistedEntityId);
            }
            this.blacklistedEntities = blacklistedEntities.size() > 0 ? (EntityType<?>[])blacklistedEntities.toArray() : new EntityType<?>[0];
            LOGGER.info(ScaledMobsMod.SCALEDMOBS_MARKER, "Loaded {} custom blacklisted entities", this.blacklistedEntities.length);
        }
        else {
            this.blacklistedEntities = new EntityType<?>[0];
        }

        // Load defaultSettings
        if (configObject.defaultSettings != null) {
            defaultSettings = new DefaultEntityScaleSettings(
                    ShouldScaleEnum.fromString(configObject.defaultSettings.shouldScale),
                    configObject.defaultSettings
            );
        }
        else {
            defaultSettings = DefaultEntityScaleSettings.DEFAULT_SCALE_SETTINGS;
        }

        // Load class settings
        if (configObject.classSettings != null) {
            // Passive (excluding Passive when Peaceful)
            if (configObject.classSettings.passive != null) {
                ConfigJson.PassiveSettings passiveSettings = configObject.classSettings.passive;
                // Creatures
                if (passiveSettings.creatures != null) {
                    loadClass(passiveSettings.creatures, PASSIVE_CREATURES);
                }
                // Golems
                if (passiveSettings.golems != null) {
                    loadClass(passiveSettings.golems, PASSIVE_GOLEMS);
                }
                // Rideable
                if (passiveSettings.rideable != null) {
                    loadClass(passiveSettings.rideable, PASSIVE_RIDEABLE);
                }
                // Tameable
                if (passiveSettings.tameable != null) {
                    loadClass(passiveSettings.tameable, PASSIVE_TAMEABLE);
                }
                // Villagers
                if (passiveSettings.villagers != null) {
                    loadClass(passiveSettings.villagers, PASSIVE_VILLAGERS);
                }
                // Class Defaults
                loadClass(passiveSettings, PASSIVE);
            }
            // Neutral
            if (configObject.classSettings.neutral != null) {
                ConfigJson.NeutralSettings neutralSettings = configObject.classSettings.neutral;
                // Arthropods
                if (neutralSettings.arthropods != null) {
                    loadClass(neutralSettings.arthropods, NEUTRAL_ARTHROPODS);
                }
                // Creatures
                if (neutralSettings.creatures != null) {
                    loadClass(neutralSettings.creatures, NEUTRAL_CREATURES);
                }
                // Golems
                if (neutralSettings.golems != null) {
                    loadClass(neutralSettings.golems, NEUTRAL_GOLEMS);
                }
                // Rideable
                if (neutralSettings.rideable != null) {
                    loadClass(neutralSettings.rideable, NEUTRAL_RIDEABLE);
                }
                // Tameable
                if (neutralSettings.tameable != null) {
                    loadClass(neutralSettings.tameable, NEUTRAL_TAMEABLE);
                }
                // Class Defaults
                loadClass(neutralSettings, NEUTRAL);
            }
            // Hostile
            if (configObject.classSettings.hostile != null) {
                ConfigJson.HostileSettings hostileSettings = configObject.classSettings.hostile;
                // Arthropods
                if (hostileSettings.arthropods != null) {
                    loadClass(hostileSettings.arthropods, HOSTILE_ARTHROPODS);
                }
                // End
                if (hostileSettings.end != null) {
                    loadClass(hostileSettings.end, HOSTILE_END);
                }
                // Guardians
                if (hostileSettings.guardians != null) {
                    loadClass(hostileSettings.guardians, HOSTILE_GUARDIANS);
                }
                // Illagers
                if (hostileSettings.illagers != null) {
                    loadClass(hostileSettings.illagers, HOSTILE_ILLAGERS);
                }
                // Nether
                if (hostileSettings.nether != null) {
                    loadClass(hostileSettings.nether, HOSTILE_NETHER);
                }
                // Overworld
                if (hostileSettings.overworld != null) {
                    loadClass(hostileSettings.overworld, HOSTILE_OVERWORLD);
                }
                // Skeletons
                if (hostileSettings.skeletons != null) {
                    loadClass(hostileSettings.skeletons, HOSTILE_SKELETONS);
                }
                // Zombies
                if (hostileSettings.zombies != null) {
                    loadClass(hostileSettings.zombies, HOSTILE_ZOMBIES);
                }
                // Class Defaults
                loadClass(hostileSettings, HOSTILE);
            }
            // Passive when Peaceful
            if (configObject.classSettings.passive != null) {
                ConfigJson.PassiveSettings passiveSettings = configObject.classSettings.passive;
                // Creatures
                if (passiveSettings.creatures != null) {
                    loadPeacefulPassiveClass(passiveSettings.creatures, PASSIVE_WHEN_PEACEFUL_CREATURES);
                }
                // Golems
                if (passiveSettings.golems != null) {
                    loadPeacefulPassiveClass(passiveSettings.golems, PASSIVE_WHEN_PEACEFUL_GOLEMS);
                }
                // Nether
                if (passiveSettings.nether != null) {
                    loadPeacefulPassiveClass(passiveSettings.nether, PASSIVE_WHEN_PEACEFUL_NETHER);
                }
                // Tameable
                if (passiveSettings.tameable != null) {
                    loadPeacefulPassiveClass(passiveSettings.tameable, PASSIVE_WHEN_PEACEFUL_TAMEABLE);
                }
                // Class Defaults
                loadPeacefulPassiveClass(passiveSettings, PASSIVE_WHEN_PEACEFUL);
            }
        }

        // Load entitySettings
        if (configObject.entitySettings != null) {
            for (ConfigJson.EntitySettings entitySettings : configObject.entitySettings) {
                EntityType<?> entity = entityRegistry.getValue(new ResourceLocation(entitySettings.id));
                if (entity != null) {
                    this.entitySettings.put(
                            entity,
                            new DefaultEntityScaleSettings(
                                    ShouldScaleEnum.fromString(entitySettings.settings.shouldScale),
                                    entitySettings.settings
                            )
                    );
                }
                else
                    LOGGER.warn(ScaledMobsMod.SCALEDMOBS_MARKER, "Couldn't find entity \"{}\". Make sure that it's spelled correctly and the mod it's a part of is loaded if it's from a mod.", entitySettings.id);
            }
        }

        LOGGER.info(ScaledMobsMod.SCALEDMOBS_MARKER, "Loaded scaling settings for {} entities", entitySettings.size());
    }

    private void loadClass(ConfigJson.ScaleSettings classSettings, @Nonnull EntityType<?>[] classEntities) {
        for (EntityType<?> entity : classEntities) {
            if (!entitySettings.containsKey(entity)) {
                entitySettings.put(
                        entity,
                        new DefaultEntityScaleSettings(
                                ShouldScaleEnum.fromString(classSettings.shouldScale),
                                classSettings
                        )
                );
            }
        }
    }

    private void loadPeacefulPassiveClass(ConfigJson.ScaleSettings classSettings, @Nonnull EntityType<?>[] classEntities) {
        for (EntityType<?> entity : classEntities) {
            IEntityScaleSettings existingSettings = entitySettings.get(entity);
            if (existingSettings instanceof DefaultEntityScaleSettings) {
                entitySettings.put(
                        entity,
                        new PeacefulPassiveEntityScaleSettings(
                                (DefaultEntityScaleSettings)existingSettings,
                                new DefaultEntityScaleSettings(
                                        ShouldScaleEnum.fromString(classSettings.shouldScale),
                                        classSettings
                                )
                        )
                );
            }
            else if (existingSettings == null) {
                entitySettings.put(
                        entity,
                        new PeacefulPassiveEntityScaleSettings(
                                DefaultEntityScaleSettings.DEFAULT_SCALE_SETTINGS,
                                new DefaultEntityScaleSettings(
                                        ShouldScaleEnum.fromString(classSettings.shouldScale),
                                        classSettings
                                )
                        )
                );
            }
        }
    }

    @Nonnull
    public char[] getSerializedConfig() { return serializedConfig; }

    public boolean canSpawnScaled(@Nonnull SpawnReason spawnReason) {
        switch (spawnReason) {
        case NATURAL:
        case CHUNK_GENERATION:
        case STRUCTURE:
            return spawnConfig.scaleNaturallySpawned;
        case SPAWNER:
        case BREEDING:
        case MOB_SUMMONED:
        case JOCKEY:
        case EVENT:
     // case CONVERSION:
        case REINFORCEMENT:
        case TRIGGERED:
        case PATROL:
            return spawnConfig.scaleTriggerSpawned;
        case BUCKET:
        case DISPENSER:
            return spawnConfig.scaleArtificiallySpawned;
        case SPAWN_EGG:
        case COMMAND:
            return spawnConfig.scaleManuallySpawned;
        }
        return false;
    }

    public boolean isEntityBlacklisted(@Nonnull LivingEntity entity) {
        EntityType<?> entityType = entity.getType();
        for (EntityType<?> blacklistedEntityType : blacklistedEntities) {
            if (entityType == blacklistedEntityType)
                return true;
        }
        return false;
    }

    @Nonnull
    public IEntityScaleSettings getEntityScaleSettings(@Nonnull LivingEntity entity) {
        IEntityScaleSettings entityScaleSettings = entitySettings.get(entity.getType());
        if (entityScaleSettings == null) {
            entitySettings.put(
                    entity.getType(),
                    defaultSettings
            );
            entityScaleSettings = entitySettings.get(entity.getType());
        }
        return entityScaleSettings;
    }
}

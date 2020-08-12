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

package scaledmobs.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scaledmobs.ScaledMobsMod;
import scaledmobs.config.Config;
import scaledmobs.config.ConfigManager;
import scaledmobs.config.IEntityScaleSettings;
import scaledmobs.config.ShouldScaleEnum;
import scaledmobs.util.EntityScaleUtil;
import scaledmobs.util.EntityScaler;

import javax.annotation.Nonnull;
import java.util.Random;

@EventBusSubscriber(modid = "scaledmobs")
public class LivingSpawnHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int MAX_SPAWNABLE_CHECKS = 5;

    @SubscribeEvent
    public static void onLivingCheckSpawnEvent(@Nonnull LivingSpawnEvent.CheckSpawn event) {
        // Even though other checks are applicable, keep it simple and only check this special condition
        if (event.getSpawnReason() == SpawnReason.CHUNK_GENERATION) {
            if (!handleSpawnEvent(event.getEntityLiving(), event.getWorld(), event.getSpawnReason()))
                event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onLivingSpawnSpecialEvent(@Nonnull LivingSpawnEvent.SpecialSpawn event) {
        LivingEntity entity = event.getEntityLiving();
        if (!handleSpawnEvent(event.getEntityLiving(), event.getWorld(), event.getSpawnReason()))
            entity.remove(false);
    }

    // NOTE: The world argument MUST be the WorldGenRegion passed by the event when spawning from
    // Chunk Generation! Failure to do so may result in the server deadlocking itself and crashing!
    public static boolean handleSpawnEvent(LivingEntity entity, IWorld world, SpawnReason spawnReason) {
        Config scaleConfig = ConfigManager.getConfig(world);
        IEntityScaleSettings scaleSettings = scaleConfig.getEntityScaleSettings(entity);
        if (scaleConfig.canSpawnScaled(spawnReason) &&
            !EntityScaleUtil.isEntityBlacklisted(entity) &&
            scaleSettings.getShouldScale(world) != ShouldScaleEnum.FALSE) {
            float entityScale = getSpawnableScale(entity, world);
            if (!Float.isNaN(entityScale))
                EntityScaler.setEntityScale(entity, entityScale);
            else if (spawnReason != SpawnReason.NATURAL && spawnReason != SpawnReason.CHUNK_GENERATION && spawnReason != SpawnReason.STRUCTURE)
                EntityScaler.setEntityScale(entity, scaleSettings.getEntityScale(world)); // Either they couldn't spawn normally or the size is fixed, so just go with whatever
            else
                return false;
        }
        return true;
    }

    // NOTE: The world argument MUST be the WorldGenRegion passed by the event when spawning from
    // Chunk Generation! Failure to do so may result in the server deadlocking itself and crashing!
    private static float getSpawnableScale(@Nonnull LivingEntity entity, @Nonnull IWorld world) {
        IEntityScaleSettings scaleSettings = ConfigManager.getConfig(entity.getEntityWorld()).getEntityScaleSettings(entity);
        ShouldScaleEnum shouldScale = scaleSettings.getShouldScale(world);
        switch (shouldScale) {
            case RANDOM:
                // Grab a few variables
                Random rng = world.getRandom();
                float maxGrow = scaleSettings.getMaxGrow(world);
                float maxShrink = scaleSettings.getMaxShrink(world);
                // THE BIG QUESTION: Do we want to grow or shrink???
                if (rng.nextBoolean()) { // We want to grow
                    // Calculate some stuff so that we don't perform too many checks
                    float maxScale = maxGrow;
                    float checkDecrements = (maxScale - 1.0f) / MAX_SPAWNABLE_CHECKS;
                    for (int passes = 0; passes < MAX_SPAWNABLE_CHECKS; ++passes) {
                        float scale = rng.nextFloat() * (maxScale - 1.0f) + 1.0f;
                        EntityScaler.setEntityScale(entity, scale);
                        boolean canSpawn = false;
                        try {
                            canSpawn = ((MobEntity)entity).isNotColliding(world);
                        }
                        catch (RuntimeException ignored) {
                            // Might be too big for checking, just make them smaller
                            LOGGER.info(ScaledMobsMod.SCALEDMOBS_MARKER, "Got an exception while trying to find a suitable size for a scaled mob, probably went out of bounds during world generation");
                        }
                        if (canSpawn)
                            return scale; // This scale will work
                        else
                            maxScale -= checkDecrements;
                    }
                    // Nothing else works, so just stick with the defaults
                    return 1.0f;
                }
                else {
                    float minScale = 1.0f / maxShrink;
                    return rng.nextFloat() * (1.0f - minScale) + minScale;
                }
            case FIXED:
                // This only works if they can live at their fixed size here
                float scale = scaleSettings.getFixedScale(world);
                EntityScaler.setEntityScale(entity, scale);
                boolean canSpawn = false;
                try {
                    canSpawn = ((MobEntity)entity).isNotColliding(world);
                }
                catch (RuntimeException ignored) {
                    // Might be too big for checking, just don't spawn
                    LOGGER.info(ScaledMobsMod.SCALEDMOBS_MARKER, "Got an exception while trying to test a fixed size for a scaled mob, probably went out of bounds during world generation");
                }
                if (canSpawn)
                    return scale; // This scale will work
                else
                    return Float.NaN; // There's no other size they can be
        }
        throw new IllegalArgumentException(entity.getType().getName().getString() + "'s shouldScale isn't RANDOM or FIXED. Report this problem to the mod developer (This line of code should be impossible to run!)");
    }
}

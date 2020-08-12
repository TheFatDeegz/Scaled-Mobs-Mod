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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import scaledmobs.capabilities.IScalableMobCapability;
import scaledmobs.capabilities.ScalableMobCapabilityProvider;
import scaledmobs.config.ConfigManager;

import javax.annotation.Nonnull;

public class EntityScaleUtil {
    private static final EntityType<?>[] HARD_BLACKLISTED_ENTITIES = {
            // Players, unused entities, and slimes shouldn't have this mod's capabilities
            EntityType.GIANT,
            EntityType.MAGMA_CUBE,
            EntityType.PLAYER,
            EntityType.SLIME
    };

    public static boolean isEntityBlacklisted(@Nonnull Entity entity) {
        // Check for non-living and boss entities
        if (!(entity instanceof LivingEntity) || !entity.isNonBoss())
            return true;
        // Check for hard blacklisted entities
        EntityType<?> entityType = entity.getType();
        for (EntityType<?> blacklistedEntityType : HARD_BLACKLISTED_ENTITIES) {
            if (entityType == blacklistedEntityType)
                return true;
        }
        // Check for config blacklisted entities
        return ConfigManager.getConfig(entity.getEntityWorld()).isEntityBlacklisted((LivingEntity)entity);
    }

    public static boolean isEntitySpawnHandled(@Nonnull LivingEntity entity) {
        IScalableMobCapability scalableMobCapability = entity.getCapability(ScalableMobCapabilityProvider.scalableMobCapability).orElse(null);
        if (scalableMobCapability != null) {
            return scalableMobCapability.isSpawnHandled();
        }
        return true;
    }

    public static void setEntitySpawnHandled(@Nonnull LivingEntity entity, boolean spawnHandled) {
        IScalableMobCapability scalableMobCapability = entity.getCapability(ScalableMobCapabilityProvider.scalableMobCapability).orElse(null);
        if (scalableMobCapability != null) {
            scalableMobCapability.setSpawnHandled(spawnHandled);
        }
    }
}

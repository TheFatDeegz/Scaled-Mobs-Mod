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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import scaledmobs.config.Config;
import scaledmobs.config.ConfigManager;
import scaledmobs.util.EntityScaleUtil;

@EventBusSubscriber(modid = "scaledmobs")
public class ServerEntityJoinWorldHandler {
    @SubscribeEvent
    public static void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        IWorld world = event.getWorld();
        Entity entity = event.getEntity();
        if (world instanceof ServerWorld && !EntityScaleUtil.isEntityBlacklisted(entity)) {
            EntityType<?> entityType = entity.getType();
            Config config = ConfigManager.getServerConfig();
            if (entityType == EntityType.PHANTOM &&
                config.spawnConfig.scalePhantoms) {
                // Phantoms don't have a spawn event hook for some reason, so assume they should be scaled
                if (!EntityScaleUtil.isEntitySpawnHandled((LivingEntity)entity)) {
                    LivingSpawnHandler.handleSpawnEvent((LivingEntity)entity, entity.getEntityWorld(), SpawnReason.NATURAL);
                }
            }
        }
    }
}

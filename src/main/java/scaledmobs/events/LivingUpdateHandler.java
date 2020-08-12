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
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import scaledmobs.util.EntityScaleUtil;
import scaledmobs.util.EntityScaler;

import javax.annotation.Nonnull;

@EventBusSubscriber(modid = "scaledmobs")
public class LivingUpdateHandler {
    @SubscribeEvent
    public static void onLivingUpdateEvent(@Nonnull LivingUpdateEvent event) {
        // Check the scale of this entity
        LivingEntity entity = event.getEntityLiving();
        if (!EntityScaleUtil.isEntityBlacklisted(entity))
            EntityScaler.updateIfIncorrect(entity);
    }
}

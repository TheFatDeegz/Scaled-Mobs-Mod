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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import scaledmobs.capabilities.ScalableMobCapabilityProvider;
import scaledmobs.util.EntityScaleUtil;

import javax.annotation.Nonnull;

@EventBusSubscriber(modid = "scaledmobs")
public class AttachCapabilitiesHandler {
    @SubscribeEvent
    public static void onAttachCapabilitiesEvent(@Nonnull AttachCapabilitiesEvent<Entity> event) {
        // Make sure that this entity is allowed to be scaled
        if (!EntityScaleUtil.isEntityBlacklisted(event.getObject()))
            // This mob is allowed to be scaled, so attach the capability
            event.addCapability(
                    new ResourceLocation("scaledmobs", "scalablemobcapability"),
                    new ScalableMobCapabilityProvider()
            );
    }
}

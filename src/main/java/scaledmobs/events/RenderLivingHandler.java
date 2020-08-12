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

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import scaledmobs.util.EntityScaleUtil;
import scaledmobs.util.EntityScaler;

import javax.annotation.Nonnull;

@EventBusSubscriber(modid = "scaledmobs", value = Dist.CLIENT)
public class RenderLivingHandler {
    @SubscribeEvent
    public static void onRenderLivingPreEvent(@Nonnull RenderLivingEvent.Pre<?, ?> event) {
        LivingEntity entity = event.getEntity();
        if (!EntityScaleUtil.isEntityBlacklisted(entity)) {
            // Scale the entity's transformation matrix
            MatrixStack matrixStack = event.getMatrixStack();
            matrixStack.push();
            float entityScale = EntityScaler.getEntityScale(entity);
            matrixStack.scale(entityScale, entityScale, entityScale);
        }
    }

    @SubscribeEvent
    public static void onRenderLivingPoseEvent(@Nonnull RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();
        if (!EntityScaleUtil.isEntityBlacklisted(entity))
            event.getMatrixStack().pop();
    }
}

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

import net.minecraft.world.IWorld;

public interface IEntityScaleSettings {
    ShouldScaleEnum getShouldScale(IWorld world);
    float getMaxGrow(IWorld world);
    float getMaxShrink(IWorld world);
    float getFixedScale(IWorld world);
    float getEntityScale(IWorld world);
    boolean getShouldScaleHealth(IWorld world);
    float getMaxHealthMultiplier(IWorld world);
    float getMinHealthMultiplier(IWorld world);
    boolean getShouldScaleAttack(IWorld world);
    float getMaxAttackMultiplier(IWorld world);
    float getMinAttackMultiplier(IWorld world);
    boolean getShouldScaleView(IWorld world);
    float getMaxViewMultiplier(IWorld world);
    float getMinViewMultiplier(IWorld world);
}

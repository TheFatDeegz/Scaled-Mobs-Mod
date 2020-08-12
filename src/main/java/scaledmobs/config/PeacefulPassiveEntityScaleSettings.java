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

import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;

public class PeacefulPassiveEntityScaleSettings extends DefaultEntityScaleSettings {
    private final DefaultEntityScaleSettings peacefulSettings;

    PeacefulPassiveEntityScaleSettings(
            DefaultEntityScaleSettings nonPeacefulSettings,
            DefaultEntityScaleSettings peacefulSettings
    ) {
        super(nonPeacefulSettings);
        this.peacefulSettings = peacefulSettings;
    }

    @Override
    public ShouldScaleEnum getShouldScale(IWorld world) {
        if (world.getDifficulty() != Difficulty.PEACEFUL)
            return super.getShouldScale(world);
        else
            return peacefulSettings.getShouldScale(world);
    }

    @Override
    public float getMaxGrow(IWorld world) {
        if (world.getDifficulty() != Difficulty.PEACEFUL)
            return super.getMaxGrow(world);
        else
            return peacefulSettings.getMaxGrow(world);
    }

    @Override
    public float getMaxShrink(IWorld world) {
        if (world.getDifficulty() != Difficulty.PEACEFUL)
            return super.getMaxShrink(world);
        else
            return peacefulSettings.getMaxShrink(world);
    }

    @Override
    public float getFixedScale(IWorld world) {
        if (world.getDifficulty() != Difficulty.PEACEFUL)
            return super.getFixedScale(world);
        else
            return peacefulSettings.getFixedScale(world);
    }

    @Override
    public float getEntityScale(IWorld world) {
        if (world.getDifficulty() != Difficulty.PEACEFUL)
            return super.getEntityScale(world);
        else
            return peacefulSettings.getEntityScale(world);
    }

    @Override
    public boolean getShouldScaleHealth(IWorld world) {
        if (world.getDifficulty() != Difficulty.PEACEFUL)
            return super.getShouldScaleHealth(world);
        else
            return peacefulSettings.getShouldScaleHealth(world);
    }

    @Override
    public float getMaxHealthMultiplier(IWorld world) {
        if (world.getDifficulty() != Difficulty.PEACEFUL)
            return super.getMaxHealthMultiplier(world);
        else
            return peacefulSettings.getMaxHealthMultiplier(world);
    }

    @Override
    public boolean getShouldScaleAttack(IWorld world) {
        if (world.getDifficulty() != Difficulty.PEACEFUL)
            return super.getShouldScaleAttack(world);
        else
            return peacefulSettings.getShouldScaleAttack(world);
    }

    @Override
    public float getMaxAttackMultiplier(IWorld world) {
        if (world.getDifficulty() != Difficulty.PEACEFUL)
            return super.getMaxAttackMultiplier(world);
        else
            return peacefulSettings.getMaxAttackMultiplier(world);
    }

    @Override
    public float getMinAttackMultiplier(IWorld world) {
        if (world.getDifficulty() != Difficulty.PEACEFUL)
            return super.getMinAttackMultiplier(world);
        else
            return peacefulSettings.getMinAttackMultiplier(world);
    }

    @Override
    public boolean getShouldScaleView(IWorld world) {
        if (world.getDifficulty() != Difficulty.PEACEFUL)
            return super.getShouldScaleView(world);
        else
            return peacefulSettings.getShouldScaleView(world);
    }

    @Override
    public float getMaxViewMultiplier(IWorld world) {
        if (world.getDifficulty() != Difficulty.PEACEFUL)
            return super.getMaxViewMultiplier(world);
        else
            return peacefulSettings.getMaxViewMultiplier(world);
    }

    @Override
    public float getMinViewMultiplier(IWorld world) {
        if (world.getDifficulty() != Difficulty.PEACEFUL)
            return super.getMinViewMultiplier(world);
        else
            return peacefulSettings.getMinViewMultiplier(world);
    }
}

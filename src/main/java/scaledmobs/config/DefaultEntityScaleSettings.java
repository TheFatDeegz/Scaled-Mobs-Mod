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

import javax.annotation.Nonnull;
import java.util.Random;

public class DefaultEntityScaleSettings implements IEntityScaleSettings {
    public static final DefaultEntityScaleSettings DEFAULT_SCALE_SETTINGS = new DefaultEntityScaleSettings(
            ShouldScaleEnum.FALSE,
            Float.NaN,
            Float.NaN,
            Float.NaN,
            false,
            Float.NaN,
            Float.NaN,
            false,
            Float.NaN,
            Float.NaN,
            false,
            Float.NaN,
            Float.NaN
    );

    private final ShouldScaleEnum shouldScale;
    private final float maxGrow;
    private final float maxShrink;
    private final float fixedScale;
    private final boolean shouldScaleHealth;
    private final float maxHealthMultiplier;
    private final float minHealthMultiplier;
    private final boolean shouldScaleAttack;
    private final float maxAttackMultiplier;
    private final float minAttackMultiplier;
    private final boolean shouldScaleView;
    private final float maxViewMultiplier;
    private final float minViewMultiplier;

    public DefaultEntityScaleSettings(
            @Nonnull ShouldScaleEnum shouldScale,
            float maxGrow,
            float maxShrink,
            float fixedScale,
            boolean shouldScaleHealth,
            float maxHealthMultiplier,
            float minHealthMultiplier,
            boolean shouldScaleAttack,
            float maxAttackMultiplier,
            float minAttackMultiplier,
            boolean shouldScaleView,
            float maxViewMultiplier,
            float minViewMultiplier
    ) {
        switch (shouldScale) {
        case RANDOM:
            // Filter out invalid input
            if (Float.isInfinite(maxGrow) ||
                Float.isNaN(maxGrow) ||
                maxGrow < 0.0f)
                throw new IllegalArgumentException("maxGrow is not a valid float (" + maxGrow + ")");
            else if (Float.isInfinite(maxShrink) ||
                Float.isNaN(maxShrink) ||
                maxShrink < 0.0f)
                throw new IllegalArgumentException("maxShrink is not a valid float (" + maxShrink + ")");
            else if (shouldScaleHealth && (
                Float.isInfinite(maxHealthMultiplier) ||
                Float.isNaN(maxHealthMultiplier) ||
                maxHealthMultiplier < 0.0f ||
                maxHealthMultiplier < 1.0f))
                throw new IllegalArgumentException("maxHealthMultiplier is not a valid float (" + maxHealthMultiplier + ")");
            else if (shouldScaleHealth && (
                Float.isInfinite(minHealthMultiplier) ||
                Float.isNaN(minHealthMultiplier) ||
                minHealthMultiplier < 0.0f ||
                minHealthMultiplier > 1.0f))
                throw new IllegalArgumentException("minHealthMultiplier is not a valid float (" + minHealthMultiplier + ")");
            else if (shouldScaleAttack && (
                Float.isInfinite(maxAttackMultiplier) ||
                Float.isNaN(maxAttackMultiplier) ||
                maxAttackMultiplier < 0.0f ||
                maxAttackMultiplier < 1.0f))
                throw new IllegalArgumentException("maxAttackMultiplier is not a valid float (" + maxAttackMultiplier + ")");
            else if (shouldScaleAttack && (
                Float.isInfinite(minAttackMultiplier) ||
                Float.isNaN(minAttackMultiplier) ||
                minAttackMultiplier < 0.0f ||
                minAttackMultiplier > 1.0f))
                throw new IllegalArgumentException("minAttackMultiplier is not a valid float (" + minAttackMultiplier + ")");
            else if (shouldScaleAttack && (
                Float.isInfinite(maxViewMultiplier) ||
                Float.isNaN(maxViewMultiplier) ||
                maxViewMultiplier < 0.0f ||
                maxViewMultiplier < 1.0f))
                throw new IllegalArgumentException("maxViewMultiplier is not a valid float (" + maxViewMultiplier + ")");
            else if (shouldScaleAttack && (
                Float.isInfinite(minViewMultiplier) ||
                Float.isNaN(minViewMultiplier) ||
                minViewMultiplier < 0.0f ||
                minViewMultiplier > 1.0f))
                throw new IllegalArgumentException("minViewMultiplier is not a valid float (" + minViewMultiplier + ")");

            // Initialize class variables
            this.shouldScale = ShouldScaleEnum.RANDOM;
            this.maxGrow = maxGrow;
            this.maxShrink = maxShrink;
            this.fixedScale = Float.NaN;
            this.shouldScaleHealth = shouldScaleHealth;
            if (shouldScaleHealth) {
                this.maxHealthMultiplier = maxHealthMultiplier;
                this.minHealthMultiplier = minHealthMultiplier;
            }
            else {
                this.maxHealthMultiplier = Float.NaN;
                this.minHealthMultiplier = Float.NaN;
            }
            this.shouldScaleAttack = shouldScaleAttack;
            if (shouldScaleAttack) {
                this.maxAttackMultiplier = maxAttackMultiplier;
                this.minAttackMultiplier = minAttackMultiplier;
            }
            else {
                this.maxAttackMultiplier = Float.NaN;
                this.minAttackMultiplier = Float.NaN;
            }
            this.shouldScaleView = shouldScaleView;
            if (shouldScaleView) {
                this.maxViewMultiplier = maxViewMultiplier;
                this.minViewMultiplier = minViewMultiplier;
            }
            else {
                this.maxViewMultiplier = Float.NaN;
                this.minViewMultiplier = Float.NaN;
            }
            break;
        case FIXED:
            // Filter out invalid input
            if (Float.isInfinite(fixedScale) ||
                    Float.isNaN(fixedScale) ||
                    fixedScale < 0.0f)
                throw new IllegalArgumentException("fixedScale is not a valid float (" + fixedScale + ")");

            // Initialize class variables
            this.shouldScale = ShouldScaleEnum.FIXED;
            this.maxGrow = Float.NaN;
            this.maxShrink = Float.NaN;
            this.fixedScale = fixedScale;
            this.shouldScaleHealth = shouldScaleHealth;
            this.maxHealthMultiplier = Float.NaN;
            this.minHealthMultiplier = Float.NaN;
            this.shouldScaleAttack = shouldScaleAttack;
            this.maxAttackMultiplier = Float.NaN;
            this.minAttackMultiplier = Float.NaN;
            this.shouldScaleView = shouldScaleView;
            this.maxViewMultiplier = Float.NaN;
            this.minViewMultiplier = Float.NaN;
            break;
        default:
            this.shouldScale = ShouldScaleEnum.FALSE;
            this.maxGrow = Float.NaN;
            this.maxShrink = Float.NaN;
            this.fixedScale = Float.NaN;
            this.shouldScaleHealth = false;
            this.maxHealthMultiplier = Float.NaN;
            this.minHealthMultiplier = Float.NaN;
            this.shouldScaleAttack = false;
            this.maxAttackMultiplier = Float.NaN;
            this.minAttackMultiplier = Float.NaN;
            this.shouldScaleView = false;
            this.maxViewMultiplier = Float.NaN;
            this.minViewMultiplier = Float.NaN;
        }
    }

    public DefaultEntityScaleSettings(ShouldScaleEnum shouldScale, ConfigJson.ScaleSettings scaleSettings) {
        this (
                shouldScale,
                shouldScale == ShouldScaleEnum.RANDOM ? scaleSettings.maxGrow : Float.NaN,
                shouldScale == ShouldScaleEnum.RANDOM ? scaleSettings.maxShrink : Float.NaN,
                shouldScale == ShouldScaleEnum.FIXED ? scaleSettings.fixedScale : Float.NaN,
                shouldScale != ShouldScaleEnum.FALSE ? scaleSettings.shouldScaleHealth : false,
                shouldScale == ShouldScaleEnum.RANDOM && scaleSettings.shouldScaleHealth ? scaleSettings.maxHealthMultiplier : Float.NaN,
                shouldScale == ShouldScaleEnum.RANDOM && scaleSettings.shouldScaleHealth ? scaleSettings.minHealthMultiplier : Float.NaN,
                shouldScale != ShouldScaleEnum.FALSE ? scaleSettings.shouldScaleAttack : false,
                shouldScale == ShouldScaleEnum.RANDOM && scaleSettings.shouldScaleAttack ? scaleSettings.maxAttackMultiplier : Float.NaN,
                shouldScale == ShouldScaleEnum.RANDOM && scaleSettings.shouldScaleAttack ? scaleSettings.minAttackMultiplier : Float.NaN,
                shouldScale != ShouldScaleEnum.FALSE ? scaleSettings.shouldScaleView : false,
                shouldScale == ShouldScaleEnum.RANDOM && scaleSettings.shouldScaleView ? scaleSettings.maxViewMultiplier : Float.NaN,
                shouldScale == ShouldScaleEnum.RANDOM && scaleSettings.shouldScaleView ? scaleSettings.minViewMultiplier : Float.NaN
        );
    }

    public DefaultEntityScaleSettings(DefaultEntityScaleSettings settings) {
        shouldScale = settings.shouldScale;
        maxGrow = settings.maxGrow;
        maxShrink = settings.maxShrink;
        fixedScale = settings.fixedScale;
        shouldScaleHealth = settings.shouldScaleHealth;
        maxHealthMultiplier = settings.maxHealthMultiplier;
        minHealthMultiplier = settings.minHealthMultiplier;
        shouldScaleAttack = settings.shouldScaleAttack;
        maxAttackMultiplier = settings.maxAttackMultiplier;
        minAttackMultiplier = settings.minAttackMultiplier;
        shouldScaleView = settings.shouldScaleView;
        maxViewMultiplier = settings.maxViewMultiplier;
        minViewMultiplier = settings.minViewMultiplier;
    }

    @Override
    public ShouldScaleEnum getShouldScale(IWorld world) { return shouldScale; }

    @Override
    public float getMaxGrow(IWorld world) {
        assert shouldScale == ShouldScaleEnum.RANDOM;
        return maxGrow;
    }

    @Override
    public float getMaxShrink(IWorld world) {
        assert shouldScale == ShouldScaleEnum.RANDOM;
        return maxShrink;
    }

    @Override
    public float getFixedScale(IWorld world) {
        assert shouldScale == ShouldScaleEnum.FIXED;
        return fixedScale;
    }

    @Override
    public float getEntityScale(IWorld world) {
        if (shouldScale == ShouldScaleEnum.RANDOM) {
            Random rng = world.getRandom();
            if (rng.nextBoolean())
                // Grow entity
                return rng.nextFloat() * maxGrow;
            else
                // Shrink entity
                return 1 / (rng.nextFloat() * maxShrink);
        }
        else if (shouldScale == ShouldScaleEnum.FIXED)
            return fixedScale;
        else
            return 1.0f;
    }

    @Override
    public boolean getShouldScaleHealth(IWorld world) { return shouldScaleHealth; }

    @Override
    public float getMaxHealthMultiplier(IWorld world) {
        assert shouldScale == ShouldScaleEnum.RANDOM;
        assert shouldScaleHealth;
        return maxHealthMultiplier;
    }

    @Override
    public float getMinHealthMultiplier(IWorld world) {
        assert shouldScale == ShouldScaleEnum.RANDOM;
        assert shouldScaleHealth;
        return minHealthMultiplier;
    }

    @Override
    public boolean getShouldScaleAttack(IWorld world) { return shouldScaleAttack; }

    @Override
    public float getMaxAttackMultiplier(IWorld world) {
        assert shouldScale == ShouldScaleEnum.RANDOM;
        assert shouldScaleAttack;
        return maxAttackMultiplier;
    }

    @Override
    public float getMinAttackMultiplier(IWorld world) {
        assert shouldScale == ShouldScaleEnum.RANDOM;
        assert shouldScaleAttack;
        return minAttackMultiplier;
    }

    @Override
    public boolean getShouldScaleView(IWorld world) { return shouldScaleView; }

    @Override
    public float getMaxViewMultiplier(IWorld world) {
        assert shouldScale == ShouldScaleEnum.RANDOM;
        assert shouldScaleView;
        return maxViewMultiplier;
    }

    @Override
    public float getMinViewMultiplier(IWorld world) {
        assert shouldScale == ShouldScaleEnum.RANDOM;
        assert shouldScaleView;
        return minViewMultiplier;
    }
}

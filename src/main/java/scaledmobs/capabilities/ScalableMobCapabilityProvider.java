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

package scaledmobs.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

import javax.annotation.Nonnull;

public class ScalableMobCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {
    private static class CapabilitySupplier<T> implements NonNullSupplier<T> {
        private final T capability;

        public CapabilitySupplier(T capability) {
            this.capability = capability;
        }

        @Nonnull
        @Override
        public T get() { return capability; }
    }

    @CapabilityInject(IScalableMobCapability.class)
    public static Capability<IScalableMobCapability> scalableMobCapability = null;

    IScalableMobCapability capabilityInstance = new ScalableMobCapability();

    public ScalableMobCapabilityProvider() { }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> capability, final Direction side) {
        if (capability == scalableMobCapability) {
            return LazyOptional.of(new CapabilitySupplier<>((T)capabilityInstance));
        }
        return LazyOptional.empty();
    }

    public CompoundNBT serializeNBT() {
        return (CompoundNBT)capabilityInstance.writeNBT(scalableMobCapability, capabilityInstance, null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        capabilityInstance.readNBT(scalableMobCapability, capabilityInstance, null, nbt);
    }
}

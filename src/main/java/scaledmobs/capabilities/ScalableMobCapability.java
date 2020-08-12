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

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ScalableMobCapability implements IScalableMobCapability, Capability.IStorage<IScalableMobCapability> {
    private boolean spawnHandled = false; // This is currently only used for phantoms, so don't worry about setting it elsewhere
    private float entityScale = 1.0f;

    @Override
    public boolean isSpawnHandled() { return spawnHandled; }

    @Override
    public void setSpawnHandled(boolean spawnHandled) { this.spawnHandled = spawnHandled; }

    @Override
    public float getScale() { return entityScale; }

    @Override
    public void setScale(float mobScale) { this.entityScale = mobScale; }

    @Override
    public INBT writeNBT(Capability<IScalableMobCapability> capability, IScalableMobCapability instance, Direction side) {
        return ScalableMobCapabilityStorage.instance.writeNBT(capability, instance, null);
    }

    @Override
    public void readNBT(Capability<IScalableMobCapability> capability, IScalableMobCapability instance, Direction side, INBT nbt) {
        ScalableMobCapabilityStorage.instance.readNBT(capability, instance, null, nbt);
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(
                IScalableMobCapability.class,
                ScalableMobCapabilityStorage.instance,
                ScalableMobCapability::new);
    }
}

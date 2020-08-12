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
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ScalableMobCapabilityStorage implements Capability.IStorage<IScalableMobCapability> {
    private static final int BYTE_NBT_ID = 1;
    private static final int FLOAT_NBT_ID = 5;

    public static final ScalableMobCapabilityStorage instance = new ScalableMobCapabilityStorage();

    @Override
    public INBT writeNBT(Capability<IScalableMobCapability> capability, IScalableMobCapability instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("SpawnHandled", instance.isSpawnHandled());
        tag.putFloat("EntityScale", instance.getScale());
        return tag;
    }

    @Override
    public void readNBT(Capability<IScalableMobCapability> capability, IScalableMobCapability instance, Direction side, INBT nbt) {
        if (nbt instanceof CompoundNBT) {
            CompoundNBT compoundNBT = (CompoundNBT)nbt;
            if (compoundNBT.contains("SpawnHandled", BYTE_NBT_ID)) {
                instance.setSpawnHandled(compoundNBT.getBoolean("SpawnHandled"));
            }
            if (compoundNBT.contains("EntityScale", FLOAT_NBT_ID)) {
                instance.setScale(compoundNBT.getFloat("EntityScale"));
            }
        }
    }
}

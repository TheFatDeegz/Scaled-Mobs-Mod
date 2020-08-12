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

package scaledmobs.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scaledmobs.ScaledMobsMod;
import scaledmobs.capabilities.IScalableMobCapability;
import scaledmobs.capabilities.ScalableMobCapabilityProvider;

import java.util.function.Supplier;

public class ServerboundRequestEntityScale {
    private static final Logger LOGGER = LogManager.getLogger();

    private final int entityId;

    public ServerboundRequestEntityScale(int entityId) {
        this.entityId = entityId;
    }

    public static void encodePacket(ServerboundRequestEntityScale packet, PacketBuffer packetBuffer) {
        packetBuffer.writeInt(packet.entityId);
    }

    public static ServerboundRequestEntityScale decodePacket(PacketBuffer packetBuffer) {
        int entityId = packetBuffer.readInt();
        return new ServerboundRequestEntityScale(entityId);
    }

    public static void handleMessage(ServerboundRequestEntityScale packet, Supplier<Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ServerPlayerEntity sender = contextSupplier.get().getSender();
            assert sender != null;
            Entity target = sender.getEntityWorld().getEntityByID(packet.entityId);
            if (target instanceof LivingEntity) {
                LivingEntity livingTarget = (LivingEntity) target;
                IScalableMobCapability scalableMobCapability = livingTarget.getCapability(ScalableMobCapabilityProvider.scalableMobCapability).orElse(null);
                if (scalableMobCapability != null)
                    PacketHandler.respondScaleRequest(sender, livingTarget, scalableMobCapability.getScale());
                else
                    LOGGER.warn(ScaledMobsMod.SCALEDMOBS_MARKER, "Entity ID {} ({}) doesn't have the ScalableMobCapability. Configuration may not be synced properly between the client and server", packet.entityId, livingTarget.getType());
            }
            contextSupplier.get().setPacketHandled(true);
        });
    }
}
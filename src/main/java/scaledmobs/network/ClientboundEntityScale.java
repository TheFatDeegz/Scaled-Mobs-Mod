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

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import scaledmobs.util.EntityScaler;

import java.util.function.Supplier;

public class ClientboundEntityScale {
    private final int entityId;
    private final float entityScale;

    public ClientboundEntityScale(int entityId, float entityScale) {
        this.entityId = entityId;
        this.entityScale = entityScale;
    }

    public static void encodePacket(ClientboundEntityScale packet, PacketBuffer packetBuffer) {
        packetBuffer.writeInt(packet.entityId);
        packetBuffer.writeFloat(packet.entityScale);
    }

    public static ClientboundEntityScale decodePacket(PacketBuffer packetBuffer) {
        int entityId = packetBuffer.readInt();
        float entityScale = packetBuffer.readFloat();
        return new ClientboundEntityScale(entityId, entityScale);
    }

    public static void handleMessage(ClientboundEntityScale packet, Supplier<Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().world;
            assert world != null;
            Entity entity = world.getEntityByID(packet.entityId);
            if (entity instanceof LivingEntity) {
                EntityScaler.setEntityScale((LivingEntity)entity, packet.entityScale);
            }
            contextSupplier.get().setPacketHandled(true);
        });
    }
}

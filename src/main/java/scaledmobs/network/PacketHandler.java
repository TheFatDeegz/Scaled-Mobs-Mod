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

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import scaledmobs.config.ConfigManager;

public class PacketHandler {
    private enum PacketIds {
        CLIENTBOUND_ENTITY_SCALE(0),
        CLIENTBOUND_SYNC_CONFIG(1),
        SERVERBOUND_REQUEST_ENTITY_SCALE(2);

        public final int packetId;

        PacketIds(int packetId) {
            this.packetId = packetId;
        }
    }

    private static final String PROTOCOL_VERSION = "1.0.0";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("scaledmobs", "networkchannel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            (serverVersion) -> serverVersion.equals(NetworkRegistry.ACCEPTVANILLA) || serverVersion.equals(PROTOCOL_VERSION) // Playing on vanilla servers with this mod is fine
    );

    public static void registerPackets() {
        // ClientboundEntityScale
        CHANNEL.registerMessage(
                PacketIds.CLIENTBOUND_ENTITY_SCALE.packetId,
                ClientboundEntityScale.class,
                ClientboundEntityScale::encodePacket,
                ClientboundEntityScale::decodePacket,
                ClientboundEntityScale::handleMessage
        );
        // ClientboundSyncConfig
        CHANNEL.registerMessage(
                PacketIds.CLIENTBOUND_SYNC_CONFIG.packetId,
                ClientboundSyncConfig.class,
                ClientboundSyncConfig::encodePacket,
                ClientboundSyncConfig::decodePacket,
                ClientboundSyncConfig::handleMessage
        );
        // ServerboundRequestEntityScale
        CHANNEL.registerMessage(
                PacketIds.SERVERBOUND_REQUEST_ENTITY_SCALE.packetId,
                ServerboundRequestEntityScale.class,
                ServerboundRequestEntityScale::encodePacket,
                ServerboundRequestEntityScale::decodePacket,
                ServerboundRequestEntityScale::handleMessage
        );
    }

    public static void respondScaleRequest(ServerPlayerEntity requester, LivingEntity entity, float entityScale) {
        CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> requester),
                new ClientboundEntityScale(entity.getEntityId(), entityScale)
        );
    }

    public static void notifyScaleChange(LivingEntity entity, float entityScale) {
        CHANNEL.send(
                PacketDistributor.DIMENSION.with(() -> entity.getEntityWorld().func_234923_W_()),
                new ClientboundEntityScale(entity.getEntityId(), entityScale)
        );
    }

    public static void syncClientConfig(ServerPlayerEntity player) {
        CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new ClientboundSyncConfig(ConfigManager.getServerConfig().getSerializedConfig())
        );
    }

    public static void requestEntityScale(LivingEntity entity) {
        CHANNEL.sendToServer(
                new ServerboundRequestEntityScale(entity.getEntityId())
        );
    }
}

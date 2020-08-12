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

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import scaledmobs.config.ConfigManager;

import java.io.IOException;
import java.util.function.Supplier;

public class ClientboundSyncConfig {
    private final char[] serializedConfig;

    public ClientboundSyncConfig(char[] serializedConfig) {
        this.serializedConfig = serializedConfig;
    }

    public static void encodePacket(ClientboundSyncConfig packet, PacketBuffer packetBuffer) {
        packetBuffer.writeInt(packet.serializedConfig.length);
        for (char copy : packet.serializedConfig)
            packetBuffer.writeChar(copy);
    }

    public static ClientboundSyncConfig decodePacket(PacketBuffer packetBuffer) {
        char[] serializedConfig = new char[packetBuffer.readInt()];
        for (int i = 0; i < serializedConfig.length; ++i) {
            serializedConfig[i] = packetBuffer.readChar();
        }
        return new ClientboundSyncConfig(serializedConfig);
    }

    public static void handleMessage(ClientboundSyncConfig packet, Supplier<Context> contextSupplier) {
        // We're dealing with our own stuff, so no need to enqueueWork()
        try {
            ConfigManager.loadClientConfig(packet.serializedConfig);
        }
        catch (IOException e) {
            RuntimeException exception = new RuntimeException("Failed to deserialize server config: " + e.getMessage());
            exception.setStackTrace(e.getStackTrace());
            throw exception;
        }
        contextSupplier.get().setPacketHandled(true);
    }
}

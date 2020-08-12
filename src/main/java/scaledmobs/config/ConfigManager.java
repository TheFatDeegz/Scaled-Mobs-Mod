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

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scaledmobs.ScaledMobsMod;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;

@EventBusSubscriber(modid = "scaledmobs", bus = EventBusSubscriber.Bus.MOD)
public class ConfigManager {
    /*
     * FML Config
     */
    public static class ForgeConfig {
        public final ForgeConfigSpec.ConfigValue<String> configName;

        public ForgeConfig(ForgeConfigSpec.Builder builder) {
            configName = builder
                    .comment("Name of the config file used by this mod (NOT THIS FILE!!!)")
                    .worldRestart()
                    .define("configName", "scaledmobs-common.json");
        }
    }
    public static final ForgeConfigSpec FORGE_CONFIG_SPEC;
    public static final ForgeConfig FORGE_CONFIG;

    static {
        Pair<ForgeConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ForgeConfig::new);
        FORGE_CONFIG_SPEC = specPair.getRight();
        FORGE_CONFIG = specPair.getLeft();
    }

    /*
     * Our config implementation
     */
    private static final int MiB = 1048576;
    private static final Logger LOGGER = LogManager.getLogger();

    private static Config serverConfig;
    private static Config clientConfig = null;

    @SubscribeEvent
    public static void onModConfigLoadingEvent(@Nonnull ModConfig.Loading event) throws IOException {
        String configName = event.getConfig().getConfigData().get("configName");
        LOGGER.info(ScaledMobsMod.SCALEDMOBS_MARKER, "Loading Scaled Mobs Config File \"{}\"", configName);

        // Open the config file
        Path configDirectory = event.getConfig().getFullPath().getParent();
        File configFile = configDirectory.resolve(configName).toFile();
        if (configFile.createNewFile()) {
            LOGGER.info(ScaledMobsMod.SCALEDMOBS_MARKER, "\"{}\" doesn't exist, writing default config file", configName);
            // Copy the contents of the default config file to the config file
            // Load the default config file
            InputStream defaultConfigFileStream = ConfigManager.class.getClassLoader().getResourceAsStream("scaledmobs-common.json");
            ArrayList<byte[]> defaultConfigBuffer = new ArrayList<>();
            int totalBytesRead = 0;
            byte[] defaultConfigChunk = new byte[MiB]; // Read the file in chunks of 1MiB (If the user isn't doing crazy things, this should be way more than enough)
            for (;;) {
                int chunkBytesRead = defaultConfigFileStream.read(defaultConfigChunk);
                if (chunkBytesRead != -1) {
                    totalBytesRead += chunkBytesRead;
                    defaultConfigBuffer.add(defaultConfigChunk.clone());
                }
                else
                    break;
            }
            defaultConfigFileStream.close();
            // Write the contents of the default config file to the config file
            FileOutputStream configFileWriter = new FileOutputStream(configFile);
            for (byte[] chunk : defaultConfigBuffer) {
                configFileWriter.write(chunk, 0, Math.min(totalBytesRead, MiB));
                totalBytesRead -= MiB;
            }
            configFileWriter.close();
        }

        // Read the config file
        FileReader configFileReader = new FileReader(configFile);
        char[] configFileBuffer = new char[(int)configFile.length()];
        if (configFileReader.read(configFileBuffer) != configFileBuffer.length)
            LOGGER.warn(ScaledMobsMod.SCALEDMOBS_MARKER, "Didn't read the same number of bytes from {} as expected", configFile.getName());
        configFileReader.close();

        // Parse the config file
        serverConfig = new Config(configFileBuffer);
    }

    @Nonnull
    public static Config getServerConfig() { return serverConfig; }

    @Nonnull
    public static Config getConfig(IWorld world) {
        if (clientConfig == null)
            return serverConfig;
        else {
            if (world instanceof ClientWorld)
                return clientConfig;
            else
                return serverConfig;
        }
    }

    public static void loadClientConfig(char[] buffer) throws IOException { clientConfig = new Config(buffer); }

    public static void unloadClientConfig() { clientConfig = null; }
}

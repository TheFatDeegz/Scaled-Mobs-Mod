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

package scaledmobs;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import scaledmobs.capabilities.ScalableMobCapability;
import scaledmobs.config.ConfigManager;
import scaledmobs.network.PacketHandler;

@Mod("scaledmobs")
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ScaledMobsMod
{
    public static final Marker SCALEDMOBS_MARKER = MarkerManager.getMarker("ScaledMobsMod");
    private static final Logger LOGGER = LogManager.getLogger();

    public ScaledMobsMod() {
        LOGGER.info(SCALEDMOBS_MARKER, "Loading Scaled Mobs Mod version {}", ScaledMobsMod.class.getPackage().getImplementationVersion());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigManager.FORGE_CONFIG_SPEC);
    }

    @SubscribeEvent()
    public static void onFMLCommonSetupEvent(final FMLCommonSetupEvent event) {
        ScalableMobCapability.register();
        PacketHandler.registerPackets();
    }
}

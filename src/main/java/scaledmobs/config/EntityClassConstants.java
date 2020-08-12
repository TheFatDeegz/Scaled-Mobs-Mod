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

import net.minecraft.entity.EntityType;

public interface EntityClassConstants {
    /*
     * Passive Entity Class
     */
    EntityType<?>[] PASSIVE = {
            EntityType.BAT,
            EntityType.CAT,
            EntityType.CHICKEN,
            EntityType.COD,
            EntityType.COW,
            EntityType.DONKEY,
            EntityType.FOX,
            EntityType.HORSE,
            EntityType.MULE,
            EntityType.MOOSHROOM,
            EntityType.OCELOT,
            EntityType.PARROT,
            EntityType.PIG,
            EntityType.PUFFERFISH,
            EntityType.RABBIT,
            EntityType.SALMON,
            EntityType.SHEEP,
            EntityType.SKELETON_HORSE,
            EntityType.SNOW_GOLEM,
            EntityType.SQUID,
            EntityType.field_233589_aE_, // Strider
            EntityType.TROPICAL_FISH,
            EntityType.TURTLE,
            EntityType.VILLAGER,
            EntityType.WANDERING_TRADER,
            EntityType.ZOMBIE_HORSE
    };
    EntityType<?>[] PASSIVE_WHEN_PEACEFUL = {
            EntityType.field_233588_G_, // Hoglin
            EntityType.IRON_GOLEM,
            EntityType.PANDA,
            EntityType.field_233591_ai_, // Piglin
            EntityType.POLAR_BEAR,
            EntityType.WOLF
    };
    EntityType<?>[] PASSIVE_CREATURES = {
            EntityType.BAT,
            EntityType.CHICKEN,
            EntityType.COD,
            EntityType.COW,
            EntityType.FOX,
            EntityType.MOOSHROOM,
            EntityType.OCELOT,
            EntityType.PUFFERFISH,
            EntityType.RABBIT,
            EntityType.SALMON,
            EntityType.SHEEP,
            EntityType.SQUID,
            EntityType.TROPICAL_FISH,
            EntityType.TURTLE
    };
    EntityType<?>[] PASSIVE_WHEN_PEACEFUL_CREATURES = {
            EntityType.PANDA,
            EntityType.POLAR_BEAR
    };
    EntityType<?>[] PASSIVE_GOLEMS = {
            EntityType.SNOW_GOLEM
    };
    EntityType<?>[] PASSIVE_WHEN_PEACEFUL_GOLEMS = {
            EntityType.IRON_GOLEM
    };
    EntityType<?>[] PASSIVE_WHEN_PEACEFUL_NETHER = {
            EntityType.field_233588_G_, // Hoglin
            EntityType.field_233591_ai_ // Piglin
    };
    EntityType<?>[] PASSIVE_RIDEABLE = {
            EntityType.DONKEY,
            EntityType.HORSE,
            EntityType.MULE,
            EntityType.PIG,
            EntityType.field_233589_aE_ // Strider
    };
    EntityType<?>[] PASSIVE_TAMEABLE = {
            EntityType.CAT,
            EntityType.PARROT,
            EntityType.SKELETON_HORSE,
            EntityType.ZOMBIE_HORSE
    };
    EntityType<?>[] PASSIVE_WHEN_PEACEFUL_TAMEABLE = {
            EntityType.WOLF
    };
    EntityType<?>[] PASSIVE_VILLAGERS = {
            EntityType.VILLAGER,
            EntityType.WANDERING_TRADER
    };

    /*
     * Neutral Entity Class
     */
    EntityType<?>[] NEUTRAL = {
            EntityType.BEE,
            EntityType.DOLPHIN,
            EntityType.IRON_GOLEM,
            EntityType.LLAMA,
            EntityType.PANDA,
            EntityType.POLAR_BEAR,
            EntityType.TRADER_LLAMA,
            EntityType.WOLF
    };
    EntityType<?>[] NEUTRAL_ARTHROPODS = {
            EntityType.BEE
    };
    EntityType<?>[] NEUTRAL_CREATURES = {
            EntityType.DOLPHIN,
            EntityType.PANDA,
            EntityType.POLAR_BEAR
    };
    EntityType<?>[] NEUTRAL_GOLEMS = {
            EntityType.IRON_GOLEM
    };
    EntityType<?>[] NEUTRAL_RIDEABLE = {
            EntityType.LLAMA,
            EntityType.TRADER_LLAMA
    };
    EntityType<?>[] NEUTRAL_TAMEABLE = {
            EntityType.WOLF
    };

    /*
     * Hostile Entity Class
     */
    EntityType<?>[] HOSTILE = {
            EntityType.BLAZE,
            EntityType.CAVE_SPIDER,
            EntityType.CREEPER,
            EntityType.DROWNED,
            EntityType.ELDER_GUARDIAN,
            EntityType.ENDERMAN,
            EntityType.ENDERMITE,
            EntityType.EVOKER,
            EntityType.GHAST,
            EntityType.GUARDIAN,
            EntityType.field_233588_G_, // Hoglin
            EntityType.HUSK,
            EntityType.ILLUSIONER,
            EntityType.PHANTOM,
            EntityType.field_233591_ai_, // Piglin
            EntityType.PILLAGER,
            EntityType.RAVAGER,
            EntityType.SHULKER,
            EntityType.SILVERFISH,
            EntityType.SKELETON,
            EntityType.SPIDER,
            EntityType.STRAY,
            EntityType.VEX,
            EntityType.VINDICATOR,
            EntityType.WITCH,
            EntityType.WITHER_SKELETON,
            EntityType.field_233590_aW_, // Zoglin
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.field_233592_ba_ // Zombified Piglin
    };
    EntityType<?>[] HOSTILE_ARTHROPODS = {
            EntityType.CAVE_SPIDER,
            EntityType.ENDERMITE,
            EntityType.SILVERFISH,
            EntityType.SPIDER
    };
    EntityType<?>[] HOSTILE_END = {
            EntityType.ENDERMAN,
            EntityType.SHULKER
    };
    EntityType<?>[] HOSTILE_GUARDIANS = {
            EntityType.ELDER_GUARDIAN,
            EntityType.GUARDIAN
    };
    EntityType<?>[] HOSTILE_ILLAGERS = {
            EntityType.EVOKER,
            EntityType.ILLUSIONER,
            EntityType.PILLAGER,
            EntityType.RAVAGER,
            EntityType.VEX,
            EntityType.VINDICATOR
    };
    EntityType<?>[] HOSTILE_NETHER = {
            EntityType.BLAZE,
            EntityType.GHAST,
            EntityType.field_233588_G_, // Hoglin
            EntityType.field_233591_ai_, // Piglin
    };
    EntityType<?>[] HOSTILE_OVERWORLD = {
            EntityType.CREEPER,
            EntityType.PHANTOM,
            EntityType.WITCH
    };
    EntityType<?>[] HOSTILE_SKELETONS = {
            EntityType.SKELETON,
            EntityType.STRAY,
            EntityType.WITHER_SKELETON
    };
    EntityType<?>[] HOSTILE_ZOMBIES = {
            EntityType.DROWNED,
            EntityType.HUSK,
            EntityType.field_233590_aW_, // Zoglin
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.field_233592_ba_, // Zombified Piglin
    };
}

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

// I think I now understand why Object-Oriented Programming is so popular
public class ConfigJson {
    /*
     * JSON Object Definitions
     */
    public static class SpawnConfig {
        /*
        configObject.spawnConfig.scaleNaturallySpawned,
                    configObject.spawnConfig.scaleTriggerSpawned,
                    configObject.spawnConfig.scaleArtificiallySpawned,
                    configObject.spawnConfig.scaleManuallySpawned,
                    configObject.spawnConfig.scalePhantoms,
                    configObject.spawnConfig.keepConvertedScale
         */
        public boolean scaleNaturallySpawned = true;
        public boolean scaleTriggerSpawned = true;
        public boolean scaleArtificiallySpawned = true;
        public boolean scaleManuallySpawned = false;
        public boolean scalePhantoms = true;
    }

    public static class ScaleSettings {
        public String shouldScale = "false";
        public float maxGrow = 2.5f;
        public float maxShrink = 5.0f;
        public float fixedScale = 1.0f;
        public boolean shouldScaleHealth = false;
        public float maxHealthMultiplier = 2.5f;
        public float minHealthMultiplier = 0.2f;
        public boolean shouldScaleAttack = false;
        public float maxAttackMultiplier = 2.5f;
        public float minAttackMultiplier = 0.2f;
        public boolean shouldScaleView = true;
        public float maxViewMultiplier = 2.5f;
        public float minViewMultiplier = 0.2f;
    }

    public static class EntitySettings {
        public String id = null;
        public ScaleSettings settings = null;
    }

    public static class PassiveSettings extends ScaleSettings {
        public ScaleSettings creatures = null;
        public ScaleSettings golems = null;
        public ScaleSettings nether = null;
        public ScaleSettings rideable = null;
        public ScaleSettings tameable = null;
        public ScaleSettings villagers = null;
    }

    public static class NeutralSettings extends ScaleSettings {
        public ScaleSettings arthropods = null;
        public ScaleSettings creatures = null;
        public ScaleSettings golems = null;
        public ScaleSettings rideable = null;
        public ScaleSettings tameable = null;
    }

    public static class HostileSettings extends ScaleSettings {
        public ScaleSettings arthropods = null;
        public ScaleSettings end = null;
        public ScaleSettings guardians = null;
        public ScaleSettings illagers = null;
        public ScaleSettings nether = null;
        public ScaleSettings overworld = null;
        public ScaleSettings skeletons = null;
        public ScaleSettings zombies = null;
    }

    public static class ClassSettings {
        public PassiveSettings passive = null;
        public NeutralSettings neutral = null;
        public HostileSettings hostile = null;
    }

    /*
     * Class Definition (I know, it's really tiny compared to the JSON definitions)
     */

    SpawnConfig spawnConfig = null;
    String[] blacklistedEntities = null;
    ScaleSettings defaultSettings = null;
    EntitySettings[] entitySettings = null;
    ClassSettings classSettings = null;
}

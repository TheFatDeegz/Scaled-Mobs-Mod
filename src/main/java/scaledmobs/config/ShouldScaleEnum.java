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

public enum ShouldScaleEnum {
    RANDOM,
    FIXED,
    FALSE;

    public static ShouldScaleEnum fromString(String string) {
        switch (string.toUpperCase()) {
            case "RANDOM":
                return ShouldScaleEnum.RANDOM;
            case "FIXED":
                return ShouldScaleEnum.FIXED;
            case "FALSE":
                return ShouldScaleEnum.FALSE;
            default:
                throw new IllegalArgumentException("Invalid value for shouldScale \"" + string + "\"");
        }
    }
}

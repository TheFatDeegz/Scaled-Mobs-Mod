# Scaled Mobs Mod
A free, open-source mod for Minecraft 1.16.1 that allows you to customize the size of mobs in your
Minecraft world.

- Set a custom size for certain mobs in your Minecraft world
- Change the default size mobs spawn at in your world
- Make mobs spawn at random sizes in your world

## Installing
First, head over to the
[Forge website](https://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.16.1.html)
and install Minecraft Forge 1.16.1 version 32.0.108 or later if you haven't already.

Once you have Forge successfully installed,
[download a copy](https://github.com/TheFatDeegz/Scaled-Mobs-Mod/releases) of this mod (or
[compile it yourself](#building)). Once the file has finished downloading (or compiling), install
it to your `mods` folder in your `.minecraft` folder by dragging the `.jar` file into the `mods`
folder. The location of your `.minecraft` folder depends on your platform:

- On **Windows**, it is located at `C:\Users\<Your Username>\AppData\Roaming\.minecraft` (Hint: to
get to the `Roaming` folder quickly, simply type `%AppData%` in the address bar of File Explorer)
- On **macOS**, it is located at `/Users/<Your Username>/Library/Application Support/minecraft`
(or simply `~/Library/Application Support/minecraft`) (**DON'T** include the `.`)
- On **Linux**, it is located at `/home/<Your Username>/.minecraft` (or simly `~/.minecraft`)

Once you have successfully installed the mod, you will want to [configure it](#configuration) to
begin using it.

## <a name="building"></a> Building
**NOTE:** for most users, it is overkill to compile the mod yourself. You will most likely want to
download a precompiled binary for the mod unless you are making your own changes to the mod
(remember to follow the [license terms](#license))

In order to compile the mod yourself, you will need to have a JDK 8 install on your system. Once
you have that installed, download the source files for the mod by cloning the GitHub repository.
Once the source files are downloaded, the shell for your platform and `cd` to the directory you
saved the source files at (there should be `gradlew` and `gradlew.bat` files in there). To begin
the building process type the following command for your platform:

- On **Windows**, type `gradlew build`
- On **macOS** and **Linux**, type `./gradlew build`

Let Gradle run the build process for the mod. This may take several minutes to complete, so be
patient. Once the build process completes, your fresh new `.jar` file will be located in the
`build/libs` folder of the project directory.

## <a name="configuration"></a> Configuration
This mod has a lot of flexibility in terms of configuration. If you want a quick config preset,
look in the `config-presets` folder of the repository. Or if you want to make your own config
file, check out the `CONFIGURATION.md` file. All configuration files are placed in the `config`
folder located inside `.minecraft`

## Contributing
This mod is an open-source project freely available to the community. Contributions to the mod
will be accepted as long as you test your changes, keep code clean, and don't do anything that
breaks compatibility with older versions. This mod is still brand new, and has a lot of growth
potential. Contributing only makes it better. Your feedback and suggestions will be greatly
appreciated as I am new to Minecraft modding.

## License
This software is licensed under the terms of the GNU General Public License version 3. A copy of
the GNU GPLv3 is located in the LICENSE file. You are allowed to use and modify this software so
long as you comply with the terms of its license. All source code files are prepended with the
following text:

```
Scaled Mobs Mod
Copyright (c) 2020 Ryan Sammons

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
```
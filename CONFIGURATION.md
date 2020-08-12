# Configuration
The Scaled Mobs Mod provides several options for configuring of the mod. The mod mainly uses the
JSON file format for configuration. The Forge-provided `scaledmobs-common.toml` file contains a
single property to provide the name of the JSON format config file used by the mod:

```
#Name of the config file used by this mod (NOT THIS FILE!!!)
configName = "scaledmobs-common.json"
```

You are permitted to change the name of this file to your liking, but it is recommended to leave
this file at its defaults to prevent confusion.

## Config File Structure Overview
The JSON config file (named `scaledmobs-common.json` by default) gives you a wide variety of
options related to configuration of the mod. Here is an overview of the structure of the file:

```
{
  "spawnConfig": {
    "scaleNaturallySpawned": true|false,
    "scaleTriggerSpawned": true|false,
    "scaleArtificiallySpawned": true|false,
    "scaleManuallySpawned": true|false,
    "scalePhantoms": true|false
  },

  "blacklistedEntities": [
    <Namespaced IDs of entities to disable the scaling feature of> 
  ],

  "defaultSettings": {
    <The settings that are applied to by default to all entities>
  },

  "entitySettings": [
    <Settings specific to certain entities>
  ],

  "classSettings": {
    <Settings that apply to groups of entities>
  }
}
```

The `"spawnConfig"` object contains general settings that affect which mobs will be scaled upon
spawning. This gives you control over mob scaling based off of different spawn reasons.

The `"blacklistedEntities"` array is a list of mobs that have scaling capabilities completely
disabled. These mobs cannot be scaled and do not have any mod-applied properties.

The `"defaultSettings` object contains settings for mobs that do not have any specific spawning
settings applied to them. By default, scaling is disabled for mobs.

The `"entitySettings"` array contains settings that apply to specific mobs. This can be used to
tweak certain mobs without affecting the properties of others.

The `"classSettings"` object contains settings that apply to specific categories of mobs. This
can be used to apply settings to groups of mobs without individually specifying properties of
them.

Some settings may conflict with each other in the configuration file. Whenever this happens,
settings will be applied bottom to top (meaning the topmost settings will be effective):

1. Entity-Specific Settings
2. Class Settings
3. Default Settings

## Spawn Configuration
All properties in the `"spawnConfig"` object are booleans that can be set to `true` or `false`.
These settings control what types of spawns will result in mob scaling at spawn. All settings
default to true.

- #### `"scaleNaturallySpawned"`
  Scale mobs that are spawned naturally, such as world generation and monster spawning.

- #### `"scaleTriggerSpawned"`
  Scale mobs that are spawned as a result of player triggers, such as mob spawners or events.

- #### `"scaleArtificiallySpawned"`
  Scale mobs that are spawned through artificial means, such as by dispensers or buckets.

- #### `"scaleManuallySpawned"`
  Scale mobs that are spawned via spawn eggs or commands.

- #### `"scalePhantoms"`
  Forge does not have an event hook for Phantom spawning at the time of writing this, so scaling
  Phantoms will be handled differently. Regardless of the method Phantoms are spawned, if this is
  `true`, they will be scaled. `"scaleNaturallySpawned"` must also be `true` for this setting to be
  effective

## Entity Blacklist
Any entities that should not be able to be modified by this mod are listed in the
`"entityBlacklist"`. The format of this array is simple:

```
"blacklistedEntities": [
  "<Namespace of the mob>:<ID of the mob>",
  "<Namespace of the mob>:<ID of the mob>",
  ...
]
```

## Scaling and Attribute Settings Fields
Many settings in the configuration file follow the same format for changing scaling and attribute
settings for mobs. All settings fields are formatted as follows:

```
"shouldScale": "random|fixed|false",
"maxGrow": 2.5,
"maxShrink": 5.0,
"fixedScale": 1.0,
"shouldScaleHealth": true|false,
"maxHealthMultiplier": 2.5,
"minHealthMultiplier": 0.2,
"shouldScaleAttack": true|false,
"maxAttackMultiplier": 2.5,
"minAttackMultiplier": 0.2,
"shouldScaleView": true|false,
"maxViewMultiplier": 2.5,
"minViewMultiplier": 0.2
```

These fields may be encapsulated inside of an object depending the specification of certain
settings objects. These fields will be referred to as `<Scaling and Attribute Settings Fields>`.

- #### `"shouldScale"`
  Controls how mobs will be scaled at spawn. When set to `"random"`, mobs will spawn with random
  sizes in a range. When set to `"fixed"`, mobs will spawn at a fixed scale. When `"false"`, mobs
  will spawn at their default vanilla size. This is not a boolean, so make sure to set it to
  `"false"`, not `false`. By default, this will be `"false"`.

- #### `"maxGrow"`
  The maximum size multiplier that mobs can spawn at. Can be any positive value. Only applicable
  when `"shouldScale"` is set to `"random"`. By default, this will be `2.5`.

- #### `"maxShrink"`
  The maximum shrink multiplier that mobs can spawn at. The minimum size multiplier mobs can spawn
  at is `1 / maxShrink`. Can be any positive value. Only applicable when `"shouldScale"` is set to
  `"random"`. By default, this will be `5.0`.

- #### `"fixedScale"`
  The fixed scale multiplier that mobs will spawn at. Can be any positive value. Only applicable
  when `"shouldScale"` is set to `"fixed"`. By default, this will be `1.0`.

- #### `"shouldScaleHealth"`
  Whether the mobs' maximum health should scale with their size. When `"shouldScale"` is set to
  `"random"`, their health will be scaled based off of `"maxHealthMultiplier"` and
  `"minHealthMultiplier"`. When `"shouldScale"` is set to `"fixed"`, their health multiplier will
  be their scale multiplier. Can be `true` or `false`. Only applicable when `"shouldScale"` isn't
  set to `"false"`. By default, this will be `false`.

- #### `"maxHealthMultiplier"`
  When `"shouldScale"` is `"random"`, this is the maximum health multiplier that mobs can have at
  max size. Can be set to any positive value. Only applicable when `"shouldScale"` is `"random"`
  and `"shouldScaleHealth"` is `true`.

- #### `"minHealthMultiplier"`
  Like `"maxHealthMultiplier"`, but for shrinking mobs. Can be set to any positive value. Only
  applicable when `"shouldScale"` is `"random"` and `"shouldScaleHealth"` is `true`.

- #### `"shouldScaleAttack"`
  Like `"shouldScaleHealth"`, but for attack damage. Can be set to `true` or `false`. Only
  applicable when `"shouldScale"` isn't set to `"false"`. By default, this will be `false`.

- #### `"maxAttackMultiplier"`
  Like `"maxHealthMultiplier"`, but for attack damage. Can be set to any positive value. Only
  applicable when `"shouldScale"` is `"random"` and `"shouldScaleAttack"` is `true`. By default,
  this will be `2.5`.

- #### `"minAttackMultiplier"`
  Like `"minHealthMultiplier"`, but for attack damage. Can be set to any positive value. Only
  applicable when `"shouldScale"` is `"random"` and `"shouldScaleAttack"` is `true`. By default,
  this will be `0.2`.

- #### `"shouldScaleView"`
  Like `"shouldScaleHealth"`, but for view distance. Can be set to `true` or `false`. Only
  applicable when `"shouldScale"` isn't set to `"false"`. By default, this will be `false`.

- #### `"maxViewMultiplier"`
  Like `"maxHealthMultiplier"`, but for view distance. Can be set to any positive value. Only
  applicable when `"shouldScale"` is `"random"` and `"shouldScaleView"` is `true`. By default, this
  will be `2.5`.

- #### `"minViewMultiplier"`
  Like `"minHealthMultiplier"`, but for view distance. Can be set to any positive value. Only
  applicable when `"shouldScale"` is `"random"` and `"shouldScaleView"` is `true`. By default, this
  will be `0.2`.

## Default settings
These settings are for scaling spawned mobs that don't have any settings set for them. The format
of `"defaultSettings"` is simple:

```
"defaultSettings": {
  <Scaling and Attribute Settings Fields>
}
```

## Entity-Specific Settings
Mobs can have settings that apply specifically to them. Using the `"entitySettings"` array, you can
set settings individually for mobs:

```
"entitySettings": [
  {
    "name": "<Namespace of the mob>:<ID of the mob>",
    "settings": {
      <Scaling and Attribute Settings Fields>
    }
  },
  ...
]
```

## Class Settings
Groups of mobs can have settings applied to them based off of what categories they fall into. The
`"classSettings"` object has several groups of mobs that can have settings applied to them:

```
"classSettings": {
  "passive": {
    <Scaling and Attribute Settings Fields>
    "creatures": {
      <Scaling and Attribute Settings Fields>
    }
    "golems": { ... }
    "nether": { ... }
    "rideable": { ... }
    "tameable": { ... }
    "villagers": { ... }
  }
  "neutral": {
    <Scaling and Attribute Settings Fields>
    "arthropods": {
      <Scaling and Attribute Settings Fields>
    }
    "creatures": { ... }
    "golems": { ... }
    "rideable": { ... }
    "tameable": { ... }
  }
  "hostile": {
    <Scaling and Attribute Settings Fields>
    "arthropods": {
      <Scaling and Attribute Settings Fields>
    }
    "end": { ... }
    "guardians": { ... }
    "illagers": { ... }
    "nether": { ... }
    "overworld": { ... }
    "skeletons": { ... }
    "zombies": { ... }
  }
}
```

Each "class" of mobs has settings applied to all mobs in that category. "Subclasses" can also
be present applying settings to smaller mob categories. Some mobs in the `"passive"` mob category
will only be considered `"passive"` when the difficulty has been set to Peaceful. `"passive"`
settings for those mobs only apply in Peaceful difficulty.

### `"passive"` Mobs
- Bat
- Cat
- Chicken
- Cod
- Cow
- Donkey
- Fox
- Horse
- Mule
- Mooshroom
- Ocelot
- Parrot
- Pig
- Pufferfish
- Rabbit
- Salmon
- Sheep
- Skeleton Horse
- Snow Golem
- Squid
- Strider
- Tropical Fish
- Turtle
- Villager
- Wandering Trader
- Zombie Horse
- Hoglin (in Peaceful difficulty)
- Iron Golem (in Peaceful difficulty)
- Panda (in Peaceful difficulty)
- Piglin (in Peaceful difficulty)
- Polar Bear (in Peaceful difficulty)
- Wolf (in Peaceful difficulty)

### `"passive"."creatures"` Mobs
- Bat
- Chicken
- Cod
- Cow
- Fox
- Mooshroom
- Ocelot
- Pufferfish
- Rabbit
- Salmon
- Sheep
- Squid
- Tropical Fish
- Panda (in Peaceful difficulty)
- Polar Bear (in Peaceful difficulty)

### `"passive"."golems"` Mobs
- Snow Golem
- Iron Golem (in Peaceful difficulty)

### `"passive"."nether"` Mobs
- Hoglin (in Peaceful difficulty)
- Piglin (in Peaceful difficulty)

### `"passive"."tameable"` Mobs
- Cat
- Parrot
- Skeleton Horse
- Zombie Horse
- Wolf (in Peaceful difficulty)

### `"passive"."villagers"` Mobs
- Villager
- Wandering Trader

### `"neutral"` Mobs
- Bee
- Dolphin
- Iron Golem
- Llama
- Panda
- Polar Bear
- Trader Llama
- Wolf

### `"neutral"."arthropods"` Mobs
- Bee

### `"neutral"."creatures"` Mobs
- Dolphin
- Panda
- Polar Bear

### `"neutral"."golems"` Mobs
- Iron Golem

### `"neutral"."rideable"` Mobs
- Llama
- Trader Llama

### `"neutral"."tameable"` Mobs
- Wolf

### `"hostile"` Mobs
- Blaze
- Cave Spider
- Creeper
- Drowned
- Elder Guardian
- Enderman
- Endermite
- Evoker
- Ghast
- Guardian
- Hoglin
- Husk
- Illusioner
- Phantom
- Piglin
- Pillager
- Ravager
- Shulker
- Silverfish
- Skeleton
- Spider
- Stray
- Vex
- Vindicator
- Witch
- Wither Skeleton
- Zoglin
- Zombie
- Zombie Villager
- Zombified Piglin

### `"hostile"."arthropods"` Mobs
- Cave Spider
- Endermite
- Silverfish
- Spider

### `"hostile"."end"` Mobs
- Enderman
- Shulker

### `"hostile"."guardians"` Mobs
- Elder Guardian
- Guardian

### `"hostile"."illagers"` Mobs
- Evoker
- Illusioner
- Pillager
- Ravager
- Vex
- Vindicator

### `"hostile"."nether"` Mobs
- Blaze
- Ghast
- Hoglin
- Piglin

### `"hostile"."overworld"` Mobs
- Creeper
- Phantom
- Witch

### `"hostile"."skeletons"` Mobs
- Skeleton
- Stray
- Wither Skeleton

### `"hostile"."zombies"` Mobs
- Drowned
- Husk
- Zoglin
- Zombie
- Zombie Villager
- Zombified Piglin

## Notes
- Some configuration settings may not behave as expected or work at all due to limitations of
  Forge. Fixing this requires changes to forge implementing more hooks into vanilla events or
  workaround changes to the mod in the meantime.
- This configuration specification has the potential to grow to allow more control. In the future,
  expect the possibility to see new features implemented in the configuration file!
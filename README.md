# WEBlocks

A mod for Minecraft 1.7.10

## About
This mod is a fix for sk89q's WorldEdit to support blocks added by other mods. It has integrated support for CarpentersBlocks and most stair blocks. It also provides an API for other developers to make their blocks work with WorldEdit, alongside a configuration file for basic integration.

## Configuration

The configuration file is located at `/config/worldedit/`. It maps a blocks unlocalized name to a list of states. States basically represent the metadata of a block and the direction they're facing to.

Here's a small example. `dataMask` is optional. It is used to repeat the states internal via the bitwise "and" operator. For example, a value of 7 means that the states will repeat after the 8th entry.

```
{
  "modid:unlocalizedName": [
    {
      "dataMask": 7,
      "values": [
        {
          "meta": 0,
          "direction": [1.0, -1.0, 0.0]
        },
        {
          "meta": 1,
          "direction": [-1.0, -1.0, 0.0]
        },
        {
          "meta": 2,
          "direction": [0.0, -1.0, 1.0]
        },
        {
          "meta": 3,
          "direction": [0.0, -1.0, -1.0]
        },
        {
          "meta": 4,
          "direction": [1.0, 1.0, 0.0]
        },
        {
          "meta": 5,
          "direction": [-1.0, 1.0, 0.0]
        },
        {
          "meta": 6,
          "direction": [0.0, 1.0, 1.0]
        },
        {
          "meta": 7,
          "direction": [0.0, 1.0, -1.0]
        }
      ]
    }
  ]
}
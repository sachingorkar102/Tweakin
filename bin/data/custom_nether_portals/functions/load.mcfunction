scoreboard objectives add cusNetPor trigger "Custom Nether Portals"
scoreboard objectives add cusNetPor.config dummy "Custom Nether Portals Config"
scoreboard objectives add cusNetPor.dummy dummy
execute unless score #nonRectangular cusNetPor.config matches 0..1 run scoreboard players set #nonRectangular cusNetPor.config 1
execute unless score #cryingObsidian cusNetPor.config matches 0..1 run scoreboard players set #cryingObsidian cusNetPor.config 1
execute unless score #minSize cusNetPor.config matches 0.. run scoreboard players set #minSize cusNetPor.config 10
execute unless score #maxSize cusNetPor.config matches 0.. run scoreboard players set #maxSize cusNetPor.config 84
advancement revoke @a only custom_nether_portals:use_ignition
summon minecraft:area_effect_cloud ~ ~ ~ {Tags:["cusNetPor.marker"]}
execute if score #cryingObsidian cusNetPor.config matches 0 if block ~ ~ ~ minecraft:crying_obsidian run scoreboard players set #success cusNetPor.dummy -1
execute if block ~ ~ ~ #custom_nether_portals:obsidian run scoreboard players add #size cusNetPor.dummy 1
execute if score #size cusNetPor.dummy > #maxSize cusNetPor.config run scoreboard players set #success cusNetPor.dummy -1
execute if score #success cusNetPor.dummy matches 0 unless block ~ ~ ~ #custom_nether_portals:obsidian run function custom_nether_portals:continue_x
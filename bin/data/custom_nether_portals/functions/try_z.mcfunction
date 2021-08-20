scoreboard players set #success cusNetPor.dummy 0
scoreboard players set #size cusNetPor.dummy 0
function custom_nether_portals:try_to_iterate_z
execute if score #size cusNetPor.dummy < #minSize cusNetPor.config run scoreboard players set #success cusNetPor.dummy -1
execute unless score #nonRectangular cusNetPor.config matches 1 if score #success cusNetPor.dummy matches 0 as @e[type=minecraft:area_effect_cloud,tag=cusNetPor.marker] at @s if block ~ ~ ~ #custom_nether_portals:obsidian run function custom_nether_portals:check_z_diagonal
execute if score #success cusNetPor.dummy matches 0 at @e[type=minecraft:area_effect_cloud,tag=cusNetPor.marker] if block ~ ~ ~ #custom_nether_portals:air run setblock ~ ~ ~ minecraft:nether_portal[axis=z]
kill @e[type=minecraft:area_effect_cloud,tag=cusNetPor.marker]
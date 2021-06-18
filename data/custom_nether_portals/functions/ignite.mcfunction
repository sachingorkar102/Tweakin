scoreboard players set #ignited cusNetPor.dummy 1
scoreboard players set #success cusNetPor.dummy 0
scoreboard players set #size cusNetPor.dummy 0
function custom_nether_portals:try_to_iterate_x
execute if score #size cusNetPor.dummy < #minSize cusNetPor.config run scoreboard players set #success cusNetPor.dummy -1
execute unless score #nonRectangular cusNetPor.config matches 1 if score #success cusNetPor.dummy matches 0 at @e[type=minecraft:area_effect_cloud,tag=cusNetPor.marker] if block ~ ~ ~ #custom_nether_portals:obsidian run function custom_nether_portals:check_x_diagonal
execute if score #success cusNetPor.dummy matches 0 run function custom_nether_portals:create_portal_x
kill @e[type=minecraft:area_effect_cloud,tag=cusNetPor.marker]
execute unless score #success cusNetPor.dummy matches 1 run function custom_nether_portals:try_z
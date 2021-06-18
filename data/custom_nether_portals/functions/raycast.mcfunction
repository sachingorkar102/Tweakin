scoreboard players remove #steps cusNetPor.dummy 1
execute if score #ignited cusNetPor.dummy matches 0 if block ~ ~ ~ minecraft:fire unless block ^ ^ ^0.1 #custom_nether_portals:air align xyz run function custom_nether_portals:ignite
execute unless score #ignited cusNetPor.dummy matches 0 unless block ~ ~ ~ minecraft:fire run scoreboard players set #ignited cusNetPor.dummy 0
execute unless score #steps cusNetPor.dummy matches 0 positioned ^ ^ ^0.1 run function custom_nether_portals:raycast
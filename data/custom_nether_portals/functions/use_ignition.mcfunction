advancement revoke @s only custom_nether_portals:use_ignition
scoreboard players set #ignited cusNetPor.dummy 0
scoreboard players set #steps cusNetPor.dummy 50
execute anchored eyes positioned ^ ^ ^ run function custom_nether_portals:raycast
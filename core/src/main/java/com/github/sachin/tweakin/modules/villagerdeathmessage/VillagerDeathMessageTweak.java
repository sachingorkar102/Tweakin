package com.github.sachin.tweakin.modules.villagerdeathmessage;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

// Permission: tweakin.villagerdeathmessage.notify
@Tweak(name = "villager-death-message")
public class VillagerDeathMessageTweak extends BaseTweak implements Listener{

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("deprecation")
    public void onVillagerDeath(EntityDeathEvent e){
        if(e.getEntity() instanceof Villager && !getBlackListWorlds().contains(e.getEntity().getWorld().getName())){
            Location loc = e.getEntity().getLocation();
            String world;
            switch (e.getEntity().getWorld().getEnvironment()) {
                case NETHER:
                    world = "§cThe Nether";
                    break;
                case THE_END:
                    world = "§dThe End";
                default:
                    world = "§aOverworld";
                    break;
            }
            String message = getTweakManager().getMessageManager().getMessageWithoutPrefix("villager-death-message")
            .replace("%x%", String.valueOf(loc.getBlockX()))
            .replace("%y%", String.valueOf(loc.getBlockY()))
            .replace("%z%", String.valueOf(loc.getBlockZ()))
            .replace("%world-type%", world);
            for(Player player : Bukkit.getOnlinePlayers()){
                if(hasPermission(player, Permissions.VIL_DTH_MSG)){
                    player.sendMessage(message);
                }
            }
        }
    }
    
}

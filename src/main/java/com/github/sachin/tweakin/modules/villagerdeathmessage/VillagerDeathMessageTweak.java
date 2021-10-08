package com.github.sachin.tweakin.modules.villagerdeathmessage;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

// Permission: tweakin.villagerdeathmessage.notify
public class VillagerDeathMessageTweak extends BaseTweak implements Listener{

    public VillagerDeathMessageTweak(Tweakin plugin) {
        super(plugin, "villager-death-message");
    }

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
            Bukkit.broadcast(getTweakManager().getMessageManager().getMessageWithoutPrefix("villager-death-message")
            .replace("%x%", String.valueOf(loc.getBlockX()))
            .replace("%y%", String.valueOf(loc.getBlockY()))
            .replace("%z%", String.valueOf(loc.getBlockZ()))
            .replace("%world-type%", world), "tweakin.villagerdeathmessage.notify");
        }
    }
    
}

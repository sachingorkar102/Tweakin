package com.github.sachin.tweakin.modules.tradernotify;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

// Permission: tweakin.tradernotify
public class TraderNotifyTweak extends BaseTweak implements Listener{

    public TraderNotifyTweak(Tweakin plugin) {
        super(plugin, "trader-notify");
    }
/*
# a message will be broadcasted to all players when a wandering trader spawns near a player
# the message is configurable in messages.yml, optionally use placeholders like %x%,%y%,%z% and %world-type% to specidy accurate location
# Permission:
  # tweakin.tradernotify: players with this permission will only recieve the spawn notification 
trader-notify:
  enabled: false
  black-list-worlds: []  
*/


    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("deprecation")
    public void onTraderSpawn(EntitySpawnEvent e){
        if(e.getEntity() instanceof WanderingTrader && !getBlackListWorlds().contains(e.getEntity().getWorld().getName())){
            Location loc = e.getEntity().getLocation();
            WanderingTrader trader = (WanderingTrader) e.getEntity();
            
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
            String message = getTweakManager().getMessageManager().getMessageWithoutPrefix("trader-notify")
            .replace("%x%", String.valueOf(loc.getBlockX()))
            .replace("%y%", String.valueOf(loc.getBlockY()))
            .replace("%z%", String.valueOf(loc.getBlockZ()))
            .replace("%world-type%", world);
            if(!message.contains("%player%")){
                Bukkit.broadcast(message, "tweakin.villagerdeathmessage.notify");
                return;
            }
        }
    }
    
}

package com.github.sachin.tweakin.modules.wanderingtraderannouncements;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.TConstants;
import com.github.sachin.tweakin.utils.annotations.Config;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

@Tweak(name = "wandering-trader-announcement")
public class WanderingTraderAnnouncementTweak extends BaseTweak implements Listener {

    @Config(key = "range")
    private int range = 60;

    @EventHandler
    public void onTraderSpawnEvent(EntitySpawnEvent e){
        if(e.getEntity().getType() != EntityType.WANDERING_TRADER || containsWorld(e.getEntity().getWorld())) return;
        Entity trader = e.getEntity();
        for(Entity p : e.getEntity().getNearbyEntities(range,range,range)){
            if(p instanceof Player){
                Player player = (Player) p;
                if(hasPermission(player, Permissions.WANDDERING_TRADER_MSG)){
                    player.sendMessage(plugin.getMessageManager().getMessageWithoutPrefix(TConstants.WANDERING_TRADER_MSG)
                            .replace("%x%",String.valueOf(trader.getLocation().getBlockX()))
                            .replace("%y%",String.valueOf(trader.getLocation().getBlockY()))
                            .replace("%z%",String.valueOf(trader.getLocation().getBlockZ())));
                }
            }
        }


    }
}

package com.github.sachin.tweakin.modules.waterextinguish;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.annotations.Config;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import com.github.sachin.tweakin.utils.Permissions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;

@Tweak(name = "water-extinguish")
public class WaterExtinguishTweak extends BaseTweak implements Listener {

    @Config(key = "works-on-entities")
    private boolean extinguishEntities = true;

    @Config(key = "range")
    private int range = 2;

    @EventHandler
    public void onWaterSplash(PotionSplashEvent e){
        if(getBlackListWorlds().contains(e.getEntity().getWorld().getName())) return;
        ThrownPotion potion = e.getPotion();
        if(potion.isEmpty()){
            for(Entity en : potion.getNearbyEntities(range,range,range)){
                if(en.getFireTicks() == 0 || !(en instanceof LivingEntity)) continue;
                if(en instanceof Player){
                    Player player = (Player) en;
                    if(hasPermission(player, Permissions.WATER_EX)){
                        player.setFireTicks(0);
                    }
                    continue;
                }
                if(extinguishEntities){
                    en.setFireTicks(0);
                }
            }
        }
    }
}

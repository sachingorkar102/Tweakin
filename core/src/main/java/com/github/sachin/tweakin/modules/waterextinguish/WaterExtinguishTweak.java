package com.github.sachin.tweakin.modules.waterextinguish;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.Permissions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;

public class WaterExtinguishTweak extends BaseTweak implements Listener {

    private boolean extinguishEntities;
    private int range;

    public WaterExtinguishTweak(Tweakin plugin) {
        super(plugin, "water-extinguish");
    }

    @Override
    public void reload() {
        super.reload();
        extinguishEntities = getConfig().getBoolean("works-on-entities",true);
        range = getConfig().getInt("range",2);
    }

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

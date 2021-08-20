package com.github.sachin.tweakin.snowballknockback;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

public class SnowBallKnockBackTweak extends BaseTweak implements Listener{

    public SnowBallKnockBackTweak(Tweakin plugin) {
        super(plugin, "snowball-knockback");
    }

    @EventHandler
    public void onShoot(ProjectileHitEvent e){
        if(e.getEntityType() == EntityType.SNOWBALL || e.getEntityType() == EntityType.EGG) {
            Entity hitEntity = e.getHitEntity();
            if(hitEntity != null){
                if(getBlackListWorlds().contains(hitEntity.getWorld().getName())) return;
                hitEntity.setVelocity(e.getEntity().getVelocity().multiply(getConfig().getDouble("modifier")));

                if(hitEntity instanceof LivingEntity){
                    new BukkitRunnable(){

                        @Override
                        public void run() {
                            ((LivingEntity)hitEntity).damage(getConfig().getDouble("damage"));
                        }
                        
                    }.runTaskLater(plugin, 2L);
                }
            }
        }
    }


    
}

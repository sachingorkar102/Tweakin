package com.github.sachin.tweakin.modules.snowballknockback;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

public class SnowBallKnockBackTweak extends BaseTweak implements Listener{

    private SBKFlag flag;

    public SnowBallKnockBackTweak(Tweakin plugin) {
        super(plugin, "snowball-knockback");
        if(plugin.isWorldGuardEnabled){
            this.flag = (SBKFlag) plugin.getWGFlagManager().getFlag(TConstants.SBK_FLAG);
        }
    }

    @EventHandler
    public void onShoot(ProjectileHitEvent e){
        if(e.getEntityType() == EntityType.SNOWBALL || e.getEntityType() == EntityType.EGG) {
            Entity hitEntity = e.getHitEntity();
            if(hitEntity != null){
                if(getBlackListWorlds().contains(hitEntity.getWorld().getName())) return;
                if(flag != null && !flag.queryFlag(hitEntity.getLocation())){
                    return;
                }
                hitEntity.setVelocity(e.getEntity().getVelocity().multiply(getConfig().getDouble("modifier")));

                if(hitEntity instanceof LivingEntity){
                    new BukkitRunnable(){

                        @Override
                        public void run() {
                            if(e.getEntity().getShooter() instanceof Player){
                                ((LivingEntity)hitEntity).damage(getConfig().getDouble("damage"),(Player)e.getEntity().getShooter());
                            }
                            else{
                                ((LivingEntity)hitEntity).damage(getConfig().getDouble("damage"));
                            }
                        }
                    }.runTaskLater(plugin, 2L);
                }
            }
        }
    }


    
}

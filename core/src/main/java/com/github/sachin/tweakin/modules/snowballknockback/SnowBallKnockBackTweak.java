package com.github.sachin.tweakin.modules.snowballknockback;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;
import com.github.sachin.tweakin.utils.Tweak;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@Tweak(name = "snowball-knockback")
public class SnowBallKnockBackTweak extends BaseTweak implements Listener{

    private SBKFlag flag;


    @Override
    public void onLoad() {
        super.onLoad();
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
                if(hitEntity instanceof Player){
                    UUID uuid = hitEntity.getUniqueId();
                    boolean isActualPlayer = false;
                    for(Player player : Bukkit.getOnlinePlayers()){
                        if(player.getUniqueId().equals(uuid)){
                            isActualPlayer = true;
                            break;
                        }
                    }
                    if(!isActualPlayer) return;
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

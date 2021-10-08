package com.github.sachin.tweakin.modules.patdogs;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PatDogTweak extends BaseTweak implements Listener{

    public PatDogTweak(Tweakin plugin) {
        super(plugin, "pat-dogs");
    }

    @EventHandler
    public void onPat(PlayerInteractEntityEvent e){
        if(e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        if(player.getInventory().getItemInMainHand().getType() != Material.AIR || !player.isSneaking()) return;
        if(e.getRightClicked() instanceof Wolf && player.hasPermission("tweakin.patdog")){
            Wolf wolf = (Wolf) e.getRightClicked();
            PatTime time = new PatTime(wolf);
            if(!time.canPet(getConfig().getLong("cooldown",20)) || !wolf.isSitting()) return;
            Location loc = wolf.getLocation();
            player.getWorld().spawnParticle(Particle.HEART, loc.add(0, 0.5, 0), 1, 0, 0, 0, 0.1);
            player.getWorld().playSound(wolf.getLocation(), Sound.ENTITY_WOLF_WHINE, 1F, 0.5F + (float) Math.random() * 0.5F);
            player.swingMainHand();
            time.setPetTime();
            if(getConfig().getBoolean("heal") && wolf.getHealth() < wolf.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() ){
                double health = wolf.getHealth()+Math.random();
                if(health < 20){
                    wolf.setHealth(health);
                }
            }

            e.setCancelled(true);
        }
        else if(e.getRightClicked() instanceof Cat && player.hasPermission("tweakin.patcat") && getConfig().getBoolean("pat-cats",false)){
            Cat cat = (Cat) e.getRightClicked();
            PatTime time = new PatTime(cat);
            if(!time.canPet(getConfig().getLong("cooldown",20)) || !cat.isSitting()) return;
            Location loc = cat.getLocation();
            player.getWorld().spawnParticle(Particle.HEART, loc.add(0, 0.5, 0), 1, 0, 0, 0, 0.1);
            player.getWorld().playSound(cat.getLocation(), Sound.ENTITY_CAT_PURREOW, 1F, 0.5F + (float) Math.random() * 0.5F);
            player.swingMainHand();
            time.setPetTime();
            if(getConfig().getBoolean("heal") && cat.getHealth() < cat.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() ){
                double health = cat.getHealth()+Math.random();
                if(health < 20){
                    cat.setHealth(health);
                }
            }

            e.setCancelled(true);
        }
        
    }
    
    
}

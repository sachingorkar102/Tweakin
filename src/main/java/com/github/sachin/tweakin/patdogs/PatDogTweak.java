package com.github.sachin.tweakin.patdogs;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
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
        if(!(e.getRightClicked() instanceof Wolf) || e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        if(player.getInventory().getItemInMainHand().getType() == Material.AIR && player.isSneaking() && player.hasPermission("tweakin.patdog")){
            Wolf wolf = (Wolf) e.getRightClicked();
            PatTime time = new PatTime(wolf);
            if(!time.canPet(getConfig().getLong("cooldown",20)) || !wolf.isSitting()) return;
            Location loc = wolf.getLocation();
            player.getWorld().spawnParticle(Particle.HEART, loc.add(0, 0.5, 0), 1, 0, 0, 0, 0.1);
            player.getWorld().playSound(wolf.getLocation(), Sound.ENTITY_WOLF_WHINE, 1F, 0.5F + (float) Math.random() * 0.5F);
            player.swingMainHand();
            time.setPetTime();
            if(getConfig().getBoolean("heal") && wolf.getHealth() < wolf.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() ){
                wolf.setHealth(wolf.getHealth()+Math.random());
            }

            e.setCancelled(true);
        }
        
    }
    
    
}

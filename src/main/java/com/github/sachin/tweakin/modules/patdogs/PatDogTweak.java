package com.github.sachin.tweakin.modules.patdogs;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Random;

@Tweak(name = "pat-dogs")
public class PatDogTweak extends BaseTweak implements Listener{


    @EventHandler
    public void onPat(PlayerInteractEntityEvent e){
        if(e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        if(player.getInventory().getItemInMainHand().getType() != Material.AIR || !player.isSneaking()) return;
        Attribute maxHealthAttribute = Registry.ATTRIBUTE.get(NamespacedKey.minecraft("max_health"));
        if(maxHealthAttribute==null){
            maxHealthAttribute = Registry.ATTRIBUTE.get(NamespacedKey.minecraft("generic.max_health"));
        }
        if(maxHealthAttribute==null) {
            return;
        }
        if(e.getRightClicked() instanceof Wolf && hasPermission(player, Permissions.PAT_DOG)){
            Wolf wolf = (Wolf) e.getRightClicked();
            PatTime time = new PatTime(wolf);
            if(!time.canPet(getConfig().getLong("cooldown",20)) || !wolf.isSitting()) return;
            Location loc = wolf.getLocation();
            player.getWorld().spawnParticle(Particle.HEART, loc.add(0, 0.5, 0), 1, 0, 0, 0, 0.1);
            player.getWorld().playSound(wolf.getLocation(), Sound.ENTITY_WOLF_WHINE, 1F, 0.5F + (float) Math.random() * 0.5F);
            player.swingMainHand();
            time.setPetTime();


            double maxHealth = wolf.getAttribute(maxHealthAttribute).getValue();
            if(getConfig().getBoolean("heal") && wolf.getHealth() < maxHealth ){
                double health = wolf.getHealth()+(new Random().nextBoolean() ? 0.5 : 0.0);
                if(health <= maxHealth){
                    wolf.setHealth(health);
                }
            }

            e.setCancelled(true);
        }
        else if(e.getRightClicked() instanceof Cat && hasPermission(player, Permissions.PAT_CAT) && getConfig().getBoolean("pat-cats",false)){
            Cat cat = (Cat) e.getRightClicked();
            PatTime time = new PatTime(cat);
            if(!time.canPet(getConfig().getLong("cooldown",20)) || !cat.isSitting()) return;
            Location loc = cat.getLocation();
            player.getWorld().spawnParticle(Particle.HEART, loc.add(0, 0.5, 0), 1, 0, 0, 0, 0.1);
            player.getWorld().playSound(cat.getLocation(), Sound.ENTITY_CAT_PURREOW, 1F, 0.5F + (float) Math.random() * 0.5F);
            player.swingMainHand();
            time.setPetTime();
            double maxHealth = cat.getAttribute(maxHealthAttribute).getValue();
            if(getConfig().getBoolean("heal") && cat.getHealth() < maxHealth ){
                double health = cat.getHealth()+(new Random().nextBoolean() ? 0.5 : 0.0);
                if(health <= maxHealth){
                    cat.setHealth(health);
                }
            }

            e.setCancelled(true);
        }
        
    }
    
    
}

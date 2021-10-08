package com.github.sachin.tweakin.modules.poisonpotatousage;

import java.util.ArrayList;
import java.util.List;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

// Permission: tweakin.posionpotato.use
public class PoisonPotatoUsageTweak extends BaseTweak implements Listener{

    private double chance;
    private List<EntityType> blackListAnimals = new ArrayList<>();

    public PoisonPotatoUsageTweak(Tweakin plugin) {
        super(plugin, "poison-potato-usage");
    }

    @Override
    public void reload() {
        super.reload();
        this.chance = getConfig().getDouble("chance",0.5);
        List<String> list = getConfig().getStringList("black-list-animals");
        list.forEach(s -> {
            if(EntityType.valueOf(s) != null){
                blackListAnimals.add(EntityType.valueOf(s));
            }
        });
    }
    
    @EventHandler
    public void onPotatoFeed(PlayerInteractEntityEvent e){
        if(!(e.getRightClicked() instanceof Breedable) || e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        if(!player.hasPermission("tweakin.posionpotato.use")) return;
        if(getBlackListWorlds().contains(player.getWorld().getName())) return;
        if(blackListAnimals.contains(e.getRightClicked().getType())) return;
        if(player.getInventory().getItemInMainHand() == null) return;
        if(player.getInventory().getItemInMainHand().getType() != Material.POISONOUS_POTATO) return;
        Breedable entity = (Breedable) e.getRightClicked();
        if(entity.isAdult() || entity.getAgeLock()) return;
        
        World world = entity.getWorld();
        if(Math.random() < chance){
            entity.setAgeLock(true);
            PotionEffect effect = new PotionEffect(PotionEffectType.POISON, 55, 1);
            entity.addPotionEffect(effect);
        }
        else{
            world.playEffect(entity.getEyeLocation(), Effect.SMOKE, 12);
        }
        e.setCancelled(true);
        world.playSound(entity.getLocation(), Sound.ENTITY_GENERIC_EAT, 0.2f, 0.8f);

        player.swingMainHand();
        if(player.getGameMode() == GameMode.SURVIVAL){
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1);
        }
    }
}

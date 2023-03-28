package com.github.sachin.tweakin.modules.chickenshearing;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.ItemBuilder;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.TConstants;
import com.github.sachin.tweakin.utils.annotations.Config;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

@Tweak(name = "chicken-shearing")
public class ChickenShearingTweak extends BaseTweak implements Listener {

    private int minDamage=0;
    private int maxDamage=0;

    @Config(key="cooldown")
    private int cooldown = 20;

    @Config(key="drop-feathers-on-death")
    private boolean dropFeathersOnDeath = false;

    @Config(key="dispenser-usable")
    private boolean dispenserUsable = true;

    @Override
    public void reload() {
        super.reload();
        String[] damage = getConfig().getString("damage").split("-");
        minDamage = Integer.parseInt(damage[0]);
        maxDamage = Integer.parseInt(damage[1]);
    }

    @EventHandler
    public void onDispenseItem(BlockDispenseEvent e){
        if(e.getBlock().getType() == Material.DISPENSER && dispenserUsable){

            if(e.getItem().getType()==Material.SHEARS){

                Directional directional = (Directional) e.getBlock().getBlockData();
                Block relative = e.getBlock().getRelative(directional.getFacing());
                ItemStack shears = e.getItem();
                for(Entity en : relative.getWorld().getNearbyEntities(relative.getLocation().add(0.5,0.5,0.5),0.5,0.5,0.5, en -> en.getType()== EntityType.CHICKEN && ((Chicken)en).isAdult() && !en.isDead())){
                    relative.getWorld().playSound(en.getLocation(), Sound.ENTITY_SHEEP_SHEAR,1,1);
                    if(!en.getPersistentDataContainer().has(TConstants.SHEARED_CHICKEN_KEY, PersistentDataType.INTEGER)){
                        en.getPersistentDataContainer().set(TConstants.SHEARED_CHICKEN_KEY,PersistentDataType.INTEGER,1);
                    }
                    ((Chicken)en).damage(ThreadLocalRandom.current().nextInt(minDamage,maxDamage));
                    en.getWorld().dropItem(en.getLocation(), new ItemStack(Material.FEATHER));
                    Dispenser dispenser = (Dispenser) e.getBlock().getState();
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            ItemStack damagedShears = ItemBuilder.damageItem(1,shears.clone(),plugin.RANDOM,null);
                            int slot = dispenser.getInventory().first(shears);
                            if(slot != -1){
                                dispenser.getSnapshotInventory().setItem(slot,damagedShears);
                                dispenser.update(true);
                            }
                        }
                    }.runTaskLater(plugin,1);
                    break;
                }

            }
        }
    }


    @EventHandler
    public void onShearsUsed(PlayerInteractEntityEvent e){
        if(e.getRightClicked() instanceof Chicken){
            Chicken chicken = (Chicken) e.getRightClicked();
            Player player = e.getPlayer();
            ItemStack shears = player.getInventory().getItem(e.getHand());
            if(shears != null && shears.getType()== Material.SHEARS && chicken.isAdult() && !chicken.isDead() && player.getCooldown(Material.SHEARS) == 0 && hasPermission(player, Permissions.CHICKEN_SHEARING) && !containsWorld(player.getWorld())){
                swingHand(e.getHand(),player);
                if(player.getGameMode() != GameMode.CREATIVE){
                    player.getInventory().setItem(e.getHand(), ItemBuilder.damageItem(1,shears,plugin.RANDOM,player));
                }
                player.setCooldown(Material.SHEARS,cooldown);
                player.getWorld().playSound(chicken.getLocation(), Sound.ENTITY_SHEEP_SHEAR,1,1);
                if(!chicken.getPersistentDataContainer().has(TConstants.SHEARED_CHICKEN_KEY, PersistentDataType.INTEGER)){
                    chicken.getPersistentDataContainer().set(TConstants.SHEARED_CHICKEN_KEY,PersistentDataType.INTEGER,1);
                }
                chicken.damage(ThreadLocalRandom.current().nextInt(minDamage,maxDamage));
                player.getWorld().dropItem(chicken.getLocation(), new ItemStack(Material.FEATHER));
            }
        }
    }

    @EventHandler
    public void onChickenDeath(EntityDeathEvent e){
        if(e.getEntity() instanceof Chicken && !dropFeathersOnDeath && e.getEntity().getPersistentDataContainer().has(TConstants.SHEARED_CHICKEN_KEY,PersistentDataType.INTEGER)){
            Chicken chicken = (Chicken) e.getEntity();
            e.getDrops().removeIf(item -> item.getType()==Material.FEATHER);
        }
    }
}

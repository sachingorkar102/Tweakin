package com.github.sachin.tweakin.modules.chickenshearing;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.ItemBuilder;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Config;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;

import java.util.concurrent.ThreadLocalRandom;

@Tweak(name = "chicken-shearing")
public class ChickenShearingTweak extends BaseTweak implements Listener {

    private int minDamage=0;
    private int maxDamage=0;

    @Config(key="cooldown")
    private int cooldown = 20;

    @Override
    public void reload() {
        super.reload();
        String[] damage = getConfig().getString("damage").split("-");
        minDamage = Integer.parseInt(damage[0]);
        maxDamage = Integer.parseInt(damage[1]);
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
                chicken.damage(ThreadLocalRandom.current().nextInt(minDamage,maxDamage));
                player.getWorld().dropItem(chicken.getLocation(), new ItemStack(Material.FEATHER));
            }
        }
    }
}

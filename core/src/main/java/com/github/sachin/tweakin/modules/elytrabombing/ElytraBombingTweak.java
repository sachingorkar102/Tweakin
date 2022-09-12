package com.github.sachin.tweakin.modules.elytrabombing;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Config;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.Vector;
import scala.concurrent.impl.FutureConvertersImpl;

import java.util.ArrayList;
import java.util.List;

@Tweak(name = "elytra-bombing")
public class ElytraBombingTweak extends BaseTweak implements Listener {

    @Config(key="cooldown")
    private int cooldown = 40;

    @Config(key = "igniter-items")
    private List<String> igniterItems = new ArrayList<>();

    @EventHandler
    public void onIgniterClick(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(!player.isGliding() || e.getAction() != Action.RIGHT_CLICK_AIR) return;
        ItemStack igniter = e.getItem();
        if(igniter != null
                && hasPermission(player, Permissions.ELYTRABOMBING)
                && !containsWorld(player.getWorld())
                && igniterItems.contains(igniter.getType().toString())
                && player.getCooldown(Material.TNT) == 0){
            ItemStack tnt = null;
            if(false){
                ItemStack handItem = player.getInventory().getItemInMainHand();
                if(handItem.getType()==Material.TNT){
                    tnt = player.getInventory().getItemInMainHand();
                }
            }
            else{
                int slot = player.getInventory().first(Material.TNT);
                if(slot != -1){
                    tnt = player.getInventory().getItem(slot);
                }
            }
            if(tnt == null) return;
            if(player.getGameMode() != GameMode.CREATIVE){
                tnt.setAmount(tnt.getAmount()-1);
                if(igniter.getItemMeta() instanceof Damageable){
                    Damageable damageable = (Damageable) igniter.getItemMeta();
                    damageable.setDamage(damageable.getDamage()-1);
                    igniter.setItemMeta(damageable);
                }
                else{
                    igniter.setAmount(igniter.getAmount()-1);
                }

            }
            TNTPrimed tntPrimed = player.getWorld().spawn(player.getLocation(), TNTPrimed.class);
            tntPrimed.setVelocity(new Vector(0,0,0));
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS,1F,1F);
            player.setCooldown(Material.TNT,cooldown);

        }
    }
}

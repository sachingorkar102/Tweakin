package com.github.sachin.tweakin.modules.craftingtableonstick;

import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.Permissions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class CraftTableOnStick extends TweakItem implements Listener{

    public CraftTableOnStick(Tweakin plugin) {
        super(plugin, "crafting-table-on-stick");
    }

    @Override
    public void register() {
        super.register();
        registerRecipe();
    }

    @Override
    public void unregister() {
        super.unregister();
        unregisterRecipe();
    }


    @EventHandler
    public void onRightClick(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(hasItem(player, e.getHand()) && e.getAction()==Action.RIGHT_CLICK_AIR && player.hasPermission(Permissions.CRATINGTABLE_ON_STICK_USE)){
            player.openWorkbench(null, true);
        }
    }


}

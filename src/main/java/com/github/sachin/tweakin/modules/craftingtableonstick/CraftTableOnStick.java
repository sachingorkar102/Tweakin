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
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

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
    public void onRightClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        if(e.getCurrentItem() == null || e.getClick() != ClickType.MIDDLE) return;
        if((isSimilar(e.getCurrentItem()) || (getConfig().getBoolean("works-with-crafting-table") && e.getCurrentItem().getType()==Material.CRAFTING_TABLE)) && e.getClickedInventory() instanceof PlayerInventory && player.hasPermission(Permissions.CRATINGTABLE_ON_STICK_USE)){
            new BukkitRunnable(){
                @Override
                public void run() {
                    player.openWorkbench(null, true);
                }
            }.runTaskLater(plugin, 1);
        }
    }


}

package com.github.sachin.tweakin.modules.craftingtableonstick;

import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Config;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

@Tweak(name = "crafting-table-on-stick")
public class CraftTableOnStick extends TweakItem implements Listener{

    @Config(key = "works-with-crafting-table")
    private boolean worksWithCraftingTable = false;

    @Config(key = "works-in-world")
    private boolean worksInWorld = false;


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
        if(e.getCurrentItem() == null || !e.getClick().toString().equals(getConfig().getString("hotkey"))) return;
        if((isSimilar(e.getCurrentItem()) || (worksWithCraftingTable && e.getCurrentItem().getType()==Material.CRAFTING_TABLE)) && e.getClickedInventory() instanceof PlayerInventory && hasPermission(player, Permissions.CRAFTINGTABLE_USE)){
            e.setCancelled(true);
            new BukkitRunnable(){
                @Override
                public void run() {
                    player.openWorkbench(null, true);
                }
            }.runTaskLater(plugin, 1);
        }
    }

    @EventHandler
    public void onRightClickInWorld(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(!worksInWorld || e.getItem() == null || !player.isSneaking()) return;
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(isSimilar(e.getItem()) || (worksWithCraftingTable && e.getItem().getType()==Material.CRAFTING_TABLE)){
                e.setCancelled(true);
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        player.openWorkbench(null, true);
                    }
                }.runTaskLater(plugin, 1);
            }
        }
    }


}

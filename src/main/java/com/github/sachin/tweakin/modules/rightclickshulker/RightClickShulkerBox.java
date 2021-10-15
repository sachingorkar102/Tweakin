package com.github.sachin.tweakin.modules.rightclickshulker;

import java.util.Arrays;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

// permission: tweakin.shulkerboxclick,tweakin.enderchestclick
public class RightClickShulkerBox extends BaseTweak implements Listener{



    public RightClickShulkerBox(Tweakin plugin) {
        super(plugin, "shulker-box-preview");
    }

    @EventHandler
    public void shulkerGuiCloseEvent(InventoryCloseEvent e){
        if(e.getInventory().getHolder() instanceof ShulkerGui){
            ShulkerGui gui = (ShulkerGui) e.getInventory().getHolder();
            gui.update();
        }
    }

    @EventHandler
    public void shulkerGuiClickEvent(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        if(e.getView().getTopInventory().getHolder() instanceof ShulkerGui){
            ShulkerGui gui = (ShulkerGui) e.getView().getTopInventory().getHolder();
            gui.update();
            if((e.getClick() == ClickType.NUMBER_KEY && gui.getSlot() == e.getHotbarButton()) || getConfig().getBoolean("preview-only") || (e.getCurrentItem() != null && e.getCurrentItem().getType().name().endsWith("SHULKER_BOX"))){
                e.setCancelled(true);
            }
        }
        if(e.getCurrentItem() != null && e.getClickedInventory() instanceof PlayerInventory && e.getAction()==InventoryAction.NOTHING && e.getClick()==ClickType.MIDDLE){
            if(e.getCurrentItem().getType().toString().endsWith("_BOX") && player.hasPermission("tweakin.shulkerboxclick")){
                e.setCancelled(true);
                ItemStack item = e.getCurrentItem().clone();
                BlockStateMeta im = (BlockStateMeta) item.getItemMeta();
                ShulkerBox shulker = (ShulkerBox) im.getBlockState();
                String displayName = "Shulker Box";
                if(item.getItemMeta() != null){
                    ItemMeta meta = item.getItemMeta();
                    if(meta.hasDisplayName()){
                        displayName = item.getItemMeta().getDisplayName();
                    }
                }
                ShulkerGui gui = new ShulkerGui(player, shulker,e.getSlot(),item,displayName);
                gui.open();
            }
            else if(e.getCurrentItem().getType()==Material.ENDER_CHEST && player.hasPermission("tweakin.enderchestclick")){
                e.setCancelled(true);
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        player.openInventory(player.getEnderChest());   
                    }
                }.runTaskLater(plugin, 1);
            }
        }
    }
    
}

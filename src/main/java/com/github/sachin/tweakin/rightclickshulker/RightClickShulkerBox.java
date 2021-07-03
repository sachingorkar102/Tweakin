package com.github.sachin.tweakin.rightclickshulker;

import java.util.Arrays;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

// permission: tweakin.shulkerboxclick
public class RightClickShulkerBox extends BaseTweak implements Listener{



    public RightClickShulkerBox(Tweakin plugin) {
        super(plugin, "right-click-shulker-box");
    }

    @EventHandler
    public void shulkerBoxClickEvent(PlayerInteractEvent e){

        if(e.getAction() != Action.LEFT_CLICK_AIR) return;
        if(e.getHand() != EquipmentSlot.HAND) return;
        if(e.getItem() == null) return;
        if(!e.getItem().getType().toString().endsWith("SHULKER_BOX")) return;
        Player player = e.getPlayer();
        if(!player.hasPermission("tweakin.shulkerboxclick")) return;
        e.setCancelled(true);
        if(!player.isSneaking()) return;
        ItemStack item = e.getItem().clone();
        BlockStateMeta im = (BlockStateMeta) item.getItemMeta();
        ShulkerBox shulker = (ShulkerBox) im.getBlockState();
        ShulkerGui gui = new ShulkerGui(player, shulker,player.getInventory().getHeldItemSlot(),item);
        String displayName = "Shulker Box";
        if(item.getItemMeta() != null){
            ItemMeta meta = item.getItemMeta();
            if(meta.hasDisplayName()){
                displayName = item.getItemMeta().getDisplayName();
            }
        }
        Inventory inv = Bukkit.createInventory(gui, 27, displayName);
        gui.setInventory(inv);
        inv.setContents(shulker.getInventory().getContents());
        if(player.getInventory().getItemInMainHand() != null){
            if(player.getInventory().getItemInMainHand().getType().name().endsWith("SHULKER_BOX")){
                player.openInventory(inv);
            }
        }
    }

    @EventHandler
    public void shulkerGuiCloseEvent(InventoryCloseEvent e){
        if(e.getInventory().getHolder() instanceof ShulkerGui){
            ShulkerGui gui = (ShulkerGui) e.getInventory().getHolder();
            ItemStack shulkerBox = gui.getShulkerItem();
            BlockStateMeta im = (BlockStateMeta) shulkerBox.getItemMeta();
            ShulkerBox shulker = (ShulkerBox) im.getBlockState();
            shulker.getInventory().setContents(gui.getInventory().getContents());
            im.setBlockState(shulker);
            shulker.update();
            shulkerBox.setItemMeta(im);
            e.getPlayer().getInventory().setItemInMainHand(shulkerBox);
        }
    }

    @EventHandler
    public void shulkerGuiClickEvent(InventoryClickEvent e){
        if(e.getInventory().getHolder() instanceof ShulkerGui){
            if(e.getCurrentItem() == null) return;
            if(e.getCurrentItem().getType().name().endsWith("SHULKER_BOX")){
                e.setCancelled(true);
            }
        }
    }
    
}

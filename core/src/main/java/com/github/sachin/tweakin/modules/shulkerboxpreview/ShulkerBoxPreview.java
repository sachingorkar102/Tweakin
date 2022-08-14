package com.github.sachin.tweakin.modules.shulkerboxpreview;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.Tweak;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

// permission: tweakin.shulkerboxclick,tweakin.enderchestclick
@Tweak(name = "shulker-box-preview")
public class ShulkerBoxPreview extends BaseTweak implements Listener{




    @EventHandler
    public void shulkerGuiCloseEvent(InventoryCloseEvent e){
        if(e.getInventory().getHolder() instanceof ShulkerGui){
            ShulkerGui gui = (ShulkerGui) e.getInventory().getHolder();
            gui.update(true);


        }
    }

    @EventHandler
    public void shulkerGuiClickEvent(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        if(e.getView().getTopInventory().getHolder() instanceof ShulkerGui){
            ShulkerGui gui = (ShulkerGui) e.getView().getTopInventory().getHolder();
            gui.update(false);

            if((e.getClick() == ClickType.NUMBER_KEY && gui.getSlot() == e.getHotbarButton()) || getConfig().getBoolean("preview-only") || (e.getCurrentItem() != null && e.getCurrentItem().getType().name().endsWith("SHULKER_BOX"))){
                e.setCancelled(true);
            }
        }
        if(e.getCurrentItem() != null && e.getClickedInventory() instanceof PlayerInventory && e.getClick().toString().equals(getConfig().getString("hotkey"))){
            if(e.getCurrentItem().getType().toString().endsWith("_BOX") && hasPermission(player, Permissions.SHULKERBOX_CLICK)){
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
            else if(e.getCurrentItem().getType()==Material.ENDER_CHEST && hasPermission(player, Permissions.ENDERCHEST_CLICK)){
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

package com.github.sachin.tweakin.modules.rightclickarmor;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


// permission: tweakin.armorclick
public class RightClickArmor extends BaseTweak implements Listener {



    public RightClickArmor(Tweakin plugin) {
        super(plugin,"armor-right-click");
    }

    @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
    public void armorClickInAir(PlayerInteractEvent e){
        if(e.isCancelled()) return;
        if(e.getAction() != Action.RIGHT_CLICK_AIR || e.getItem() == null || e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        if(!player.hasPermission("tweakin.armorclick")) return;
        ItemStack item = e.getItem();
        String itemName = item.getType().toString();
        PlayerInventory inv = player.getInventory();
        ItemStack swapItem;
        if(itemName.endsWith("HELMET")){
            e.setCancelled(true);
            swapItem = inv.getHelmet();
            inv.setItem(EquipmentSlot.HEAD,item);
            inv.setItem(inv.getHeldItemSlot(),swapItem);
        }
        else if(itemName.endsWith("CHESTPLATE") || itemName.endsWith("ELYTRA")){
            e.setCancelled(true);
            swapItem = inv.getChestplate();
            inv.setItem(EquipmentSlot.CHEST,item);
            inv.setItem(inv.getHeldItemSlot(),swapItem);
        }
        else if(itemName.endsWith("LEGGINGS")){
            e.setCancelled(true);
            swapItem = inv.getLeggings();
            inv.setItem(EquipmentSlot.LEGS,item);
            inv.setItem(inv.getHeldItemSlot(),swapItem);
        }
        else if(itemName.endsWith("BOOTS")){
            e.setCancelled(true);
            swapItem = inv.getBoots();
            inv.setItem(EquipmentSlot.FEET,item);
            inv.setItem(inv.getHeldItemSlot(),swapItem);
        }
    }

    @EventHandler
    public void armorClickEvent(InventoryClickEvent e){
        if(e.getClick() != ClickType.RIGHT) return;
        if(e.getClickedInventory() != null && e.getClickedInventory().getType()==InventoryType.PLAYER){
            Player player = (Player) e.getWhoClicked();
            if(!player.hasPermission("tweakin.armorclick")) return;
            if(e.getCurrentItem() == null) return;
            ItemStack item = e.getCurrentItem().clone();
            String itemName = item.getType().name();
            PlayerInventory inv = player.getInventory();
            ItemStack swapItem;
            if(itemName.endsWith("HELMET")){
                e.setCancelled(true);
                swapItem = inv.getHelmet();
                inv.setItem(EquipmentSlot.HEAD,item);
                inv.setItem(e.getSlot(),swapItem);
            }
            else if(itemName.endsWith("CHESTPLATE") || itemName.endsWith("ELYTRA")){
                e.setCancelled(true);
                swapItem = inv.getChestplate();
                inv.setItem(EquipmentSlot.CHEST,item);
                inv.setItem(e.getSlot(),swapItem);
            }
            else if(itemName.endsWith("LEGGINGS")){
                e.setCancelled(true);
                swapItem = inv.getLeggings();
                inv.setItem(EquipmentSlot.LEGS,item);
                inv.setItem(e.getSlot(),swapItem);
            }
            else if(itemName.endsWith("BOOTS")){
                e.setCancelled(true);
                swapItem = inv.getBoots();
                inv.setItem(EquipmentSlot.FEET,item);
                inv.setItem(e.getSlot(),swapItem);
            }
        }
    }

}

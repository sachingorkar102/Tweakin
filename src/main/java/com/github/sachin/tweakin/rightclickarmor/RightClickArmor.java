package com.github.sachin.tweakin.rightclickarmor;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


// permission: tweakin.armorclick
public class RightClickArmor extends BaseTweak implements Listener {



    public RightClickArmor(Tweakin plugin) {
        super(plugin,"armor-right-click");
    }

    @EventHandler
    public void armorClickEvent(InventoryClickEvent e){
        if(e.getClick() != ClickType.RIGHT) return;
        if(e.getInventory().getType() != InventoryType.CRAFTING) return;
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

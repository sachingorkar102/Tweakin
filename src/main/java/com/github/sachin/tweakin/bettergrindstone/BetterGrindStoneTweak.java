package com.github.sachin.tweakin.bettergrindstone;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.scheduler.BukkitRunnable;

// Permission: tweakin.bettergrindstone
public class BetterGrindStoneTweak extends BaseTweak implements Listener{

    public BetterGrindStoneTweak(Tweakin plugin) {
        super(plugin, "better-grindstone");
    }


    @EventHandler
    public void onGrindStoneUse(InventoryClickEvent e){
        if(e.getClickedInventory() instanceof GrindstoneInventory){
            GrindstoneInventory inv = (GrindstoneInventory) e.getClickedInventory();
            ItemStack cursor = e.getCursor();
            Player player = (Player) e.getWhoClicked();
            if(cursor != null){
                if(e.getSlot() == 1 && cursor.getType() == Material.BOOK && inv.getItem(1) == null){
                    e.setCancelled(true);
                    cursor.setAmount(cursor.getAmount()-1);
                    inv.setItem(1, new ItemStack(Material.BOOK));
                }
            }
            if(!player.hasPermission("tweakin.bettergrindstone")) return;
            new BukkitRunnable(){
                @Override
                public void run() {
                    ItemStack weapon = inv.getItem(0);
                    ItemStack book = inv.getItem(1);
                    ItemStack result = inv.getItem(2);
                    if(weapon != null && book != null && result == null){
                        if(book.getType() == Material.BOOK && !weapon.getEnchantments().isEmpty() && weapon.getType() != Material.ENCHANTED_BOOK){
        
                            ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);
                            EnchantmentStorageMeta enchMeta = (EnchantmentStorageMeta) enchantedBook.getItemMeta();
                            for(Enchantment ench : weapon.getEnchantments().keySet()){
                                enchMeta.addStoredEnchant(ench, weapon.getEnchantmentLevel(ench), false);
                            }
                            enchantedBook.setItemMeta(enchMeta);
                            inv.setItem(2, enchantedBook);
                        }
                        
                    }
                
                }
            }.runTaskLater(plugin, 2);

        }
    }
    
}

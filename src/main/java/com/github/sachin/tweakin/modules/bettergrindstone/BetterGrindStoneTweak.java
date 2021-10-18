package com.github.sachin.tweakin.modules.bettergrindstone;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.nbtapi.NBTItem;
import com.github.sachin.tweakin.utils.compat.AdvancedEnchantments;
import com.github.sachin.tweakin.utils.compat.ExcellentEnchantsCompat;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;


// Permission: tweakin.bettergrindstone
public class BetterGrindStoneTweak extends BaseTweak implements Listener{

    private final NamespacedKey key = Tweakin.getKey("ignore-better-grindstone");

    public BetterGrindStoneTweak(Tweakin plugin) {
        super(plugin, "better-grindstone");
        
    }

    @EventHandler
    public void onItemEnchant(EnchantItemEvent e){
        if(getConfig().getBoolean("ignore-items-from-enchanting-table")){
            ItemMeta meta = e.getItem().getItemMeta();
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
            e.getItem().setItemMeta(meta);
        }
    }


    @EventHandler
    public void onGrindStoneUse(InventoryClickEvent e){
        if(e.getView().getTopInventory() instanceof GrindstoneInventory){
            GrindstoneInventory inv = (GrindstoneInventory) e.getView().getTopInventory();
            
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
                        if(weapon.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.INTEGER) && getConfig().getBoolean("ignore-items-from-enchanting-table")) return;
                        if(book.getType() == Material.BOOK && book.getAmount()==1 && !weapon.getEnchantments().isEmpty() && weapon.getType() != Material.ENCHANTED_BOOK){
                            NBTItem nbti = new NBTItem(weapon);
                            if(nbti.hasKey("armored-elytra")) return;
                            ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);
                            if(AdvancedEnchantments.isPluginEnabled){
                                AdvancedEnchantments.applyEnchantments(enchantedBook, weapon);
                            }
                            
                            else{
                                EnchantmentStorageMeta enchMeta = (EnchantmentStorageMeta) enchantedBook.getItemMeta();
                                for(Enchantment ench : weapon.getEnchantments().keySet()){
                                    enchMeta.addStoredEnchant(ench, weapon.getEnchantmentLevel(ench), false);
                                }
                                enchantedBook.setItemMeta(enchMeta);
                                if(ExcellentEnchantsCompat.isEnabled){
                                    ExcellentEnchantsCompat.applyEnchantMents(enchantedBook, weapon);
                                }
                            }
                            inv.setItem(2, enchantedBook);
                        }
                        
                    }
                
                }
            }.runTaskLater(plugin, 2);

        }
    }
    
}

package com.github.sachin.tweakin.infinitybucket;

import java.util.Map;

import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.meta.ItemMeta;

// tweakin.infinitybucket.craft,tweakin.infinitybucket.use
public class InfinityWaterBucketTweak extends TweakItem implements Listener{

	public InfinityWaterBucketTweak(Tweakin plugin) {
		super(plugin, "infinity-water-bucket");
	}

    @EventHandler
    public void onAnvilUse(PrepareAnvilEvent e){
        if(e.getView().getBottomInventory().getHolder() instanceof Player){
            Player player = (Player) e.getView().getBottomInventory().getHolder();
            if(!player.hasPermission("tweakin.infinitybucket.craft")) return;
            AnvilInventory inv = e.getInventory();
            
            ItemStack slot1 = inv.getItem(0);
            ItemStack slot2 = inv.getItem(1);
            if(slot1 == null || slot2 == null) return;
            if(slot1.getType() == Material.WATER_BUCKET && slot2.getType() == Material.ENCHANTED_BOOK){
                Map<Enchantment,Integer> enchs = slot2.getEnchantments();
                if(enchs.containsKey(Enchantment.ARROW_INFINITE) && enchs.get(Enchantment.ARROW_INFINITE)>0){
                    ItemStack result = getItem().clone();
                    if(!inv.getRenameText().equals("")){
                        ItemMeta meta = result.getItemMeta();
                        meta.setDisplayName(inv.getRenameText());
                        result.setItemMeta(meta);
                    }
                    e.setResult(result);
                }
            }
        }
    }

    @EventHandler
    public void onWaterBucketUse(PlayerBucketEmptyEvent e){
        Player player = e.getPlayer();
        if(hasItem(player, EquipmentSlot.HAND) || hasItem(player, EquipmentSlot.OFF_HAND)){
            if(player.hasPermission("tweakin.infinitybucket.use")){
                e.setItemStack(getItem());
            }
            else{
                e.setCancelled(true);
            }
        }
        
    }

    @EventHandler
    public void onFishPickUp(PlayerInteractEntityEvent e){
        Player player = e.getPlayer();
        if(e.getRightClicked() instanceof Fish){
            if(hasItem(player, e.getHand())){
                e.setCancelled(true);
                player.sendMessage(getTweakManager().getMessageManager().getMessage("cant-catch-fish"));
            }
        }
        else if(plugin.getVersion().startsWith("v1_17")){
            if(e.getRightClicked().getType().toString().equals("AXOLOTL")){
                e.setCancelled(true);
                player.sendMessage(getTweakManager().getMessageManager().getMessage("cant-catch-axolotl"));
            }
        }
    }


    
}

package com.github.sachin.tweakin.modules.infinitybucket;

import java.util.Map;

import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

// tweakin.infinitybucket.craft,tweakin.infinitybucket.use
public class InfinityWaterBucketTweak extends TweakItem implements Listener{

	public InfinityWaterBucketTweak(Tweakin plugin) {
		super(plugin, "infinity-water-bucket");
	}

    @EventHandler
    public void onDispenseItem(BlockDispenseEvent e){
        if(e.getBlock().getType() == Material.DISPENSER && getConfig().getBoolean("dispenser-usable")){
        
            if(getItem().isSimilar(e.getItem())){
                e.setCancelled(true);
                Directional directional = (Directional) e.getBlock().getBlockData();
                Block relative = e.getBlock().getRelative(directional.getFacing());
                if(relative.getType() == Material.AIR){
                    relative.getWorld().playSound(relative.getLocation(), Sound.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1, 1);
                    relative.setType(Material.WATER, true);
                }
                
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvilUse(PrepareAnvilEvent e){
        if(e.getView().getBottomInventory().getHolder() instanceof Player){
            Player player = (Player) e.getView().getBottomInventory().getHolder();
            if(!player.hasPermission("tweakin.infinitybucket.craft")) return;
            AnvilInventory inv = e.getInventory();
            
            ItemStack slot1 = inv.getItem(0);
            ItemStack slot2 = inv.getItem(1);
            if(slot1 == null || slot2 == null) return;
            if(slot1.getType() == Material.WATER_BUCKET && slot2.getType() == Material.ENCHANTED_BOOK){
                EnchantmentStorageMeta enchMeta = (EnchantmentStorageMeta) slot2.getItemMeta();
    
                Map<Enchantment,Integer> enchs = enchMeta.getStoredEnchants();
                if(enchs.containsKey(Enchantment.ARROW_INFINITE) && enchs.get(Enchantment.ARROW_INFINITE)>0){
                    ItemStack result = getItem().clone();
                    ItemMeta meta = result.getItemMeta();
                    meta.setDisplayName(inv.getRenameText());
                    result.setItemMeta(meta);
                    inv.setRepairCost(getConfig().getInt("cost"));
                    e.setResult(result);
                }
            }
        }
    }

    @EventHandler
    public void onWaterBucketUse(PlayerBucketEmptyEvent e){
        Player player = e.getPlayer();
        if(player.getInventory().getItemInMainHand().isSimilar(getItem()) || player.getInventory().getItemInOffHand().isSimilar(getItem())){
            if(player.hasPermission("tweakin.infinitybucket.use")){
                e.setItemStack(getItem());
            }
            else{
                e.setCancelled(true);
            }
        }
        
    }

    @EventHandler
    public void onFishPickUp(PlayerBucketEntityEvent e){
        Player player = e.getPlayer();
        ItemStack item = e.getOriginalBucket();
        if(item == null) return;
        if(!item.isSimilar(getItem())) return;
        if(e.getEntity() instanceof Fish){
            e.setCancelled(true);
            player.sendMessage(getTweakManager().getMessageManager().getMessage("cant-catch-fish"));
        }
        else if(plugin.getVersion().startsWith("v1_17")){
            if(e.getEntity().getType().toString().equals("AXOLOTL")){
                e.setCancelled(true);
                
                player.sendMessage(getTweakManager().getMessageManager().getMessage("cant-catch-axolotl"));
            }
        }
    }


    
}

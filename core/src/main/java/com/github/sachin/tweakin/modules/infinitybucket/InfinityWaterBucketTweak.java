package com.github.sachin.tweakin.modules.infinitybucket;

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
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

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
            if(!hasPermission(player,"tweakin.infinitybucket.craft")) return;
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
    public void onBucketUse(PlayerInteractEvent e){
        if(isSimilar(e.getItem()) && e.getAction()==Action.RIGHT_CLICK_BLOCK && e.useItemInHand()!=Result.DENY){
            Player player = e.getPlayer();
            e.setCancelled(true);
            if(!hasPermission(player,"tweakin.infinitybucket.use")) return;
            Block block = e.getClickedBlock().getRelative(e.getBlockFace());
            block.setType(Material.WATER,true);
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

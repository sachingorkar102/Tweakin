package com.github.sachin.tweakin.modules.infinitybucket;

import java.util.Map;

import com.github.sachin.tweakin.TweakItem;

import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Config;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Waterlogged;
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

// tweakin.infinitybucket.craft,tweakin.infinitybucket.use
@Tweak(name = "infinity-water-bucket")
public class InfinityWaterBucketTweak extends TweakItem implements Listener{

    @Config(key = "cooldown")
    public int cooldown = 10;

    @EventHandler
    public void onDispenseItem(BlockDispenseEvent e){
        if(e.getBlock().getType() == Material.DISPENSER && getConfig().getBoolean("dispenser-usable")){
        
            if(getItem().isSimilar(e.getItem())){
                e.setCancelled(true);
                Directional directional = (Directional) e.getBlock().getBlockData();
                Block relative = e.getBlock().getRelative(directional.getFacing());
                if(e.getBlock().getWorld().getEnvironment()==Environment.NETHER) return;
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
            if(!hasPermission(player, Permissions.INFIBUCKET_CRAFT)) return;
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
            e.setUseItemInHand(Result.DENY);
            if(player.getCooldown(Material.WATER_BUCKET) != 0){
                return;
            }
            if(!hasPermission(player, Permissions.INFIBUCKET_USE) || player.getWorld().getEnvironment()==Environment.NETHER) return;
            Block block = e.getClickedBlock().getRelative(e.getBlockFace());
            if(plugin.griefCompat != null && !plugin.griefCompat.canBuild(player,block.getLocation(),Material.WATER_BUCKET)) return;
            if(e.getClickedBlock().getBlockData() instanceof Waterlogged){
                Waterlogged waterlogged = (Waterlogged) e.getClickedBlock().getBlockData();
                if(!waterlogged.isWaterlogged()){
                    waterlogged.setWaterlogged(true);
                    e.getClickedBlock().setBlockData(waterlogged);
                    player.setCooldown(Material.WATER_BUCKET,cooldown);
                }
            }
            else if(!block.isEmpty() && block.getBlockData() instanceof Waterlogged){
                // idk, the BlockState#update method was not working so sorted to using nms
                plugin.getNMSHandler().placeWater(block);
                player.setCooldown(Material.WATER_BUCKET,cooldown);
            }
            else{
                block.setType(Material.WATER,true);
                player.setCooldown(Material.WATER_BUCKET,cooldown);
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
            return;
        }
        if(e.getEntity().getType().toString().equals("AXOLOTL")){
            e.setCancelled(true);
            player.sendMessage(getTweakManager().getMessageManager().getMessage("cant-catch-axolotl"));
        }
    }


    
}

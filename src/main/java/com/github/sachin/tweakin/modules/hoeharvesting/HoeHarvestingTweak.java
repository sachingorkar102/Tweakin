package com.github.sachin.tweakin.modules.hoeharvesting;

import java.util.Random;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;


public class HoeHarvestingTweak extends BaseTweak implements Listener{

    private final Random RANDOM = new Random();

    public HoeHarvestingTweak(Tweakin plugin) {
        super(plugin, "hoe-harvesting");
    }

    private int getRange(String itemType){
        switch (itemType) {
            case "WOODEN_HOE":
                return getConfig().getInt("range.wooden");
            case "STONE_HOE":
                return getConfig().getInt("range.stone");
            case "IRON_HOE":
                return getConfig().getInt("range.iron");
            case "DIAMOND_HOE":
                return getConfig().getInt("range.diamond");
            case "NETHERITE_HOE":
                return getConfig().getInt("range.netherite");
            default:
                return 1;
        }
    }



    @EventHandler
    public void onHarvest(BlockBreakEvent e){
        Player player = e.getPlayer();
        if(!player.isSneaking() && getConfig().getBoolean("require-sneaking")) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        Location breakedBlock = e.getBlock().getLocation();
        
        if(matchesHarvestable(e.getBlock().getType())){

            if(item == null) return;
            
            String itemType = item.getType().toString();
            if(!itemType.endsWith("_HOE")) return; 
            int range = getRange(itemType);
            for (int x = breakedBlock.getBlockX() - range; x <= breakedBlock.getBlockX() + range; x++){
                for (int z = breakedBlock.getBlockZ() - range; z <= breakedBlock.getBlockZ() + range; z++){
                    Location loc = new Location(breakedBlock.getWorld(), x, breakedBlock.getBlockY(), z);
                    Material blockType = loc.getBlock().getType();
                    if(matchesHarvestable(blockType)){
                        loc.getBlock().breakNaturally(item);
                        damageItem(1, item, RANDOM, player);
                    }  
                }
            }
        }
        
    }

    private ItemStack damageItem(int amount,ItemStack item,Random random,Player player){
        ItemMeta meta = item.getItemMeta();
        if(!(meta instanceof Damageable) || amount < 0) return item;
        int m = item.getEnchantmentLevel(Enchantment.DURABILITY);
        int k = 0;
        for (int l = 0; m > 0 && l < amount; l++) {
            if (random.nextInt(m +1) > 0){
                k++; 
            }
        }  
        amount -= k;
        if(player != null){
            PlayerItemDamageEvent damageEvent = new PlayerItemDamageEvent(player, item, amount);
            plugin.getServer().getPluginManager().callEvent(damageEvent);
            if(amount != damageEvent.getDamage() || damageEvent.isCancelled()){
                damageEvent.getPlayer().updateInventory();
            }
            else if(damageEvent.isCancelled()){
                return item;
            }
            amount = damageEvent.getDamage();

        }
        if (amount <= 0)
            return item; 
        
        Damageable damageable = (Damageable) meta;
        damageable.setDamage(damageable.getDamage()+amount);
        item.setItemMeta(meta);    
        return item;
    }

    private boolean matchesHarvestable(Material mat){
        return matchString(mat.toString(),getConfig().getStringList("harvestable-materials")) || matchTag(mat, getConfig().getStringList("harvestable-materials"));
    }
    
}

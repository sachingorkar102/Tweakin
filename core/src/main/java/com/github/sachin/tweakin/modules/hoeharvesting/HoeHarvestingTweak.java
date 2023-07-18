package com.github.sachin.tweakin.modules.hoeharvesting;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.ItemBuilder;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;


@Tweak(name = "hoe-harvesting")
public class HoeHarvestingTweak extends BaseTweak implements Listener{


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
        if(!hasPermission(player, Permissions.HOE_HARVESTING)) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        Location breakedBlock = e.getBlock().getLocation();
        
        if(matchesHarvestable(e.getBlock().getType())){

            if(item == null) return;
            
            String itemType = item.getType().toString();
            if(!itemType.endsWith("_HOE")) return; 
            int range = getRange(itemType);
            for (int x = breakedBlock.getBlockX() - range; x <= breakedBlock.getBlockX() + range; x++){
                for (int z = breakedBlock.getBlockZ() - range; z <= breakedBlock.getBlockZ() + range; z++){
//                    if(item == null) break;
                    Location loc = new Location(breakedBlock.getWorld(), x, breakedBlock.getBlockY(), z);
                    Material blockType = loc.getBlock().getType();
                    ItemMeta meta = item.getItemMeta();
                    Damageable damageable = (Damageable) meta;
                    if(damageable.getDamage()==item.getType().getMaxDurability()){
                        break;
                    }
                    if(matchesHarvestable(blockType)){
                        loc.getBlock().breakNaturally(item);
                        ItemBuilder.damageItem(1, item, plugin.RANDOM, player);
                    }  
                }
            }
        }
        
    }



    private boolean matchesHarvestable(Material mat){
        return matchString(mat.toString(),getConfig().getStringList("harvestable-materials")) || matchTag(mat, getConfig().getStringList("harvestable-materials"));
    }
    
}

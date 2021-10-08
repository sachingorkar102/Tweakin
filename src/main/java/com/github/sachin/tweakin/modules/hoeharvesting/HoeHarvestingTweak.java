package com.github.sachin.tweakin.modules.hoeharvesting;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;


public class HoeHarvestingTweak extends BaseTweak implements Listener{

    

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

    // Not in use
    // public boolean isHarvestAbleMaterial(Material type){
    //     String blockName = type.toString();
    //     for (String string : getConfig().getStringList("harvestable-materials")) {
    //         if(string.startsWith("^")){
    //             if(blockName.endsWith(string.replace("^", ""))){
    //                 return true;
    //             }
    //         }
    //         if(blockName.equals(string)){
    //             return true;
    //         }
    //     }
    //     return false;
    // }


    @EventHandler
    public void onHarvest(BlockBreakEvent e){
        Player player = e.getPlayer();
        if(!player.isSneaking()) return;
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
                        plugin.getNmsHelper().harvestBlock(player, loc, item);
                        
                    }
                }
            }
        }
        
    }

    private boolean matchesHarvestable(Material mat){
        return matchString(mat.toString(),getConfig().getStringList("harvestable-materials")) || matchTag(mat, getConfig().getStringList("harvestable-materials"));
    }
    
}

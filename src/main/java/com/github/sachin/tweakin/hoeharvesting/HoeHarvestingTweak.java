package com.github.sachin.tweakin.hoeharvesting;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class HoeHarvestingTweak extends BaseTweak implements Listener{

    public HoeHarvestingTweak(Tweakin plugin) {
        super(plugin, "hoe-harvesting");
    }


    @EventHandler
    public void onHarvest(BlockBreakEvent e){
        Block block = e.getBlock().getRelative(BlockFace.EAST);
        if(block.getType() != Material.AIR){
            plugin.getNmsHelper().harvestBlock(e.getPlayer(), block.getLocation(), e.getPlayer().getInventory().getItemInMainHand());
        }
    }
    
}

package com.github.sachin.tweakin.modules.cauldronconcrete;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.google.common.base.Enums;
import io.papermc.paper.event.entity.EntityInsideBlockEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class CauldronConcreteTweak extends BaseTweak implements Listener{

    public CauldronConcreteTweak(Tweakin plugin) {
        super(plugin, "cauldron-concrete");
    }

    @EventHandler
    public void onPowderDrop(EntityInsideBlockEvent e){
        Block block = e.getBlock();
        if(getBlackListWorlds().contains(e.getBlock().getWorld().getName())) return;
        if(block.getType()==Material.WATER_CAULDRON && e.getEntity() instanceof Item){
            Item item = (Item) e.getEntity();
            ItemStack concrete = item.getItemStack();
            if(!concrete.getType().toString().endsWith("_CONCRETE_POWDER")) return;
            Material mat = Enums.getIfPresent(Material.class, concrete.getType().toString().replace("_POWDER", "")).orNull();
            if(mat != null){
                item.getWorld().dropItem(item.getLocation(), new ItemStack(mat,concrete.getAmount()));
                item.remove();
            }
        }
    }
    
}

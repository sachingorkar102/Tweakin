package com.github.sachin.tweakin.modules.cauldronconcrete;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import com.google.common.base.Enums;
import io.papermc.paper.event.entity.EntityInsideBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

// tweakin.cauldronconcrete.use
@Tweak(name = "cauldron-concrete",clashingTeaksTweak = "Cauldron Concrete")
public class CauldronConcreteTweak extends BaseTweak implements Listener{

    @EventHandler
    public void onPowderDrop(EntityInsideBlockEvent e){
        Block block = e.getBlock();
        if(getBlackListWorlds().contains(e.getBlock().getWorld().getName())) return;
        if(block.getType()==Material.WATER_CAULDRON && e.getEntity() instanceof Item){
            Item item = (Item) e.getEntity();
            if(item.getThrower() != null){
                Entity thrower = Bukkit.getEntity(item.getThrower());
                if( thrower instanceof Player){
                    Player player = (Player) thrower;
                    if(!hasPermission(player,Permissions.CAULDRON_CONCRETE)) return;
                }
            }
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

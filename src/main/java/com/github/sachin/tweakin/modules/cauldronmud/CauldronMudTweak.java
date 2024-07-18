package com.github.sachin.tweakin.modules.cauldronmud;

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

// tweakin.cauldronmud.use
@Tweak(name = "cauldron-mud")
public class CauldronMudTweak extends BaseTweak implements Listener {

    @EventHandler
    public void onPowderDrop(EntityInsideBlockEvent e){
        Block block = e.getBlock();
        if(getBlackListWorlds().contains(e.getBlock().getWorld().getName())) return;
        if(block.getType()== Material.WATER_CAULDRON && e.getEntity() instanceof Item){
            Item item = (Item) e.getEntity();
            if(item.getThrower() != null){
                Entity thrower = Bukkit.getEntity(item.getThrower());
                if( thrower instanceof Player){
                    Player player = (Player) thrower;
                    if(!hasPermission(player, Permissions.CAULDRON_MUD)) return;
                }
            }
            ItemStack dirt = item.getItemStack();
            if(dirt.getType()!=Material.DIRT) return;
            item.getWorld().dropItem(item.getLocation(), new ItemStack(Material.MUD,dirt.getAmount()));
            item.remove();
        }
    }
}

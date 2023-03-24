package com.github.sachin.tweakin.modules.recyclablewax;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@Tweak(name = "recyclable-wax")
public class RecyclableWaxTweak extends BaseTweak implements Listener {


    @EventHandler
    public void onWaxOff(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(e.getAction()== Action.RIGHT_CLICK_BLOCK
                && e.getClickedBlock().getType().toString().startsWith("WAXED_")
                && e.getItem() != null
                && e.getItem().getType().toString().endsWith("_AXE")
                && !containsWorld(player.getWorld())){
            if(plugin.griefCompat != null && !plugin.griefCompat.canBuild(player,e.getClickedBlock().getLocation(),null)) return;
            player.getWorld().dropItemNaturally(e.getClickedBlock().getLocation().add(0,0.5,0),new ItemStack(Material.HONEYCOMB));
        }
    }
}

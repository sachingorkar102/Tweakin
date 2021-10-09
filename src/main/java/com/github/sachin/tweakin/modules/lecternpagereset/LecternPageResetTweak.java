package com.github.sachin.tweakin.modules.lecternpagereset;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.block.Lectern;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.LecternInventory;

public class LecternPageResetTweak extends BaseTweak implements Listener{

    public LecternPageResetTweak(Tweakin plugin) {
        super(plugin, "lectern-page-reset");
    }


    @EventHandler
    public void onLecterClose(InventoryCloseEvent e){
        if(e.getInventory().getType() == InventoryType.LECTERN && !getBlackListWorlds().contains(e.getPlayer().getWorld().getName())){
            LecternInventory inv = (LecternInventory) e.getInventory();
            Lectern lectern = inv.getHolder();
            lectern.setPage(0);
            lectern.update(true);
        }
    }
    
}

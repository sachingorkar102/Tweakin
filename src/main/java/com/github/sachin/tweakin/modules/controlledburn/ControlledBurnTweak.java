package com.github.sachin.tweakin.modules.controlledburn;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;

public class ControlledBurnTweak extends BaseTweak implements Listener{

    public ControlledBurnTweak(Tweakin plugin) {
        super(plugin, "controlled-burn");
    }

    @Override
    public void register() {
        registerEvents(this);
        registered = true;
    }

    @Override
    public void unregister() {
        unregisterEvents(this);
        registered = false;
    }

    @EventHandler
    public void fireSpreadEvent(BlockSpreadEvent e){
        if(e.getSource().getType() == Material.FIRE){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void blockBurnEvent(BlockBurnEvent e){
        e.getIgnitingBlock().setType(Material.AIR);
    }
    
}

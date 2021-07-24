package com.github.sachin.tweakin.gui;

import com.github.sachin.tweakin.Tweakin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GuiListener implements Listener{

    private Tweakin plugin;

    public GuiListener(Tweakin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getInventory().getHolder() instanceof PagedGuiHolder){
            PagedGuiHolder gui = (PagedGuiHolder) e.getInventory().getHolder();
            gui.handlePageClicks(e);
        }

    }

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if(e.getInventory().getHolder() instanceof PagedGuiHolder){
            plugin.getTweakManager().reload();
        }
    }
    
}

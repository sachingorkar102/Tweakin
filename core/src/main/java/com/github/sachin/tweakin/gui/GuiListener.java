package com.github.sachin.tweakin.gui;

import com.github.sachin.tweakin.Tweakin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

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
        if(!(e.getInventory().getHolder() instanceof PagedGuiHolder)) return;
        Player player = (Player) e.getPlayer();
        new BukkitRunnable(){
            @Override
            public void run() {
                if(!(player.getOpenInventory().getTopInventory().getHolder() instanceof PagedGuiHolder)){
                    plugin.getTweakManager().reload();
                }
            }
        }.runTaskLater(plugin, 5L);
    }
    
}

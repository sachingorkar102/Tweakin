package com.github.sachin.tweakin.modules.unconsumablearmortrims;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.SmithingInventory;

@Tweak(name = "unconsumable-armor-trims")
public class UnconsumableArmorTrimsTweak extends BaseTweak implements Listener {


    @EventHandler
    public void onSmithingTableUse(InventoryClickEvent e){
        if(!(e.getView().getTopInventory() instanceof SmithingInventory)) return;
        SmithingInventory inv = (SmithingInventory) e.getClickedInventory();
        System.out.println(e.getSlot());
    }

}

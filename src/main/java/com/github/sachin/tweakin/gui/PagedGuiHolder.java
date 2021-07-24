package com.github.sachin.tweakin.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.ItemBuilder;
import com.github.sachin.tweakin.utils.MiscItems;
import com.github.sachin.tweakin.utils.TConstants;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.server.v1_16_R1.ItemBucket;

public class PagedGuiHolder implements InventoryHolder{

    protected int page = 0;
    protected int maxItemsPerPage = 28;
    protected int index = 0;
    private Tweakin plugin;
    private MiscItems miscItems;
    private Player player;
    private Inventory inventory;
    private List<ItemStack> items = new ArrayList<>();

    public PagedGuiHolder(Tweakin plugin,Player player){
        this.plugin = plugin;
        this.player = player;
        this.miscItems = plugin.getMiscItems();
        for(BaseTweak t : plugin.getTweakManager().getTweakList()){
            if(t.shouldEnable()){
                items.add(getModifiedDisplay(t.getName(), miscItems.ENABLED_BUTTON));
            }
            else{
                items.add(getModifiedDisplay(t.getName(), miscItems.DISABLED_BUTTON));
            }
            plugin.getTweakManager().getGuiMap().put(t, t.shouldEnable());
        }
    }

    public ItemStack getModifiedDisplay(String display,ItemStack item){
        ItemStack cloned = item.clone();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(display);
        cloned.setItemMeta(meta);
        return cloned;
    }

    public BaseTweak getTweakName(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        String name = meta.getDisplayName();
        return plugin.getTweakManager().getTweakFromName(name);
    }


    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }



    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    public Player getPlayer() {
        return player;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void handlePageClicks(InventoryClickEvent e){
        e.setCancelled(true);
        if(e.getCurrentItem() == null) return;
        handleMiscClicks(e);
        if(ItemBuilder.hasKey(e.getCurrentItem(), "enabled-button")){
            e.setCurrentItem(getModifiedDisplay(e.getCurrentItem().getItemMeta().getDisplayName(), miscItems.DISABLED_BUTTON));
            BaseTweak t = getTweakName(e.getCurrentItem());
            if(t != null){
                plugin.getTweakManager().getGuiMap().put(t, false);
            }
        }
        else if(ItemBuilder.hasKey(e.getCurrentItem(), "disabled-button")){
            e.setCurrentItem(getModifiedDisplay(e.getCurrentItem().getItemMeta().getDisplayName(), miscItems.ENABLED_BUTTON));
            BaseTweak t = getTweakName(e.getCurrentItem());
            if(t != null){
                plugin.getTweakManager().getGuiMap().put(t, true);
            }
        }
        
        
    }

    public void handleMiscClicks(InventoryClickEvent e){
        if(ItemBuilder.hasKey(e.getCurrentItem(), "previous-button")){
            if (page != 0){
                page = page - 1;
                openPage();
            }
        }
        else if (ItemBuilder.hasKey(e.getCurrentItem(), "next-button")){
            if (!((index + 1) >= items.size())){
                page = page + 1;
                openPage();
            }
        }
    }

    public void openPage(){
        if(items.isEmpty()) return;
        this.inventory = Bukkit.createInventory(this, 54,"Tweaks");
        setBorderItems();
        player.openInventory(inventory); 
    }

    public void setBorderItems(){
        inventory.setItem(45, miscItems.FILLAR_GLASS);
        inventory.setItem(53,miscItems.FILLAR_GLASS);
        inventory.setItem(50,miscItems.FILLAR_GLASS);
        
        inventory.setItem(49, miscItems.INFO_PAPER);
        for (int slot : TConstants.BORDER_SLOTS) {
            inventory.setItem(slot, miscItems.FILLAR_GLASS);
        }
        for(int i = 0; i < getMaxItemsPerPage(); i++) {
            index = getMaxItemsPerPage() * page + i;
            if(index >= items.size()) break;
            if (items.get(index) != null){
                inventory.setItem(inventory.firstEmpty(), items.get(index));
            }
        }
        if(page != 0){
            inventory.setItem(45, miscItems.PREVIOUS_BUTTON);
        }
        if(items.size() > maxItemsPerPage){
            inventory.setItem(53, miscItems.NEXT_BUTTON);
        }
    }
    
}

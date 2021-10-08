package com.github.sachin.tweakin.modules.betterarmorstands;

import java.util.ArrayList;
import java.util.List;

import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.nbtapi.NBTItem;
import com.github.sachin.tweakin.utils.ItemBuilder;
import com.github.sachin.tweakin.utils.MiscItems;
import com.github.sachin.tweakin.utils.TConstants;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class PresetPoseGui implements InventoryHolder{


    protected int page = 0;
    protected int maxItemsPerPage = 28;
    protected int index = 0;
    private Inventory inventory;
    private List<ItemStack> items;
    private Player player;
    private ArmorStand as;
    private BetterArmorStandTweak instance;
    private MiscItems miscItems;
    

    public PresetPoseGui(Player player,ArmorStand as){
        this.instance = (BetterArmorStandTweak) Tweakin.getPlugin().getTweakManager().getTweakFromName("better-armorstands");
        this.miscItems = instance.getPlugin().getMiscItems();
        this.player = player;
        this.as = as;
        List<ItemStack> i = new ArrayList<>();
        for(PresetPose pose : instance.getPoseManager().getPoses().values()){
            i.add(pose.item);
        }
        this.items = i;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void handlePageClicks(InventoryClickEvent e){
        e.setCancelled(true);
        if(e.getCurrentItem() == null) return;
        handleMiscClicks(e);
        NBTItem nbti = new NBTItem(e.getCurrentItem());
        if(nbti.hasKey("preset-pose-button")){
            PresetPose pose = instance.getPoseManager().getPoses().get(nbti.getString("preset-pose-button"));
            pose.setPose(as);
            ASGuiHolder.openGui(player, as);
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
        else if(ItemBuilder.hasKey(e.getCurrentItem(), "back-button")){
            ASGuiHolder.openGui(player, as);
        }
    }

    public void openPage(){
        if(items.isEmpty()) return;
        this.inventory = Bukkit.createInventory(this, 54,instance.getPlugin().getTweakManager().getMessageManager().getMessageWithoutPrefix("armorstand-poses-gui-title"));
        setBorderItems();
        player.openInventory(inventory); 
    }

    public void setBorderItems(){
        inventory.setItem(45, miscItems.FILLAR_GLASS);
        inventory.setItem(53,miscItems.FILLAR_GLASS);
        inventory.setItem(50,miscItems.FILLAR_GLASS);
        
        for (int slot : TConstants.BORDER_SLOTS) {
            inventory.setItem(slot, miscItems.FILLAR_GLASS);
        }
        inventory.setItem(49, miscItems.BACK_BUTTON);
        for(int i = 0; i < maxItemsPerPage; i++) {
            index = maxItemsPerPage * page + i;
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

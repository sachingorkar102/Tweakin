package com.github.sachin.tweakin.modules.rightclickshulker;

import java.util.List;

import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class ShulkerGui implements InventoryHolder{

    private Player player;
    private ShulkerBox shulker;
    private Inventory inventory;
    private int slot;
    private ItemStack shulkerItem;

    public ShulkerGui(Player player,ShulkerBox shulker,int slot,ItemStack shulkerItem){
        this.player = player;
        this.shulker = shulker;
        this.slot = slot;
        this.shulkerItem = shulkerItem;

    }

    public void update(){
        BlockStateMeta im = (BlockStateMeta) shulkerItem.getItemMeta();
        ShulkerBox shulker = (ShulkerBox) im.getBlockState();
        shulker.getInventory().setContents(getInventory().getContents());
        im.setBlockState(shulker);
        shulker.update();
        shulkerItem.setItemMeta(im);
        player.getInventory().setItem(slot, shulkerItem);
    }

    public ShulkerBox getShulker() {
        return shulker;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getShulkerItem() {
        return shulkerItem;
    }

    public Player getPlayer() {
        return player;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
    
}

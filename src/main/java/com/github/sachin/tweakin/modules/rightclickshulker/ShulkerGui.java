package com.github.sachin.tweakin.modules.rightclickshulker;

import java.util.List;
import java.util.UUID;

import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class ShulkerGui implements InventoryHolder{

    private UUID uuid;
    private ShulkerBox shulker;
    private final Inventory inventory;
    private int slot;
    private ItemStack shulkerItem;

    public ShulkerGui(Player player,ShulkerBox shulker,int slot,ItemStack shulkerItem,String title){
        this.uuid = player.getUniqueId();
        this.shulker = shulker;
        this.slot = slot;
        this.shulkerItem = shulkerItem;
        this.inventory = Bukkit.createInventory(this, 27,title);

    }

    public void update(){
        BlockStateMeta im = (BlockStateMeta) shulkerItem.getItemMeta();
        ShulkerBox shulker = (ShulkerBox) im.getBlockState();
        shulker.getInventory().setContents(getInventory().getContents());
        im.setBlockState(shulker);
        shulker.update();
        shulkerItem.setItemMeta(im);
        getPlayer().getInventory().setItem(slot, shulkerItem);
    }

    public ShulkerBox getShulker() {
        return shulker;
    }

    public void open(){
        inventory.setContents(shulker.getInventory().getContents());
        new BukkitRunnable(){
            @Override
            public void run() {
                getPlayer().openInventory(inventory);
                
            }
        }.runTaskLater(Tweakin.getPlugin(), 1);
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getShulkerItem() {
        return shulkerItem;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }


    @Override
    public Inventory getInventory() {
        return inventory;
    }
    
}

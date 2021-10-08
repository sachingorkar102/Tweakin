package com.github.sachin.tweakin.modules.trowel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.reacharound.ReachAroundTweak;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

// Permission: tweakin.trowel.use
public class TrowelItem extends TweakItem implements Listener{

    private List<Player> players = new ArrayList<>();

    public TrowelItem(Tweakin plugin) {
        super(plugin, "trowel");
    }

    @Override
    public void register() {
        super.register();
        registerRecipe();
    }

    @Override
    public void unregister() {
        super.unregister();
        unregisterRecipe();
    }



    @EventHandler
    public void onRightClick(PlayerInteractEvent e){

        if(e.getHand() != EquipmentSlot.HAND) return;
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = e.getPlayer();
        if(!player.hasPermission("tweakin.trowel.use")) return;
        if(!hasItem(player, EquipmentSlot.HAND)) return;
        e.setCancelled(true);
        if(players.contains(player)) return;
        Block clickedBlock = e.getClickedBlock();
        Block block = clickedBlock.getRelative(e.getBlockFace());
        if(clickedBlock.isPassable()){
            block = clickedBlock;
        }
        placeBlock(block.getLocation(), player, e.getBlockFace(),false,null);
        
    }

    public void placeBlock(Location loc,Player player,BlockFace hitFace,boolean isReacharound,ReachAroundTweak instance){
        List<ItemStack> hotBar = getHotBarContents(player);
        ItemStack iteminHand = player.getInventory().getItemInMainHand().clone();
        UsedItem usedItem = new UsedItem(iteminHand);
        if(!hotBar.isEmpty()){
            ItemStack item = hotBar.get(new Random().nextInt(hotBar.size()));
            if(isReacharound){
                if(matchString(item.getType().toString(), instance.getConfig().getStringList("black-list-materials"))){
                    return;
                }
                
            }
            player.getInventory().setItemInMainHand(item);
            boolean placed = getPlugin().getNmsHelper().placeItem(player, loc, player.getInventory().getItemInMainHand(),hitFace,getName(),true);
            player.getInventory().setItemInMainHand(iteminHand);
            players.add(player);
            new BukkitRunnable(){
                @Override
                public void run() {
                    players.remove(player);
                }
            }.runTaskLater(getPlugin(),4);
            if(placed){
                if(usedItem.getUses() != -1 && getConfig().getBoolean("take-damage",false) && player.getGameMode() != GameMode.CREATIVE){
                    if(usedItem.getUses() >= iteminHand.getType().getMaxDurability()){
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1f, 1f);
                        player.getInventory().setItemInMainHand(null);
                    }
                    else{
                        usedItem.use();
                        player.getInventory().setItemInMainHand(usedItem.getItem());
                    }
                }
                else{
                    player.getInventory().setItemInMainHand(iteminHand);
                }
                if(player.getGameMode() != GameMode.CREATIVE){
                    item.setAmount(item.getAmount()-1);
                }
            }
            
        }
    }

    private List<ItemStack> getHotBarContents(Player player){
        PlayerInventory inv = player.getInventory();
        List<ItemStack> list = new ArrayList<>();
        for(int i =0;i<9;i++){
            ItemStack item = inv.getItem(i);

            if(item != null){
                if(!item.isSimilar(player.getInventory().getItemInMainHand()) && item.getType().isBlock()){
                    list.add(inv.getItem(i));
                }
            }
        }
        return list;
    }



    
}

package com.github.sachin.tweakin.modules.trowel;

import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.modules.reacharound.ReachAroundTweak;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Permission: tweakin.trowel.use
@Tweak(name = "trowel")
public class TrowelItem extends TweakItem implements Listener{

    private List<Player> players = new ArrayList<>();


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


//        if(e.getHand() != EquipmentSlot.HAND) return;
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = e.getPlayer();
        if(!hasPermission(player, Permissions.TROWEL)) return;
        if(!isSimilar(e.getItem())) return;
        e.setCancelled(true);
        if(players.contains(player)) return;
        Block clickedBlock = e.getClickedBlock();
        Block block = clickedBlock.getRelative(e.getBlockFace());
        if(clickedBlock.isPassable()){
            block = clickedBlock;
        }
        placeBlock(block.getLocation(), player, e.getBlockFace(),e.getItem().clone(),e.getHand(),false,null);
        
    }

    public void placeBlock(Location loc,Player player,BlockFace hitFace,ItemStack trowel,EquipmentSlot slot,boolean isReacharound,ReachAroundTweak instance){
        List<ItemStack> hotBar = getHotBarContents(player,slot);
        UsedItem usedItem = new UsedItem(trowel);
        if(!hotBar.isEmpty()){
            ItemStack item = hotBar.get(new Random().nextInt(hotBar.size()));
            if(isReacharound){
                if(matchString(item.getType().toString(), instance.getConfig().getStringList("black-list-materials"))){
                    return;
                }
                
            }
            player.getInventory().setItem(slot,item);
            boolean placed = getPlugin().getNMSHandler().placeItem(player, loc, player.getInventory().getItem(slot),hitFace,getName(),true);
            player.getInventory().setItem(slot,trowel);
            players.add(player);
            new BukkitRunnable(){
                @Override
                public void run() {
                    players.remove(player);
                }
            }.runTaskLater(getPlugin(),4);
            if(placed){
                if(usedItem.getUses() != -1 && getConfig().getBoolean("take-damage",false) && player.getGameMode() != GameMode.CREATIVE){
                    if(usedItem.getUses() >= trowel.getType().getMaxDurability()){
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1f, 1f);
                        player.getInventory().setItem(slot,null);
                    }
                    else{
                        usedItem.use();
                        player.getInventory().setItem(slot,usedItem.getItem());
                    }
                }
                else{
                    player.getInventory().setItem(slot,trowel);
                }
                if(player.getGameMode() != GameMode.CREATIVE){
                    item.setAmount(item.getAmount()-1);
                }
            }
            
        }
    }

    private List<ItemStack> getHotBarContents(Player player,EquipmentSlot slot){
        PlayerInventory inv = player.getInventory();
        List<ItemStack> list = new ArrayList<>();
        for(int i =0;i<9;i++){
            ItemStack item = inv.getItem(i);

            if(item != null){
                if(!item.isSimilar(player.getInventory().getItem(slot)) && item.getType().isBlock()){
                    list.add(inv.getItem(i));
                }
            }
        }
        return list;
    }



    
}

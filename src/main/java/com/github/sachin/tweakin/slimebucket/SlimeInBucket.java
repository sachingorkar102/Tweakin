package com.github.sachin.tweakin.slimebucket;

import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SlimeInBucket extends TweakItem implements Listener{

    public SlimeInBucket(Tweakin plugin) {
        super(plugin, "slime-in-bucket");
    }

    @Override
    public void buildItem() {
        super.buildItem();
        ItemMeta meta = this.item.getItemMeta();
        meta.setCustomModelData(getConfig().getInt("model-undetected",103));
        this.item.setItemMeta(meta);
    }

    
    @EventHandler
    public void onSlimePickUp(PlayerInteractEntityEvent e){
        if(!(e.getRightClicked() instanceof Slime)) return;
        Slime slime = (Slime) e.getRightClicked();
        if(slime.getSize() != 1) return;
        if(getBlackListWorlds().contains(slime.getWorld().getName()))return;
        Player player = e.getPlayer();
        if(!player.hasPermission("tweakin.slimebucket.pickup")) return;
        if(e.getHand() != EquipmentSlot.HAND) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item == null) return;
        if(item.getType() != Material.BUCKET) return;
        e.setCancelled(true);
        player.swingMainHand();
        player.getInventory().setItemInMainHand(getItem());
        slime.remove();
    }

    @EventHandler
    public void onSlimeDeploy(PlayerInteractEvent e){
        if(e.getAction() != Action.LEFT_CLICK_BLOCK || e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        if(!hasItem(player, EquipmentSlot.HAND)) return;
        if(getBlackListWorlds().contains(player.getWorld().getName())) return;
        Block clickedBlock = e.getClickedBlock();
        Block relative = clickedBlock.getRelative(e.getBlockFace());
        if(relative.getType() != Material.AIR) return;
        boolean placed = getPlugin().getNmsHelper().placeItem(player, relative.getLocation(), new ItemStack(Material.DIRT), e.getBlockFace());
        if(placed){
            e.setCancelled(true);
            relative.setType(Material.AIR);
            Slime slime = player.getWorld().spawn(relative.getLocation().add(0.5,0.5,0.5), Slime.class);
            slime.setSize(1);
            player.getInventory().setItemInMainHand(new ItemStack(Material.BUCKET));
        }
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e){
        Location l1 = e.getFrom();
        Location l2 = e.getTo();
        if(l1.getBlockX() != l2.getBlockX() || l1.getBlockZ() != l2.getBlockZ()){
            Player player = e.getPlayer();
            if(getBlackListWorlds().contains(player.getWorld().getName())) return;
            int model = 0;
            if(l2.getChunk().isSlimeChunk()){
                model = getConfig().getInt("model-detected",104);
            }
            else{
                model = getConfig().getInt("model-undetected",103);
            }
            if(hasItem(player, EquipmentSlot.HAND)){
                
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                meta.setCustomModelData(model);
                item.setItemMeta(meta);
            }
            if(hasItem(player, EquipmentSlot.OFF_HAND)){
                ItemStack item = player.getInventory().getItemInOffHand();
                ItemMeta meta = item.getItemMeta();
                meta.setCustomModelData(model);
                item.setItemMeta(meta);
            }
        }
    }
}

package com.github.sachin.tweakin.modules.slimebucket;

import com.github.sachin.prilib.nms.NBTItem;
import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

@Tweak(name = "slime-in-bucket")
public class SlimeInBucket extends TweakItem implements Listener{

    private SlimeRunnable runnable;
    private final Set<Player> enabled = new HashSet<>();

    @Override
    public void register() {
        
        super.register();
        if(runnable != null){
            runnable.cancel();
        }
        this.runnable = new SlimeRunnable();
        runnable.runTaskTimer(plugin, 1, getConfig().getLong("interval-ticks",20));
        Bukkit.getOnlinePlayers().forEach(p -> enabled.add(p));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        enabled.add(e.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        enabled.remove(e.getPlayer());
        
    }

    @Override
    public void unregister() {
        super.unregister();
        if(runnable != null){
            this.runnable.cancel();
            enabled.clear();
        }
    }


    @Override
    public void buildItem() {
        super.buildItem();
        ItemMeta meta = this.item.getItemMeta();
        meta.setCustomModelData(getConfig().getInt("model-undetected",103));
        if(getConfig().contains("item-model-undetected") && plugin.isPost1_21_4()){
            String itemModel = getConfig().getString("item-model-undetected");
            String[] parts = itemModel.split(":",2);
            meta.setItemModel(new NamespacedKey(parts[0],parts[1]));
        }
        this.item.setItemMeta(meta);
    }

    @EventHandler
    public void onSlimePickUp(PlayerInteractEntityEvent e){
        if(e.getRightClicked().getType() != EntityType.SLIME) return;
        
        Slime slime = (Slime) e.getRightClicked();
        if(slime.getSize() != 1) return;
        if(getBlackListWorlds().contains(slime.getWorld().getName()))return;
        Player player = e.getPlayer();
        if(!hasPermission(player, Permissions.SLIMEBUCKET_PICKUP)) return;
        if(e.getHand() != EquipmentSlot.HAND) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item == null) return;
        if(item.getType() != Material.BUCKET) return;
        e.setCancelled(true);
        player.swingMainHand();
        item.setAmount(item.getAmount()-1);
        ItemStack slimeBucket = getItem().clone();
        if(slime.getCustomName() != null){
            NBTItem nbtItem = plugin.getNMSHandler().newItem(slimeBucket);
            nbtItem.setString("tweakin-slime-name",slime.getCustomName());
            slimeBucket = nbtItem.getItem();
        }
        player.getInventory().addItem(slimeBucket);
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
        Slime slime = player.getWorld().spawn(relative.getLocation().add(0.5,0.5,0.5), Slime.class);
        ItemStack slimeBucket = player.getInventory().getItem(EquipmentSlot.HAND);
        NBTItem nbtItem = plugin.getNMSHandler().newItem(slimeBucket);
        if(nbtItem.hasKey("tweakin-slime-name")){
            slime.setCustomName(nbtItem.getString("tweakin-slime-name"));
        }
        slime.setSize(1);
        e.getItem().setAmount(e.getItem().getAmount()-1);
        player.getInventory().addItem(new ItemStack(Material.BUCKET));
    }

    private class SlimeRunnable extends BukkitRunnable{

        @Override
        public void run() {
            if(enabled.isEmpty()) return;
            enabled.forEach(player -> {
                if(getBlackListWorlds().contains(player.getWorld().getName()) || !hasPermission(player, Permissions.SLIMEBUCKET_DETECT)) return;
                int model = 0;
                String itemModel = null;
                Location loc = player.getLocation();
                if(loc.getChunk().isSlimeChunk() && getConfig().getInt("max-y-level") > loc.getBlockY()){
//                    if(getConfig().isString("model-detected")) itemModel = getConfig().getString("model-detected");
                    model = getConfig().getInt("model-detected",104);
                    itemModel = getConfig().getString("item-model-detected");
                }
                else{
//                    if(getConfig().isString("model-undetected")) itemModel = getConfig().getString("model-undetected");
                    model = getConfig().getInt("model-undetected",103);
                    itemModel = getConfig().getString("item-model-undetected");
                }
                if(hasItem(player, EquipmentSlot.HAND)){
                    
                    ItemStack item = player.getInventory().getItemInMainHand();
                    ItemMeta meta = item.getItemMeta();
                    meta.setCustomModelData(model);
                    if(itemModel != null && plugin.isPost1_21_4()){
                        String[] parts = itemModel.split(":",2);
                        meta.setItemModel(new NamespacedKey(parts[0],parts[1]));
                    }
                    item.setItemMeta(meta);
                }
                if(isSimilar(player.getInventory().getItemInOffHand())){
                    ItemStack item = player.getInventory().getItemInOffHand();
                    ItemMeta meta = item.getItemMeta();
                    meta.setCustomModelData(model);
                    item.setItemMeta(meta);
                }
            });
        }
        
    }
}

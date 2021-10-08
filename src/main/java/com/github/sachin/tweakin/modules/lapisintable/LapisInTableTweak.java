package com.github.sachin.tweakin.modules.lapisintable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.CustomBlockData;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import de.jeff_media.morepersistentdatatypes.DataType;

public class LapisInTableTweak extends BaseTweak implements Listener{

    private Map<Location,LapisData> data = new HashMap<>();
    private final NamespacedKey key = Tweakin.getKey("lapiscount");
    private final NamespacedKey itemKey = Tweakin.getKey("EnchantingTableItemKey");
    private final NamespacedKey entityItemKey = Tweakin.getKey("EnchantingTableEntityItemKey");
    private final NamespacedKey entityStandItemKey = Tweakin.getKey("EnchantingTableEntityArmorStandItemKey");
    private File lapisFile;
    private Map<Player,Location> invMap = new HashMap<>();

	public LapisInTableTweak(Tweakin plugin) {
        super(plugin, "lapis-in-table");
        this.lapisFile = new File(getPlugin().getDataFolder().getAbsolutePath()+"/"+"data/lapis-data.yml");
        
        loadLapisData();
        
	}

    @Override
    public void register() {
        super.register();
    }

    public void saveLapisData(){
        if(!this.lapisFile.exists()) return;
        FileConfiguration yml = new YamlConfiguration();
        int i =0;
        for(Location loc :data.keySet()){
            yml.set(String.valueOf(i), data.get(loc));
            i++;
        }
        try {
            yml.save(this.lapisFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadLapisData(){
        data.clear();
        if(this.lapisFile.exists()){
            FileConfiguration yml = YamlConfiguration.loadConfiguration(this.lapisFile);
            for(String config : yml.getKeys(false)){
                LapisData d = (LapisData) yml.get(config);
                data.put(d.getLocation(),d);
                Chunk chunk = d.getLocation().getChunk();
                boolean loadedChunk = true;
                if(!chunk.isLoaded()){
                    loadedChunk = chunk.load();
                } 
                if(loadedChunk){
                    CustomBlockData blockData = new CustomBlockData(d.getLocation());
                    blockData.set(key, PersistentDataType.INTEGER ,d.getCount());
                }

            }
            this.lapisFile.delete();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTableClickEvent(PlayerInteractEvent e){
        if(e.isCancelled()) return;
        
        if(e.getHand() != EquipmentSlot.HAND) return;
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return; 
        if(e.getClickedBlock() == null) return;
        if(e.getClickedBlock().getType() != Material.ENCHANTING_TABLE) return;
        e.setCancelled(true);
        Player player = e.getPlayer();
        
        Location blockLocation = e.getClickedBlock().getLocation();
        if(invMap.values().contains(blockLocation)){
            e.setCancelled(true);
            return;
        }
        if(getBlackListWorlds().contains(blockLocation.getWorld().getName())) return;

        // Item dropedItem = player.getWorld().spawn(e.getClickedBlock().getLocation().add(0,0.4,0), Item.class);
        // dropedItem.setItemStack();
        CustomBlockData cdata = new CustomBlockData(blockLocation);
        int count = 0;
        InventoryView view = player.openEnchanting(blockLocation, true);
        if(cdata.has(key, PersistentDataType.INTEGER)){
            count = cdata.get(key, PersistentDataType.INTEGER);
        }
        if(cdata.has(itemKey, DataType.ITEM_STACK) && getConfig().getBoolean("store-item")){
            ItemStack enchantItem = cdata.get(itemKey, DataType.ITEM_STACK);
            
            if(enchantItem != null && !enchantItem.getType().isAir()){
                view.getTopInventory().setItem(0, enchantItem);
                if(!hasEnchantItem(cdata)){
                    spawnEnchantItem(cdata, enchantItem);
                }
                else{
                    updateEnchantItem(cdata, enchantItem);
                }
            }
        }
        view.getTopInventory().setItem(1, new ItemStack(Material.LAPIS_LAZULI, count));
        invMap.put(player, blockLocation);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClose(InventoryCloseEvent e){
        if(e.getInventory().getType() != InventoryType.ENCHANTING) return;
        Player player = (Player) e.getPlayer();
        
        if(invMap.containsKey(player)){
            int count = 0;
            Inventory enchantInv = e.getView().getTopInventory();
            CustomBlockData data = new CustomBlockData(invMap.get(player));
            if(enchantInv.getItem(1) != null){
                count = enchantInv.getItem(1).clone().getAmount();
                enchantInv.getItem(1).setAmount(0);
            }
            if(getConfig().getBoolean("store-item")){
                ItemStack enchantItem = enchantInv.getItem(0);
                if(enchantItem != null && !enchantItem.getType().isAir()){
                    
                    data.set(itemKey,DataType.ITEM_STACK,enchantItem);
                    if(!hasEnchantItem(data)){
                        spawnEnchantItem(data, enchantItem);
                    }
                    else{
                        updateEnchantItem(data, enchantItem);
                    }
                    enchantItem.setAmount(0);
                }
                else{
                    data.set(itemKey, DataType.ITEM_STACK, new ItemStack(Material.AIR));
                    removeEnchantItem(data);
                }
            }
            data.set(key, PersistentDataType.INTEGER, count);
            invMap.remove(player);
        }
    }

    @EventHandler
    public void onEnchantItemEvent(EnchantItemEvent e){
        Player player = e.getEnchanter();
        if(invMap.containsKey(player) && getConfig().getBoolean("store-item")){
            CustomBlockData data = new CustomBlockData(invMap.get(player));
            if(hasEnchantItem(data)){
                Item item = (Item) Bukkit.getEntity(UUID.fromString(data.get(entityItemKey, PersistentDataType.STRING)));
                new BukkitRunnable(){
                    public void run() {
                        ItemStack enchantItem = e.getInventory().getItem(0);
                        if(enchantItem != null && !enchantItem.getType().isAir()){
                            item.setItemStack(enchantItem);
                        }
                    };
                }.runTaskLater(plugin, 2);
            }
            
        }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void blockBreakEvent(BlockBreakEvent e){
        Location loc = e.getBlock().getLocation();
        if(invMap.values().contains(loc)){
            e.setCancelled(true);
            return;
        }
        CustomBlockData data = new CustomBlockData(loc);
        if(data.has(key, PersistentDataType.INTEGER)){
            if(data.get(key,PersistentDataType.INTEGER) > 0){
                ItemStack lapis = new ItemStack(Material.LAPIS_LAZULI,data.get(key, PersistentDataType.INTEGER));
    
                loc.getWorld().dropItemNaturally(loc, lapis);
            }
            data.remove(key);

        }
        if(data.has(itemKey, DataType.ITEM_STACK)){
            ItemStack item = data.get(itemKey, DataType.ITEM_STACK);
            if(!item.getType().isAir()){
                loc.getWorld().dropItemNaturally(loc, item);
            }
            data.remove(itemKey);
            removeEnchantItem(data);
        }
        
    }



    private void spawnEnchantItem(CustomBlockData data,ItemStack item){
        if(item == null || item.getType().isAir()) return;
        Location loc = data.getLocation();
        Item itemEn = loc.getWorld().spawn(loc, Item.class);
        itemEn.setItemStack(item);
        itemEn.setPickupDelay(Integer.MAX_VALUE);
        itemEn.getPersistentDataContainer().set(entityItemKey, PersistentDataType.INTEGER, 1);
        ArmorStand stand = loc.getWorld().spawn(loc.add(0.5, 1.2, 0.5), ArmorStand.class);
        stand.setMarker(true);
        stand.setVisible(false);
        stand.addPassenger(itemEn);
        
        data.set(entityItemKey, PersistentDataType.STRING, itemEn.getUniqueId().toString());
        data.set(entityStandItemKey, PersistentDataType.STRING, stand.getUniqueId().toString());
        
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent e){
        if(e.getEntity().getPersistentDataContainer().has(entityItemKey, PersistentDataType.INTEGER)){
            e.setCancelled(true);
        }
    }

    private void removeEnchantItem(CustomBlockData data){
        if(data.has(entityItemKey, PersistentDataType.STRING)){
            Entity item = Bukkit.getEntity(UUID.fromString(data.get(entityItemKey, PersistentDataType.STRING)));
            if(item != null){
                item.remove();
                data.remove(entityItemKey);
            }
        }
        if(data.has(entityStandItemKey, PersistentDataType.STRING)){
            Entity stand = Bukkit.getEntity(UUID.fromString(data.get(entityStandItemKey, PersistentDataType.STRING)));
            if(stand != null){
                stand.remove();
                data.remove(entityStandItemKey);
            }
        }
    }

    private void updateEnchantItem(CustomBlockData data,ItemStack updatedItem){
        if(!hasEnchantItem(data)){
            spawnEnchantItem(data, updatedItem);
            return;
        }
        Item item = (Item) Bukkit.getEntity(UUID.fromString(data.get(entityItemKey, PersistentDataType.STRING)));
        ArmorStand stand = (ArmorStand) Bukkit.getEntity(UUID.fromString(data.get(entityStandItemKey, PersistentDataType.STRING)));
        if(item != null && !item.isDead()){
            stand.removePassenger(item);
            item.setItemStack(updatedItem);
            stand.addPassenger(item);
        }
        // new BukkitRunnable(){
        //     public void run() {
        //     };
        // }.runTaskLater(plugin, 1);
    }

    private boolean hasEnchantItem(CustomBlockData data){
        return data.has(entityItemKey, PersistentDataType.STRING) && data.has(entityStandItemKey, PersistentDataType.STRING);
    }

    @Override
    public void onDisable() {
        saveLapisData();
    }

    

  



    
    
}

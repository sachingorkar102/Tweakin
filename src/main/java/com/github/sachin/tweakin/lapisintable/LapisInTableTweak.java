package com.github.sachin.tweakin.lapisintable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.CustomBlockData;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
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

public class LapisInTableTweak extends BaseTweak implements Listener{

    private Map<Location,LapisData> data = new HashMap<>();
    private NamespacedKey key;
    private File lapisFile;
    private Map<Player,Location> invMap = new HashMap<>();

	public LapisInTableTweak(Tweakin plugin) {
		super(plugin, "lapis-in-table");
        this.lapisFile = new File(getPlugin().getDataFolder().getAbsolutePath()+"/"+"data/lapis-data.yml");
        this.key = new NamespacedKey(plugin, "lapiscount");
        
        loadLapisData();
        
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTableClickEvent(PlayerInteractEvent e){
        if(e.getHand() != EquipmentSlot.HAND) return;
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return; 
        if(e.getClickedBlock() == null) return;
        if(e.getClickedBlock().getType() != Material.ENCHANTING_TABLE) return;
        e.setCancelled(true);
        Player player = e.getPlayer();
        Location blockLocation = e.getClickedBlock().getLocation();
        if(getBlackListWorlds().contains(blockLocation.getWorld().getName())) return;
        CustomBlockData data = new CustomBlockData(blockLocation);
        int count = 0;
        if(data.has(key, PersistentDataType.INTEGER)){
            count = data.get(key, PersistentDataType.INTEGER);
        }
        InventoryView view = player.openEnchanting(blockLocation, true);
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
            if(enchantInv.getItem(1) != null){
                count = enchantInv.getItem(1).clone().getAmount();
                enchantInv.getItem(1).setAmount(0);
            }
            CustomBlockData data = new CustomBlockData(invMap.get(player));
            data.set(key, PersistentDataType.INTEGER, count);
            // data.put(invMap.get(player),new LapisData(count, invMap.get(player)));
            invMap.remove(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void blockBreakEvent(BlockBreakEvent e){
        Location loc = e.getBlock().getLocation();
        CustomBlockData data = new CustomBlockData(loc);
        if(data.has(key, PersistentDataType.INTEGER)){
            ItemStack lapis = new ItemStack(Material.LAPIS_LAZULI,data.get(key, PersistentDataType.INTEGER));
            loc.getWorld().dropItemNaturally(loc, lapis);
            data.remove(key);

        }
    }

    

  



    
    
}

package com.github.sachin.tweakin.modules.netherportalcoords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.nbtapi.NBTItem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;

// Permission: tweakin.netherportalcoords
public class NetherPortalCoordsTweak extends BaseTweak implements Listener{

    private Map<String,String> worldPairs;

    public Map<String, String> getWorldPairs() {
        return worldPairs;
        
    }

    public NetherPortalCoordsTweak(Tweakin plugin) {
        super(plugin, "nether-portal-coords");
    }

    @Override
    public void reload() {
        super.reload();
        this.worldPairs = new HashMap<>();
        for(String key : getConfig().getConfigurationSection("world-pairs").getKeys(false)){
            this.worldPairs.put(key, getConfig().getString("world-pairs."+key));
        }
    }


    @EventHandler
    public void portalClickEvent(PlayerInteractEvent e){
        if(e.getItem() == null) return;
        if(e.getItem().getType() != Material.COMPASS) return;
        
        if(e.getHand() != EquipmentSlot.HAND) return;
        ItemStack item = e.getItem().clone();
        ItemMeta meta = item.getItemMeta();
        Player player = e.getPlayer();
        if(!player.hasPermission("tweakin.netherportalcoords")) return;
        if(isUntrackedCompass(item) && e.getAction() == Action.RIGHT_CLICK_AIR){
            if(player.isSneaking()){
                player.getInventory().setItemInMainHand(new ItemStack(Material.COMPASS));
                return;
            }
            Location loc = getTracingLocation(item);
            if(loc != null && loc.getWorld().getName().equals(player.getWorld().getName())){
                meta.setDisplayName("Tracking");
                CompassMeta cMeta = (CompassMeta) meta;
                cMeta.setLodestoneTracked(false);
                cMeta.setLodestone(loc);
                item.setItemMeta(meta);
                player.getInventory().setItemInMainHand(item);
                return;
            }
        }
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block clickedBlock = e.getClickedBlock();
        if(clickedBlock.getType() != Material.NETHER_PORTAL) return;
        String worldName = player.getWorld().getName();
        for (String overworld : worldPairs.keySet()) {
            // world: world_nether
            if(worldName.equals(overworld)){
                World world = Bukkit.getWorld(worldPairs.get(overworld));
                if(world != null){
                    Location newTrackLocation = new Location(world,clickedBlock.getLocation().getBlockX()/8,clickedBlock.getLocation().getBlockY(),clickedBlock.getLocation().getBlockZ()/8);
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aNether Portal Syncing Compass"));
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&6Nether: &e"+newTrackLocation.getBlockX()+","+newTrackLocation.getBlockY()+","+newTrackLocation.getBlockZ()));
                    lore.add(ChatColor.GRAY+"Follow the above coordinates in");
                    lore.add(ChatColor.GRAY+"nether to link the portals");
                    lore.add(ChatColor.YELLOW+"RIGHT-CLICK in air to track");
                    lore.add(ChatColor.YELLOW+"SHIFT+RIGHT-CLICK to clear");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    item = setTracingLocation(newTrackLocation, item);
                    player.getInventory().setItemInMainHand(item);
                }
            }
            else if(worldName.equals(worldPairs.get(overworld))){
                World world = Bukkit.getWorld(overworld);
                if(world != null){
                    Location newTrackLocation = new Location(world,clickedBlock.getLocation().getBlockX()*8,clickedBlock.getLocation().getBlockY(),clickedBlock.getLocation().getBlockZ()*8);
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aNether Portal Syncing Compass"));
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&6OverWorld: &e"+newTrackLocation.getBlockX()+","+newTrackLocation.getBlockY()+","+newTrackLocation.getBlockZ()));
                    lore.add(ChatColor.GRAY+"Follow the above coordinates in");
                    lore.add(ChatColor.GRAY+"overworld to link the portals");
                    lore.add(ChatColor.YELLOW+"RIGHT-CLICK in air to track");
                    lore.add(ChatColor.YELLOW+"SHIFT+RIGHT-CLICK to clear");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    item = setTracingLocation(newTrackLocation, item);
                    player.getInventory().setItemInMainHand(item);
                }
            }
        }
    }

    private boolean isUntrackedCompass(ItemStack item){
        NBTItem nbti = new NBTItem(item);
        if(nbti.hasKey("npct")){
            return !nbti.getBoolean("npct");
        }
        return false;
    }

    private ItemStack setTracingLocation(Location loc,ItemStack item){
        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("npct", false);
        nbti.setInt("npcx", loc.getBlockX());
        nbti.setInt("npcy", loc.getBlockY());
        nbti.setInt("npcz", loc.getBlockZ());
        nbti.setString("npcw", loc.getWorld().getName());
        return nbti.getItem();
    }

    private Location getTracingLocation(ItemStack item){
        NBTItem nbti = new NBTItem(item);
        if(nbti.hasKey("npct")){
            if(!nbti.getBoolean("npct")){
                int x = nbti.getInt("npcx");
                int y = nbti.getInt("npcy");
                int z = nbti.getInt("npcz");
                String worldName = nbti.getString("npcw");
                if(Bukkit.getWorld(worldName) != null){
                    nbti.setBoolean("npct", true);
                    item = nbti.getItem();
                    return new Location(Bukkit.getWorld(worldName),x,y,z);
                }
            }
        }
        return null;
    }

    
    
}

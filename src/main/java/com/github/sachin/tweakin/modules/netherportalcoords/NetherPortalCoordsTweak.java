package com.github.sachin.tweakin.modules.netherportalcoords;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.prilib.nms.NBTItem;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Config;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Permission: tweakin.netherportalcoords
@Tweak(name = "nether-portal-coords")
public class NetherPortalCoordsTweak extends BaseTweak implements Listener{

    private List<WorldPair> worldPairs2;



    @Override
    public void reload() {
        super.reload();
        this.worldPairs2 = new ArrayList<>();
        for(int i =0;i<getConfig().getStringList("world-pairs").size();i++){
            String[] pairs = getConfig().getStringList("world-pairs").get(i).split("\\|");
            worldPairs2.add(new WorldPair(pairs[0],pairs[1],getConfig().getIntegerList("coordinate-scale").get(i)));
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
        if(!hasPermission(player, Permissions.NETHERCOORDS)) return;
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
        for (WorldPair worldPair : worldPairs2) {
            // world: world_nether
            if(worldName.equals(worldPair.overworld)){
                World world = Bukkit.getWorld(worldPair.nether);
                if(world != null){
                    Location newTrackLocation = new Location(world
                            ,clickedBlock.getLocation().getBlockX()/worldPair.coordScale
                            ,clickedBlock.getLocation().getBlockY()
                            ,clickedBlock.getLocation().getBlockZ()/worldPair.coordScale);
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
            else if(worldName.equals(worldPair.nether)){
                World world = Bukkit.getWorld(worldPair.overworld);
                if(world != null){
                    Location newTrackLocation = new Location(world
                            ,clickedBlock.getLocation().getBlockX()*worldPair.coordScale
                            ,clickedBlock.getLocation().getBlockY()
                            ,clickedBlock.getLocation().getBlockZ()*worldPair.coordScale);
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
        NBTItem nbti = plugin.getNMSHandler().newItem(item);
        if(nbti.hasKey("npct")){
            return !nbti.getBoolean("npct");
        }
        return false;
    }

    private ItemStack setTracingLocation(Location loc,ItemStack item){
        NBTItem nbti = plugin.getNMSHandler().newItem(item);
        nbti.setBoolean("npct", false);
        nbti.setInt("npcx", loc.getBlockX());
        nbti.setInt("npcy", loc.getBlockY());
        nbti.setInt("npcz", loc.getBlockZ());
        nbti.setString("npcw", loc.getWorld().getName());
        return nbti.getItem();
    }

    private Location getTracingLocation(ItemStack item){
        NBTItem nbti = plugin.getNMSHandler().newItem(item);
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


    private class WorldPair{

        public final String overworld;
        public final String nether;
        public final int coordScale;

        private WorldPair(String overworld, String nether, int coordScale) {
            this.overworld = overworld;
            this.nether = nether;
            this.coordScale = coordScale;
        }
    }
    
}

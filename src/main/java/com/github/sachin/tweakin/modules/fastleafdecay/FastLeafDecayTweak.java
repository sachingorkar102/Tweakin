package com.github.sachin.tweakin.modules.fastleafdecay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class FastLeafDecayTweak extends BaseTweak implements Listener{

    private int duration;
    private final Set<LeavesDecayEvent> fastDecayEvents = new HashSet<>();


    public FastLeafDecayTweak(Tweakin plugin) {
        super(plugin, "fast-leaf-decay");
    }

    @Override
    public void reload() {
        super.reload();
        this.duration = getConfig().getInt("duration",10) * 20;
    }

    @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
    public void onLeafDecay(LeavesDecayEvent e){
        if(fastDecayEvents.remove(e)) return;
        if(getBlackListWorlds().contains(e.getBlock().getWorld().getName())) return;
        e.setCancelled(true);
        Block block = e.getBlock();
        if(getConfig().getBoolean("use-async")){
            new BukkitRunnable(){
                @Override
                public void run() {
                    List<Location> locs = getNearbyBlocks(block.getLocation(), 10,true);
                    removeLeaves(locs);
                }
            }.runTaskAsynchronously(getPlugin());
        }
        else{
            List<Location> locs = getNearbyBlocks(block.getLocation(), 10,false);
            removeLeaves(locs);
        }
        
    }
    
    private void removeLeaves(List<Location> locs){
        for (Location location : locs) {
    
            
            Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> {
                Block block = location.getBlock();
                if(block.getType().name().endsWith("LEAVES")){
                    LeavesDecayEvent event = new LeavesDecayEvent(block);
                    fastDecayEvents.add(event);
                    Bukkit.getPluginManager().callEvent(event);
                    if(!event.isCancelled()){
                        block.breakNaturally();
                        locs.remove(location);
                    }
                }
            },new Random().nextInt(duration));
    
    
        }
    }

    public List<Location> getNearbyBlocks(Location location, int radius,boolean isAsync) {
        // List<Block> blocks = new ArrayList<Block>();
        List<Location> locs = new ArrayList<>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    Block block = location.getWorld().getBlockAt(x, y, z);
                    if(!block.getType().isAir()){
                        if(isAsync){
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
                                if(isLeaf(block)){
                                    
                                    Leaves leaves = (Leaves) block.getBlockData();
                                    if(!leaves.isPersistent() && leaves.getDistance() > 6 && !locs.contains(block.getLocation())){
                                        locs.add(block.getLocation());
                                    }
                                }
                            });
                        }
                        else{
                            if(isLeaf(block)){
                                    
                                Leaves leaves = (Leaves) block.getBlockData();
                                if(!leaves.isPersistent() && leaves.getDistance() > 6 && !locs.contains(block.getLocation())){
                                    locs.add(block.getLocation());
                                }
                            }
                        }
                    }
                }
            }
        }
        return locs;
    }

    private boolean isLeaf(Block block){
        return Tag.LEAVES.isTagged(block.getType());
    }
    
}

package com.github.sachin.tweakin.fastleafdecay;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class FastLeafDecayTweak extends BaseTweak implements Listener{

    private int duration;


    public FastLeafDecayTweak(Tweakin plugin) {
        super(plugin, "fast-leaf-decay");
    }

    @Override
    public void reload() {
        super.reload();
        this.duration = getConfig().getInt("duration",10) * 20;
    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent e){
        if(getBlackListWorlds().contains(e.getBlock().getWorld().getName())) return;
        e.setCancelled(true);
        Block block = e.getBlock();
        ItemStack hoe = new ItemStack(Material.WOODEN_HOE);
        new BukkitRunnable(){
            @Override
            public void run() {
                List<Location> locs = getNearbyBlocks(block.getLocation(), 10);
                for (Location location : locs) {


                    Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> {
                        Block block = location.getBlock();
                        if(block.getType().name().endsWith("LEAVES")){
                            block.breakNaturally(hoe);
                            locs.remove(location);
                        }
                    },new Random().nextInt(duration));


                }
            }
        }.runTaskAsynchronously(getPlugin());
        
    }

    public List<Location> getNearbyBlocks(Location location, int radius) {
        // List<Block> blocks = new ArrayList<Block>();
        List<Location> locs = new ArrayList<>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    Block block = location.getWorld().getBlockAt(x, y, z);
                    if(!block.getType().isAir()){
                        if(block.getBlockData() instanceof Leaves){
                            
                            Leaves leaves = (Leaves) block.getBlockData();
                            if(!leaves.isPersistent() && leaves.getDistance() > 6 && !locs.contains(block.getLocation())){
                                locs.add(block.getLocation());
                            }
                        }
                    }
                }
            }
        }
        return locs;
    }
    
}

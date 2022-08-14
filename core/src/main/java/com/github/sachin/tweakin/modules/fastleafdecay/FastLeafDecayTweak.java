package com.github.sachin.tweakin.modules.fastleafdecay;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.Tweak;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Tweak(name = "fast-leaf-decay")
public class FastLeafDecayTweak extends BaseTweak implements Listener {

    private int duration;

    @Override
    public void reload() {
        super.reload();
        this.duration = getConfig().getInt("duration", 10) * 20;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLeafDecay(LeavesDecayEvent e) {
        if (e instanceof LeavesDecayEventForChecking) return;
        if (getBlackListWorlds().contains(e.getBlock().getWorld().getName())) return;
        e.setCancelled(true);
        Block block = e.getBlock();
        removeLeaves(block.getLocation());

    }

    private void removeLeaves(Location startLocation) {
        List<Location> leavesLocations = getNearbyLeaves(startLocation, 10);
        for (Location location : leavesLocations) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> {
                Block block = location.getBlock();
                LeavesDecayEvent event = new LeavesDecayEventForChecking(block);
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    block.breakNaturally();
                }
            }, ThreadLocalRandom.current().nextInt(duration));
        }
    }

    public List<Location> getNearbyLeaves(Location location, int radius) {
        // List<Block> blocks = new ArrayList<Block>();
        List<Location> locs = new ArrayList<>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    Block block = location.getWorld().getBlockAt(x, y, z);
                    if (!block.getType().isAir() && isLeaf(block)) {
                        Leaves leaves = (Leaves) block.getBlockData();
                        if (!leaves.isPersistent() && leaves.getDistance() > 6 && !locs.contains(block.getLocation())) {
                            locs.add(block.getLocation());
                        }
                    }
                }
            }
        }
        return locs;
    }

    private boolean isLeaf(Block block) {
        return Tag.LEAVES.isTagged(block.getType());
    }

    private static class LeavesDecayEventForChecking extends LeavesDecayEvent {
        public LeavesDecayEventForChecking(Block block) {
            super(block);
        }
    }

}

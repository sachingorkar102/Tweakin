package com.github.sachin.tweakin.modules.compassworkeverywhere;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class CompassEveryWhereTweak extends BaseTweak implements Listener{

    public CompassEveryWhereTweak(Tweakin plugin) {
        super(plugin, "compass-work-everywhere");
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onSetBedSpawn(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(!player.hasPermission("tweakin.compasstrack")) return;
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getHand() != EquipmentSlot.HAND) return;
        if(!e.getClickedBlock().getType().toString().endsWith("BED")) return;
        if(e.getItem() == null) return;
        if(!e.getItem().isSimilar(new ItemStack(Material.COMPASS))) return;
        if(getBlackListWorlds().contains(player.getLocation().getWorld().getName())) return;
        new BukkitRunnable(){
            public void run() {
                if(player.isOnline()){
                    Location loc = player.getBedSpawnLocation();
                    if(loc != null){
                        player.setCompassTarget(loc);
                    }
                }
            };
        }.runTaskLater(getPlugin(), 3);
    }

    @EventHandler
    public void onPortalEvent(PlayerPortalEvent e){
        Location loc2 = e.getTo();
        Player player = e.getPlayer();
        if(!player.hasPermission("tweakin.compasstrack")) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item == null) return;
        if(!item.isSimilar(new ItemStack(Material.COMPASS))) return;
        if(getBlackListWorlds().contains(loc2.getWorld().getName())) return;
        CompassMeta meta = (CompassMeta) item.getItemMeta();
        meta.setLodestoneTracked(false);
        meta.setLodestone(loc2);
        if(getConfig().getBoolean("nether",true) && loc2.getWorld().getEnvironment() == Environment.NETHER){
            item.setItemMeta(meta);
        }
        else if(getConfig().getBoolean("end",true) && loc2.getWorld().getEnvironment() == Environment.THE_END){
            item.setItemMeta(meta);
        }
    }
}

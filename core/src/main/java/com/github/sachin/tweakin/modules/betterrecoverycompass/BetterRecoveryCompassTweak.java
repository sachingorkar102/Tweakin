package com.github.sachin.tweakin.modules.betterrecoverycompass;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CartographyInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.scheduler.BukkitRunnable;

// Permission: tweakin.betterrecoverycompass.use
public class BetterRecoveryCompassTweak extends BaseTweak implements Listener {

    private byte zoom;
    private boolean biomepreview;

    public BetterRecoveryCompassTweak(Tweakin plugin) {
        super(plugin, "better-recovery-compass");
    }

    @Override
    public void reload() {
        super.reload();
        this.zoom = (byte)getConfig().getInt("zoom",2);
        this.biomepreview = getConfig().getBoolean("show-biome-preview",true);
    }

    private boolean getMap(Player player,CartographyInventory inv){
        ItemStack map = inv.getItem(1);
        ItemStack compass = inv.getItem(0);
        if(map != null && map.getType()==Material.MAP
                && compass != null && compass.getType()==Material.RECOVERY_COMPASS) {
            compass = compass.clone();
            if (player.getLastDeathLocation() != null) {
                Location loc = player.getLastDeathLocation();
                if (loc.getWorld().getUID() == player.getWorld().getUID()) {
                    inv.setItem(2, getPlugin().getNmsHelper().createMap(loc, zoom, biomepreview));
                    return true;
                }
            }
        }
        return false;
    }

    @EventHandler
    public void cartographyInvClickEvent(InventoryClickEvent e){
        if(e.getInventory().getType()!= InventoryType.CARTOGRAPHY) return;
        Player player = (Player) e.getWhoClicked();
        if(!hasPermission(player,"tweakin.betterrecoverycompass.use")) return;
        CartographyInventory inv = (CartographyInventory) e.getInventory();
        ItemStack compass = e.getCursor();
        ItemStack slotCompass = inv.getItem(0);

        if(compass != null && compass.getType()==Material.RECOVERY_COMPASS
                && e.getSlot()==0
                && (slotCompass == null || slotCompass.getType()==Material.AIR)){
            compass = compass.clone();
            e.setCancelled(true);
            e.getCursor().setAmount(0);
            inv.setItem(0,compass);
        }
        new BukkitRunnable(){
            @Override
            public void run() {
                getMap(player,inv);
            }
        }.runTaskLater(getPlugin(),1);

    }

}

package com.github.sachin.tweakin.modules.betterrecoverycompass;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.annotations.Config;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CartographyInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

// Permission: tweakin.betterrecoverycompass.use
@Tweak(name = "better-recovery-compass")
public class BetterRecoveryCompassTweak extends BaseTweak implements Listener {

    @Config(key = "zoom")
    private int scale = 2;
    @Config(key = "show-biome-preview")
    private boolean biomepreview = true;

    private void generateMap(Player player,CartographyInventory inv){
        ItemStack map = inv.getItem(1);
        ItemStack compass = inv.getItem(0);
        if(map != null && map.getType()==Material.MAP
                && compass != null && compass.getType()==Material.RECOVERY_COMPASS) {
            compass = compass.clone();
            if (player.getLastDeathLocation() != null) {
                Location loc = player.getLastDeathLocation();
                if (loc.getWorld().getUID() == player.getWorld().getUID()) {
                    inv.setItem(2, getPlugin().getNMSHandler().createMap(loc, (byte) scale, biomepreview));

                }
            }
        }
    }

    @EventHandler
    public void cartographyInvClickEvent(InventoryClickEvent e){
        if(e.getInventory().getType()!= InventoryType.CARTOGRAPHY) return;
        Player player = (Player) e.getWhoClicked();
        if(!hasPermission(player, Permissions.BETTERRECOVERYCOMPASS_USE)) return;
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
                generateMap(player,inv);
            }
        }.runTaskLater(getPlugin(),1);

    }

}

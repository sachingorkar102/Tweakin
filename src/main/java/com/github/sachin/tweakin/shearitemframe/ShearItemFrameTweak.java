package com.github.sachin.tweakin.shearitemframe;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class ShearItemFrameTweak extends BaseTweak implements Listener{

    private final NamespacedKey key = Tweakin.getKey("tweakin-frame");
    private SIFFlag flag;

    public ShearItemFrameTweak(Tweakin plugin) {
        super(plugin, "shear-item-frame");
        if(plugin.isWorldGuardEnabled){
            this.flag = (SIFFlag) plugin.getWGFlagManager().getFlag(TConstants.SIF_FLAG);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = false)
    public void onShear(PlayerInteractEntityEvent e){
        if(!(e.getRightClicked() instanceof ItemFrame)) return;
        Player player = e.getPlayer();
        if(player.isSneaking() || !player.hasPermission("tweakin.shearitemframe.use")) return;
        if(getBlackListWorlds().contains(player.getWorld().getName())) return;
        if(flag != null && !flag.queryFlag(player, e.getRightClicked().getLocation())) return;
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        if(item.getType() != Material.SHEARS) return;
        ItemFrame frame = (ItemFrame) e.getRightClicked();
        if(frame.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) return;
        if(!frame.getItem().getType().isAir()){
            boolean placed = plugin.getNmsHelper().placeItem(player, frame.getLocation(), new ItemStack(Material.DIRT), frame.getAttachedFace(),null);
            if(placed){
                frame.getLocation().getBlock().setType(Material.AIR);
                frame.setVisible(false);
                player.getWorld().playSound(frame.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);
                frame.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof ItemFrame){
            ItemFrame frame = (ItemFrame) e.getEntity();
            new BukkitRunnable(){
                @Override
                public void run() {
                    if(frame.getItem().getType().isAir() && frame.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)){
                        frame.setVisible(true);
                        frame.getPersistentDataContainer().remove(key);
                    }
                
                }
            }.runTaskLater(plugin, 2);
        }
    }

    
}

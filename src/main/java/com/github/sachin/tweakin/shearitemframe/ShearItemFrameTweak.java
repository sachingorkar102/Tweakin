package com.github.sachin.tweakin.shearitemframe;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ShearItemFrameTweak extends BaseTweak implements Listener{

    private final NamespacedKey key = Tweakin.getKey("tweakin-frame");

    public ShearItemFrameTweak(Tweakin plugin) {
        super(plugin, "shear-item-frame");
    }

    @EventHandler
    public void onShear(PlayerInteractEntityEvent e){
        if(!(e.getRightClicked() instanceof ItemFrame)) return;
        Player player = e.getPlayer();
        if(player.isSneaking()) return;
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        if(item.getType() != Material.SHEARS) return;
        ItemFrame frame = (ItemFrame) e.getRightClicked();
        if(frame.getItem() != null){
            frame.setVisible(false);
            player.getWorld().playSound(frame.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);
            frame.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof ItemFrame){
            ItemFrame frame = (ItemFrame) e.getEntity();
            
            if(frame.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)){
                frame.setVisible(true);
                frame.getPersistentDataContainer().remove(key);
            }
        }
    }

    
}

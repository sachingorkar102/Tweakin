package com.github.sachin.tweakin.modules.shearnametag;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.TConstants;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

@Tweak(name = "shear-name-tag")
public class ShearNameTagTweak extends BaseTweak implements Listener{



    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNameTagUse(PlayerInteractAtEntityEvent e){
        if(e.isCancelled()) return;
        
        Player player = e.getPlayer();
        if(getBlackListWorlds().contains(player.getWorld().getName())) return;
        ItemStack item = player.getInventory().getItem(e.getHand());
        Entity entity = e.getRightClicked();
        if(!(entity instanceof LivingEntity)) return;
        if(item == null) return;
        if(item.getType() == Material.NAME_TAG && !(entity instanceof ArmorStand) && !entity.getPersistentDataContainer().has(TConstants.NAMETAGED_MOB,PersistentDataType.INTEGER)){
            entity.getPersistentDataContainer().set(TConstants.NAMETAGED_MOB, PersistentDataType.INTEGER, 1);
        }
        else if(item.getType() == Material.SHEARS && player.isSneaking() && hasPermission(player, Permissions.SHEARNAMETAG) && entity.getPersistentDataContainer().has(TConstants.NAMETAGED_MOB, PersistentDataType.INTEGER)){
            e.setCancelled(true);
            entity.getPersistentDataContainer().remove(TConstants.NAMETAGED_MOB);
            ItemStack nameTag = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = nameTag.getItemMeta();
            meta.setDisplayName(entity.getCustomName());
            nameTag.setItemMeta(meta);
            player.getWorld().dropItemNaturally(entity.getLocation(), nameTag);
            player.getWorld().playSound(entity.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);
            if(entity instanceof ArmorStand){
                entity.setCustomNameVisible(false);
            }
            entity.setCustomName(null);
        }
    }
    
}

package com.github.sachin.tweakin.lavabucketcan;

import java.util.ArrayList;
import java.util.List;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class LavaBucketTrashCan extends BaseTweak implements Listener{

    public LavaBucketTrashCan(Tweakin plugin) {
        super(plugin, "lava-bucket-trash-can");
    }


    @EventHandler
    public void onLavaPickUp(PlayerBucketFillEvent e){

        ItemStack item = e.getItemStack();
        if(item.getType() != Material.LAVA_BUCKET) return;
        ItemMeta meta = item.getItemMeta();
        List<String> helpLore = new ArrayList<>();
        List<String> list = getConfig().getStringList("help-lore");
        if(list == null) return;
        if(list.isEmpty()) return;
        for (String string : list) {
            helpLore.add(ChatColor.translateAlternateColorCodes('&', string));
        }
        meta.setLore(helpLore);
        item.setItemMeta(meta);
    }


    @EventHandler
    public void onTrashCanUse(InventoryClickEvent e){
        if(e.getClick() != ClickType.RIGHT || e.getAction() != InventoryAction.SWAP_WITH_CURSOR) return;
        if(e.getCurrentItem() == null || e.getCursor() == null) return;
        if(!(e.getClickedInventory() instanceof PlayerInventory)) return;
        if(isBlackListMaterial(e.getCursor().getType())) return;
        if(e.getCurrentItem().getType() != Material.LAVA_BUCKET) return;
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        player.setItemOnCursor(null);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 0.25F, 2F + (float)Math.random());

    }

    private boolean isBlackListMaterial(Material mat){
        String blockName = mat.toString();
        for (String string : getConfig().getStringList("black-list-materials")) {
            if(string.startsWith("^")){
                if(blockName.endsWith(string.replace("^", ""))){
                    return true;
                }
            }
            else if(string.startsWith("*")){
                if(blockName.startsWith(string.replace("*", ""))){
                    return true;
                }
            }
            if(blockName.equals(string)){
                return true;
            }
        }
        return false;
    }
    
}

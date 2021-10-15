package com.github.sachin.tweakin.modules.lavabucketcan;

import java.util.ArrayList;
import java.util.List;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.Permissions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
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
        if(e.getInventory().getHolder() instanceof LavaBucketTrashGui){
            Inventory inv = e.getView().getTopInventory();
            if(e.getSlot() != 4 && e.isShiftClick()){
                inv.setItem(4, null);
                
            }
            if(e.getCurrentItem() != null && e.getCurrentItem().isSimilar(plugin.getMiscItems().FILLAR_GLASS)) e.setCancelled(true);
            return;
        }
        Player player = (Player) e.getWhoClicked();
        if(!player.hasPermission(Permissions.LAVABUCKET_DRAGDROP)) return;
        if(e.getClick() != ClickType.RIGHT || e.getAction() != InventoryAction.SWAP_WITH_CURSOR) return;
        if(e.getCurrentItem() == null || e.getCursor() == null) return;
        if(!(e.getClickedInventory() instanceof PlayerInventory)) return;
        if(matchString(e.getCursor().getType().toString(), getConfig().getStringList("black-list-materials"))) return;
        if(e.getCurrentItem().getType() != Material.LAVA_BUCKET) return;
        e.setCancelled(true);
        player.setItemOnCursor(null);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 0.25F, 2F + (float)Math.random());

    }


    // @EventHandler
    // public void onPlayerClick(PlayerInteractEvent e){
    //     if(e.getAction()==Action.RIGHT_CLICK_AIR && e.getItem() != null && e.getItem().getType()==Material.LAVA_BUCKET && e.getPlayer().hasPermission(Permissions.LAVABUCKET_GUI)){
    //         new LavaBucketTrashGui(e.getPlayer()).open();
    //     }
    // }


    private class LavaBucketTrashGui implements InventoryHolder{

        private Inventory inventory;
        private Player player;

        public LavaBucketTrashGui(Player player){
            this.player = player;
            this.inventory = Bukkit.createInventory(this,9, LavaBucketTrashCan.this.getTweakManager().getMessageManager().getMessageWithoutPrefix("lava-bucket-trash-can-gui"));
            for(int i=0;i<9;i++){
                if(i == 4) continue;
                inventory.setItem(i, Tweakin.getPlugin().getMiscItems().FILLAR_GLASS);
            }
        }

        public void open(){
            this.player.openInventory(inventory);
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }
    }

    // NOT USED ANYMORE
    // private boolean isBlackListMaterial(Material mat){
    //     String blockName = mat.toString();
    //     for (String string : getConfig().getStringList("black-list-materials")) {
    //         if(string.startsWith("^")){
    //             if(blockName.endsWith(string.replace("^", ""))){
    //                 return true;
    //             }
    //         }
    //         else if(string.startsWith("*")){
    //             if(blockName.startsWith(string.replace("*", ""))){
    //                 return true;
    //             }
    //         }
    //         if(blockName.equals(string)){
    //             return true;
    //         }
    //     }
    //     return false;
    // }
    
}

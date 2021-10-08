package com.github.sachin.tweakin.modules.betterarmorstands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.nbtapi.NBTItem;
import com.github.sachin.tweakin.utils.ItemBuilder;
import com.github.sachin.tweakin.utils.TConstants;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.EulerAngle;

import de.jeff_media.morepersistentdatatypes.DataType;
import net.md_5.bungee.api.ChatColor;

public enum GuiItems {


    COPY_BUTTON("copy-button"){
        @Override
        public void handleClick(InventoryClickEvent e, ArmorStand as, ClickType click, Inventory inv, int slot,Location loc, double changedValue, EulerAngle angle) {
            e.getWhoClicked().getPersistentDataContainer().set(TConstants.COPY_PASTE_KEY, DataType.UUID, as.getUniqueId());
            inv.setItem(slot, PASTE_BUTTON.item);
        }
    },
    PASTE_BUTTON("paste-button"){
        @Override
        public void handleClick(InventoryClickEvent e, ArmorStand as, ClickType click, Inventory inv, int slot,Location loc, double changedValue, EulerAngle angle) {
            Player player = (Player) e.getWhoClicked();
            UUID uuid = player.getPersistentDataContainer().get(TConstants.COPY_PASTE_KEY, DataType.UUID);
            if(uuid != null){
                Entity en = Bukkit.getEntity(uuid);
                if(en != null && !en.isDead()){
                    ArmorStand cAS = (ArmorStand) en;
                    as.setHeadPose(cAS.getHeadPose());
                    as.setBodyPose(cAS.getBodyPose());
                    as.setLeftArmPose(cAS.getLeftArmPose());
                    as.setRightArmPose(cAS.getRightArmPose());
                    as.setLeftLegPose(cAS.getLeftLegPose());
                    as.setRightLegPose(cAS.getRightLegPose());
                }
                inv.setItem(slot, COPY_BUTTON.item);
                player.getPersistentDataContainer().remove(TConstants.COPY_PASTE_KEY);
            }
        }
    },
    PLATE_EN("plate-enabled"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            inv.setItem(slot, PLATE_DI.item);
            as.setBasePlate(false);
        }
    },
    PLATE_DI("plate-disabled"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            inv.setItem(slot, PLATE_EN.item);
            as.setBasePlate(true);
        }
    },
    SMALL_EN("small-enabled"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            
            inv.setItem(slot, SMALL_DI.item);
            as.setSmall(false);
        }
    },
    SMALL_DI("small-disabled"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            inv.setItem(slot, SMALL_EN.item);
            as.setSmall(true);
        }
    },
    VISIBLE_EN("visible-enabled"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            
            inv.setItem(slot, VISIBLE_DI.item);
            as.setVisible(false);
        }
    },

    VISIBLE_DI("visible-disabled"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            inv.setItem(slot, VISIBLE_EN.item);
            as.setVisible(true);             
        }
    },
    GRAVITY_EN("gravity-enabled"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            inv.setItem(slot, GLOWING_DI.item);
            as.setGravity(false);
        }
    },
    GRAVITY_DI("gravity-disabled"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            
            inv.setItem(slot, GRAVITY_EN.item);
            as.setGravity(true);  
        }
    },
    ARMS_EN("arms-enabled"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            
            inv.setItem(slot, ARMS_DI.item);
            as.setArms(false);
        }
    },
    ARMS_DI("arms-disabled"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            inv.setItem(slot, ARMS_EN.item);
            as.setArms(true);
        }
    },
    GLOWING_EN("glowing-enabled"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            
            inv.setItem(slot, GLOWING_DI.item);
            as.setGlowing(false);
        }
    },
    GLOWING_DI("glowing-disabled"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            inv.setItem(slot, GLOWING_EN.item);
            as.setGlowing(true);
        }
    },
    INVULNERABLE_EN("invulnerable-enabled"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            
            inv.setItem(slot, INVULNERABLE_DI.item);
            as.setInvulnerable(false);
        }
    },
    INVULNERABLE_DI("invulnerable-disabled"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            
            inv.setItem(slot, INVULNERABLE_EN.item);
            as.setInvulnerable(true);
        }
    },
    INTERACTABLE_EN("interactable-enabled"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            inv.setItem(slot, INTERACTABLE_DI.item);
            as.getPersistentDataContainer().set(TConstants.INTERACTABLE_AS, PersistentDataType.INTEGER, 1);
        }
    },
    INTERACTABLE_DI("interactable-disabled"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {            
            
            inv.setItem(slot, INTERACTABLE_EN.item);
            as.getPersistentDataContainer().remove(TConstants.INTERACTABLE_AS);
        }
    },
    HEAD_X("head-x"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getHeadPose();
            changedValue = getChangedAngle(angle.getX(), click);
            angle = angle.setX(changedValue);
            as.setHeadPose(angle);
            inv.setItem(slot, HEAD_X.setDouble(Math.toDegrees(changedValue)));
        }
    },
    HEAD_Y("head-y"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getHeadPose();
            changedValue = getChangedAngle(angle.getY(), click);
            angle = angle.setY(changedValue);
            as.setHeadPose(angle);
            inv.setItem(slot, HEAD_Y.setDouble(Math.toDegrees(changedValue)));
        }
    },
    HEAD_Z("head-z"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getHeadPose();
            changedValue = getChangedAngle(angle.getZ(), click);
            angle = angle.setZ(changedValue);
            as.setHeadPose(angle);
            inv.setItem(slot, HEAD_Z.setDouble(Math.toDegrees(changedValue)));
        }
    },
    POS_X("pos-x"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            double pos = loc.getX();
            changedValue = getChangedAngle(pos, click);
            loc.setX(changedValue); as.teleport(loc);
            inv.setItem(slot, POS_X.setDouble(changedValue));

        }
    },
    POS_Y("pos-y"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            double pos = loc.getY();
            changedValue = getChangedAngle(pos, click);
            loc.setY(changedValue); as.teleport(loc);
            inv.setItem(slot, POS_Y.setDouble(changedValue));
        }
    },
    POS_Z("pos-z"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            double pos = loc.getZ();
            changedValue = getChangedAngle(pos, click);
            loc.setZ(changedValue); as.teleport(loc);
            inv.setItem(slot, POS_Z.setDouble(changedValue));
        }
    },
    TORSO_X("torso-x"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getBodyPose();
            changedValue = getChangedAngle(angle.getX(), click);
            angle = angle.setX(changedValue); as.setBodyPose(angle);
            inv.setItem(slot, TORSO_X.setDouble(Math.toDegrees(changedValue)));
        }
    },
    TORSO_Y("torso-y"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getBodyPose();
            changedValue = getChangedAngle(angle.getY(), click);
            angle = angle.setY(changedValue); as.setBodyPose(angle);
            inv.setItem(slot, TORSO_Y.setDouble(Math.toDegrees(changedValue)));
        }
    },
    TORSO_Z("torso-z"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getBodyPose();
            changedValue = getChangedAngle(angle.getZ(), click);
            angle = angle.setZ(changedValue); as.setBodyPose(angle);
            inv.setItem(slot, TORSO_Z.setDouble(Math.toDegrees(changedValue)));
        }
    },
    LARM_X("left-arm-x"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getLeftArmPose();
            changedValue = getChangedAngle(angle.getX(), click);
            angle = angle.setX(changedValue); as.setLeftArmPose(angle);
            inv.setItem(slot, LARM_X.setDouble(Math.toDegrees(changedValue)));
        }
    },
    LARM_Y("left-arm-y"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getLeftArmPose();
            changedValue = getChangedAngle(angle.getY(), click);
            angle = angle.setY(changedValue); as.setLeftArmPose(angle);
            inv.setItem(slot, LARM_Y.setDouble(Math.toDegrees(changedValue)));
        }
    },
    LARM_Z("left-arm-z"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getLeftArmPose();
            changedValue = getChangedAngle(angle.getZ(), click);
            angle = angle.setZ(changedValue); as.setLeftArmPose(angle);
            inv.setItem(slot, LARM_Z.setDouble(Math.toDegrees(changedValue)));
        }
    },
    RARM_X("right-arm-x"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getRightArmPose();
            changedValue = getChangedAngle(angle.getX(), click);
            angle = angle.setX(changedValue); as.setRightArmPose(angle);
            inv.setItem(slot, RARM_X.setDouble(Math.toDegrees(changedValue)));
        }
    },
    RARM_Y("right-arm-y"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getRightArmPose();
            changedValue = getChangedAngle(angle.getY(), click);
            angle = angle.setY(changedValue); as.setRightArmPose(angle);
            inv.setItem(slot, RARM_Y.setDouble(Math.toDegrees(changedValue)));
        }
    },
    RARM_Z("right-arm-z"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getRightArmPose();
            changedValue = getChangedAngle(angle.getZ(), click);
            angle = angle.setZ(changedValue); as.setRightArmPose(angle);
            inv.setItem(slot, RARM_Z.setDouble(Math.toDegrees(changedValue)));
        }
    },
    LLEG_X("left-leg-x"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getLeftLegPose();
            changedValue = getChangedAngle(angle.getX(), click);
            angle = angle.setX(changedValue); as.setLeftLegPose(angle);
            inv.setItem(slot, LLEG_X.setDouble(Math.toDegrees(changedValue)));
        }
    },
    LLEG_Y("left-leg-y"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getLeftLegPose();
            changedValue = getChangedAngle(angle.getY(), click);
            angle = angle.setY(changedValue); as.setLeftLegPose(angle);
            inv.setItem(slot, LLEG_Y.setDouble(Math.toDegrees(changedValue)));
        }
    },
    LLEG_Z("left-leg-z"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getLeftLegPose();
            changedValue = getChangedAngle(angle.getZ(), click);
            angle = angle.setZ(changedValue); as.setLeftLegPose(angle);
            inv.setItem(slot, LLEG_Z.setDouble(Math.toDegrees(changedValue)));
        }
    },
    RLEG_X("right-leg-x"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getRightLegPose();
            changedValue = getChangedAngle(angle.getX(), click);
            angle = angle.setX(changedValue); as.setRightLegPose(angle);
            inv.setItem(slot, RLEG_X.setDouble(Math.toDegrees(changedValue)));
        }
    },
    RLEG_Y("right-leg-y"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getRightLegPose();
            changedValue = getChangedAngle(angle.getY(), click);
            angle = angle.setY(changedValue); as.setRightLegPose(angle);
            inv.setItem(slot, RLEG_Y.setDouble(Math.toDegrees(changedValue)));
        }
    },
    RLEG_Z("right-leg-z"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            angle = as.getRightLegPose();
            changedValue = getChangedAngle(angle.getZ(), click);
            angle = angle.setZ(changedValue); as.setRightLegPose(angle);
            inv.setItem(slot, RLEG_Z.setDouble(Math.toDegrees(changedValue)));
        }
    },
    BODY_ROTATION("body-rotation"){
        @Override
        public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle) {
            
            
            float val = getYawChange(loc.getYaw(), click);
            loc.setYaw(val);
            as.teleport(loc);
            inv.setItem(slot, BODY_ROTATION.setDouble(val));
        }
    },
    UUID_LOCKED("uuid-locked"){
        @Override
        public void handleClick(InventoryClickEvent e, ArmorStand as, ClickType click, Inventory inv, int slot,Location loc, double changedValue, EulerAngle angle) {
            as.getPersistentDataContainer().remove(TConstants.UUID_LOCK_KEY);
            inv.setItem(slot, UUID_UNLOCKED.item);
        }
    },
    UUID_UNLOCKED("uuid-unlocked"){
        @Override
        public void handleClick(InventoryClickEvent e, ArmorStand as, ClickType click, Inventory inv, int slot,Location loc, double changedValue, EulerAngle angle) {
            Player player = (Player) e.getWhoClicked();
            as.getPersistentDataContainer().set(TConstants.UUID_LOCK_KEY, DataType.UUID, player.getUniqueId());
            inv.setItem(slot, UUID_LOCKED.setUuidLoc(player.getName()));
        }
    },
    PRESET_POSES("preset-poses"){
        @Override
        public void handleClick(InventoryClickEvent e, ArmorStand as, ClickType click, Inventory inv, int slot,Location loc, double changedValue, EulerAngle angle) {
            Player player = (Player) e.getWhoClicked();
            PresetPoseGui gui = new PresetPoseGui(player, as);
            gui.openPage();
        }
    }
    ;


    private String id;
    public ItemStack item;
    private final FileConfiguration config = Tweakin.getPlugin().getMiscItems().CONFIG;


    private GuiItems(String id){
        this.id = id;
        this.item = ItemBuilder.asGuiItem(config.getConfigurationSection(id), this);
        if(id.endsWith("-x") || id.endsWith("-z") || id.endsWith("-y")){
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            if(meta.hasLore()){
                lore = meta.getLore();
            }
            for(String s : config.getStringList("lore-helper")){
                lore.add(ChatColor.translateAlternateColorCodes('&',s));

            }
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
    }

    public String getId() {
        return id;
    }

    public ItemStack getItem() {
        return item;
    }


    public double getDouble(ItemStack item){
        NBTItem nbti = new NBTItem(item);
        if(nbti.hasKey("tweakin-as-double")){
            return nbti.getDouble("tweakin-as-double");
        }
        return 0.0;
    }

    public ItemStack setDouble(double value){
        value = Math.round(value*100.9);
        value = value/100.0;
        ItemStack i = item.clone();
        ItemMeta meta = i.getItemMeta();
        if(meta.hasDisplayName()){
            meta.setDisplayName(meta.getDisplayName().replace("%value%", String.valueOf(value)));
        }
        
        i.setItemMeta(meta);
        NBTItem nbti = new NBTItem(i);
        nbti.setDouble("tweakin-as-double", value);
        return nbti.getItem();
    }

    public ItemStack setFloat(float value){
        ItemStack i = item.clone();
        ItemMeta meta = i.getItemMeta();
        if(meta.hasDisplayName()){
            meta.setDisplayName(meta.getDisplayName().replace("%value%", String.valueOf(value)));
        }
        
        i.setItemMeta(meta);
        NBTItem nbti = new NBTItem(i);
        nbti.setDouble("tweakin-as-double", value);
        return nbti.getItem(); 
    }


    public ItemStack setUuidLoc(String playername){
        ItemStack i = this.item.clone();
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(meta.getDisplayName().replace("%player_name%", playername));
        i.setItemMeta(meta);
        return i;
    }


    public static GuiItems getGuiItem(ItemStack item){
        NBTItem nbtItem = new NBTItem(item);
        if(nbtItem.hasKey("tweakin-armor-stand-gui")){
            return GuiItems.valueOf(nbtItem.getString("tweakin-armor-stand-gui").toUpperCase());
        }
        return null;
    }


    public void handleClick(InventoryClickEvent e,ArmorStand as,ClickType click,Inventory inv,int slot,Location loc,double changedValue,EulerAngle angle){
    }

    private static double getChangedAngle(double orignal,ClickType click){
        double changedValue = Math.toDegrees(orignal);
        if(click==ClickType.LEFT){
            changedValue = changedValue+0.5;
        }
        else if(click==ClickType.SHIFT_LEFT){
            changedValue = changedValue+5;
        }
        else if(click==ClickType.RIGHT){
            changedValue = changedValue-0.5;
        }
        else if(click == ClickType.SHIFT_RIGHT){
            changedValue = changedValue-5;
        }
        return Math.toRadians(changedValue);
    }

    private static float getYawChange(float orignal,ClickType click){
        if(click==ClickType.LEFT){
            orignal = orignal+1;
        }
        else if(click==ClickType.SHIFT_LEFT){
            orignal = orignal+5;
        }
        else if(click==ClickType.RIGHT){
            orignal = orignal-1;
        }
        else if(click == ClickType.SHIFT_RIGHT){
            orignal= orignal-5;
        }
        if(orignal > 360){
            return orignal-360;
        }
        return orignal;
    }
    
}

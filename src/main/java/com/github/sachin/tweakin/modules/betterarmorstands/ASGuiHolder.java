package com.github.sachin.tweakin.modules.betterarmorstands;

import java.util.Arrays;
import java.util.UUID;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.EulerAngle;

import de.jeff_media.morepersistentdatatypes.DataType;

public class ASGuiHolder implements InventoryHolder{

    public final Player player;
    public final ArmorStand armorStand;
    public final Inventory inventory;
    public static final Tweakin plugin = Tweakin.getPlugin();


    public ASGuiHolder(Player player,ArmorStand armorStand){
        this.armorStand= armorStand;
        this.player = player;
        this.inventory = Bukkit.createInventory(this, 54, plugin.getTweakManager().getMessageManager().getMessageWithoutPrefix("armorstand-editor-gui-title"));
    }

    public static void openGui(Player player,ArmorStand as){
        if(as.getPersistentDataContainer().has(TConstants.UUID_LOCK_KEY, DataType.UUID) && !player.getUniqueId().equals(as.getPersistentDataContainer().get(TConstants.UUID_LOCK_KEY,DataType.UUID)) && !player.hasPermission("tweakin.betterarmorstands.uuidlockbypass")){
            player.sendMessage(plugin.getTweakManager().getMessageManager().getMessage("armorstand-locked").replace("%player%", Bukkit.getOfflinePlayer(as.getPersistentDataContainer().get(TConstants.UUID_LOCK_KEY,DataType.UUID)).getName()));
            return;
            
        }
        if(as.getPersistentDataContainer().has(TConstants.ARMORSTAND_EDITED, PersistentDataType.INTEGER)){
            player.sendMessage(plugin.getTweakManager().getMessageManager().getMessage("armorstand-edited"));
            return;
        }
        ASGuiHolder gui = new ASGuiHolder(player,as);
        gui.setItems();
        player.openInventory(gui.getInventory());
        as.getPersistentDataContainer().set(TConstants.ARMORSTAND_EDITED, PersistentDataType.INTEGER, 1);
    }

    


    public void setItems(){
        for(int i : Arrays.asList(0,2,3,12,13,18,20,21,22,23,24,25,26,27,37)){
            inventory.setItem(i, plugin.getMiscItems().FILLAR_GLASS);
        }
        inventory.setItem(4, armorStand.hasBasePlate() ? GuiItems.PLATE_EN.item : GuiItems.PLATE_DI.item);
        inventory.setItem(5, armorStand.isSmall() ? GuiItems.SMALL_EN.item : GuiItems.SMALL_DI.item);
        inventory.setItem(6, armorStand.isVisible() ? GuiItems.VISIBLE_EN.item : GuiItems.VISIBLE_DI.item);
        inventory.setItem(7, armorStand.hasGravity() ? GuiItems.GRAVITY_EN.item : GuiItems.GRAVITY_DI.item);
        inventory.setItem(8, armorStand.hasArms() ? GuiItems.ARMS_EN.item : GuiItems.ARMS_DI.item);
        inventory.setItem(14, armorStand.isGlowing() ? GuiItems.GLOWING_EN.item : GuiItems.GLOWING_DI.item);
        inventory.setItem(15, armorStand.isInvulnerable() ? GuiItems.INVULNERABLE_EN.item : GuiItems.INVULNERABLE_DI.item);
        inventory.setItem(16, armorStand.getPersistentDataContainer().has(TConstants.INTERACTABLE_AS, PersistentDataType.INTEGER)? GuiItems.INTERACTABLE_DI.item : GuiItems.INTERACTABLE_EN.item);
        EulerAngle headA = armorStand.getHeadPose();
        inventory.setItem(29, GuiItems.HEAD_X.setDouble(headA.getX()));
        inventory.setItem(38, GuiItems.HEAD_Y.setDouble(headA.getY()));
        inventory.setItem(47, GuiItems.HEAD_Z.setDouble(headA.getZ()));
        EulerAngle torsoA = armorStand.getBodyPose();
        inventory.setItem(30, GuiItems.TORSO_X.setDouble(torsoA.getX()));
        inventory.setItem(39, GuiItems.TORSO_Y.setDouble(torsoA.getY()));
        inventory.setItem(48, GuiItems.TORSO_Z.setDouble(torsoA.getZ()));
        EulerAngle lArmA = armorStand.getLeftArmPose();
        inventory.setItem(31, GuiItems.LARM_X.setDouble(lArmA.getX()));
        inventory.setItem(40, GuiItems.LARM_Y.setDouble(lArmA.getY()));
        inventory.setItem(49, GuiItems.LARM_Z.setDouble(lArmA.getZ()));
        EulerAngle rArmA = armorStand.getRightArmPose();
        inventory.setItem(32, GuiItems.RARM_X.setDouble(rArmA.getX()));
        inventory.setItem(41, GuiItems.RARM_Y.setDouble(rArmA.getY()));
        inventory.setItem(50, GuiItems.RARM_Z.setDouble(rArmA.getZ()));
        EulerAngle lLegA = armorStand.getLeftLegPose();
        inventory.setItem(33, GuiItems.LLEG_X.setDouble(lLegA.getX()));
        inventory.setItem(42, GuiItems.LLEG_Y.setDouble(lLegA.getY()));
        inventory.setItem(51, GuiItems.LLEG_Z.setDouble(lLegA.getZ()));
        EulerAngle rLegA = armorStand.getRightLegPose();
        inventory.setItem(34, GuiItems.RLEG_X.setDouble(rLegA.getX()));
        inventory.setItem(43, GuiItems.RLEG_Y.setDouble(rLegA.getY()));
        inventory.setItem(52, GuiItems.RLEG_Z.setDouble(rLegA.getZ()));
        Location loc = armorStand.getLocation();
        inventory.setItem(35, GuiItems.POS_X.setDouble(loc.getX()));
        inventory.setItem(44, GuiItems.POS_Y.setDouble(loc.getY()));
        inventory.setItem(53, GuiItems.POS_Z.setDouble(loc.getZ()));

        inventory.setItem(1, armorStand.getEquipment().getHelmet());
        inventory.setItem(10, armorStand.getEquipment().getChestplate());
        inventory.setItem(19, armorStand.getEquipment().getLeggings());
        inventory.setItem(28, armorStand.getEquipment().getBoots());
        inventory.setItem(9, armorStand.getEquipment().getItemInMainHand());
        inventory.setItem(11, armorStand.getEquipment().getItemInOffHand());
        if(armorStand.getPersistentDataContainer().has(TConstants.UUID_LOCK_KEY, DataType.UUID)){
            inventory.setItem(17, GuiItems.UUID_LOCKED.setUuidLoc(Bukkit.getOfflinePlayer(armorStand.getPersistentDataContainer().get(TConstants.UUID_LOCK_KEY, DataType.UUID)).getName()));
        }
        else{
            inventory.setItem(17, GuiItems.UUID_UNLOCKED.item);
        }
        inventory.setItem(46, GuiItems.BODY_ROTATION.setDouble(armorStand.getLocation().getYaw()));
        inventory.setItem(36,GuiItems.PRESET_POSES.item);
        inventory.setItem(45, player.getPersistentDataContainer().has(TConstants.COPY_PASTE_KEY, DataType.UUID) ? GuiItems.PASTE_BUTTON.item : GuiItems.COPY_BUTTON.item);
    }


    @Override
    public Inventory getInventory() {
        return inventory;
    }
    
}

package com.github.sachin.tweakin.nbtapi.nms;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_16_R2.*;

import org.bukkit.inventory.ItemStack;



public class NBTItem_1_16_R2 extends NMSHelper{

    
    private net.minecraft.server.v1_16_R2.ItemStack nmsItem;
    private NBTTagCompound compound;

    public NBTItem_1_16_R2(ItemStack item){
        if(item == null) return;
        ItemStack bukkitItem = item.clone();
        this.nmsItem = CraftItemStack.asNMSCopy(bukkitItem);
        this.compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();

    }

    @Override
    public NMSHelper newItem(ItemStack item) {
        NMSHelper nbti = new NBTItem_1_16_R2(item);
        return nbti;
    }
    
    @Override
    public void setString(String key,String value){
        compound.setString(key, value);
    }

    @Override
    public ItemStack getItem() {
        nmsItem.setTag(compound);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public boolean hasKey(String key) {
        return compound.hasKey(key);
    }

    @Override
    public String getString(String key) {
        return compound.getString(key);
    }

    @Override
    public void removeKey(String key) {
        compound.remove(key);
    }

    @Override
    public void setBoolean(String key, boolean value) {
        compound.setBoolean(key, value);
    }

    @Override
    public boolean getBoolean(String key) {
        return compound.getBoolean(key);
    }

    @Override
    public void setInt(String key, int value) {
        compound.setInt(key, value);
    }

    @Override
    public void setLong(String key, long value) {
        compound.setLong(key, value);
    }

    @Override
    public void setDouble(String key, double value) {
        compound.setDouble(key, value);
        
    }

    @Override
    public int getInt(String key) {
        return compound.getInt(key);
    }

    @Override
    public long getLong(String key) {
        return compound.getLong(key);
    }

    @Override
    public double getDouble(String key) {
        return compound.getDouble(key);
    }

    @Override
    public void attack(Player player, Entity target) {
        
        ((CraftPlayer)player).getHandle().attack(((CraftEntity)target).getHandle());
        ((CraftPlayer)player).getHandle().resetAttackCooldown();
        
    }

    public void placeItem(Player player, Location location){
        net.minecraft.server.v1_16_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(player.getInventory().getItemInMainHand());
        BlockPosition pos = new BlockPosition(location.getX(), location.getY(), location.getZ());
        EntityPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
        MovingObjectPositionBlock mop = new MovingObjectPositionBlock(new Vec3D(location.getX(),location.getY(),location.getZ()),EnumDirection.DOWN,pos,false);
        if(player.getGameMode() !=GameMode.SURVIVAL){
            player.swingMainHand();
        }
        nmsItem.placeItem(new ItemActionContext(nmsPlayer,EnumHand.MAIN_HAND,mop), EnumHand.MAIN_HAND);
    }

    
}

package com.github.sachin.tweakin.nbtapi.nms;


import com.google.common.base.Enums;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.util.ColorUtil;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.item.context.BlockActionContext;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.Vec3D;

public class NBTItem_1_17_R1 extends NMSHelper{
    private net.minecraft.world.item.ItemStack nmsItem;
    private NBTTagCompound compound;

    public NBTItem_1_17_R1(ItemStack item){
        if(item == null) return;
        ItemStack bukkitItem = item.clone();
        this.nmsItem = CraftItemStack.asNMSCopy(bukkitItem);
        this.compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();

    }

    @Override
    public NMSHelper newItem(ItemStack item) {
        NMSHelper nbti = new NBTItem_1_17_R1(item);
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

    public boolean placeItem(Player player, Location location,ItemStack item,BlockFace hitFace){
        
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        BlockPosition pos = new BlockPosition(location.getX(), location.getY(), location.getZ());
        EntityPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
        
        MovingObjectPositionBlock mop = new MovingObjectPositionBlock(new Vec3D(location.getX(),location.getY(),location.getZ()),Enums.getIfPresent(EnumDirection.class, hitFace.toString()).or(EnumDirection.c),pos,false);
        
        EnumInteractionResult result = nmsItem.placeItem(new BlockActionContext(nmsPlayer,EnumHand.valueOf("MAIN_HAND"),nmsItem,mop), EnumHand.valueOf("MAIN_HAND"));
        // nmsItem.a(nmsPlayer, TooltipFlag.a.a).;
        if(result.toString() == "CONSUME"){
            player.swingMainHand();
            player.getWorld().playSound(location, location.getBlock().getBlockData().getSoundGroup().getPlaceSound(), 1F, 1F);
            return true;
        }
        else{
            return false;
        }
        
        
    }


    public int getColor(String str,int transparency){
        String[] array = str.replace(" ", "").split(",");
        if(array == null || array.length == 0){
            return 100;
        }
        if(array.length != 3) return 100;
        int red = Integer.parseInt(array[0]);
        int green = Integer.parseInt(array[1]);
        int blue = Integer.parseInt(array[2]);
        return ColorUtil.a.a(transparency, red, green, blue);
    }


}
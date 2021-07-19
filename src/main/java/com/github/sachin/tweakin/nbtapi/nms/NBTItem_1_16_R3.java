package com.github.sachin.tweakin.nbtapi.nms;

import java.awt.Color;
import java.util.concurrent.locks.Lock;

import com.google.common.base.Enums;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_16_R3.*;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;



public class NBTItem_1_16_R3 extends NMSHelper{

    
    private net.minecraft.server.v1_16_R3.ItemStack nmsItem;
    private NBTTagCompound compound;

    public NBTItem_1_16_R3(ItemStack item){
        if(item == null) return;
        ItemStack bukkitItem = item.clone();
        this.nmsItem = CraftItemStack.asNMSCopy(bukkitItem);
        this.compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();

    }

    @Override
    public NMSHelper newItem(ItemStack item) {
        NMSHelper nbti = new NBTItem_1_16_R3(item);
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
        net.minecraft.server.v1_16_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        
        BlockPosition pos = new BlockPosition(location.getX(), location.getY(), location.getZ());
        EntityPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
        MovingObjectPositionBlock mop = new MovingObjectPositionBlock(new Vec3D(location.getX(),location.getY(),location.getZ()),Enums.getIfPresent(EnumDirection.class, hitFace.toString()).or(EnumDirection.DOWN),pos,false);
        
        EnumInteractionResult result = nmsItem.placeItem( new BlockActionContext(nmsPlayer, EnumHand.MAIN_HAND, nmsItem, mop), EnumHand.MAIN_HAND);
        
        if(result == EnumInteractionResult.CONSUME){
            player.swingMainHand();
            player.getWorld().playSound(location, location.getBlock().getBlockData().getSoundGroup().getPlaceSound(), 1F, 1F);
            return true;
        }
        else{
            return false;
        }
    }

    public void harvestBlock(Player player,Location location,ItemStack tool){
        net.minecraft.server.v1_16_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(tool);
        EntityPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
        
        BlockPosition pos = new BlockPosition(location.getX(),location.getY(),location.getZ());
        World world = ((CraftWorld)player.getWorld()).getHandle();
        IBlockData blockData = world.getType(pos);
        Block nmsBlock = blockData.getBlock();
        nmsBlock.a(world, nmsPlayer, pos, blockData, world.getTileEntity(pos), nmsItem);
        
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
        return new Color(red,green,blue,transparency).getRGB();
    }

 
    
}

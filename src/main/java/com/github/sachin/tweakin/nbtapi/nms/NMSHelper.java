package com.github.sachin.tweakin.nbtapi.nms;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

public abstract class NMSHelper {


    public abstract NMSHelper newItem(ItemStack item);

    
    
    public abstract void setString(String key,String value);
    public abstract void setBoolean(String key,boolean value);
    public abstract void setInt(String key,int value);
    public abstract void setLong(String key,long value);
    public abstract void setDouble(String key,double value);
    
    
    public abstract String getString(String key);
    public abstract boolean getBoolean(String key);
    public abstract int getInt(String key);
    public abstract long getLong(String key);
    public abstract double getDouble(String key);
    
    
    public abstract boolean hasKey(String key);
    
    public abstract ItemStack getItem();
    
    public abstract void removeKey(String key);

    public abstract void attack(Player player,Entity target);

    public abstract boolean placeItem(Player player,Location location,ItemStack item,BlockFace hitFace,String tweakName,boolean playSound);

    public abstract int getColor(String str,int transparency);

    public abstract void harvestBlock(Player player,Location location,ItemStack tool);

    public abstract void spawnVillager(Villager villager);
    
    public abstract void avoidPlayer(Entity entity,Player player,ConfigurationSection config);


    public abstract boolean matchAxoltlVariant(Entity entity,String color);
    public abstract boolean isScreamingGoat(Entity entity);

    public abstract List<Entity> getEntitiesWithinRadius(int radius,Entity center);



    public void spawnArmorStand(Location location,double radius) {
    }

    public void registerStonecuttingRecipe(String keyName, String group, ItemStack result, ItemStack[] ingredient, boolean exact) {}

}

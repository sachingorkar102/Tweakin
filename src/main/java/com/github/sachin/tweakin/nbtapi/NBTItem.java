package com.github.sachin.tweakin.nbtapi;

import com.github.sachin.tweakin.nbtapi.nms.NMSHelper;

import org.bukkit.inventory.ItemStack;


public class NBTItem {

    private NMSHelper helper;

    public NBTItem(ItemStack item){
        if(item == null){
            return;
        }
        this.helper = NBTAPI.NMSHelper.newItem(item);
    }

    public void setString(String key,String value){
        helper.setString(key,value);
    }

    public boolean hasKey(String key){
        return helper.hasKey(key);
    }

    public String getString(String key){
        return helper.getString(key);
    }


    public ItemStack getItem(){
        return helper.getItem();
    }

    public void setBoolean(String key,boolean value){
        helper.setBoolean(key, value);
    }

    public boolean getBoolean(String key){
        return helper.getBoolean(key);
    }

    public void removeKey(String key){
        helper.removeKey(key);
    }

    public void setInt(String key,int value){
        helper.setInt(key, value);
    }


    public void setLong(String key,long value){
        helper.setLong(key, value);
    }

    public void setDouble(String key,double value){
        helper.setDouble(key, value);
    }

    public int getInt(String key){
        return helper.getInt(key);
    }

    public long getLong(String key){
        return helper.getLong(key);
    }
    
    public double getDouble(String key){
        return helper.getDouble(key);
    }
    

    
}

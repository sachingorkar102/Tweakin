package com.github.sachin.tweakin.utils.compat;

import java.util.Map;

import com.github.sachin.tweakin.Tweakin;

import org.bukkit.inventory.ItemStack;

public class AdvancedEnchantments {

    public static final boolean isPluginEnabled;

    static{
        isPluginEnabled = Tweakin.getPlugin().getServer().getPluginManager().isPluginEnabled("AdvancedEnchantments");
    }


    public static void applyEnchantments(ItemStack book,ItemStack weapon){
        Map<String,Integer> enchants = net.advancedplugins.ae.api.AEAPI.getEnchantmentsOnItem(weapon);
        for(String ench : enchants.keySet()){
            net.advancedplugins.ae.api.AEAPI.applyEnchant(ench, enchants.get(ench), book);
        }
        
    }
    
}

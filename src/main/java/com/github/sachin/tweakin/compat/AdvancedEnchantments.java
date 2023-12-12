package com.github.sachin.tweakin.compat;

import com.github.sachin.tweakin.Tweakin;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class AdvancedEnchantments {

    public static final boolean isPluginEnabled;

    static{
        isPluginEnabled = Tweakin.getPlugin().getServer().getPluginManager().isPluginEnabled("AdvancedEnchantments");
    }


    public static ItemStack applyEnchantments(ItemStack book,ItemStack weapon){
        Map<String,Integer> enchants = net.advancedplugins.ae.api.AEAPI.getEnchantmentsOnItem(weapon);
        ItemStack newBook = book.clone();
        for(String ench : enchants.keySet()){
            newBook = net.advancedplugins.ae.api.AEAPI.applyEnchant(ench, enchants.get(ench), book);
        }
        return newBook;
        
    }
    
}

package com.github.sachin.tweakin.bettergrindstone;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.bukkit.inventory.ItemStack;
import su.nightexpress.excellentenchants.api.enchantment.ExcellentEnchant;
import su.nightexpress.excellentenchants.manager.EnchantManager;

public class ExcellentEnchantsCompat {


    public static void applyEnchantMents(ItemStack book,ItemStack weapon){
        Map<ExcellentEnchant,Integer> enchs = EnchantManager.getItemCustomEnchants(weapon);
        try {
            Class<?> enchantManagerClass = Class.forName("su.nightexpress.excellentenchants.manager.EnchantManager");
            Method method = enchantManagerClass.getMethod("addEnchant");
            
            for(ExcellentEnchant ench : enchs.keySet()){
                method.invoke(book,ench,enchs.get(ench),true);    
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
}

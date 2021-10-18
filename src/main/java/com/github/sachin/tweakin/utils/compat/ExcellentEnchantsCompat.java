package com.github.sachin.tweakin.utils.compat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.inventory.ItemStack;


public class ExcellentEnchantsCompat {


    public static final boolean isEnabled;
    private static Method addEnchant;

    static{
        isEnabled = Tweakin.getPlugin().getServer().getPluginManager().isPluginEnabled("ExcellentEnchants");
        if(isEnabled){
            
            try {
                Class<?> enchantManagerClass = Class.forName("su.nightexpress.excellentenchants.manager.EnchantManager");
                for(Method m : enchantManagerClass.getDeclaredMethods()){
                    if(m.getName().equals("addEnchant")){
                        addEnchant = m;
                        addEnchant.setAccessible(true);
                        break;

                    }
                }
            } catch (SecurityException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public static void applyEnchantMents(ItemStack book,ItemStack weapon){
        if(addEnchant != null){
            Map<su.nightexpress.excellentenchants.api.enchantment.ExcellentEnchant,Integer> enchs = su.nightexpress.excellentenchants.manager.EnchantManager.getItemCustomEnchants(weapon);
            try {
                for(su.nightexpress.excellentenchants.api.enchantment.ExcellentEnchant ench : enchs.keySet()){
                    addEnchant.invoke( null,book,ench,enchs.get(ench),true);    
                }
                su.nightexpress.excellentenchants.manager.EnchantManager.updateItemLoreEnchants(book);
            } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
    
}

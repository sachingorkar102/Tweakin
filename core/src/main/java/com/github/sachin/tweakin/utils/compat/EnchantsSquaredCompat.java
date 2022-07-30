package com.github.sachin.tweakin.utils.compat;

import com.github.sachin.tweakin.Tweakin;
import org.bukkit.inventory.ItemStack;

public class EnchantsSquaredCompat {

    public static boolean isEnabled;

    static {
        isEnabled = Tweakin.getPlugin().getServer().getPluginManager().isPluginEnabled("Enchantssquared");
    }

    public static void applyEnchants(ItemStack book,ItemStack weapon){
        me.athlaeos.enchantssquared.managers.CustomEnchantManager customEnchantManager = me.athlaeos.enchantssquared.managers.CustomEnchantManager.getInstance();
        customEnchantManager.setItemEnchants(book,customEnchantManager.getItemsEnchantsFromPDC(weapon));
    }
}

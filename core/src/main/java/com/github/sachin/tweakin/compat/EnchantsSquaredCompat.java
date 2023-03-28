package com.github.sachin.tweakin.compat;

import com.github.sachin.tweakin.Tweakin;
import org.bukkit.inventory.ItemStack;

public class EnchantsSquaredCompat {

    public static boolean isEnabled;

    static {
        isEnabled = Tweakin.getPlugin().getServer().getPluginManager().isPluginEnabled("Enchantssquared");
        if(isEnabled){
            try {
                me.athlaeos.enchantssquared.EnchantsSquared.setGrindstonesEnabled(false);
                Tweakin.getPlugin().getLogger().info("Disabling GrindStone Listener by EnchantsSquared");
            }catch (Exception ignored){}
        }
    }

    public static void applyEnchants(ItemStack book,ItemStack weapon){
        me.athlaeos.enchantssquared.managers.CustomEnchantManager customEnchantManager = me.athlaeos.enchantssquared.managers.CustomEnchantManager.getInstance();
        customEnchantManager.setItemEnchants(book,customEnchantManager.getItemsEnchantsFromPDC(weapon));
    }
}

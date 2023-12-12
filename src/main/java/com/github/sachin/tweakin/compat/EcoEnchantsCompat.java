package com.github.sachin.tweakin.compat;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;
import org.bukkit.inventory.ItemStack;

public class EcoEnchantsCompat {

    public static boolean isEnabled;

    static {
        isEnabled = Tweakin.getPlugin().getServer().getPluginManager().isPluginEnabled(TConstants.ECOENCHANTS);
    }

    public static void applyEnchants(ItemStack book, ItemStack weapon){

    }
}

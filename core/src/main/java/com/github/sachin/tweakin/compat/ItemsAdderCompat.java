package com.github.sachin.tweakin.compat;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;
import dev.lone.itemsadder.api.CustomFurniture;
import org.bukkit.entity.ArmorStand;

public class ItemsAdderCompat {

    public static final boolean isEnabled;

    static {
        isEnabled = Tweakin.getPlugin().isPluginEnabled(TConstants.ITEMSADDER);
    }

    public static boolean isCustomFurniture(ArmorStand entity){
        return CustomFurniture.byAlreadySpawned(entity) != null;
    }
}

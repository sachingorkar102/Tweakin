package com.github.sachin.tweakin.compat;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;

public class TeaksTweaksCompat {

    public static boolean isEnabled;

    static {
        isEnabled = Tweakin.getPlugin().getServer().getPluginManager().isPluginEnabled(TConstants.TEAKSTWEAKS);
    }

    public static boolean isPackEnabled(String name){
        me.teakivy.teakstweaks.TeaksTweaks instance = Tweakin.getPlugin(me.teakivy.teakstweaks.TeaksTweaks.class);
        return instance.getPacks().contains(name);
    }
}

package com.github.sachin.tweakin.utils.compat;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceHolderApiCompat {

    public static final boolean isEnabled;

    static {
        isEnabled = Tweakin.getPlugin().isPluginEnabled(TConstants.PAPI);
    }

    public static String parse(Player player,String text){
        return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player,text);
    }

    public static String parse(OfflinePlayer player,String text){
        return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, text);
    }
}

package com.github.sachin.tweakin.compat;

import com.github.sachin.tweakin.BaseListener;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;
import com.github.sirblobman.combatlogx.api.event.PlayerTagEvent;
import com.github.sirblobman.combatlogx.api.event.PlayerUntagEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.persistence.PersistentDataType;

public class CombatLogXCompat extends BaseListener {

    public static final boolean isPluginEnabled;

    static{
        isPluginEnabled = Tweakin.getPlugin().isPluginEnabled(TConstants.COMBATLOGX);
    }

    @EventHandler
    public void onEnterCombat(PlayerTagEvent e){
        e.getPlayer().getPersistentDataContainer().set(TConstants.COMBATX_TAG_KEY, PersistentDataType.INTEGER,1);
    }

    @EventHandler
    public void onLeaveCombat(PlayerUntagEvent e){
        e.getPlayer().getPersistentDataContainer().remove(TConstants.COMBATX_TAG_KEY);
    }

}

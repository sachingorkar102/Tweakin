package com.github.sachin.tweakin.compat;

import com.github.sachin.tweakin.BaseListener;
import com.github.sachin.tweakin.Tweakin;
import me.frep.vulcan.api.event.VulcanFlagEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VulcanListenerCompat extends BaseListener {


    @EventHandler
    public void onFlagEvent(VulcanFlagEvent e){
//        plugin.getLogger().info("Check name: "+e.getCheck().getName() );
//        plugin.getLogger().info("Check category: "+e.getCheck().getComplexType());

    }
}

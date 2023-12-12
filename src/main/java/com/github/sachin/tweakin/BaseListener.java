package com.github.sachin.tweakin;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class BaseListener implements Listener {

    protected final Tweakin plugin = Tweakin.getPlugin();


    public void registerEvents(){
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    public void unRegisterEvents(){
        HandlerList.unregisterAll(this);
    }

}

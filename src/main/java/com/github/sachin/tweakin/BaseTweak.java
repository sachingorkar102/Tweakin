package com.github.sachin.tweakin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class BaseTweak {

    private final Tweakin plugin;
    private boolean shouldEnable;
    private ConfigurationSection config;
    private String configKey;
    public boolean registered;

    public BaseTweak(Tweakin plugin,String configKey){
        this.plugin = plugin;
        this.configKey = configKey;
        this.config = plugin.getConfig().getConfigurationSection(configKey);
        if(config == null){
            plugin.getLogger().info("Could not found config section for "+configKey+", ignoring the tweak module..");
            this.shouldEnable = false;
            return;
        }
        this.shouldEnable = config.getBoolean("enabled",true);
    }

    
    public boolean shouldEnable() {
        return shouldEnable;
    }


    public ConfigurationSection getConfig() {
        return config;
    }

    public List<String> getBlackListWorlds(){
        if(config.contains("black-list-worlds")){
            List<String> list = config.getStringList("black-list-worlds");
            if(list != null){
                return list;
            }
        }
        return new ArrayList<>();
    }

    protected void registerEvents(Listener listener) {
        this.plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    protected void unregisterEvents(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public Tweakin getPlugin() {
        return plugin;
    }

    abstract public void register();

    abstract public void unregister();

    public void reload() {
        this.config = plugin.getConfig().getConfigurationSection(configKey);
        this.shouldEnable = config.getBoolean("enabled");
    }
}

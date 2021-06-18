package com.github.sachin.tweakin;

import com.github.sachin.tweakin.commands.ReloadCommand;
import com.github.sachin.tweakin.lapisintable.LapisData;
import com.github.sachin.tweakin.lapisintable.LapisInTableTweak;
import com.github.sachin.tweakin.manager.TweakManager;
import com.github.sachin.tweakin.nbtapi.NBTAPI;
import com.github.sachin.tweakin.nbtapi.nms.NMSHelper;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.PaperCommandManager;

public final class Tweakin extends JavaPlugin {

    private static Tweakin plugin;
    private PaperCommandManager commandManager;
    private TweakManager tweakManager;
    private NMSHelper nmsHelper;
    private boolean isEnabled;



    @Override
    public void onEnable() {
        plugin = this;
        this.isEnabled = true;
        NBTAPI nbtapi = new NBTAPI();
        if(!nbtapi.loadVersions(this)){
            getLogger().warning("Running incompataible version, stopping twekin");
            this.isEnabled = false;
            this.getServer().getPluginManager().disablePlugin(this);
            return;

        }
        this.nmsHelper = nbtapi.getNMSHelper();
        ConfigurationSerialization.registerClass(LapisData.class,"LapisData");
        this.commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new ReloadCommand(this));
        this.saveDefaultConfig();
        this.reloadConfig();
        this.tweakManager = new TweakManager(this);
        tweakManager.load();
        getLogger().info("Tweakin loaded successfully");
    }

    @Override
    public void onDisable() {
        if(!isEnabled) return;
        LapisInTableTweak tweak = (LapisInTableTweak) getTweakManager().getTweakList().get(6);
        if(tweak.registered){
            tweak.saveLapisData();
        }
    }

    public NMSHelper getNmsHelper() {
        return nmsHelper;
    }

    public static Tweakin getPlugin() {
        return plugin;
    }

    public PaperCommandManager getCommandManager() {
        return commandManager;
    }

    public TweakManager getTweakManager() {
        return tweakManager;
    }
}

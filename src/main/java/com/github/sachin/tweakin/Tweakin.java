package com.github.sachin.tweakin;

import com.github.sachin.tweakin.commands.ReloadCommand;
import com.github.sachin.tweakin.lapisintable.LapisData;
import com.github.sachin.tweakin.lapisintable.LapisInTableTweak;
import com.github.sachin.tweakin.manager.TweakManager;
import com.github.sachin.tweakin.nbtapi.NBTAPI;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.PaperCommandManager;

public final class Tweakin extends JavaPlugin {

    private static Tweakin plugin;
    private PaperCommandManager commandManager;
    private TweakManager tweakManager;

    @Override
    public void onEnable() {
        plugin = this;
        NBTAPI nbtapi = new NBTAPI();
        nbtapi.loadVersions(this);
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
        LapisInTableTweak tweak = (LapisInTableTweak) getTweakManager().getTweakList().get(6);
        if(tweak.registered){
            tweak.saveLapisData();
        }
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

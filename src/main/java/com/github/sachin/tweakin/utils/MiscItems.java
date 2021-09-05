package com.github.sachin.tweakin.utils;

import java.io.File;

import com.github.sachin.tweakin.Tweakin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class MiscItems {

    public final FileConfiguration CONFIG;

    public final ItemStack ENABLED_BUTTON;
    public final ItemStack DISABLED_BUTTON;
    public final ItemStack FILLAR_GLASS;
    public final ItemStack INFO_PAPER;
    public final ItemStack NEXT_BUTTON;
    public final ItemStack PREVIOUS_BUTTON;
    public final ItemStack BACK_BUTTON;

    


    public MiscItems(Tweakin plugin){
        File file = new File(plugin.getDataFolder(),"items.yml");
        if(!file.exists())
            plugin.saveResource("items.yml", false);
        else
            ConfigUpdater.updateWithoutComments(plugin, "items.yml", file);
        this.CONFIG = YamlConfiguration.loadConfiguration(file);
        this.ENABLED_BUTTON = ItemBuilder.guiItemFromFile(CONFIG.getConfigurationSection("enabled-button"), "enabled-button");
        this.DISABLED_BUTTON = ItemBuilder.guiItemFromFile(CONFIG.getConfigurationSection("disabled-button"), "disabled-button");
        this.FILLAR_GLASS = ItemBuilder.guiItemFromFile(CONFIG.getConfigurationSection("fillar-glass"), "fillar-glass");
        this.INFO_PAPER = ItemBuilder.guiItemFromFile(CONFIG.getConfigurationSection("info-paper"), "info-paper");
        this.NEXT_BUTTON = ItemBuilder.guiItemFromFile(CONFIG.getConfigurationSection("next-button"), "next-button");
        this.PREVIOUS_BUTTON = ItemBuilder.guiItemFromFile(CONFIG.getConfigurationSection("previous-button"), "previous-button");
        this.BACK_BUTTON = ItemBuilder.guiItemFromFile(CONFIG.getConfigurationSection("back-button"), "back-button");
    }

}

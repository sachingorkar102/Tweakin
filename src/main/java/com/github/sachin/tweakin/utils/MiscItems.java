package com.github.sachin.tweakin.utils;

import java.io.File;

import com.github.sachin.tweakin.Tweakin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class MiscItems {

    public final ItemStack ENABLED_BUTTON;
    public final ItemStack DISABLED_BUTTON;
    public final ItemStack FILLAR_GLASS;
    public final ItemStack INFO_PAPER;
    public final ItemStack NEXT_BUTTON;
    public final ItemStack PREVIOUS_BUTTON;


    public MiscItems(Tweakin plugin){
        File file = new File(plugin.getDataFolder(),"misc-items.yml");
        if(!file.exists()){
            plugin.saveResource("misc-items.yml", false);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        this.ENABLED_BUTTON = ItemBuilder.guiItemFromFile(config.getConfigurationSection("enabled-button"), "enabled-button");
        this.DISABLED_BUTTON = ItemBuilder.guiItemFromFile(config.getConfigurationSection("disabled-button"), "disabled-button");
        this.FILLAR_GLASS = ItemBuilder.guiItemFromFile(config.getConfigurationSection("fillar-glass"), "fillar-glass");
        this.INFO_PAPER = ItemBuilder.guiItemFromFile(config.getConfigurationSection("info-paper"), "info-paper");
        this.NEXT_BUTTON = ItemBuilder.guiItemFromFile(config.getConfigurationSection("next-button"), "next-button");
        this.PREVIOUS_BUTTON = ItemBuilder.guiItemFromFile(config.getConfigurationSection("previous-button"), "previous-button");
    }
    
}

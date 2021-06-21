package com.github.sachin.tweakin;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Message {

    private Tweakin plugin;
    private YamlConfiguration messages;

    public Message(Tweakin plugin){
        this.plugin = plugin;
        reload();
        
    }

    public void reload(){
        File file = new File(plugin.getDataFolder(),"messages.yml");
        if(!file.exists()){
            plugin.saveResource("messages.yml", false);
        }
        this.messages = YamlConfiguration.loadConfiguration(file); 
    }

    public String getMessage(String key){
        return ChatColor.translateAlternateColorCodes('&', messages.getString(key,""));
    }

    
}

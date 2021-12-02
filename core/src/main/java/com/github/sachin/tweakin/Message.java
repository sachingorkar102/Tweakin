package com.github.sachin.tweakin;

import com.github.sachin.tweakin.utils.ConfigUpdater;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
        try {
            ConfigUpdater.update(plugin, "messages.yml", file, new ArrayList<>(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage(String key){
        return ChatColor.translateAlternateColorCodes('&', messages.getString("prefix","")+messages.getString(key,""));
    }

    public String getMessageWithoutPrefix(String key){
        return ChatColor.translateAlternateColorCodes('&', messages.getString(key,""));
    }

    
}

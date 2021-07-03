package com.github.sachin.tweakin.coordinatehud;


import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;


public class ToggleCommand extends BaseCommand{
    
    private CoordinateHUDTweak instance;

    public ToggleCommand(CoordinateHUDTweak instance){
        this.instance = instance;
    }

    @CommandAlias("togglehud|thud")
    public void onCommand(Player player){
        if(!player.hasPermission("tweakin.coordinatehud")){
            player.sendMessage(instance.getTweakManager().getMessageManager().getMessage("no-permission"));
            return;
        }
        if(instance.enabled.remove(player)){
            player.getPersistentDataContainer().remove(instance.key);
            player.sendMessage(instance.getTweakManager().getMessageManager().getMessage("hud-disabled"));
        }
        else{
            player.getPersistentDataContainer().set(instance.key,PersistentDataType.INTEGER, 1);
            instance.enabled.add(player);
            player.sendMessage(instance.getTweakManager().getMessageManager().getMessage("hud-enabled"));
        }
    }
}

package com.github.sachin.tweakin.modules.coordinatehud;


import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;


public class ToggleCommand extends BaseCommand{
    
    private CoordinateHUDTweak instance;

    public ToggleCommand(CoordinateHUDTweak instance){
        instance.getPlugin().replacements.addReplacement("togglehudalias", instance.getConfig().getString("alias","togglehud"));
        this.instance = instance;
    }

    @CommandAlias("%togglehudalias")
    public void onCommand(Player player){
        if(!player.hasPermission("tweakin.coordinatehud")){
            player.sendMessage(instance.getTweakManager().getMessageManager().getMessage("no-permission"));
            return;
        }
        if(instance.enabled.remove(player)){
            player.getPersistentDataContainer().remove(instance.key);
            if(instance.isBossBar){
                instance.removeBossBar(player);
            }
            player.sendMessage(instance.getTweakManager().getMessageManager().getMessage("hud-disabled"));
        }
        else{
            player.getPersistentDataContainer().set(instance.key,PersistentDataType.INTEGER, 1);
            instance.enabled.add(player);
            if(instance.isBossBar){
                instance.createBossBar(player);
            }
            player.sendMessage(instance.getTweakManager().getMessageManager().getMessage("hud-enabled"));
        }
    }
}

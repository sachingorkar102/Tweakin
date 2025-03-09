package com.github.sachin.tweakin.modules.reacharound;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import com.github.sachin.tweakin.utils.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

public class ToggleCommand extends BaseCommand{

    private ReachAroundTweak instance;

    public ToggleCommand(ReachAroundTweak instance){
        instance.getPlugin().replacements.addReplacement("reacharoundalias", instance.getConfig().getString("alias","togglereacharound"));
        this.instance = instance;
    }
    
    @CommandAlias("%reacharoundalias")
    public void onCommand(Player player){
        if(!instance.hasPermission(player, Permissions.REACHAROUND_TOGGLE)){
            player.sendMessage(instance.getTweakManager().getMessageManager().getMessage("no-permission"));
            return;
        }
        if(player.getPersistentDataContainer().has(instance.key,PersistentDataType.INTEGER)){
            player.getPersistentDataContainer().remove(instance.key);
            player.sendMessage(instance.getTweakManager().getMessageManager().getMessage("reacharound-disabled"));
            if(instance.getCurrentTasks().containsKey(player.getUniqueId())){
                BukkitTask task = instance.getCurrentTasks().remove(player.getUniqueId());
                task.cancel();
            }
        }
        else{
            player.getPersistentDataContainer().set(instance.key, PersistentDataType.INTEGER, 1);
            player.sendMessage(instance.getTweakManager().getMessageManager().getMessage("reacharound-enabled"));
            instance.creatPlayerTask(player);
        }
    }
}

package com.github.sachin.tweakin.modules.reacharound;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.vagdedes.spartan.api.PlayerViolationEvent;
import me.vagdedes.spartan.system.Enums.HackType;

public class SpartanIntegration implements Listener{



    private ReachAroundTweak instance;

    public SpartanIntegration(ReachAroundTweak instance){
        this.instance = instance;
    }


    @EventHandler
    public void onImpossibleAction(PlayerViolationEvent e){
        Player player = e.getPlayer();
        
        if(instance.getPlugin().getPlacedPlayers().contains(player) && e.getHackType() == HackType.ImpossibleActions){
            e.setCancelled(true);
        }
    }
}
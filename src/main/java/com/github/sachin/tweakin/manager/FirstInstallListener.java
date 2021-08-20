package com.github.sachin.tweakin.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.github.sachin.tweakin.Tweakin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class FirstInstallListener implements Listener{

    private Tweakin plugin;
    public List<UUID> flaggedPlayers = new ArrayList<>();

    public FirstInstallListener(){
        this.plugin = Tweakin.getPlugin();
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(p.isOp() && !flaggedPlayers.contains(p.getUniqueId())){
            plugin.getTweakManager().sendFirstInstallMessage(p);
            flaggedPlayers.add(p.getUniqueId());
        }
    }
    
}

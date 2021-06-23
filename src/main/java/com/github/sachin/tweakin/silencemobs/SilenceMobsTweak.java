package com.github.sachin.tweakin.silencemobs;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

//Permission: tweakin.silencemobs.silence,tweakin.silencemobs.unsilence
public class SilenceMobsTweak extends BaseTweak implements Listener{

    public SilenceMobsTweak(Tweakin plugin) {
        super(plugin, "silence-mobs");
    }


    @EventHandler
    public void nameTagUseEvent(PlayerInteractEntityEvent e){

    }
    
}

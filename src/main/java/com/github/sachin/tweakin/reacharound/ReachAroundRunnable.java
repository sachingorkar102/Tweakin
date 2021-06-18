package com.github.sachin.tweakin.reacharound;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ReachAroundRunnable extends BukkitRunnable{

    private ReachAroundTweak instance;
    private Player player;

    public ReachAroundRunnable(ReachAroundTweak instance,Player player){
        this.instance = instance;
        this.player = player;
    }

    @Override
    public void run() {
        if(!player.isOnline()){
            this.cancel();
            return;
        }
        Location target = instance.getPlayerReachAroundTarget(player);
        if(target != null){
            BlockHighLight.sendBlockHighlight(player, target, instance.getColor().getRGB());
        }
    }
    
}

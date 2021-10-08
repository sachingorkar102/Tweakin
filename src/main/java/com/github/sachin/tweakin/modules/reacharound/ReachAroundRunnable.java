package com.github.sachin.tweakin.modules.reacharound;

import java.awt.Color;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.util.ColorUtil;

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
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item == null) return;
        if(!item.getType().isBlock()) return;
        // if(instance.matchString(item.getType().toString(), instance.getConfig().getStringList("black-list-materials"))) return;
        Location target = instance.getPlayerReachAroundTarget(player);
        if(target != null){
            BlockHighLight.sendBlockHighlight(player, target, instance.getColor());
        }
    }

    public int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.
    
        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
    
}

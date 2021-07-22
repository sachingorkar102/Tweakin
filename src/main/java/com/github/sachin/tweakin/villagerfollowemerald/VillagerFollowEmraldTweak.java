package com.github.sachin.tweakin.villagerfollowemerald;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class VillagerFollowEmraldTweak extends BaseTweak implements Listener{

    public VillagerFollowEmraldTweak(Tweakin plugin) {
        super(plugin, "villager-follow-emerald");
    }

    @EventHandler
    public void onClick(EntitySpawnEvent e){
        if(e.getEntity() instanceof Villager){
            plugin.getNmsHelper().spawnVillager((Villager)e.getEntity());
        }
    }
    
}

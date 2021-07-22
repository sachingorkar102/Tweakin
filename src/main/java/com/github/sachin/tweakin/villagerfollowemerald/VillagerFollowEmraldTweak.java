package com.github.sachin.tweakin.villagerfollowemerald;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class VillagerFollowEmraldTweak extends BaseTweak implements Listener{

    public VillagerFollowEmraldTweak(Tweakin plugin) {
        super(plugin, "villager-follow-emerald");
    }

    @EventHandler
    public void onClick(EntitySpawnEvent e){
        if(e.getEntity() instanceof Villager && !getBlackListWorlds().contains(e.getEntity().getWorld().getName())){
            plugin.getNmsHelper().spawnVillager((Villager)e.getEntity());
        }
    }
    


    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkLoad(ChunkLoadEvent e){
        if(getBlackListWorlds().contains(e.getChunk().getWorld().getName())) return;
        for(Entity en : e.getChunk().getEntities()){
            if(en instanceof Villager){
                Villager vil = (Villager) en;
                if(vil.hasAI()){
                    plugin.getNmsHelper().spawnVillager(vil);
                }
            }
        }
    }
}

package com.github.sachin.tweakin.villagerfollowemerald;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class VillagerFollowEmraldTweak extends BaseTweak implements Listener{

    private final NamespacedKey key = Tweakin.getKey("villager-already-set");

    public VillagerFollowEmraldTweak(Tweakin plugin) {
        super(plugin, "villager-follow-emerald");
    }

    @EventHandler
    public void onClick(EntitySpawnEvent e){
        if(e.getEntity() instanceof Villager && !getBlackListWorlds().contains(e.getEntity().getWorld().getName())){
            plugin.getNmsHelper().spawnVillager((Villager)e.getEntity());
            e.getEntity().getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
        }
    }
    


    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e){
        if(getBlackListWorlds().contains(e.getChunk().getWorld().getName())) return;
        new BukkitRunnable(){
            public void run() {
                if(!e.getChunk().isLoaded()) return;
                for(Entity en : e.getChunk().getEntities()){
                    if(en instanceof Villager){
        
                        Villager vil = (Villager) en;
                        if(!vil.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)){
                            plugin.getNmsHelper().spawnVillager(vil);
                            vil.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
                        }
                    }
                }
            };
        }.runTaskLater(plugin, 7L); 
        // using a delay as it dosnt work at the time of event
    }
}

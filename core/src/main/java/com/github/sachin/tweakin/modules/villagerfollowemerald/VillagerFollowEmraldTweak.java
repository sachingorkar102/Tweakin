package com.github.sachin.tweakin.modules.villagerfollowemerald;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;
import com.github.sachin.tweakin.utils.Tweak;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

@Tweak(name = "villager-follow-emerald")
public class VillagerFollowEmraldTweak extends BaseTweak implements Listener{

    @EventHandler
    public void onVillagerJoinWorld(EntityAddToWorldEvent e){
        if(!(e.getEntity() instanceof Villager)) return;
        if(getBlackListWorlds().contains(e.getEntity().getWorld().getName())) return;
        Villager vil = (Villager) e.getEntity();

        if(!vil.getPersistentDataContainer().has(TConstants.VILLAGER_FOLLOW_KEY, PersistentDataType.INTEGER)){
            plugin.getNmsHelper().spawnVillager(vil,false);
            vil.getPersistentDataContainer().set(TConstants.VILLAGER_FOLLOW_KEY, PersistentDataType.INTEGER, 1);
        }
        else{
            plugin.getNmsHelper().spawnVillager(vil,true);
        }

    }

//    @EventHandler
//    public void onClick(EntitySpawnEvent e){
//        if(e.getEntity() instanceof Villager && !getBlackListWorlds().contains(e.getEntity().getWorld().getName())){
//            plugin.getNmsHelper().spawnVillager((Villager)e.getEntity());
//            e.getEntity().getPersistentDataContainer().set(TConstants.VILLAGER_FOLLOW_KEY, PersistentDataType.INTEGER, 1);
//        }
//    }
    


//    @EventHandler
//    public void onChunkLoad(ChunkLoadEvent e){
//        if(getBlackListWorlds().contains(e.getChunk().getWorld().getName())) return;
//        new BukkitRunnable(){
//            public void run() {
//                if(!e.getChunk().isLoaded()) return;
//                for(Entity en : e.getChunk().getEntities()){
//                    if(en instanceof Villager){
//
//                        Villager vil = (Villager) en;
//                        if(!vil.getPersistentDataContainer().has(TConstants.VILLAGER_FOLLOW_KEY, PersistentDataType.INTEGER)){
//                            plugin.getNmsHelper().spawnVillager(vil);
//                            vil.getPersistentDataContainer().set(TConstants.VILLAGER_FOLLOW_KEY, PersistentDataType.INTEGER, 1);
//                        }
//                    }
//                }
//            };
//        }.runTaskLater(plugin, 7L);
//        // using a delay as it dosnt work at the time of event
//    }
}

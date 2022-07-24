package com.github.sachin.tweakin.modules.villagerfollowemerald;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.github.sachin.tweakin.BaseListener;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

public class VillagerJoinWorldEvent extends BaseListener {

    @EventHandler
    public void onVillagerJoinWorld(EntityAddToWorldEvent e){
        if(!(e.getEntity() instanceof Villager)) return;
        Villager vil = (Villager) e.getEntity();
        if(!vil.getPersistentDataContainer().has(TConstants.VILLAGER_FOLLOW_KEY, PersistentDataType.INTEGER)){
            plugin.getNmsHelper().spawnVillager(vil);
            vil.getPersistentDataContainer().set(TConstants.VILLAGER_FOLLOW_KEY, PersistentDataType.INTEGER, 1);
        }
    }


}

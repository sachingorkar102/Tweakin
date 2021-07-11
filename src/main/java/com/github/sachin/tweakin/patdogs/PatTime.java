package com.github.sachin.tweakin.patdogs;

import com.github.sachin.tweakin.Tweakin;

import org.apache.commons.lang.WordUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Wolf;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PatTime {

    private Wolf wolf;
    private PersistentDataContainer data;
    private static final NamespacedKey key = Tweakin.getKey("pet-time");

    public PatTime(Wolf wolf){
        this.wolf = wolf;
        this.data = wolf.getPersistentDataContainer();
    }

    public void setPetTime(){
        data.set(key, PersistentDataType.LONG, wolf.getWorld().getGameTime());
    }

    public boolean canPet(long cooldown) {
        return timeSinceLastPet() > cooldown;
    }

    public long getPatTime(){
        return data.has(key, PersistentDataType.LONG) ? data.get(key, PersistentDataType.LONG) : 0;
    }

    public long timeSinceLastPet() {
        if(!wolf.isTamed()){
            return 0;
        }
        long lastPetAt = 0;
        if(data.has(key, PersistentDataType.LONG)){
            lastPetAt = data.get(key, PersistentDataType.LONG);
        }
        return wolf.getWorld().getGameTime() - lastPetAt;
    }
    
}

package com.github.sachin.tweakin.modules.patdogs;

import com.github.sachin.tweakin.Tweakin;

import org.apache.commons.lang.WordUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PatTime {

    private Tameable tameable;
    private PersistentDataContainer data;
    private static final NamespacedKey key = Tweakin.getKey("pet-time");

    public PatTime(Tameable tameable){
        this.tameable = tameable;
        this.data = tameable.getPersistentDataContainer();
    }

    public void setPetTime(){
        data.set(key, PersistentDataType.LONG, tameable.getWorld().getGameTime());
    }

    public boolean canPet(long cooldown) {
        return timeSinceLastPet() > cooldown;
    }

    public long getPatTime(){
        return data.has(key, PersistentDataType.LONG) ? data.get(key, PersistentDataType.LONG) : 0;
    }

    public long timeSinceLastPet() {
        if(!tameable.isTamed()){
            return 0;
        }
        long lastPetAt = 0;
        if(data.has(key, PersistentDataType.LONG)){
            lastPetAt = data.get(key, PersistentDataType.LONG);
        }
        return tameable.getWorld().getGameTime() - lastPetAt;
    }
    
}

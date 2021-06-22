package com.github.sachin.tweakin.utils;

import java.util.Set;

import javax.annotation.Nonnull;

import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CustomBlockData implements PersistentDataContainer{

    private Location location;
    private NamespacedKey key;
    private PersistentDataContainer pdc;

    public CustomBlockData(Location location){
        this.location = location;
        this.key = Tweakin.getKey(String.format("%d/%d/%d", location.getBlockX(),location.getBlockY(),location.getBlockZ()));
        this.pdc = getData();
    }

    private PersistentDataContainer getData(){
        Chunk chunk = location.getBlock().getChunk();
        PersistentDataContainer chunkPDC = chunk.getPersistentDataContainer();
        if(chunkPDC.has(key, PersistentDataType.TAG_CONTAINER)){
            return chunkPDC.get(key,PersistentDataType.TAG_CONTAINER);
        }
        PersistentDataContainer blockPDC = chunkPDC.getAdapterContext().newPersistentDataContainer();
        chunkPDC.set(key, PersistentDataType.TAG_CONTAINER, blockPDC);
        return chunkPDC;
    }


    @Override
    public <T, Z> void set(@Nonnull NamespacedKey namespacedKey, @Nonnull PersistentDataType<T, Z> persistentDataType, @Nonnull Z z) {
        pdc.set(namespacedKey, persistentDataType, z);
        Chunk chunk = location.getBlock().getChunk();
        chunk.getPersistentDataContainer().set(key, PersistentDataType.TAG_CONTAINER, pdc);
    }

    @Override
    public <T, Z> boolean has(@Nonnull NamespacedKey namespacedKey, @Nonnull PersistentDataType<T, Z> persistentDataType) {
        return pdc.has(namespacedKey, persistentDataType);
    }

    @Override
    public <T, Z> Z get(@Nonnull NamespacedKey namespacedKey, @Nonnull PersistentDataType<T, Z> persistentDataType) {
        return pdc.get(namespacedKey, persistentDataType);
    }

    @Override
    public <T, Z> Z getOrDefault(@Nonnull NamespacedKey namespacedKey, @Nonnull PersistentDataType<T, Z> persistentDataType, @Nonnull Z z) {
        return pdc.getOrDefault(namespacedKey, persistentDataType, z);
    }

    @Override
    public Set<NamespacedKey> getKeys() {
        return pdc.getKeys();
    }

    @Override
    public void remove(@Nonnull NamespacedKey namespacedKey) {
        pdc.remove(namespacedKey);
    }

    @Override
    public boolean isEmpty() {
        return pdc.isEmpty();
    }

    @Override
    public PersistentDataAdapterContext getAdapterContext() {
        return pdc.getAdapterContext();
    }

    
    
}

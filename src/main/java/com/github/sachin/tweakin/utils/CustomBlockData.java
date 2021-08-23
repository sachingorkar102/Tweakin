package com.github.sachin.tweakin.utils;

import java.util.Set;

import javax.annotation.Nonnull;

import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CustomBlockData implements PersistentDataContainer{

    private Location location;
    private Chunk chunk;
    private NamespacedKey key;
    private PersistentDataContainer pdc;

    public CustomBlockData(Location location){
        this.location = location;
        this.chunk = location.getBlock().getChunk();
        int x = location.getBlockX() & 0x000F;
        int y = location.getBlockY() & 0x00FF;
        int z = location.getBlockZ() & 0x000F;
        this.key = Tweakin.getKey(String.format("x%dy%dz%d", x, y, z));
        this.pdc = getPersistentDataContainer();
    }


    
    private PersistentDataContainer getPersistentDataContainer() {
        final PersistentDataContainer chunkPDC = chunk.getPersistentDataContainer();
        final PersistentDataContainer blockPDC;
        if (chunkPDC.has(key, PersistentDataType.TAG_CONTAINER)) {
            blockPDC = chunkPDC.get(key, PersistentDataType.TAG_CONTAINER);
            assert blockPDC != null;
            return blockPDC;
        }
        blockPDC = chunkPDC.getAdapterContext().newPersistentDataContainer();
        chunkPDC.set(key, PersistentDataType.TAG_CONTAINER, blockPDC);
        return blockPDC;
    }

    private void save() {
        Chunk chunk = location.getBlock().getChunk();
        
        chunk.getPersistentDataContainer().set(key, PersistentDataType.TAG_CONTAINER, pdc);
    }


    @Override
    public <T, Z> void set(@Nonnull NamespacedKey namespacedKey, @Nonnull PersistentDataType<T, Z> persistentDataType, @Nonnull Z z) {
        pdc.set(namespacedKey, persistentDataType, z);
        save();
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
        save();
    }

    @Override
    public boolean isEmpty() {
        return pdc.isEmpty();
    }

    @Override
    public PersistentDataAdapterContext getAdapterContext() {
        return pdc.getAdapterContext();
    }

    public Location getLocation() {
        return location;
    }

    
    
}

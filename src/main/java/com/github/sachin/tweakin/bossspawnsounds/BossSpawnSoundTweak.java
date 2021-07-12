package com.github.sachin.tweakin.bossspawnsounds;

import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

public class BossSpawnSoundTweak extends BaseTweak {

    private ProtocolManager manager;
    private SoundPacketListener listener;

    public BossSpawnSoundTweak(Tweakin plugin) {
        super(plugin, "boss-spawn-sound");
        this.manager = ProtocolLibrary.getProtocolManager();
        this.listener = new SoundPacketListener(plugin);
    }

    @Override
    public void register() {
        super.register();
        manager.addPacketListener(listener);
    }
    
    @Override
    public void unregister() {
        super.unregister();
        manager.removePacketListener(listener);
    }
}

package com.github.sachin.tweakin.modules.bossspawnsounds;

import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

public class BroadCastSoundTweak extends BaseTweak {

    private ProtocolManager manager;
    private SoundPacketListener listener;

    public BroadCastSoundTweak(Tweakin plugin) {
        super(plugin, "broadcast-sound-nerf");
        initProtocolLib();
    }

    private void initProtocolLib(){
        this.manager = ProtocolLibrary.getProtocolManager();
        this.listener = new SoundPacketListener(plugin);
    }

    @Override
    public void register() {
        registered = true;
        manager.addPacketListener(listener);
    }

    @Override
    public void unregister() {
        manager.removePacketListener(listener);
        registered = false;
    }

}

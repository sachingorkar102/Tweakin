package com.github.sachin.tweakin.modules.bossspawnsounds;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.Tweak;

@Tweak(name = "broadcast-sound-nerf")
public class BroadCastSoundTweak extends BaseTweak {

    private ProtocolManager manager;
    private SoundPacketListener listener;


    @Override
    public void onLoad() {
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

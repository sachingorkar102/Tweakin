package com.github.sachin.tweakin.modules.jumpyboats;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.annotations.Tweak;

@Tweak(name = "jumpy-boats")
public class JumpyBoatsTweak extends BaseTweak {

    private SteerBoatPacketListener packetListener;

    @Override
    public void onLoad() {
        this.packetListener = new SteerBoatPacketListener(this);
    }

    @Override
    public void register() {
        super.register();
        ProtocolLibrary.getProtocolManager().addPacketListener(packetListener);
    }

    @Override
    public void unregister() {
        super.unregister();
        ProtocolLibrary.getProtocolManager().removePacketListener(packetListener);
    }
}

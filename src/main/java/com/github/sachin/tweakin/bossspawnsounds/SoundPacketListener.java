package com.github.sachin.tweakin.bossspawnsounds;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import org.bukkit.plugin.Plugin;

public class SoundPacketListener extends PacketAdapter{

    public SoundPacketListener(Plugin plugin) {
        super(plugin, PacketType.Play.Server.ENTITY_SOUND);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        System.out.println(packet.getSoundEffects().read(0));
    }


    
}

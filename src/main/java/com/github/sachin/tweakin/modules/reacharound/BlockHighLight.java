package com.github.sachin.tweakin.modules.reacharound;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.MinecraftKey;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

public class BlockHighLight {

    public static void sendBlockHighlight(Player pl, Location loc,int color) {
        ByteBuf packet = Unpooled.buffer();
        packet.writeLong(blockPosToLong(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        packet.writeInt(color);
        writeString(packet, "");
        packet.writeInt(200);
        sendPayload(pl, "debug/game_test_add_marker", packet);
    }

    private static void sendPayload(Player receiver, String channel, ByteBuf bytes) {
        PacketContainer handle = new PacketContainer(PacketType.Play.Server.CUSTOM_PAYLOAD);
        handle.getMinecraftKeys().write(0, new MinecraftKey(channel));

        Object serializer = MinecraftReflection.getPacketDataSerializer(bytes);
        handle.getModifier().withType(ByteBuf.class).write(0, serializer);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, handle);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Unable to send the packet", e);
        }
    }

    private static long blockPosToLong(int x, int y, int z) {
        return ((long) x & 67108863L) << 38 | (long) y & 4095L | ((long) z & 67108863L) << 12;
    }

    private static void d(ByteBuf packet, int i) {
        while ((i & -128) != 0) {
            packet.writeByte(i & 127 | 128);
            i >>>= 7;
        }

        packet.writeByte(i);
    }

    private static void writeString(ByteBuf packet, String s) {
        byte[] abyte = s.getBytes(StandardCharsets.UTF_8);
        d(packet, abyte.length);
        packet.writeBytes(abyte);
    }
    
}

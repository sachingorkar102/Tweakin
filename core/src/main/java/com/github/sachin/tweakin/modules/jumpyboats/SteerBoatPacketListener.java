package com.github.sachin.tweakin.modules.jumpyboats;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.Permissions;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SteerBoatPacketListener extends PacketAdapter {

    private final JumpyBoatsTweak instance;
    private final Map<UUID,Integer> cooldowns = new HashMap<>();

    public SteerBoatPacketListener(JumpyBoatsTweak instance){
        super(Tweakin.getPlugin(), PacketType.Play.Client.STEER_VEHICLE);
        this.instance = instance;
    }

    @Override
    public void onPacketReceiving(PacketEvent e) {
        PacketContainer packet = e.getPacket();
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if(player.isInsideVehicle() && player.getVehicle() instanceof Boat){
            Boat boat = (Boat) player.getVehicle();
            boolean isJumping = packet.getBooleans().read(0) && (boat.isOnGround() || boat.isInWater());
            boolean isUnmount = packet.getBooleans().read(1);
            if(isUnmount){
                cooldowns.remove(uuid);
                return;
            }
            int i = cooldowns.getOrDefault(uuid,0);
            if(i==0){
                if(isJumping && !instance.containsWorld(player.getWorld()) && instance.hasPermission(player, Permissions.JUMPYBOATS)){
                    boat.setVelocity(boat.getVelocity().clone().add(new Vector(0,instance.getConfig().getDouble("jump-modifier"),0)));
                    cooldowns.put(uuid,instance.getConfig().getInt("cooldown"));

                }
            }
            else{
                i--;
                cooldowns.put(uuid,i);
            }

        }
    }
}

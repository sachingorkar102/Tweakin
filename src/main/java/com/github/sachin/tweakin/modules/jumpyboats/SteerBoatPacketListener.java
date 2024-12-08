package com.github.sachin.tweakin.modules.jumpyboats;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.server.InputStreamLookupBuilder;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SteerBoatPacketListener extends PacketAdapter {

    private final JumpyBoatsTweak instance;
    private final Map<UUID,Long> cooldowns = new HashMap<>();

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
            boolean isJumping;
            boolean isUnmount;
            if(Tweakin.getPlugin().getPrilib().getMcVersion().isAtLeast(1,21,3)){
                isJumping = Tweakin.getPlugin().getNMSHandler().isPlayerJumping(packet.getModifier().read(0));
                isUnmount = Tweakin.getPlugin().getNMSHandler().isPlayerHoldingShift(packet.getModifier().read(0));
            }else{
                isJumping = packet.getBooleans().read(0) && (boat.isOnGround() || boat.isInWater());
                isUnmount = packet.getBooleans().read(1);
            }
            if(isUnmount){
                cooldowns.remove(uuid);
                return;
            }
            long lastJumped = cooldowns.getOrDefault(uuid,0L);
            long coolDownTime = instance.getConfig().getInt("cooldown") * 50L;
            long currentTime = System.currentTimeMillis();
            long timeLeft = (lastJumped+coolDownTime) - currentTime;
            if(timeLeft<=0){
                if(isJumping && !instance.containsWorld(player.getWorld())){
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            boat.setVelocity(boat.getVelocity().clone().add(new Vector(0,instance.getConfig().getDouble("jump-modifier"),0)));
                        }
                    }.runTaskLater(plugin,0);
                    cooldowns.put(uuid,currentTime);

                }
            }

        }
    }
}

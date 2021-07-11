package com.github.sachin.tweakin.betterelytrarocket;

import java.lang.reflect.InvocationTargetException;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerAction;
import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class BetterElytraRocketTweak extends BaseTweak implements Listener{

    public BetterElytraRocketTweak(Tweakin plugin) {
        super(plugin, "better-elytra-rocket");
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_AIR || e.getHand() != EquipmentSlot.HAND) return;
        if(e.getItem() == null) return;
        if(e.getItem().getType() != Material.FIREWORK_ROCKET) return;
        Player player = e.getPlayer();
        
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        PacketContainer container = new PacketContainer(PacketType.Play.Client.ENTITY_ACTION);
        container.getIntegers().write(0, player.getEntityId());
        container.getPlayerActions().write(0, PlayerAction.START_FALL_FLYING);
        try {
            manager.recieveClientPacket(player, container);
        } catch (IllegalAccessException | InvocationTargetException e1) {
            e1.printStackTrace();
        }
        
    }


    
}

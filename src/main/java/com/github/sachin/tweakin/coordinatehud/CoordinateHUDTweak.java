package com.github.sachin.tweakin.coordinatehud;

import java.util.ArrayList;
import java.util.List;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import co.aikar.commands.BaseCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class CoordinateHUDTweak extends BaseTweak implements Listener{

    final List<Player> enabled = new ArrayList<>();
    final NamespacedKey key = new NamespacedKey(getPlugin(), "coordinatehud");
    final NamespacedKey firstKey = new NamespacedKey(getPlugin(),"coordinatehud-firstJoin");
    private BaseCommand command;
    private HUDRunnable runnable;
    private Long intervalTicks;

    public CoordinateHUDTweak(Tweakin plugin) {
        super(plugin, "coordinate-hud");
    }

    @Override
    public void reload() {
        super.reload();
        this.intervalTicks = getConfig().getLong("interval-ticks",2L);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        if(!player.getPersistentDataContainer().has(firstKey, PersistentDataType.INTEGER) && getConfig().getBoolean("enable-on-first-join",true)){
            enabled.add(player);
            player.getPersistentDataContainer().set(firstKey, PersistentDataType.INTEGER, 1);
            player.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
        }
        else if(player.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)){
            enabled.add(player);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        enabled.remove(e.getPlayer());
    }

    @Override
    public void register() {
        super.register();
        this.command = new ToggleCommand(this);
        registerCommands(command);
        this.runnable = new HUDRunnable();
        runnable.runTaskTimer(getPlugin(), 1L, intervalTicks);
    }

    @Override
    public void unregister() {
        super.unregister();
        unregisterCommands(command);
    }
    
    /**
     * @author Machine Maker
    */
    private class HUDRunnable extends BukkitRunnable{
    
        @Override
        public void run() {
            CoordinateHUDTweak.this.enabled.forEach(player -> {
                if(!player.getInventory().contains(Material.COMPASS) && getConfig().getBoolean("have-compass",false) && !player.hasPermission("tweakin.coordniatehud.compassbypass")){
                    return;
                }
                long time = (player.getWorld().getTime() + 6000) % 24000;
                long hours = time / 1000;
                Long extra = (time - (hours * 1000)) * 60 / 1000;
                String message = String.format(ChatColor.GOLD + "XYZ: "+ ChatColor.RESET + "%d %d %d  " + ChatColor.GOLD + "%2s      %02d:%02d",
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ(),
                getDirection(player.getLocation().getYaw()),
                hours,
                extra
                );

                if(getConfig().getBoolean("show-speed",true)){
                    if(player.isInsideVehicle()){
                        Vehicle vh = (Vehicle) player.getVehicle();
                        
                        message = message + ChatColor.GOLD+" Speed: "+ChatColor.RESET+Math.round(vh.getVelocity().length() * 100.0) / 100.0;
                    }
                }

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
            });
        }
    }

    /**
     * @author Machine Maker
    */
    public String getDirection(float yaw){
        double degrees = yaw < 0 ? (yaw % -360.0) + 360 : yaw % 360.0;
        if (degrees <= 22.5) return "S";
        if (degrees <= 67.5) return "SW";
        if (degrees <= 112.5) return "W";
        if (degrees <= 157.5) return "NW";
        if (degrees <= 202.5) return "N";
        if (degrees <= 247.5) return "NE";
        if (degrees <= 292.5) return "E";
        if (degrees <= 337.5) return "SE";
        return "S";
    }
}



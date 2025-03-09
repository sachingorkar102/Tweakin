package com.github.sachin.tweakin.modules.coordinatehud;

import co.aikar.commands.BaseCommand;
import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.annotations.Config;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import com.github.sachin.tweakin.compat.PlaceHolderApiCompat;
import com.google.common.base.Enums;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@Tweak(name = "coordinate-hud",clashingTeaksTweak = "Coords HUD")
public class CoordinateHUDTweak extends BaseTweak implements Listener{

    final List<Player> enabled = new ArrayList<>();
    final Map<UUID,BossBar> bars = new HashMap<>();
    final Map<Vehicle,SpeedData> speedDataMap = new HashMap<>();
    final NamespacedKey key = new NamespacedKey(getPlugin(), "coordinatehud");
    final NamespacedKey firstKey = new NamespacedKey(getPlugin(),"coordinatehud-firstJoin");

    private BaseCommand command;
    private HUDRunnable runnable;


    @Config(key = "interval-ticks") private int intervalTicks;
    @Config(key = "hud-type") private String msgType = "ACTIONBAR";

    @Config(key = "boss-bar.color") private String color = "YELLOW";

    @Config(key = "boss-bar.style") private String style = "SEGMENTED_6";

    @Config(key = "enable-on-first-join") private boolean enableOnFirstJoin = true;

    @Config(key = "have-compass") private boolean haveCompass = false;


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        addPlayerToHud(player);
    }
    
    public void addPlayerToHud(Player player){
        if(!player.getPersistentDataContainer().has(firstKey, PersistentDataType.INTEGER) && enableOnFirstJoin){
            enabled.add(player);
            player.getPersistentDataContainer().set(firstKey, PersistentDataType.INTEGER, 1);
            player.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
            if(isBossBar()){
                createBossBar(player);
            }
        }
        else if(player.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)){
            enabled.add(player);
            if(isBossBar()){
                createBossBar(player);
            }
    
        }
        
    }

    public boolean isBossBar(){
        return msgType.equals("BOSSBAR");
    }

    public void createBossBar(Player player){
        BossBar bar = Bukkit.createBossBar("", Enums.getIfPresent(BarColor.class, color).or(BarColor.YELLOW), Enums.getIfPresent(BarStyle.class, style).or(BarStyle.SEGMENTED_6));
        bar.addPlayer(player);
        
        bars.put(player.getUniqueId(), bar);
        
    }

    public void removeBossBar(Player player){
        if(bars.containsKey(player.getUniqueId())){
            BossBar bar = bars.remove(player.getUniqueId());
            
            bar.removeAll();
            
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
        enabled.clear();
        Bukkit.getOnlinePlayers().forEach(player -> addPlayerToHud(player));
    }

    @Override
    public void unregister() {
        super.unregister();
        unregisterCommands(command);
        if(!bars.isEmpty()){
            for(UUID id : bars.keySet()){
                bars.get(id).removeAll();
            }
            bars.clear();
        }
        if(runnable != null){
            runnable.cancel();
            enabled.clear();
        }
    }
    
    /**
     * @author Machine Maker
    */
    private class HUDRunnable extends BukkitRunnable{
    
        @Override
        public void run() {
            CoordinateHUDTweak.this.enabled.forEach(player -> {
                if((!player.getInventory().contains(Material.COMPASS) && haveCompass) || !hasPermission(player, Permissions.HUD_COMPASSBYPASS)){
                    return;
                }
                long time = (player.getWorld().getTime() + 6000) % 24000;
                long hours = time / 1000;
                Long extra = (time - (hours * 1000)) * 60 / 1000;

                // String message = String.format(ChatColor.GOLD + "XYZ: "+ ChatColor.RESET + "%d %d %d  " + ChatColor.GOLD + "%2s      %02d:%02d",
                // player.getLocation().getBlockX(),
                // player.getLocation().getBlockY(),
                // player.getLocation().getBlockZ(),
                // getDirection(player.getLocation().getYaw()),
                // hours,
                // extra
                // );
                String message = getConfig().getString("text").replace("%x%", String.valueOf(player.getLocation().getBlockX()))
                .replace("%y%", String.valueOf(player.getLocation().getBlockY()))
                .replace("%z%", String.valueOf(player.getLocation().getBlockZ()))
                .replace("%direction%", getDirection(player.getLocation().getYaw()))
                .replace("%time%", String.format("%02d",hours)+":"+String.format("%02d",extra))
                ;

                if(getConfig().getBoolean("placeholderapi-support",false) && PlaceHolderApiCompat.isEnabled){
                    message = PlaceHolderApiCompat.parse(player,message);
                }


                if(getConfig().getBoolean("show-speed",true)){
                    if(player.isInsideVehicle()){
                        EntityType vechType = player.getVehicle().getType();
                        if(isValidVehicle(vechType)){
                            Vehicle vh = (Vehicle) player.getVehicle();
                            message = message + ChatColor.GOLD+" Speed: "+ChatColor.RESET+Math.round(getSpeed(vh) * 100.0) / 100.0;
                        }
                    }
                }
                message = ChatColor.translateAlternateColorCodes('&', message);
                if(isBossBar()){
                    if(bars.containsKey(player.getUniqueId())){
                        bars.get(player.getUniqueId()).setTitle(message);
                    }
                }
                else{
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
                }
            });
        }
    }

    final List<String> validVehicles = Arrays.asList("HORSE","_HORSE","MINECART","_BOAT","MULE","DONKEY","STRIDER","LLAMA","PIG");
    private boolean isValidVehicle(EntityType vechType) {
        String entity = vechType.toString();
        for (String s : validVehicles){
            if(s.startsWith("_") && entity.endsWith(s)){
                return true;
            }
            if(entity.equalsIgnoreCase(s)){
                return true;
            }
        }
        return false;
    }

    private double getSpeed(Vehicle vh){
        SpeedData data = speedDataMap.get(vh);
        speedDataMap.put(vh, new SpeedData(vh.getLocation()));
        if(data == null) return 0;
        long timeDifference = System.currentTimeMillis() - data.getTime();
        double distance = data.getLocation().distance(vh.getLocation());
        double speed = distance / timeDifference * 1000;
        return speed;

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


    private class SpeedData {

        private Location loc;
        private final long time = System.currentTimeMillis();

        public SpeedData(Location loc){
            this.loc = loc.clone();
        }

        public long getTime() {
            return time;
        }

        public Location getLocation() {
            return loc;
        }
    }
}



package com.github.sachin.tweakin.reacharound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import java.util.UUID;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

// permissions tweakin.reacharound.vertical, tweakin.reacharound.horizontal, tweakin.reacharound.highlight
public class ReachAroundTweak extends BaseTweak implements Listener{

    private Map<UUID,BukkitTask> currentTasks = new HashMap<>();
    private Color color;
    private List<String> blackListWorlds = new ArrayList<>();


    public ReachAroundTweak(Tweakin plugin) {
        super(plugin, "reach-around");
        this.color = hex2Rgb(getConfig().getString("color","#14141f"), getConfig().getInt("transparency",100));
        this.blackListWorlds = getConfig().getStringList("black-list-worlds");
    }

    public List<String> getBlackListWorlds() {
        return blackListWorlds;
    }

    @Override
    public void register() {
        if(getPlugin().getServer().getPluginManager().isPluginEnabled("ProtocolLib")){
            registerEvents(this);
            registered = true;
        }
    }

    @Override
    public void unregister() {
        unregisterEvents(this);
        registered = false;
    }

    @Override
    public void reload() {
        super.reload();
        this.color = hex2Rgb(getConfig().getString("color","#14141f"), getConfig().getInt("transparency",100));
        this.blackListWorlds = getConfig().getStringList("black-list-worlds");
        
    }

    @EventHandler
    public void serverReload(ServerLoadEvent e){
        if(registered){
            currentTasks.values().forEach(t -> t.cancel());
            currentTasks.clear();
            Bukkit.getOnlinePlayers().forEach(p -> {
                creatPlayerTask(p);
            });
        }
    }

    public Color getColor() {
        return color;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        if(!getCurrentTasks().containsKey(player.getUniqueId())){
            if(!blackListWorlds.contains(player.getWorld().getName())){
                creatPlayerTask(e.getPlayer());
            }
           
        }
    }

    @EventHandler
    public void onWorldChangeEvent(PlayerChangedWorldEvent e){
        Player player = e.getPlayer();
        if(!getCurrentTasks().containsKey(player.getUniqueId())){
            if(!blackListWorlds.contains(player.getWorld().getName())){
                creatPlayerTask(e.getPlayer());
            }
           
        }
        else{
            if(blackListWorlds.contains(player.getWorld().getName())){
                BukkitTask task = getCurrentTasks().get(player.getUniqueId());
                task.cancel();
                getCurrentTasks().remove(player.getUniqueId());
            }
        }
    }

    private void creatPlayerTask(Player player){
        if(getConfig().getBoolean("show-highlight",true) && player.hasPermission("tweakin.reacharound.highlight")){
            ReachAroundRunnable runnable = new ReachAroundRunnable(this, player);
            BukkitTask task = runnable.runTaskTimer(getPlugin(), 60, 2);
            this.currentTasks.put(player.getUniqueId(), task);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        UUID player = e.getPlayer().getUniqueId();
        if(getCurrentTasks().containsKey(player)){
            BukkitTask task = getCurrentTasks().get(player);
            task.cancel();
            getCurrentTasks().remove(player);
        }
    }
    
    @EventHandler
    public void blockPlaceEvent(PlayerInteractEvent e){
        if(e.getHand() != EquipmentSlot.HAND) return;
        if(e.getAction() != Action.RIGHT_CLICK_AIR) return;
        if(e.getItem() == null) return;
        ItemStack item = e.getItem();
        if(item.getType().name().endsWith("SHULKER_BOX") || item.getType().name().endsWith("BANNER") || item.getType().name().endsWith("PLAYER_HEAD")) return;
        if(!item.getType().isBlock() && !item.getType().isSolid()) return;
        Player player = e.getPlayer();
        Location loc = getPlayerReachAroundTarget(player);
        if(loc != null){
            player.swingMainHand();
            loc.getBlock().setType(item.getType());
            if(player.getGameMode() == GameMode.SURVIVAL){
                player.getInventory().getItemInMainHand().setAmount(item.getAmount()-1);
            }
        }
    }

    

    public Location getPlayerReachAroundTarget(Player player){
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.getType().name().endsWith("SHULKER_BOX") || !item.getType().isBlock()){
            return null;
        }
        RayTraceResult rayTraceResult = player.getWorld().rayTraceBlocks(player.getEyeLocation(), player.getEyeLocation().getDirection(), 5);
        if(rayTraceResult == null){
            Location target = getPlayerVerticalReachAround(player);
            if(target != null){
                return target;
            }
            target = getPlayerHorizonTalReachAround(player);
            if(target != null){
                return target;
            }
        }
        
        return null;
    }

    public Location getPlayerVerticalReachAround(Player player){
        if(!player.hasPermission("tweakin.reacharound.vertical")){
            return null;
        }
        Vector vec = new Vector(0, 0.5, 0);
        RayTraceResult rayTrace = player.getWorld().rayTraceBlocks(player.getEyeLocation(), player.getEyeLocation().getDirection().clone().add(vec), 5);
        if(rayTrace != null){
            if(rayTrace.getHitBlock() != null){
                Location block = rayTrace.getHitBlock().getLocation();
                Location playerLoc = player.getLocation();
                if(playerLoc.getZ() - block.getZ() < 1.3 && playerLoc.getY() - block.getY() == 1 && playerLoc.getX() - block.getX() < 1.3){
                    Location target = block.subtract(0,1,0);
                    if(target.getBlock().getType() == Material.AIR){
                        return target;
                    }
                }
            }
        }
        return null;
    }


    public Location getPlayerHorizonTalReachAround(Player player){
        if(!player.hasPermission("tweakin.reacharound.horizontal")){
            return null;
        }
        Location playerLoc = player.getLocation();
        BlockFace facing = player.getFacing();
        Vector direction = player.getEyeLocation().getDirection();
        Vector vec = new Vector(0.5*facing.getModX(),0,0.5*facing.getModZ());
        RayTraceResult rayTrace = player.getWorld().rayTraceBlocks(player.getEyeLocation(), direction.clone().subtract(vec), 4);
        if(rayTrace != null){
            if(rayTrace.getHitBlock() != null){
                Location loc = rayTrace.getHitBlock().getLocation();
                double distance = (playerLoc.getX() - loc.getX()) + (playerLoc.getY() - loc.getY()) + (playerLoc.getZ() - loc.getZ()) / 3;
                if(distance < 1.9 && distance > 1.3){
                    Block target = loc.getBlock().getRelative(player.getFacing());
                    if(target.getType() == Material.AIR){
                        return target.getLocation();
                    }
                }
                
            }
        }
        
        return null;
    }

    private Color hex2Rgb(String colorStr, int transparency) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16), transparency);
    }

    public Map<UUID, BukkitTask> getCurrentTasks() {
        return currentTasks;
    }
}

package com.github.sachin.tweakin.modules.reacharound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.trowel.TrowelItem;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;


// permissions tweakin.reacharound.vertical, tweakin.reacharound.horizontal, tweakin.reacharound.highlight
public class ReachAroundTweak extends BaseTweak implements Listener{

    private Map<UUID,BukkitTask> currentTasks = new HashMap<>();
    private int color;
    final NamespacedKey key = new NamespacedKey(getPlugin(), "reacharound");
    final NamespacedKey firstKey = new NamespacedKey(getPlugin(), "reacharound-firstjoin");
    private List<String> blackListWorlds = new ArrayList<>();
    private ToggleCommand command;
    private SpartanIntegration spartanListener;
    private boolean isSpartanEnabled;


    public ReachAroundTweak(Tweakin plugin) {
        super(plugin, "reach-around");
        this.command = new ToggleCommand(this);
        this.isSpartanEnabled = isPluginEnabled("Spartan");
        if(isSpartanEnabled){
            this.spartanListener = new SpartanIntegration(this);
        }
        
    }

    

    public List<String> getBlackListWorlds() {
        return blackListWorlds;
    }


    

    @Override
    public void register() {
        if(isPluginEnabled("ProtocolLib")){
            registerCommands(command);
            registerEvents(this);
            registered = true;
        }
        else{
            getPlugin().getLogger().info("ProtocolLib not found, ignoring reach-around...");
            registered = false;
        }
        if(isSpartanEnabled){
            plugin.getLogger().info("Found Spartan,registering check listerners for reacharound..");
            plugin.getServer().getPluginManager().registerEvents(spartanListener, plugin);
        }
    }

    @Override
    public void unregister() {
        if(registered){
            unregisterCommands(command);
            unregisterEvents(this);
            registered = false;
            if(isSpartanEnabled){
                HandlerList.unregisterAll(spartanListener);
            }
        }
    }

    @Override
    public void reload() {
        super.reload();
        if(plugin.getVersion().equals("v1_17_R1")){
            this.color = plugin.getNmsHelper().getColor("0,16,0", getConfig().getInt("transparency",100));
            plugin.getLogger().info("Using 0,16,0 as color for reach-around, due to 1.17 green channel bug");
        }
        else{
            this.color = getPlugin().getNmsHelper().getColor(getConfig().getString("color","255,255,255"), getConfig().getInt("transparency",100));
        }
        this.blackListWorlds = getConfig().getStringList("black-list-worlds");
        
    }

    @EventHandler
    public void serverReload(ServerLoadEvent e){
        if(registered){
            currentTasks.values().forEach(t -> t.cancel());
            currentTasks.clear();
            Bukkit.getOnlinePlayers().forEach(p -> {
                if( p.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)){
                    creatPlayerTask(p);
                }
            });
        }
    }

    public int getColor() {
        return color;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        if(blackListWorlds.contains(player.getWorld().getName())) return;
        if(!player.getPersistentDataContainer().has(firstKey, PersistentDataType.INTEGER) && getConfig().getBoolean("enabled-on-first-join",true)){
            creatPlayerTask(player);
            player.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
            player.getPersistentDataContainer().set(firstKey, PersistentDataType.INTEGER, 1);
        }
        else if(!getCurrentTasks().containsKey(player.getUniqueId()) && player.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)){
            creatPlayerTask(e.getPlayer());
           
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
                BukkitTask task = getCurrentTasks().remove(player.getUniqueId());
                task.cancel();
                
            }
        }
    }

    public void creatPlayerTask(Player player){
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
        Player player = e.getPlayer();
        if(!player.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) return;
        ItemStack item = e.getItem();
        for(TweakItem tItem: getTweakManager().getRegisteredItems()){
            if(tItem instanceof TrowelItem){
                TrowelItem trowelItem = (TrowelItem) tItem;
                if(trowelItem.hasItem(player, EquipmentSlot.HAND)){
                    Location loc = getPlayerReachAroundTarget(player);
                    if(loc != null){
                        trowelItem.placeBlock(loc, player, BlockFace.UP,true,this);
                        plugin.addPlacedPlayer(player);
                        return;
                    }
                }
            }
        }
        if(matchString(item.getType().toString(), getConfig().getStringList("black-list-materials"))) return;
        if(!item.getType().isBlock()) return;
        Location loc = getPlayerReachAroundTarget(player);
        if(loc != null){
            // BlockPlaceEvent event = new BlockPlaceEvent(loc.getBlock(), loc.getBlock().getState(), placedAgainst, itemInHand, thePlayer, canBuild, hand)
            plugin.addPlacedPlayer(player);
            boolean placed = getPlugin().getNmsHelper().placeItem(player, loc,e.getItem(),BlockFace.UP,getName(),true);
            if(placed && player.getGameMode() != GameMode.CREATIVE){
                item.setAmount(item.getAmount()-1);
            }
        }
    }

    // NOT IN USE
    // public boolean isValidMaterial(Material type){
    //     String blockName = type.toString();
    //     for (String string : getConfig().getStringList("black-list-materials")) {
    //         if(string.startsWith("^")){
    //             if(blockName.endsWith(string.replace("^", ""))){
    //                 return true;
    //             }
    //         }
    //         if(blockName.equals(string)){
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    

    public Location getPlayerReachAroundTarget(Player player){
       
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
                if(distance < 1.85 && distance > 1.3){
                    Block target = loc.getBlock().getRelative(player.getFacing());
                    if(target.getType() == Material.AIR){
                        return target.getLocation();
                    }
                }
                
            }
        }
        
        return null;
    }


    public Map<UUID, BukkitTask> getCurrentTasks() {
        return currentTasks;
    }
}

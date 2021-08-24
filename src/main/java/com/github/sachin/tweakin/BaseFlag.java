package com.github.sachin.tweakin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class BaseFlag {

    private Tweakin plugin;
    private String flagName;
    private StateFlag flag;
    private WGFlagManager manager;

    public BaseFlag(Tweakin plugin,String flagName){
        this.plugin = plugin;
        this.flagName = flagName;
        this.manager = plugin.getWGFlagManager();
    }


    public void register(){
        this.flag = new StateFlag(flagName,true);
        try {
            manager.getRegistry().register(flag);
            plugin.getLogger().info("Worldguard flag: "+flagName+ " registered successfully");
        } catch (FlagConflictException e) {
            plugin.getLogger().warning("A flag with the name \"" + flag.getName() + "\" already exists and could not be registered.");
            e.printStackTrace();
        }
    }

    public String getFlagName() {
        return flagName;
    }

    public StateFlag getFlag() {
        return flag;
    }


    public boolean queryFlag(Player player){
        return queryFlag(player, player.getLocation());
    }
    
    public boolean queryFlag(Player player, Location loc){
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer regionContainer = manager.getWGinstance().getPlatform().getRegionContainer();
        RegionManager regionManager = regionContainer.get(localPlayer.getWorld());
        ApplicableRegionSet regionSet = regionManager.getApplicableRegions(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()));
        
        return regionSet.testState(localPlayer, flag);
    }

    public boolean queryFlag(Location loc){
        RegionContainer regionContainer = manager.getWGinstance().getPlatform().getRegionContainer();
        RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(loc.getWorld()));
        ApplicableRegionSet regionSet = regionManager.getApplicableRegions(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()));
        return regionSet.testState(null, flag);
    }

    // public boolean queryWGFlag(String flagName,Location loc){
    //     Flag<?> flag = Flags.fuzzyMatchFlag(manager.getRegistry(), flagName);
    //     if(flag == null){ return false;}
    //     RegionContainer regionContainer = manager.getWGinstance().getPlatform().getRegionContainer();
    //     RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(loc.getWorld()));
    //     ApplicableRegionSet regionSet = regionManager.getApplicableRegions(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()));
    //     return regionSet.testState(null, flag);
    // }


    
    
}

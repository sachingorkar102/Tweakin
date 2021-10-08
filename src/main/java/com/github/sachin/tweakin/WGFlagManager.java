package com.github.sachin.tweakin;

import java.util.HashMap;
import java.util.Map;

import com.github.sachin.tweakin.modules.bettersignedit.BSEFlag;
import com.github.sachin.tweakin.modules.shearitemframe.SIFFlag;
import com.github.sachin.tweakin.modules.snowballknockback.SBKFlag;
import com.github.sachin.tweakin.utils.TConstants;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

public class WGFlagManager {


    private Tweakin plugin;
    private WorldGuard WGinstance;
    private FlagRegistry registry; 
    private Map<String,BaseFlag> registeredFlags = new HashMap<>();

    public WGFlagManager(Tweakin plugin){
        this.plugin = plugin;
        this.WGinstance = WorldGuard.getInstance();
        this.registry = WGinstance.getFlagRegistry();
        
    }

    public WorldGuard getWGinstance() {
        return WGinstance;
    }


    public FlagRegistry getRegistry() {
        return registry;
    }


    public void registerFlags(){
            
        registeredFlags.put(TConstants.BSE_FLAG, new BSEFlag(plugin));
        registeredFlags.put(TConstants.SIF_FLAG, new SIFFlag(plugin));
        registeredFlags.put(TConstants.SBK_FLAG, new SBKFlag(plugin));
    }
    public BaseFlag getFlag(String name){
        return registeredFlags.get(name);
    }

    
    
}

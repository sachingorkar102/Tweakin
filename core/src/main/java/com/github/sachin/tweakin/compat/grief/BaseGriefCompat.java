package com.github.sachin.tweakin.compat.grief;

import com.github.sachin.tweakin.Tweakin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public abstract class BaseGriefCompat {

    protected final Tweakin plugin = Tweakin.getPlugin();
    private final String name;

    private final boolean isEnabled;

    public BaseGriefCompat(String name){
        this.name = name;
        this.isEnabled = plugin.getServer().getPluginManager().isPluginEnabled(name);
        plugin.getLogger().info(name+" grief plugin enabled, initializing compat");
    }


    public abstract boolean canBuild(Player player, Location location, Material type);

    public String getName() {
        return name;
    }
}

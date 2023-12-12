package com.github.sachin.tweakin.compat.grief;

import com.bekvon.bukkit.residence.listeners.ResidenceBlockListener;
import com.github.sachin.tweakin.utils.TConstants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;


public class ResidenceCompat extends BaseGriefCompat{

    public ResidenceCompat(){
        super(TConstants.RESIDENCE);
    }

    @Override
    public boolean canBuild(Player player, Location location, Material type) {
        return ResidenceBlockListener.canPlaceBlock(player,location.getBlock(),true);
    }
}

package com.github.sachin.tweakin.compat.grief;

import com.github.sachin.tweakin.utils.TConstants;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TownyCompat extends BaseGriefCompat{


    public TownyCompat() {
        super(TConstants.TOWNY);
    }

    @Override
    public boolean canBuild(Player player, Location location, Material type) {
        return PlayerCacheUtil.getCachePermission(player,location,type, TownyPermission.ActionType.BUILD);
    }
}

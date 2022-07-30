package com.github.sachin.tweakin.utils.compat.grief;

import com.github.sachin.tweakin.utils.TConstants;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.TextMode;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GriefPreventionCompat extends BaseGriefCompat{

    public GriefPreventionCompat() {
        super(TConstants.GRIEF_PREVENTION);
    }

    @Override
    public boolean canBuild(Player player, Location location, Material type) {
        String noBuildReason = GriefPrevention.instance.allowBuild(player,location,type);
        if(noBuildReason != null){
            GriefPrevention.sendMessage(player, ChatColor.RED,noBuildReason);
        }
        return noBuildReason == null;
    }
}

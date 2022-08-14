package com.github.sachin.tweakin.compat.grief;

import com.github.sachin.tweakin.utils.TConstants;
import me.angeschossen.lands.api.flags.Flags;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class LandsCompat extends BaseGriefCompat{

    private final LandsIntegration landsIntegration;

    public LandsCompat(){
        super(TConstants.LANDS);
        this.landsIntegration = new LandsIntegration(plugin);
    }
    @Override
    public boolean canBuild(Player player, Location location, Material type) {
        Area area = landsIntegration.getAreaByLoc(location);
        if(area != null){
           return area.hasFlag(player,Flags.BLOCK_PLACE,type,true);
        }
        return true;
    }
}

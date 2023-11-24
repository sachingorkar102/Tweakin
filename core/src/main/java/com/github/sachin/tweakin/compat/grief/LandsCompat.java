package com.github.sachin.tweakin.compat.grief;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.flags.type.Flags;
import me.angeschossen.lands.api.land.Area;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class LandsCompat extends BaseGriefCompat{

    private final LandsIntegration landsIntegration;

    public LandsCompat(){
        super(TConstants.LANDS);
        this.landsIntegration = LandsIntegration.of(Tweakin.getPlugin());
    }
    @Override
    public boolean canBuild(Player player, Location location, Material type) {
        Area area = landsIntegration.getArea(location);
        if(area != null){
           return area.hasRoleFlag(landsIntegration.getLandPlayer(player.getUniqueId()), Flags.BLOCK_PLACE,type,true);
        }
        return true;
    }

    @Override
    public boolean canUseArmorStand(Player player, ArmorStand as) {
        Area area = landsIntegration.getArea(as.getLocation());
        if(area != null){

            return area.hasRoleFlag(player.getUniqueId(),Flags.INTERACT_GENERAL);
        }
        return true;
    }
}

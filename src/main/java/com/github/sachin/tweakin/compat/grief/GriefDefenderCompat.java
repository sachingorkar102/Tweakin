package com.github.sachin.tweakin.compat.grief;

import com.github.sachin.tweakin.utils.TConstants;
import com.griefdefender.api.User;
import com.griefdefender.api.claim.Claim;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GriefDefenderCompat extends BaseGriefCompat{


    public GriefDefenderCompat() {
        super(TConstants.GRIEFDEFENDER);
    }

    @Override
    public boolean canBuild(Player player, Location location, Material type) {
        com.griefdefender.api.Core core = com.griefdefender.api.GriefDefender.getCore();
        Claim claim = core.getClaimAt(location);

        if (claim != null && !claim.isWilderness()) {
            User user = core.getUser(player.getUniqueId());
            if(user != null){
                return user.canPlace(new ItemStack(type),location);
            }
        }

        return true;
    }
}

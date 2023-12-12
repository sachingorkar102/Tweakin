package com.github.sachin.tweakin.compat.grief;

import com.github.sachin.tweakin.utils.TConstants;
import net.crashcraft.crashclaim.CrashClaim;
import net.crashcraft.crashclaim.api.CrashClaimAPI;
import net.crashcraft.crashclaim.claimobjects.Claim;
import net.crashcraft.crashclaim.permissions.PermissionRoute;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CrashClaimCompat extends BaseGriefCompat{

    private final CrashClaimAPI crashClaimAPI;

    public CrashClaimCompat() {
        super(TConstants.CRASHCLAIM);
        this.crashClaimAPI = CrashClaim.getPlugin().getApi();
    }

    @Override
    public boolean canBuild(Player player, Location location, Material type) {
        Claim claim = crashClaimAPI.getClaim(location);

        UUID uuid = player.getUniqueId();
        if(claim != null){
            return crashClaimAPI.getPermissionHelper().getBypassManager().isBypass(uuid) || claim.hasPermission(uuid,location,PermissionRoute.BUILD);
        }
        return true;
    }
}

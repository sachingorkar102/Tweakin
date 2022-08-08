package com.github.sachin.tweakin.utils;

import com.github.sachin.tweakin.Tweakin;
import com.google.common.base.Enums;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.permissions.DefaultPermissions;

import java.util.*;

public class Permissions {

    private static PermissionDefault defaultValue = PermissionDefault.TRUE;

    public static void reload(){
        defaultValue = Enums.getIfPresent(PermissionDefault.class,Tweakin.getPlugin().getConfig().getString("default-permissions","TRUE").toUpperCase()).or(PermissionDefault.TRUE);
    }

    private static Permission get(String permission){
        Permission perm = new Permission("tweakin."+permission,defaultValue);
        return perm;
    }

    private static Permission get(String parent, List<String> children){
        Map<String,Boolean> childMap = new HashMap<>();
        for(String str : children){
            childMap.put("tweakin."+parent.replace("*","")+str,true);
        }
        Permission parentPerm = new Permission("tweakin."+parent,defaultValue,childMap);
        return parentPerm;
    }


    public static final Permission WATER_EX = get("waterextinguish.use");
    public static final Permission ANVIL_REPAIR = get("anvilrepair.use");
    public static final Permission BETTERRECOVERYCOMPASS_USE = get("betterrecoverycompass.use");
    public static final Permission ARMOREDELYTRA_CRAFT = get("armoredelytra.craft");
    public static final Permission AUTORECIPEUNLOCK = get("autorecipeunlock");

    public static final Permission BETTERARMORSTAND_PARENT = get("betterarmorstands.*", Arrays.asList("command","uuidlockbypass","armorswap"));
    public static final Permission BETTERARMORSTAND_COMMAND = get("betterarmorstands.command");
    public static final Permission BETTERARMORSTAND_UUIDBYPASS = get("betterarmorstands.uuidlockbypass");
    public static final Permission BETTERARMORSTAND_ARMORSWAP = get("betterarmorstands.armorswap");

    public static final Permission ARMORSTANDWAND = get("armorstandwand.use");
    public static final Permission FLEEMOBS = get("fleemobs.bypass");
    public static final Permission BETTER_GRINDSTONE = get("bettergrindstone");

    public static final Permission BETTERLADDER_PARENT = get("betterladder.*",Arrays.asList("quickclimb","dropdown"));
    public static final Permission BETTERLADDER_QUICKCLIMB = get("betterladder.quickclimb");
    public static final Permission BETTERLADDER_DROPDOWN = get("betterladder.dropdown");
    public static final Permission BETTERSIGNEDIT = get("bettersignedit.use");

    public static final Permission BOTTLED_CLOUD_PARENT = get("bottledcloud.*",Arrays.asList("use","pickup"));
    public static final Permission BOTTLEDCLOUD_USE = get("bottledcloud.use");
    public static final Permission BOTTLEDCLOUD_PICKUP = get("bottledcloud.pickup");

    public static final Permission BURNVINETIP_USE = get("burnvinetip.use");

    public static final Permission COMPASSTRACK = get("compasstrack");

    public static final Permission HUD_PARENT = get("hud.*",Arrays.asList("compassbypass","command"));
    public static final Permission HUD_COMPASSBYPASS = get("coordinatehud.compassbypass");
    public static final Permission HUD_COMMAND = get("coordinatehud.command");
    public static final Permission CRAFTINGTABLE_USE = get("craftingtableonstick.use");
    public static final Permission CUSTOMPORTAL_USE = get("customportal.use");
    public static final Permission INFIFIREWORK_USE = get("infinitefirework.use");

    public static final Permission INFBUCKET_PARENT = get("infinitybucket.*",Arrays.asList("craft","use"));
    public static final Permission INFIBUCKET_CRAFT = get("infinitybucket.craft");
    public static final Permission INFIBUCKET_USE = get("infinitybucket.use");
    public static final Permission LAVATRASHCAN = get("lavabuckettrashcan.dragdrop");
    public static final Permission MOBHEADS = get("mobheads.drops");
    public static final Permission NETHERCOORDS = get("netherportalcoords");
    public static final Permission PAT_DOG = get("patdog");
    public static final Permission PAT_CAT = get("patcat");
    public static final Permission POISONPOTATO = get("posionpotato.use");

    public static final Permission REACHAROUND_PARENT = get("reacharound.*",Arrays.asList("highlight","vertical","horizontal","togglecommand"));
    public static final Permission REACHAROUND_HIGHLIGHT = get("reacharound.highlight");
    public static final Permission REACHAROUND_VERT = get("reacharound.vertical");
    public static final Permission REACHAROUND_HORI = get("reacharound.horizontal");
    public static final Permission REACHAROUND_TOGGLE = get("reacharound.togglecommand");


    public static final Permission ARMORCLICK = get("armorclick");

    public static final Permission SHULKERBOX_CLICK = get("shulkerboxclick");
    public static final Permission ENDERCHEST_CLICK = get("enderchestclick");
    public static final Permission ROTATION_WRENCH = get("rotationwrench.use");
    public static final Permission SHEARITEMFRAME = get("shearitemframe.use");
    public static final Permission SHEARNAMETAG = get("shearnametag.use");
    public static final Permission SILENCEMOBS_PARENT = get("silencemobs.*",Arrays.asList("silence","unsilence"));
    public static final Permission SILENCEMOBS_SILENCE = get("silencemobs.silence");
    public static final Permission SILENCEMOBS_UNSILENCE = get("silencemobs.unsilence");

    public static final Permission SLIMEBUCKET_PARENT = get("slimebucket.*",Arrays.asList("pickup","detect"));

    public static final Permission SLIMEBUCKET_PICKUP = get("slimebucket.pickup");
    public static final Permission SLIMEBUCKET_DETECT = get("slimebucket.detect");

    public static final Permission SWINGGRASS = get("swingthroughgrass");

    public static final Permission TROWEL = get("trowel.use");

    public static final Permission VIL_DTH_MSG = get("villagerdeathmessage.notify");


}

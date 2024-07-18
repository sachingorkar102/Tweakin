package com.github.sachin.tweakin.utils;

import com.github.sachin.tweakin.Tweakin;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TConstants {
    public static final List<Integer> BORDER_SLOTS = Arrays.asList(0,1,2,3,4,5,6,7,8,9,18,27,36,45,46,47,48,51,52,53,17,26,35,44);
    public static final List<Integer> TOGGLE_SLOTS = Arrays.asList(10,11,12,13,14,15,16,
                                                                19,20,21,22,23,24,25,
                                                                28,29,30,31,32,33,34,
                                                                37,38,39,40,41,42,43);

    public static final List<Material> ANVILS = Arrays.asList(Material.ANVIL,Material.CHIPPED_ANVIL,Material.DAMAGED_ANVIL);

    public static final String PREFIX = "[Tweakin] ";

    public static final String BSE_FLAG = "tweakin-better-sign-edit";
    public static final String SIF_FLAG = "tweakin-shear-item-frame";
    public static final String SBK_FLAG = "tweakin-snowball-knockback";

    public static final String RA_FLAG = "tweakin-reach-around";

    public static final String GRIEF_PREVENTION = "GriefPrevention";
    public static final String PAPI = "PlaceholderAPI";
    public static final String VULCAN = "Vulcan";

    public static final String ITEMSADDER = "ItemsAdder";
    public static final String ECOENCHANTS = "EcoEnchants";

    public static final String TEAKSTWEAKS = "TeaksTweaks";



    public static final String LANDS = "Lands";
    public static final String RESIDENCE = "Residence";

    public static final String CRASHCLAIM = "CrashClaim";
    public static final String GRIEFDEFENDER = "GriefDefender";

    public static final String COMBATLOGX = "CombatLogX";

    public static final String TOWNY = "Towny";
    public static final String RECIPE_FILE = "more-recipes.yml";

    public static final String CUSTOM_RECIPE_FILE = "custom-recipes.yml";
    public static final String MOB_HEADS_FILE = "heads.yml";
    public static final String MINI_BLOCKS_FILE = "mini-blocks.yml";

    public static final String INIT_HEAD_TEXTURE_VALUE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv";



    
    public static final NamespacedKey UUID_LOCK_KEY = Tweakin.getKey("bas-uuid-lock");
    public static final NamespacedKey ARMORSTAND_EDITED = Tweakin.getKey("bas-armorstand-edited");
    public static final NamespacedKey INTERACTABLE_AS = Tweakin.getKey("bas-interactable-armorstand");
    public static final NamespacedKey NAMETAGED_MOB = Tweakin.getKey("nametaged-mob");
    public static final NamespacedKey COPY_PASTE_KEY = Tweakin.getKey("copy-paste-key");
    public static final NamespacedKey VILLAGER_FOLLOW_KEY = Tweakin.getKey("villager-already-set");
    public static final NamespacedKey INFINITE_FIREWORK_KEY = Tweakin.getKey("infinite-firework-key");

    public static final String WANDERING_TRADER_MSG = "wandering-trader-announcement-message";

    public static final NamespacedKey TRANSFER_MOUNT_KEY = Tweakin.getKey("transfer-mount-armorstand-key");

    public static final NamespacedKey COMBATX_TAG_KEY = Tweakin.getKey("combat-logx-taf-key");

    public static final NamespacedKey SHULKER_DUPE_KEY = Tweakin.getKey("shulker-dupe-prevent-key");

    public static final NamespacedKey SHEARED_CHICKEN_KEY = Tweakin.getKey("sheared-chicken");

    public static final List<String> POST1_20_2 = Arrays.asList("v1_20_R2","v1_20_R3");

    public static final List<String> POST1_20 = Lists.asList("v1_20_R1",toArray(POST1_20_2));

    public static final List<String> POST1_19_3 = Lists.asList("v1_19_R3",toArray(POST1_20));

    public static final List<String> POST1_19 = Lists.asList("v1_19_R1","v1_19_R2",toArray(POST1_19_3));

    public static final List<String> POST1_18 = Lists.asList("v1_18_R1","v1_18_R2",toArray(POST1_19));

    public static final List<String> POST1_17 = Lists.asList("v1_17_R1",toArray(POST1_18));

    private static String[] toArray(List<String> list){
        return list.toArray(new String[0]);
    }
}

package com.github.sachin.tweakin.modules.morerecipes.recipes.universaldyeing;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public enum Dye {
    



    RED(Material.RED_DYE,Material.RED_WOOL,Material.RED_CONCRETE,Material.RED_STAINED_GLASS,Material.RED_STAINED_GLASS_PANE,Material.RED_CONCRETE_POWDER,Material.RED_TERRACOTTA,Material.RED_BED,Material.RED_CARPET),
    BLACK(Material.BLACK_DYE,Material.BLACK_WOOL,Material.BLACK_CONCRETE,Material.BLACK_STAINED_GLASS,Material.BLACK_STAINED_GLASS_PANE,Material.BLACK_CONCRETE_POWDER,Material.BLACK_TERRACOTTA,Material.BLACK_BED,Material.BLACK_CARPET),
    GREEN(Material.GREEN_DYE,Material.GREEN_WOOL,Material.GREEN_CONCRETE,Material.GREEN_STAINED_GLASS,Material.GREEN_STAINED_GLASS_PANE,Material.GREEN_CONCRETE_POWDER,Material.GREEN_TERRACOTTA,Material.GREEN_BED,Material.GREEN_CARPET),
    BROWN(Material.BROWN_DYE,Material.BROWN_WOOL,Material.BROWN_CONCRETE,Material.BROWN_STAINED_GLASS,Material.BROWN_STAINED_GLASS_PANE,Material.BROWN_CONCRETE_POWDER,Material.BROWN_TERRACOTTA,Material.BROWN_BED,Material.BROWN_CARPET),
    BLUE(Material.BLUE_DYE,Material.BLUE_WOOL,Material.BLUE_CONCRETE,Material.BLUE_STAINED_GLASS,Material.BLUE_STAINED_GLASS_PANE,Material.BLUE_CONCRETE_POWDER,Material.BLUE_TERRACOTTA,Material.BLUE_BED,Material.BLUE_CARPET),
    PURPLE(Material.PURPLE_DYE,Material.PURPLE_WOOL,Material.PURPLE_CONCRETE,Material.PURPLE_STAINED_GLASS,Material.PURPLE_STAINED_GLASS_PANE,Material.PURPLE_CONCRETE_POWDER,Material.PURPLE_TERRACOTTA,Material.PURPLE_BED,Material.PURPLE_CARPET),
    CYAN(Material.CYAN_DYE,Material.CYAN_WOOL,Material.CYAN_CONCRETE,Material.CYAN_STAINED_GLASS,Material.CYAN_STAINED_GLASS_PANE,Material.CYAN_CONCRETE_POWDER,Material.CYAN_TERRACOTTA,Material.CYAN_BED,Material.CYAN_CARPET),
    LIGHT_GRAY(Material.LIGHT_GRAY_DYE,Material.LIGHT_GRAY_WOOL,Material.LIGHT_GRAY_CONCRETE,Material.LIGHT_GRAY_STAINED_GLASS,Material.LIGHT_GRAY_STAINED_GLASS_PANE,Material.LIGHT_GRAY_CONCRETE_POWDER,Material.LIGHT_GRAY_TERRACOTTA,Material.LIGHT_GRAY_BED,Material.LIGHT_GRAY_CARPET),
    GRAY(Material.GRAY_DYE,Material.GRAY_WOOL,Material.GRAY_CONCRETE,Material.GRAY_STAINED_GLASS,Material.GRAY_STAINED_GLASS_PANE,Material.GRAY_CONCRETE_POWDER,Material.GRAY_TERRACOTTA,Material.GRAY_BED,Material.GRAY_CARPET),
    PINK(Material.PINK_DYE,Material.PINK_WOOL,Material.PINK_CONCRETE,Material.PINK_STAINED_GLASS,Material.PINK_STAINED_GLASS_PANE,Material.PINK_CONCRETE_POWDER,Material.PINK_TERRACOTTA,Material.PINK_BED,Material.PINK_CARPET),
    LIME(Material.LIME_DYE,Material.LIME_WOOL,Material.LIME_CONCRETE,Material.LIME_STAINED_GLASS,Material.LIME_STAINED_GLASS_PANE,Material.LIME_CONCRETE_POWDER,Material.LIME_TERRACOTTA,Material.LIME_BED,Material.LIME_CARPET),
    YELLOW(Material.YELLOW_DYE,Material.YELLOW_WOOL,Material.YELLOW_CONCRETE,Material.YELLOW_STAINED_GLASS,Material.YELLOW_STAINED_GLASS_PANE,Material.YELLOW_CONCRETE_POWDER,Material.YELLOW_TERRACOTTA,Material.YELLOW_BED,Material.YELLOW_CARPET),
    LIGHT_BLUE(Material.LIGHT_BLUE_DYE,Material.LIGHT_BLUE_WOOL,Material.LIGHT_BLUE_CONCRETE,Material.LIGHT_BLUE_STAINED_GLASS,Material.LIGHT_BLUE_STAINED_GLASS_PANE,Material.LIGHT_BLUE_CONCRETE_POWDER,Material.LIGHT_BLUE_TERRACOTTA,Material.LIGHT_BLUE_BED,Material.LIGHT_BLUE_CARPET),
    MAGENTA(Material.MAGENTA_DYE,Material.MAGENTA_WOOL,Material.MAGENTA_CONCRETE,Material.MAGENTA_STAINED_GLASS,Material.MAGENTA_STAINED_GLASS_PANE,Material.MAGENTA_CONCRETE_POWDER,Material.MAGENTA_TERRACOTTA,Material.MAGENTA_BED,Material.MAGENTA_CARPET),
    ORANGE(Material.ORANGE_DYE,Material.ORANGE_WOOL,Material.ORANGE_CONCRETE,Material.ORANGE_STAINED_GLASS,Material.ORANGE_STAINED_GLASS_PANE,Material.ORANGE_CONCRETE_POWDER,Material.ORANGE_TERRACOTTA,Material.ORANGE_BED,Material.ORANGE_CARPET),
    WHITE(Material.WHITE_DYE,Material.WHITE_WOOL,Material.WHITE_CONCRETE,Material.WHITE_STAINED_GLASS,Material.WHITE_STAINED_GLASS_PANE,Material.WHITE_CONCRETE_POWDER,Material.WHITE_TERRACOTTA,Material.WHITE_BED,Material.WHITE_CARPET);

    ;

    public final Material dye;
    public final Material wool;
    public final Material concrete;
    public final Material stainedGlass;
    public final Material stainedGlassPane;
    public final Material concretePowder;
    public final Material terracotta;
    public final Material bed;
    public final Material carpet;




    private Dye(Material dye, Material wool, Material concrete, Material stainedGlass, Material stainedGlassPane, Material concretePowder, Material terracotta, Material bed, Material carpet) {
        this.dye = dye;
        this.wool = wool;
        this.concrete = concrete;
        this.stainedGlass = stainedGlass;
        this.stainedGlassPane = stainedGlassPane;
        this.concretePowder = concretePowder;
        this.terracotta = terracotta;
        this.bed = bed;
        this.carpet = carpet;

    }

    public static Dye getFromDye(Material dye){
        for(Dye d : Dye.values()){
            if(d.dye == dye){
                return d;
            }
        }
        return null;
    }    
}

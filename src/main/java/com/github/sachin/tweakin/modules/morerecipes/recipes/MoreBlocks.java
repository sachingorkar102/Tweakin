package com.github.sachin.tweakin.modules.morerecipes.recipes;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.morerecipes.BaseRecipe;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;
import com.google.common.base.Enums;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Arrays;

public class MoreBlocks extends BaseRecipe{

    public MoreBlocks(MoreRecipesTweak instance) {
        super("more-blocks", instance);
    }

    @Override
    public void register() {
        ConfigurationSection section = instance.getRecipeFile().getConfigurationSection("more-blocks");
        int trapdoorcount = section.getInt("trapdoor",2);
        int barkcount = section.getInt("bark",2);
        int staircount = section.getInt("stairs",8);
        int brickcount = section.getInt("bricks",4);

        // trapdoors
        for(Material plank : Tag.PLANKS.getValues()){
            Material trapdoor = Enums.getIfPresent(Material.class, plank.toString().replace("PLANKS", "TRAPDOOR")).orNull();
            if(trapdoor!=null){
                NamespacedKey key = Tweakin.getKey(this.name+"-"+trapdoor.toString().toLowerCase());

                ShapedRecipe recipe = new ShapedRecipe(key,new ItemStack(trapdoor,trapdoorcount))
                .shape("PPP","PPP").setIngredient('P', plank);
                addRecipe(key,recipe);
            }
        }

        // Bark
        for(Material mat : Arrays.asList(Material.ACACIA_LOG,Material.BIRCH_LOG,Material.DARK_OAK_LOG,Material.JUNGLE_LOG,Material.OAK_LOG,Material.SPRUCE_LOG)){
            Material bark = Enums.getIfPresent(Material.class, mat.toString().replace("LOG", "WOOD")).orNull();
            
            if(bark != null){
                NamespacedKey key = Tweakin.getKey(this.name+"-"+bark.toString().toLowerCase());
                ShapedRecipe recipe = new ShapedRecipe(key,new ItemStack(bark,barkcount))
                .shape("PP","PP").setIngredient('P', mat);
                addRecipe(key,recipe); 
                Material stripedVersion = Enums.getIfPresent(Material.class, "STRIPPED_"+bark).orNull();
                Material strippedMat = Enums.getIfPresent(Material.class, "STRIPPED_"+mat).orNull();
                if(stripedVersion != null && strippedMat != null){
                    NamespacedKey key2 = Tweakin.getKey(this.name+"-"+stripedVersion.toString().toLowerCase());
                    ShapedRecipe recipe2 = new ShapedRecipe(key,new ItemStack(stripedVersion,barkcount))
                    .shape("PP","PP").setIngredient('P', strippedMat);
                    addRecipe(key2,recipe2);  
                } 
            }
        }
        NamespacedKey crimsonBarkKey = Tweakin.getKey(this.name+"-crimson-wood");
        ShapedRecipe crimsonBark = new ShapedRecipe(crimsonBarkKey,new ItemStack(Material.CRIMSON_HYPHAE,barkcount)).shape("PP","PP").setIngredient('P', Material.CRIMSON_STEM);
        addRecipe(crimsonBarkKey, crimsonBark);
        NamespacedKey warpedBarkKey = Tweakin.getKey(this.name+"-warped-wood");
        ShapedRecipe warpedBark = new ShapedRecipe(warpedBarkKey,new ItemStack(Material.WARPED_HYPHAE,barkcount)).shape("PP","PP").setIngredient('P', Material.WARPED_STEM);
        addRecipe(warpedBarkKey, warpedBark);
        NamespacedKey strippedCrimsonBarkKey = Tweakin.getKey(this.name+"-stripped-crimson-wood");
        ShapedRecipe strippedCrimsonBark = new ShapedRecipe(strippedCrimsonBarkKey,new ItemStack(Material.STRIPPED_CRIMSON_HYPHAE,barkcount)).shape("PP","PP").setIngredient('P', Material.STRIPPED_CRIMSON_STEM);
        addRecipe(strippedCrimsonBarkKey, strippedCrimsonBark);
        NamespacedKey strippedWarpedBarkKey = Tweakin.getKey(this.name+"-stripped-warped-wood");
        ShapedRecipe strippedWarpedBark = new ShapedRecipe(strippedWarpedBarkKey,new ItemStack(Material.STRIPPED_WARPED_HYPHAE,barkcount)).shape("PP","PP").setIngredient('P', Material.STRIPPED_WARPED_STEM);
        addRecipe(strippedWarpedBarkKey, strippedWarpedBark);

        // Bricks
        NamespacedKey brickKey = Tweakin.getKey(this.name+"-bricks");
        ShapedRecipe bricks = new ShapedRecipe(brickKey,new ItemStack(Material.BRICKS,brickcount)).shape("PPP","PPP","PPP").setIngredient('P', Material.BRICK);
        addRecipe(brickKey, bricks);
        NamespacedKey netherBrickKey = Tweakin.getKey(this.name+"-nether-bricks");
        ShapedRecipe netherBricks = new ShapedRecipe(netherBrickKey,new ItemStack(Material.NETHER_BRICKS,brickcount)).shape("PPP","PPP","PPP").setIngredient('P', Material.NETHER_BRICK);
        addRecipe(netherBrickKey, netherBricks);

        // Stairs
        StairRecipe(Material.PURPUR_STAIRS,Material.PURPUR_BLOCK,staircount);
        StairRecipe(Material.OAK_STAIRS,Material.OAK_PLANKS,staircount);
        StairRecipe(Material.ACACIA_STAIRS,Material.ACACIA_PLANKS,staircount);
        StairRecipe(Material.JUNGLE_STAIRS,Material.JUNGLE_PLANKS,staircount);
        StairRecipe(Material.BIRCH_STAIRS,Material.BIRCH_PLANKS,staircount);
        StairRecipe(Material.SPRUCE_STAIRS,Material.SPRUCE_PLANKS,staircount);
        StairRecipe(Material.DARK_OAK_STAIRS,Material.DARK_OAK_PLANKS,staircount);
        StairRecipe(Material.CRIMSON_STAIRS,Material.CRIMSON_PLANKS,staircount);
        StairRecipe(Material.WARPED_STAIRS,Material.WARPED_PLANKS,staircount);
        StairRecipe(Material.COBBLESTONE_STAIRS,Material.COBBLESTONE,staircount);
        StairRecipe(Material.BRICK_STAIRS,Material.BRICKS,staircount);
        StairRecipe(Material.STONE_BRICK_STAIRS,Material.STONE_BRICKS,staircount);
        StairRecipe(Material.NETHER_BRICK_STAIRS,Material.NETHER_BRICKS,staircount);
        StairRecipe(Material.SANDSTONE_STAIRS,Material.SANDSTONE,staircount);
        StairRecipe(Material.QUARTZ_STAIRS,Material.QUARTZ_BLOCK,staircount);
        StairRecipe(Material.PRISMARINE_STAIRS,Material.PRISMARINE,staircount);
        StairRecipe(Material.PRISMARINE_BRICK_STAIRS,Material.PRISMARINE_BRICKS,staircount);
        StairRecipe(Material.DARK_PRISMARINE_STAIRS,Material.DARK_PRISMARINE,staircount);
        StairRecipe(Material.RED_SANDSTONE_STAIRS,Material.RED_SANDSTONE,staircount);
        StairRecipe(Material.POLISHED_GRANITE_STAIRS,Material.POLISHED_GRANITE,staircount);
        StairRecipe(Material.SMOOTH_RED_SANDSTONE_STAIRS,Material.SMOOTH_RED_SANDSTONE,staircount);
        StairRecipe(Material.MOSSY_COBBLESTONE_STAIRS,Material.MOSSY_COBBLESTONE,staircount);
        StairRecipe(Material.POLISHED_DIORITE_STAIRS,Material.POLISHED_DIORITE,staircount);
        StairRecipe(Material.MOSSY_COBBLESTONE_STAIRS,Material.MOSSY_COBBLESTONE,staircount);
        StairRecipe(Material.END_STONE_BRICK_STAIRS,Material.END_STONE_BRICKS,staircount);
        StairRecipe(Material.STONE_STAIRS,Material.STONE,staircount);
        StairRecipe(Material.SMOOTH_QUARTZ_STAIRS,Material.SMOOTH_QUARTZ,staircount);
        StairRecipe(Material.GRANITE_STAIRS,Material.GRANITE,staircount);
        StairRecipe(Material.ANDESITE_STAIRS,Material.ANDESITE,staircount);
        StairRecipe(Material.DIORITE_STAIRS,Material.DIORITE,staircount);
        StairRecipe(Material.RED_NETHER_BRICK_STAIRS,Material.RED_NETHER_BRICKS,staircount);
        StairRecipe(Material.POLISHED_ANDESITE_STAIRS,Material.POLISHED_ANDESITE,staircount);
        StairRecipe(Material.BLACKSTONE_STAIRS,Material.BLACKSTONE,staircount);
        StairRecipe(Material.POLISHED_BLACKSTONE_BRICK_STAIRS,Material.POLISHED_BLACKSTONE_BRICK_STAIRS,staircount);
        StairRecipe(Material.POLISHED_BLACKSTONE_STAIRS,Material.POLISHED_BLACKSTONE,staircount);
        if(instance.getPlugin().isPost1_17()){
            StairRecipe(Material.COBBLED_DEEPSLATE_STAIRS,Material.COBBLED_DEEPSLATE,staircount);
            StairRecipe(Material.CUT_COPPER_STAIRS,Material.CUT_COPPER,staircount);
            StairRecipe(Material.DEEPSLATE_BRICK_STAIRS,Material.DEEPSLATE_BRICKS,staircount);
            StairRecipe(Material.DEEPSLATE_TILE_STAIRS,Material.DEEPSLATE_TILES,staircount);
            StairRecipe(Material.EXPOSED_CUT_COPPER_STAIRS,Material.EXPOSED_CUT_COPPER,staircount);
            StairRecipe(Material.OXIDIZED_CUT_COPPER_STAIRS,Material.OXIDIZED_CUT_COPPER,staircount);
            StairRecipe(Material.POLISHED_DEEPSLATE_STAIRS,Material.POLISHED_DEEPSLATE,staircount);
            StairRecipe(Material.WAXED_CUT_COPPER_STAIRS,Material.WAXED_CUT_COPPER,staircount);
            StairRecipe(Material.WAXED_EXPOSED_CUT_COPPER_STAIRS,Material.WAXED_EXPOSED_CUT_COPPER_STAIRS,staircount);
            StairRecipe(Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS,Material.WAXED_OXIDIZED_CUT_COPPER,staircount);
            StairRecipe(Material.WAXED_WEATHERED_CUT_COPPER_STAIRS,Material.WAXED_WEATHERED_CUT_COPPER,staircount);
            StairRecipe(Material.WEATHERED_CUT_COPPER_STAIRS,Material.WEATHERED_CUT_COPPER,staircount);
        }
        
    }

    private void StairRecipe(Material stair,Material ing,int count){
        NamespacedKey key = Tweakin.getKey(this.name+"-"+stair.toString().toLowerCase());
        ShapedRecipe recipe = new ShapedRecipe(key,new ItemStack(stair,count)).shape("P  ","PP ","PPP").setIngredient('P', ing);
        addRecipe(key,recipe);
    }
    
}

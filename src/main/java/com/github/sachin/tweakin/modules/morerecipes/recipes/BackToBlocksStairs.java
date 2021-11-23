package com.github.sachin.tweakin.modules.morerecipes.recipes;

import com.github.sachin.tweakin.modules.morerecipes.BaseRecipe;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

public class BackToBlocksStairs extends BaseRecipe{

    public BackToBlocksStairs(MoreRecipesTweak instance) {
        super("back-to-blocks-stair-edition", instance);
    }

    @Override
    public void register() {
        StairRecipe(Material.PURPUR_STAIRS,Material.PURPUR_BLOCK);
        StairRecipe(Material.OAK_STAIRS,Material.OAK_PLANKS);
        StairRecipe(Material.ACACIA_STAIRS,Material.ACACIA_PLANKS);
        StairRecipe(Material.JUNGLE_STAIRS,Material.JUNGLE_PLANKS);
        StairRecipe(Material.BIRCH_STAIRS,Material.BIRCH_PLANKS);
        StairRecipe(Material.SPRUCE_STAIRS,Material.SPRUCE_PLANKS);
        StairRecipe(Material.DARK_OAK_STAIRS,Material.DARK_OAK_PLANKS);
        StairRecipe(Material.CRIMSON_STAIRS,Material.CRIMSON_PLANKS);
        StairRecipe(Material.WARPED_STAIRS,Material.WARPED_PLANKS);
        StairRecipe(Material.COBBLESTONE_STAIRS,Material.COBBLESTONE);
        StairRecipe(Material.BRICK_STAIRS,Material.BRICKS);
        StairRecipe(Material.STONE_BRICK_STAIRS,Material.STONE_BRICKS);
        StairRecipe(Material.NETHER_BRICK_STAIRS,Material.NETHER_BRICKS);
        StairRecipe(Material.SANDSTONE_STAIRS,Material.SANDSTONE);
        StairRecipe(Material.QUARTZ_STAIRS,Material.QUARTZ_BLOCK);
        StairRecipe(Material.PRISMARINE_STAIRS,Material.PRISMARINE);
        StairRecipe(Material.PRISMARINE_BRICK_STAIRS,Material.PRISMARINE_BRICKS);
        StairRecipe(Material.DARK_PRISMARINE_STAIRS,Material.DARK_PRISMARINE);
        StairRecipe(Material.RED_SANDSTONE_STAIRS,Material.RED_SANDSTONE);
        StairRecipe(Material.POLISHED_GRANITE_STAIRS,Material.POLISHED_GRANITE);
        StairRecipe(Material.SMOOTH_RED_SANDSTONE_STAIRS,Material.RED_SANDSTONE);
        StairRecipe(Material.MOSSY_COBBLESTONE_STAIRS,Material.MOSSY_COBBLESTONE);
        StairRecipe(Material.POLISHED_DIORITE_STAIRS,Material.POLISHED_DIORITE);
        StairRecipe(Material.MOSSY_COBBLESTONE_STAIRS,Material.MOSSY_COBBLESTONE);
        StairRecipe(Material.END_STONE_BRICK_STAIRS,Material.END_STONE_BRICKS);
        StairRecipe(Material.STONE_STAIRS,Material.STONE);
        StairRecipe(Material.SMOOTH_QUARTZ_STAIRS,Material.SMOOTH_QUARTZ);
        StairRecipe(Material.GRANITE_STAIRS,Material.GRANITE);
        StairRecipe(Material.ANDESITE_STAIRS,Material.ANDESITE);
        StairRecipe(Material.DIORITE_STAIRS,Material.DIORITE);
        StairRecipe(Material.RED_NETHER_BRICK_STAIRS,Material.RED_NETHER_BRICKS);
        StairRecipe(Material.POLISHED_ANDESITE_STAIRS,Material.POLISHED_ANDESITE);
        StairRecipe(Material.BLACKSTONE_STAIRS,Material.BLACKSTONE);
        StairRecipe(Material.POLISHED_BLACKSTONE_BRICK_STAIRS,Material.POLISHED_BLACKSTONE_BRICKS);
        StairRecipe(Material.POLISHED_BLACKSTONE_STAIRS,Material.POLISHED_BLACKSTONE);
        if(instance.getPlugin().isPost1_17()){
            StairRecipe(Material.COBBLED_DEEPSLATE_STAIRS,Material.COBBLED_DEEPSLATE);
            StairRecipe(Material.CUT_COPPER_STAIRS,Material.CUT_COPPER);
            StairRecipe(Material.DEEPSLATE_BRICK_STAIRS,Material.DEEPSLATE_BRICKS);
            StairRecipe(Material.DEEPSLATE_TILE_STAIRS,Material.DEEPSLATE_TILES);
            StairRecipe(Material.EXPOSED_CUT_COPPER_STAIRS,Material.EXPOSED_CUT_COPPER);
            StairRecipe(Material.OXIDIZED_CUT_COPPER_STAIRS,Material.OXIDIZED_CUT_COPPER);
            StairRecipe(Material.POLISHED_DEEPSLATE_STAIRS,Material.POLISHED_DEEPSLATE);
            StairRecipe(Material.WAXED_CUT_COPPER_STAIRS,Material.WAXED_CUT_COPPER);
            StairRecipe(Material.WAXED_EXPOSED_CUT_COPPER_STAIRS,Material.WAXED_EXPOSED_CUT_COPPER);
            StairRecipe(Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS,Material.WAXED_OXIDIZED_CUT_COPPER);
            StairRecipe(Material.WAXED_WEATHERED_CUT_COPPER_STAIRS,Material.WAXED_WEATHERED_CUT_COPPER);
            StairRecipe(Material.WEATHERED_CUT_COPPER_STAIRS,Material.WEATHERED_CUT_COPPER);
        }
        
    }

    public void StairRecipe(Material stair,Material block){
        NamespacedKey recipeKey = new NamespacedKey(instance.getPlugin(), "back_to_blocks_"+stair.toString().toLowerCase()+"_to_"+block.toString().toLowerCase());
        ShapelessRecipe recipe = new ShapelessRecipe(recipeKey, new ItemStack(block,3)).addIngredient(4, stair);
        addRecipe(recipeKey,recipe);   
    }
    
}

package com.github.sachin.tweakin.modules.morerecipes.recipes;

import com.github.sachin.tweakin.modules.morerecipes.BaseRecipe;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;

import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class BackToBlocksSlabs extends BaseRecipe{

    public BackToBlocksSlabs(MoreRecipesTweak instance) {
        super("back-to-blocks-slab-edition", instance);
    }

    @Override
    public void register() {
        // wooden slabs
        SlabRecipe(Material.ACACIA_SLAB, Material.ACACIA_PLANKS);
        SlabRecipe(Material.JUNGLE_SLAB, Material.JUNGLE_PLANKS);
        SlabRecipe(Material.DARK_OAK_SLAB, Material.DARK_OAK_PLANKS);
        SlabRecipe(Material.OAK_SLAB, Material.OAK_PLANKS);
        SlabRecipe(Material.SPRUCE_SLAB, Material.SPRUCE_PLANKS);
        SlabRecipe(Material.BIRCH_SLAB, Material.BIRCH_PLANKS);
        SlabRecipe(Material.WARPED_SLAB, Material.WARPED_PLANKS);
        SlabRecipe(Material.CRIMSON_SLAB, Material.CRIMSON_PLANKS);

        SlabRecipe(Material.STONE_SLAB, Material.STONE);
        SlabRecipe(Material.ANDESITE_SLAB, Material.ANDESITE);
        SlabRecipe(Material.GRANITE_SLAB, Material.GRANITE);
        SlabRecipe(Material.DIORITE_SLAB, Material.DIORITE);
        SlabRecipe(Material.COBBLESTONE_SLAB, Material.COBBLESTONE);
        SlabRecipe(Material.BLACKSTONE_SLAB, Material.BLACKSTONE);
        SlabRecipe(Material.CUT_RED_SANDSTONE_SLAB, Material.RED_SANDSTONE);
        SlabRecipe(Material.CUT_SANDSTONE_SLAB, Material.SANDSTONE);
        SlabRecipe(Material.MOSSY_COBBLESTONE_SLAB, Material.MOSSY_COBBLESTONE);
        SlabRecipe(Material.DARK_PRISMARINE_SLAB, Material.DARK_PRISMARINE);
        SlabRecipe(Material.END_STONE_BRICK_SLAB, Material.END_STONE_BRICKS);
        SlabRecipe(Material.MOSSY_STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICKS);
        SlabRecipe(Material.NETHER_BRICK_SLAB, Material.NETHER_BRICKS);
        SlabRecipe(Material.BRICK_SLAB, Material.BRICKS);
        SlabRecipe(Material.POLISHED_ANDESITE_SLAB, Material.POLISHED_ANDESITE);
        SlabRecipe(Material.POLISHED_DIORITE_SLAB, Material.POLISHED_DIORITE);
        SlabRecipe(Material.POLISHED_GRANITE_SLAB, Material.POLISHED_GRANITE);
        SlabRecipe(Material.POLISHED_BLACKSTONE_BRICK_SLAB, Material.POLISHED_BLACKSTONE_BRICKS);
        SlabRecipe(Material.POLISHED_BLACKSTONE_SLAB, Material.POLISHED_BLACKSTONE);
        SlabRecipe(Material.PRISMARINE_BRICK_SLAB, Material.PRISMARINE_BRICKS);
        SlabRecipe(Material.PRISMARINE_SLAB, Material.PRISMARINE);
        SlabRecipe(Material.PURPUR_SLAB, Material.PURPUR_BLOCK);
        SlabRecipe(Material.QUARTZ_SLAB, Material.QUARTZ_BLOCK);
        SlabRecipe(Material.RED_NETHER_BRICK_SLAB, Material.RED_NETHER_BRICKS);
        SlabRecipe(Material.RED_SANDSTONE_SLAB, Material.RED_SANDSTONE);
        SlabRecipe(Material.SANDSTONE_SLAB, Material.SANDSTONE);
        SlabRecipe(Material.SMOOTH_QUARTZ_SLAB, Material.SMOOTH_QUARTZ);
        SlabRecipe(Material.SMOOTH_RED_SANDSTONE_SLAB, Material.SMOOTH_RED_SANDSTONE);
        SlabRecipe(Material.SMOOTH_SANDSTONE_SLAB, Material.SMOOTH_SANDSTONE);
        SlabRecipe(Material.SMOOTH_STONE_SLAB, Material.SMOOTH_STONE);
        SlabRecipe(Material.STONE_BRICK_SLAB, Material.STONE_BRICKS);
        if(this.instance.getPlugin().isPost1_17()){
            SlabRecipe(Material.CUT_COPPER_SLAB, Material.CUT_COPPER);
            SlabRecipe(Material.WEATHERED_CUT_COPPER_SLAB, Material.WEATHERED_CUT_COPPER);
            SlabRecipe(Material.EXPOSED_CUT_COPPER_SLAB, Material.EXPOSED_CUT_COPPER);
            SlabRecipe(Material.OXIDIZED_CUT_COPPER_SLAB, Material.OXIDIZED_CUT_COPPER);

            SlabRecipe(Material.WAXED_CUT_COPPER_SLAB, Material.WAXED_CUT_COPPER);
            SlabRecipe(Material.WAXED_WEATHERED_CUT_COPPER_SLAB, Material.WAXED_WEATHERED_CUT_COPPER);
            SlabRecipe(Material.WAXED_EXPOSED_CUT_COPPER_SLAB, Material.WAXED_EXPOSED_CUT_COPPER);
            SlabRecipe(Material.WAXED_OXIDIZED_CUT_COPPER_SLAB, Material.WAXED_OXIDIZED_CUT_COPPER);

            SlabRecipe(Material.COBBLED_DEEPSLATE_SLAB  , Material.COBBLED_DEEPSLATE);
            SlabRecipe(Material.POLISHED_DEEPSLATE_SLAB , Material.POLISHED_DEEPSLATE);

            SlabRecipe(Material.DEEPSLATE_TILE_SLAB, Material.DEEPSLATE_TILES);
            SlabRecipe(Material.DEEPSLATE_BRICK_SLAB, Material.DEEPSLATE_BRICKS);
        }

        
    }


    public void SlabRecipe(Material slab,Material block){
        NamespacedKey recipeKey = new NamespacedKey(instance.getPlugin(), "back_to_blocks_"+slab.toString().toLowerCase()+"_to_"+block.toString().toLowerCase());
        ShapedRecipe recipe = new ShapedRecipe(recipeKey, new ItemStack(block)).shape("##").setIngredient('#', slab);
        addRecipe(recipeKey, recipe);

    }

    

    

    
    
}

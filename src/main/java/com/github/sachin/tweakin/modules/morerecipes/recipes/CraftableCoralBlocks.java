package com.github.sachin.tweakin.modules.morerecipes.recipes;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.morerecipes.BaseRecipe;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;

public class CraftableCoralBlocks{

    public static final ItemStack TUBE = new ItemStack(Material.TUBE_CORAL_BLOCK);
    public static final ItemStack BRAIN = new ItemStack(Material.BRAIN_CORAL_BLOCK);
    public static final ItemStack BUBBLE = new ItemStack(Material.BUBBLE_CORAL_BLOCK);
    public static final ItemStack FIRE = new ItemStack(Material.FIRE_CORAL_BLOCK);
    public static final ItemStack HORN = new ItemStack(Material.HORN_CORAL_BLOCK);



    public static class TwoByTwo extends BaseRecipe{

        public TwoByTwo(MoreRecipesTweak instance) {
            super("craftable-coral-blocks-two-by-two", instance);
        }

        @Override
        public void register() {
            add2by2Recipe(Material.TUBE_CORAL, Material.TUBE_CORAL_FAN, TUBE);
            add2by2Recipe(Material.BRAIN_CORAL, Material.BRAIN_CORAL_FAN, BRAIN);
            add2by2Recipe(Material.BUBBLE_CORAL, Material.BUBBLE_CORAL_FAN, BUBBLE);
            add2by2Recipe(Material.FIRE_CORAL, Material.FIRE_CORAL_FAN, FIRE);
            add2by2Recipe(Material.HORN_CORAL, Material.HORN_CORAL_FAN, HORN);

            
        }

        private void add2by2Recipe(Material coral,Material fan,ItemStack block){
            NamespacedKey key = Tweakin.getKey(this.name+"-"+block.getType().toString().toLowerCase());
            ShapedRecipe recipe = new ShapedRecipe(key, block).shape("AA","AA").setIngredient('A', new MaterialChoice(coral,fan));
            addRecipe(key, recipe);
        }

    }


    public static class ThreeByThree extends BaseRecipe{

        public ThreeByThree(MoreRecipesTweak instance) {
            super("craftable-coral-blocks-three-by-three", instance);
        }

        @Override
        public void register() {
            add3by3Recipe(Material.TUBE_CORAL, Material.TUBE_CORAL_FAN, TUBE);
            add3by3Recipe(Material.BRAIN_CORAL, Material.BRAIN_CORAL_FAN, BRAIN);
            add3by3Recipe(Material.BUBBLE_CORAL, Material.BUBBLE_CORAL_FAN, BUBBLE);
            add3by3Recipe(Material.FIRE_CORAL, Material.FIRE_CORAL_FAN, FIRE);
            add3by3Recipe(Material.HORN_CORAL, Material.HORN_CORAL_FAN, HORN);
            
            
        }

        private void add3by3Recipe(Material coral,Material fan,ItemStack block){
            NamespacedKey key = Tweakin.getKey(this.name+"-"+block.getType().toString().toLowerCase());
            ShapedRecipe recipe = new ShapedRecipe(key, block).shape("AAA","AAA","AAA").setIngredient('A', new MaterialChoice(coral,fan));
            addRecipe(key, recipe);
        }

    }
    
    
}

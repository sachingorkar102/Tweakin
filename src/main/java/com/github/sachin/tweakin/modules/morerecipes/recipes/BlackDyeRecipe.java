package com.github.sachin.tweakin.modules.morerecipes.recipes;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.morerecipes.BaseRecipe;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

public class BlackDyeRecipe {

    private static final ItemStack BLACK_DYE = new ItemStack(Material.BLACK_DYE);

    public static class Charcoal extends BaseRecipe{

        public Charcoal( MoreRecipesTweak instance) {
            super("black-dye-recipe-charcoal-edition", instance);
        }

        @Override
        public void register() {
            NamespacedKey key = Tweakin.getKey(this.name);
            ShapelessRecipe recipe = new ShapelessRecipe(key, BLACK_DYE).addIngredient(Material.CHARCOAL);
            addRecipe(key, recipe);
        }

    }

    public static class Coal extends BaseRecipe{

        public Coal( MoreRecipesTweak instance) {
            super("black-dye-recipe-coal-edition", instance);
        }

        @Override
        public void register() {
            NamespacedKey key = Tweakin.getKey(this.name);
            ShapelessRecipe recipe = new ShapelessRecipe(key, BLACK_DYE).addIngredient(Material.COAL);
            addRecipe(key, recipe);       
        }

    }    

    
}

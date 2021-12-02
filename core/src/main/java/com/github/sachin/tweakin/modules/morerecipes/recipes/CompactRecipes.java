package com.github.sachin.tweakin.modules.morerecipes.recipes;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.morerecipes.BaseRecipe;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class CompactRecipes extends BaseRecipe{

    public CompactRecipes(MoreRecipesTweak instance) {
        super("compact-recipes", instance);
    }

    @Override
    public void register() {
        NamespacedKey paperKey = Tweakin.getKey(this.name+"_paper");
        NamespacedKey breadKey = Tweakin.getKey(this.name+"_bread");
        NamespacedKey cookieKey = Tweakin.getKey(this.name+"_cookie");
        String shape1 = "##";
        String shape2 = "# ";

        String shape3 = "X#";
        ShapedRecipe paperRecipe = new ShapedRecipe(paperKey, new ItemStack(Material.PAPER)).shape(shape1, shape2).setIngredient('#',Material.SUGAR_CANE);
        ShapedRecipe breadRecipe = new ShapedRecipe(breadKey, new ItemStack(Material.BREAD)).shape(shape1, shape2).setIngredient('#',Material.WHEAT);
        ShapedRecipe cookieRecipe = new ShapedRecipe(cookieKey, new ItemStack(Material.COOKIE)).shape(shape3, shape2).setIngredient('#',Material.COCOA_BEANS).setIngredient('X',Material.WHEAT);
        addRecipe(paperKey, paperRecipe);
        addRecipe(breadKey, breadRecipe);
        addRecipe(cookieKey, cookieRecipe);

        NamespacedKey shulkerKey = Tweakin.getKey(this.name+"_shulker_box");
        ShapedRecipe shulkerRecipe = new ShapedRecipe(shulkerKey, new ItemStack(Material.SHULKER_BOX)).shape(shape3, shape2).setIngredient('#',Material.SHULKER_SHELL).setIngredient('X', Material.CHEST);
        addRecipe(shulkerKey, shulkerRecipe);

        

    }
    
}

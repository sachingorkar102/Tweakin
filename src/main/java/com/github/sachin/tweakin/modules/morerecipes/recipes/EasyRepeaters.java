package com.github.sachin.tweakin.modules.morerecipes.recipes;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.morerecipes.BaseRecipe;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class EasyRepeaters extends BaseRecipe{

    public EasyRepeaters(MoreRecipesTweak instance) {
        super("easy-repeater", instance);
    }

    @Override
    public void register() {
        NamespacedKey key = Tweakin.getKey(this.name);
        ShapedRecipe recipe = new ShapedRecipe(key,new ItemStack(Material.REPEATER))
        .shape("R R","SRS","CCC")
        .setIngredient('R', Material.REDSTONE)
        .setIngredient('S', Material.STICK)
        .setIngredient('C', Material.STONE);
        addRecipe(key, recipe);
        
    }
    
}

package com.github.sachin.tweakin.modules.morerecipes.recipes;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.morerecipes.BaseRecipe;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmokingRecipe;

public class RottenFleshToLeather extends BaseRecipe{

    public RottenFleshToLeather(MoreRecipesTweak instance){
        super("rotten-flesh-to-leather",instance);
    }

    @Override
    public void register() {
        ItemStack leather = new ItemStack(Material.LEATHER);
        Material rottenFlesh = Material.ROTTEN_FLESH;
        NamespacedKey furnaceKey = Tweakin.getKey(this.name+"_furnace");
        FurnaceRecipe furnaceRecipe = new FurnaceRecipe(furnaceKey,leather,rottenFlesh, 1.0F, 200);
        addRecipe(furnaceKey, furnaceRecipe);

        NamespacedKey smokerKey = Tweakin.getKey(this.name+"_smoker");
        SmokingRecipe smokingRecipe = new SmokingRecipe(smokerKey, leather, rottenFlesh, 1.0F, 100);
        addRecipe(smokerKey, smokingRecipe);
    }
    
}

package com.github.sachin.tweakin.modules.morerecipes.recipes;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.morerecipes.BaseRecipe;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;
import com.github.sachin.tweakin.modules.morerecipes.recipes.universaldyeing.Dye;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

public class PowderToGlass extends BaseRecipe{

    public PowderToGlass( MoreRecipesTweak instance) {
        super("concrete-powder-to-glass", instance);
    }

    @Override
    public void register() {
        for(Dye dye : Dye.values()){
            NamespacedKey furnaceKey = Tweakin.getKey(this.name+"-"+dye.concretePowder.toString().toLowerCase()+"-to-"+dye.stainedGlass.toString().toLowerCase()+"-furnace");
            FurnaceRecipe furnaceRecipe = new FurnaceRecipe(furnaceKey, new ItemStack(dye.stainedGlass),dye.concretePowder, 0.1F,100);
            addRecipe(furnaceKey, furnaceRecipe);
        }
    }
    
}

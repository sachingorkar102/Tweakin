package com.github.sachin.tweakin.modules.morerecipes.recipes;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.morerecipes.BaseRecipe;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;
import com.google.common.base.Enums;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.StonecuttingRecipe;

import java.util.Arrays;
import java.util.List;

public class MoreStoneCutterRecipes extends BaseRecipe{

    private final List<String> PLANK_MATERIALS = Arrays.asList("STAIRS","SLAB","BUTTON","PRESSURE_PLATE","TRAPDOOR","FENCE_GATE","SIGN");

    public MoreStoneCutterRecipes( MoreRecipesTweak instance) {
        super("more-stonecutter-recipes", instance);
    }

    @Override
    public void register() {
        for(Material mat : Tag.PLANKS.getValues()){
            for(String option : PLANK_MATERIALS){
                Material optionMaterial = Enums.getIfPresent(Material.class, mat.toString().replace("PLANKS", option)).orNull();
                if(optionMaterial != null){
                    NamespacedKey key = Tweakin.getKey(this.name+"_"+mat.toString().toLowerCase()+"_"+optionMaterial.toString().toLowerCase()+"_stonecutting");
                    StonecuttingRecipe recipe = new StonecuttingRecipe(key,new ItemStack(optionMaterial),mat);
                    addRecipe(key, recipe);
                }
            }
        }
    }
    
}

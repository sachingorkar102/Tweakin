package com.github.sachin.tweakin.modules.morerecipes.recipes;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.morerecipes.BaseRecipe;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;
import com.google.common.base.Enums;
import com.google.common.base.Optional;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class CompatShulkerRecipe extends BaseRecipe{

    public CompatShulkerRecipe(MoreRecipesTweak instance) {
        super("compat-shulker-box-recipe", instance);
    }

    @Override
    public void register() {
        for(Material mat : Tag.SHULKER_BOXES.getValues()){
            if(mat==Material.SHULKER_BOX) continue;
            NamespacedKey key = Tweakin.getKey(mat.toString().toLowerCase()+"_compat_shulker_box");
            Optional<Material> dye = Enums.getIfPresent(Material.class, mat.toString().replace("SHULKER_BOX", "DYE"));
            if(dye.isPresent()){
                ShapedRecipe recipe = new ShapedRecipe(key,new ItemStack(mat)).shape("SC",
                                                                                     "DS")
                .setIngredient('D', dye.get())
                .setIngredient('S', Material.SHULKER_SHELL)
                .setIngredient('C', Material.CHEST);
                addRecipe(key, recipe);
                
            }
        }
    }
    
}

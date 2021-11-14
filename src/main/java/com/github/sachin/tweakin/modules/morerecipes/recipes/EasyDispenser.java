package com.github.sachin.tweakin.modules.morerecipes.recipes;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.morerecipes.BaseRecipe;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class EasyDispenser extends BaseRecipe{

    public EasyDispenser(MoreRecipesTweak instance) {
        super("easy-dispenser", instance);
    }

    @Override
    public void register() {
        NamespacedKey key1 = Tweakin.getKey(this.name+"_"+"dispenser_no_bow");
        NamespacedKey key2 = Tweakin.getKey(this.name+"_"+"dispenser_bow_combined");
        ShapedRecipe recipe1 = new ShapedRecipe(key1,new ItemStack(Material.DISPENSER))
        .shape(" ts"
              ,"tds"
              ," ts")
              .setIngredient('t', Material.STICK).setIngredient('d', Material.DROPPER).setIngredient('s', Material.STRING);
        ShapelessRecipe recipe2 = new ShapelessRecipe(key2,new ItemStack(Material.DISPENSER)).addIngredient(Material.DROPPER).addIngredient(Material.BOW);
        addRecipe(key1, recipe1);      
        addRecipe(key2, recipe2);
    }


    
}

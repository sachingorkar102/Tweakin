package com.github.sachin.tweakin.modules.morerecipes;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecipe {

    public final String name;
    public final MoreRecipesTweak instance;
    protected List<NamespacedKey> recipes = new ArrayList<>();

    public BaseRecipe(String name,MoreRecipesTweak instance){
        this.name = name;
        this.instance = instance;
    }

    public abstract void register();

    public void unregister(){
        if(recipes.isEmpty()) return;
        for (NamespacedKey namespacedKey : recipes) {
            Bukkit.removeRecipe(namespacedKey);
        }
        recipes.clear();
    }


    public List<NamespacedKey> getRecipes() {
        return recipes;
    }

    public void addRecipe(NamespacedKey key,Recipe recipe){
        if(!recipes.contains(key)){
            try {
                Bukkit.addRecipe(recipe);
            } catch (IllegalStateException ignored) {}
            recipes.add(key);
        }
    }
    
}

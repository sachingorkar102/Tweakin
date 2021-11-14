package com.github.sachin.tweakin.modules.morerecipes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.morerecipes.recipes.BackToBlocksSlabs;
import com.github.sachin.tweakin.modules.morerecipes.recipes.BackToBlocksStairs;
import com.github.sachin.tweakin.modules.morerecipes.recipes.CompactRecipes;
import com.github.sachin.tweakin.modules.morerecipes.recipes.CompatShulkerRecipe;
import com.github.sachin.tweakin.modules.morerecipes.recipes.EasyDispenser;
import com.github.sachin.tweakin.modules.morerecipes.recipes.EasyMinecarts;
import com.github.sachin.tweakin.modules.morerecipes.recipes.EasyRepeaters;
import com.github.sachin.tweakin.modules.morerecipes.recipes.EasyStoneTools;
import com.github.sachin.tweakin.modules.morerecipes.recipes.MoreStoneCutterRecipes;
import com.github.sachin.tweakin.modules.morerecipes.recipes.RottenFleshToLeather;
import com.github.sachin.tweakin.modules.morerecipes.recipes.universaldyeing.UniversalDyeing;
import com.github.sachin.tweakin.utils.ConfigUpdater;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

public class MoreRecipesTweak extends BaseTweak implements Listener{

    private FileConfiguration recipeFile;
    private final String RECIPE_FILE_NAME = "more-recipes.yml";
    private List<BaseRecipe> baseRecipes;


    public MoreRecipesTweak(Tweakin plugin) {
        super(plugin, "more-recipes");
        
    }

    @Override
    public void register() {
        super.register();
        saveRecipeFile();
        int registerdRecipes = 0;
        int enabledModules = 0;
        for(BaseRecipe baseRecipe : getBaseRecipes()){
            if(baseRecipe instanceof UniversalDyeing){
                baseRecipe.unregister();
                baseRecipe.register();
                enabledModules++;
            }
            else if(recipeFile.getBoolean(baseRecipe.name, true)){
                baseRecipe.register();
                enabledModules++;
            }
            else{
                baseRecipe.unregister();
            }
            registerdRecipes = baseRecipe.getRecipes().size() + registerdRecipes;
        }
        plugin.getLogger().info("Registered " + registerdRecipes + " recipes");
        if(enabledModules == baseRecipes.size()){
            plugin.getLogger().info("Recipes go brrrrr..");
        }
    }

    @Override
    public void unregister() {
        super.unregister();
        for (BaseRecipe baseRecipe : getBaseRecipes()) {
            if(baseRecipe.getRecipes().isEmpty()){
                continue;
            }
            baseRecipe.unregister();
        }
    }
    
    public void saveRecipeFile(){
        File file = new File(plugin.getDataFolder(), RECIPE_FILE_NAME);
        if(!file.exists()) {
            plugin.saveResource(RECIPE_FILE_NAME, false);
        }
        try {
            ConfigUpdater.update(plugin, RECIPE_FILE_NAME, file,new ArrayList<>(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        recipeFile = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getRecipeFile() {
        return recipeFile;
    }

    public List<BaseRecipe> getBaseRecipes() {
        if(baseRecipes == null) baseRecipes = new ArrayList<>();
        if(baseRecipes.isEmpty()){
            
            baseRecipes.add(new BackToBlocksSlabs(this));
            baseRecipes.add(new BackToBlocksStairs(this));
            baseRecipes.add(new EasyDispenser(this));
            baseRecipes.add(new EasyRepeaters(this));
            baseRecipes.add(new EasyMinecarts(this));
            baseRecipes.add(new CompactRecipes(this));
            baseRecipes.add(new CompatShulkerRecipe(this));
            baseRecipes.add(new EasyStoneTools(this));
            baseRecipes.add(new MoreStoneCutterRecipes(this));
            baseRecipes.add(new RottenFleshToLeather(this));
            baseRecipes.add(new UniversalDyeing(this));
        }
        return baseRecipes;
    }

    
    


    
}

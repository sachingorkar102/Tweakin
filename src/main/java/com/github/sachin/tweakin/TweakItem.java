package com.github.sachin.tweakin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.sachin.tweakin.utils.ItemBuilder;
import com.google.common.base.Enums;
import com.google.common.base.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public abstract class TweakItem extends BaseTweak {

    private ItemStack item;
    private Set<NamespacedKey> registeredRecipes = new HashSet<>();
    private FileConfiguration recipeConfig;
    

    public TweakItem(Tweakin plugin, String configKey) {
        super(plugin, configKey);
        buildItem();
    }

    @Override
    public void reload() {
        super.reload();
        this.recipeConfig = getTweakManager().getRecipeFile();
    }

    public void buildItem(){
        this.item = ItemBuilder.itemFromFile(getConfig().getConfigurationSection("item"), getName());
    }

    public ItemStack getItem() {
        return item;
    }

    public void registerRecipe(){
        if(!getConfig().getBoolean("craftable",true)) return;
        ConfigurationSection recipes = recipeConfig.getConfigurationSection(getName());
        outer: for(String key : recipes.getKeys(false)){
            List<String> ingredients = recipes.getStringList(key);
            if(ingredients.size() < 3) continue;
            NamespacedKey nKey = new NamespacedKey(getPlugin(), getName()+key);
            ShapedRecipe shapedRecipe = new ShapedRecipe(nKey, item);
            registeredRecipes.add(nKey);
            shapedRecipe.shape("abc","def","ghi");
            char[][] ing = {{'a','b','c'},{'d','e','f'},{'g','h','i'}};
            for (int i =0;i<ingredients.size();i++) {
                String[] a = ingredients.get(i).split("\\|");
                if(a.length != 3) continue outer;
                shapedRecipe.setIngredient(ing[i][0], Enums.getIfPresent(Material.class, a[0].toUpperCase()).or(Material.AIR));
                shapedRecipe.setIngredient(ing[i][1], Enums.getIfPresent(Material.class, a[1].toUpperCase()).or(Material.AIR));
                shapedRecipe.setIngredient(ing[i][2], Enums.getIfPresent(Material.class, a[2].toUpperCase()).or(Material.AIR));
            }
            Bukkit.addRecipe(shapedRecipe);
        }

    }

    public void unregisterRecipe(){
        if(registeredRecipes.isEmpty()) return;
        for (NamespacedKey namespacedKey : registeredRecipes) {
            Bukkit.removeRecipe(namespacedKey);
        }
    }

    public boolean hasItem(Player player, EquipmentSlot slot){
        ItemStack item = player.getInventory().getItem(slot);
        if(item == null) return false;
        return item.isSimilar(getItem());
    }


    
}

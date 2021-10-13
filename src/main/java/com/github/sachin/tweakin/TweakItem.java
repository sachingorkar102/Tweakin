package com.github.sachin.tweakin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.sachin.tweakin.nbtapi.NBTAPI;
import com.github.sachin.tweakin.nbtapi.NBTItem;
import com.github.sachin.tweakin.utils.ItemBuilder;
import com.github.sachin.tweakin.utils.MiscItems;
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
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class TweakItem extends BaseTweak {

    protected ItemStack item;
    private Set<NamespacedKey> registeredRecipes = new HashSet<>();
    private FileConfiguration recipeConfig;
    

    

    public TweakItem(Tweakin plugin, String configKey) {
        super(plugin, configKey);
    }

    @Override
    public void reload() {
        super.reload();
        buildItem(); 
        this.recipeConfig = getTweakManager().getRecipeFile();
    }

    public void buildItem(){
        this.item = ItemBuilder.itemFromFile(plugin.getMiscItems().CONFIG.getConfigurationSection(getName()), getName());
        
    }

    public ItemStack getItem() {
        return item;
    }

    public void registerRecipe(){
        if(!getConfig().getBoolean("craftable",true)) return;
        ConfigurationSection recipes = recipeConfig.getConfigurationSection(getName());
        if(recipes == null){
            getPlugin().getLogger().info("Could not find recipes for "+getName()+" in recipes.yml");
            return;
        }
        if(recipes.getKeys(false) == null){
            getPlugin().getLogger().info("Could not find recipes for "+getName()+" in recipes.yml");
            return;
        }
        outer: for(String key : recipes.getKeys(false)){
            if(recipes.isConfigurationSection(key) && recipes.getConfigurationSection(key).contains("type") && recipes.getConfigurationSection(key).contains("recipe")){
                ConfigurationSection subSection = recipes.getConfigurationSection(key);
                if(subSection.getString("type").equalsIgnoreCase("shapeless")){
                    List<String> ingredients = subSection.getStringList("recipe");
                    NamespacedKey nKey = Tweakin.getKey(getName()+key);
                    ShapelessRecipe recipe = new ShapelessRecipe(nKey,item);
                    registeredRecipes.add(nKey);
                    for(String i : ingredients){
                        recipe.addIngredient(Enums.getIfPresent(Material.class, i.toUpperCase()).or(Material.AIR));
                    }
                    Bukkit.addRecipe(recipe);
                }
            }
            else{
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
        NBTItem nbtItem = new NBTItem(item);
        
        return nbtItem.hasKey(getName());
    }

    public boolean isSimilar(ItemStack item){
        if(item == null) return false;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasKey(getName());
    }

    protected class UsedItem{

        private ItemStack item;
        private ItemMeta meta;

        public UsedItem(ItemStack item){
            this.item = item;
            this.meta = item.getItemMeta();
        }

        public int getUses(){
            if(meta instanceof Damageable){
                Damageable damageable = (Damageable) meta;
                return damageable.getDamage();
            }
            return -1;
        }

        public void setUses(int value){
            if(meta instanceof Damageable){
                Damageable damageable = (Damageable) meta;
                damageable.setDamage(value);
            }
        }

        public void use(){
            if(meta instanceof Damageable){
                Damageable damageable = (Damageable) meta;
                damageable.setDamage(damageable.getDamage()+1);
            }
        }


        public ItemStack getItem() {
            item.setItemMeta(meta);
            return item;
        }

    }


    
}

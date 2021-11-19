package com.github.sachin.tweakin;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.bukkit.Tag;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
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

    public void addRecipe(NamespacedKey key,Recipe recipe) {
        registeredRecipes.add(key);
        try {
            Bukkit.addRecipe(recipe);
        } catch (Exception ignored) {
        }
    }

    public void registerRecipe(){
        if(!getConfig().getBoolean("craftable",true)) return;
        unregisterRecipe();
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
            ConfigurationSection recipe = recipes.getConfigurationSection(key);
            String type = recipe.getString("type","none");
            NamespacedKey recipeKey = Tweakin.getKey(getName()+"_"+key+"_"+type);
            if(type.equals("shaped") && recipe.contains("keys") && recipe.contains("pattern")){
                ShapedRecipe shapedRecipe = new ShapedRecipe(recipeKey, item.clone());    
                List<String> patternList = recipe.getStringList("pattern");
                boolean invalidPattern = false;
                if(patternList.size()>3) invalidPattern = true;
                for(String s : patternList){
                    if(s.length()>3){
                        invalidPattern = true;
                    }
                }
                if(invalidPattern) continue outer;
                shapedRecipe.shape(patternList.toArray(new String[0]));
                for(String ing : recipe.getConfigurationSection("keys").getKeys(false)){
                    RecipeChoice choice = getIngredient(recipe.getString("keys."+ing), recipe.getBoolean("exact",false));
                    if(choice != null){
                        shapedRecipe.setIngredient(ing.charAt(0), choice);
                    }
                }
                addRecipe(recipeKey, shapedRecipe);
            }
            else if(type.equals("shapeless") && recipe.contains("ingredients")){
                ShapelessRecipe shapelessRecipe = new ShapelessRecipe(recipeKey, item.clone());
                
                for(String ing : recipe.getStringList("ingredients")){
                    RecipeChoice choice = getIngredient(ing, recipe.getBoolean("exact",false));
                    if(choice != null){
                        shapelessRecipe.addIngredient(choice);
                    }
                }
                addRecipe(recipeKey, shapelessRecipe);
            }
        }

    }

    public static RecipeChoice getIngredient(String str,boolean exact){
        if(str == null) return null;
        if(str.contains("|")){
            List<String> l = Arrays.asList(str.split("\\|"));
            List<Material> mats = new ArrayList<>();
            for(String s : l){
                Optional<Material> opMat2 = Enums.getIfPresent(Material.class, s.toUpperCase());
                if(opMat2.isPresent()){

                    mats.add(opMat2.get());
                }
            }
            return new MaterialChoice(mats);
        }
        Optional<Material> opMat = Enums.getIfPresent(Material.class, str.toUpperCase());
        if(opMat.isPresent()){
            if(exact){
                return new ExactChoice(new ItemStack(opMat.get()));
            }
            else{
                return new MaterialChoice(opMat.get());
            }
        }
        Tag<Material> tag = Bukkit.getTag("blocks", NamespacedKey.minecraft(str.toLowerCase()), Material.class);
        if(tag != null){
            List<ItemStack> items = new ArrayList<>();
            List<Material> mats = new ArrayList<>();
            for(Material m : tag.getValues()){
                items.add(new ItemStack(m));
                mats.add(m);
            }
            if(exact){
                return new ExactChoice(items);
            }
            else{
                return new MaterialChoice(mats);
            }
        }
        
        return null;
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

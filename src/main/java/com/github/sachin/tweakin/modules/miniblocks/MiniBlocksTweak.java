package com.github.sachin.tweakin.modules.miniblocks;

import com.github.sachin.prilib.utils.FastItemStack;
import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.ConfigUpdater;
import com.github.sachin.tweakin.utils.TConstants;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.StonecuttingRecipe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Tweak(name = "mini-blocks")
public class MiniBlocksTweak extends BaseTweak {

    private FileConfiguration miniblockConfig;
    private Map<String, StonecuttingRecipe> recipeMap = new HashMap<>();

    @Override
    public void reload() {
        super.reload();
        this.miniblockConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(),TConstants.MINI_BLOCKS_FILE));
        if(shouldEnable()){
            registerRecipes();
        }
    }

    @Override
    public void register() {
        super.register();
        File file = new File(plugin.getDataFolder(), TConstants.MINI_BLOCKS_FILE);
        if(!file.exists()){
            plugin.saveResource(TConstants.MINI_BLOCKS_FILE, false);
        }
        try {
            ConfigUpdater.update(plugin, TConstants.MINI_BLOCKS_FILE, file, new ArrayList<>(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.miniblockConfig = YamlConfiguration.loadConfiguration(file);
//        registerRecipes();


    }
    @Override
    public void unregister() {
        super.unregister();
        plugin.getLogger().info("Unregistered "+recipeMap.size()+" recipes from mini-blocks tweak");
        unregisterRecipes();
    }

    private void registerRecipes(){
        unregisterRecipes();
        for(String key : miniblockConfig.getKeys(false)) {
            ConfigurationSection section = miniblockConfig.getConfigurationSection(key);
            if(!section.getBoolean("enabled",true)) continue;
            try {
                String texture = TConstants.INIT_HEAD_TEXTURE_VALUE+section.getString("texture").replace(TConstants.INIT_HEAD_TEXTURE_VALUE,"");
                String display = section.getString("display");
                Material ingredient = Material.getMaterial(section.getString("ingredient"));
                int count = section.getInt("count");
                FastItemStack head = new FastItemStack(Material.PLAYER_HEAD).setHeadTexture(texture).setDisplay(ChatColor.WHITE+display).setAmount(count);
                StonecuttingRecipe recipe = new StonecuttingRecipe(new NamespacedKey(plugin,"mini_blocks_"+key.toLowerCase()),head.get(),ingredient);
                Bukkit.addRecipe(recipe);
                recipeMap.put(key,recipe);
            } catch (Exception e) {
                plugin.getLogger().severe("Could not load mini-block recipe for for " + key);
                e.printStackTrace();
            }
        }
        plugin.getLogger().info("Registered "+recipeMap.size()+" recipes from mini-blocks tweak");
    }

    private void unregisterRecipes(){
        if(recipeMap.isEmpty()) return;
        for(StonecuttingRecipe recipe : recipeMap.values()){
            Bukkit.removeRecipe(recipe.getKey());
        }
        recipeMap.clear();
    }

    public Map<String, StonecuttingRecipe> getRecipeMap() {
        return recipeMap;
    }
}

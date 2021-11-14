package com.github.sachin.tweakin.modules.morerecipes.recipes;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.morerecipes.BaseRecipe;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class EasyMinecarts extends BaseRecipe implements Listener{

    public EasyMinecarts(MoreRecipesTweak instance) {
        super("easy-minecarts", instance);
    }

    @Override
    public void register() {
        NamespacedKey chestKey = Tweakin.getKey(name+"_chest_minecart");
        NamespacedKey furnaceKey = Tweakin.getKey(name+"_furnace_minecart");
        NamespacedKey tntKey = Tweakin.getKey(name+"_tnt_minecart");
        NamespacedKey hopperKey = Tweakin.getKey(name+"_hopper_minecart");
        String shape1 = "   ";
        String shape2 = "ISI";
        String shape3 = "III";
        ShapedRecipe chestMinecart = new ShapedRecipe(chestKey, new ItemStack(Material.CHEST_MINECART)).shape(shape1, shape2, shape3).setIngredient('I', Material.IRON_INGOT).setIngredient('S', Material.CHEST);
        ShapedRecipe furnaceMinecart = new ShapedRecipe(furnaceKey, new ItemStack(Material.FURNACE_MINECART)).shape(shape1, shape2, shape3).setIngredient('I', Material.IRON_INGOT).setIngredient('S', Material.FURNACE);
        ShapedRecipe tntMinecart = new ShapedRecipe(tntKey, new ItemStack(Material.TNT_MINECART)).shape(shape1, shape2, shape3).setIngredient('I', Material.IRON_INGOT).setIngredient('S', Material.TNT);
        ShapedRecipe hopperMinecart = new ShapedRecipe(hopperKey, new ItemStack(Material.HOPPER_MINECART)).shape(shape1, shape2, shape3).setIngredient('I', Material.IRON_INGOT).setIngredient('S', Material.HOPPER);
        addRecipe(chestKey,  chestMinecart);
        addRecipe(furnaceKey,  furnaceMinecart);
        addRecipe(tntKey,  tntMinecart);
        addRecipe(hopperKey,  hopperMinecart);
    }


    
}

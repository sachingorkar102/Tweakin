package com.github.sachin.tweakin.modules.morerecipes.recipes;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.morerecipes.BaseRecipe;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Arrays;
import java.util.List;

public class EasyStoneTools extends BaseRecipe{


    private final List<Material> STONE_MATERIALS = Arrays.asList(
        Material.ANDESITE,
        Material.DIORITE,
        Material.GRANITE,
        Material.COBBLESTONE,
        Material.STONE
        );

    public EasyStoneTools(MoreRecipesTweak instance) {
        super("easy-stone-tools", instance);

    }

    @Override
    public void register() {
        NamespacedKey pickAxeKey = Tweakin.getKey(this.name+"_"+"stone_pickaxe");
        ShapedRecipe pickAxe = new ShapedRecipe(pickAxeKey,new ItemStack(Material.STONE_PICKAXE)).shape("SSS"," T "," T ").setIngredient('S',new MaterialChoice(STONE_MATERIALS)).setIngredient('T', Material.STICK);
        addRecipe(pickAxeKey, pickAxe);

        NamespacedKey axeKey = Tweakin.getKey(this.name+"_"+"stone_axe");
        ShapedRecipe axe = new ShapedRecipe(axeKey,new ItemStack(Material.STONE_AXE)).shape("SS ","ST "," T ").setIngredient('S',new MaterialChoice(STONE_MATERIALS)).setIngredient('T', Material.STICK);
        addRecipe(axeKey, axe);

        NamespacedKey shovelKey = Tweakin.getKey(this.name+"_"+"stone_shovel");
        ShapedRecipe shovel = new ShapedRecipe(shovelKey,new ItemStack(Material.STONE_SHOVEL)).shape(" S "," T "," T ").setIngredient('S',new MaterialChoice(STONE_MATERIALS)).setIngredient('T', Material.STICK);
        addRecipe(shovelKey, shovel);

        NamespacedKey hoeKey = Tweakin.getKey(this.name+"_"+"stone_hoe");
        ShapedRecipe hoe = new ShapedRecipe(hoeKey,new ItemStack(Material.STONE_HOE)).shape("SS "," T "," T ").setIngredient('S',new MaterialChoice(STONE_MATERIALS)).setIngredient('T', Material.STICK);
        addRecipe(hoeKey, hoe);

        NamespacedKey swordKey = Tweakin.getKey(this.name+"_"+"stone_sword");
        ShapedRecipe sword = new ShapedRecipe(swordKey,new ItemStack(Material.STONE_SWORD)).shape(" S "," S "," T ").setIngredient('S',new MaterialChoice(STONE_MATERIALS)).setIngredient('T', Material.STICK);
        addRecipe(swordKey, sword);

    }
    
}

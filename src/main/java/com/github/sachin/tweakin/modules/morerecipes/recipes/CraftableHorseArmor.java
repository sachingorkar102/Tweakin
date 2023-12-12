package com.github.sachin.tweakin.modules.morerecipes.recipes;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.morerecipes.BaseRecipe;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class CraftableHorseArmor extends BaseRecipe{

    public CraftableHorseArmor(MoreRecipesTweak instance) {
        super("craftable-horse-armor", instance);
    }

    @Override
    public void register() {
        addArmorRecipe(Material.DIAMOND, Material.DIAMOND_HORSE_ARMOR);
        addArmorRecipe(Material.GOLD_INGOT, Material.GOLDEN_HORSE_ARMOR);
        addArmorRecipe(Material.IRON_INGOT, Material.IRON_HORSE_ARMOR);
        addArmorRecipe(Material.LEATHER, Material.LEATHER_HORSE_ARMOR);
    }

    private void addArmorRecipe(Material ing,Material armor){
        NamespacedKey key = Tweakin.getKey(this.name+"-"+armor.name().toLowerCase());
        ShapedRecipe recipe = new ShapedRecipe(key, new ItemStack(armor)).shape("  I","III","ISI").setIngredient('S', Material.SADDLE).setIngredient('I', ing);
        addRecipe(key, recipe);
    }
    
}

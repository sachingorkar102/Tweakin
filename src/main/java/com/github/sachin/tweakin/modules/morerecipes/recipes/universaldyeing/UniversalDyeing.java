package com.github.sachin.tweakin.modules.morerecipes.recipes.universaldyeing;

import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.morerecipes.BaseRecipe;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.List;

public class UniversalDyeing extends BaseRecipe{

    private static final List<Material> CONCRETES = new ArrayList<>();
    private static final List<Material> CONCRETE_POWDERS = new ArrayList<>();
    private static final List<Material> TERRACOTTAS = new ArrayList<>();
    private static final List<Material> stainedGlasses = new ArrayList<>();
    private static final List<Material> STAINED_GLASS_PANES = new ArrayList<>();

    private static final List<Material> CANDLES = new ArrayList<>();

    static{
        for(Dye dye : Dye.values()){
            CONCRETES.add(dye.concrete);
            CONCRETE_POWDERS.add(dye.concretePowder);
            TERRACOTTAS.add(dye.terracotta);
            stainedGlasses.add(dye.stainedGlass);
            STAINED_GLASS_PANES.add(dye.stainedGlassPane);
            CANDLES.add(dye.candle);
        }
    }
    
    public UniversalDyeing(MoreRecipesTweak instance) {
        super("universal-dyeing", instance);
    }
    
    
    @Override
    public void register() {
        ConfigurationSection config = instance.getRecipeFile().getConfigurationSection(this.name);
        
        for(Dye dye : Dye.values()){
            // singles
            if(config.getBoolean("wool.single")){
                addSingleShapelessRecipe(dye.dye, dye.wool, new ArrayList<>(Tag.WOOL.getValues()));
            }
            if(config.getBoolean("concrete.single")){
                addSingleShapelessRecipe(dye.dye, dye.concrete, new ArrayList<>(CONCRETES));
            }
            if(config.getBoolean("concrete_powder.single")){
                addSingleShapelessRecipe(dye.dye, dye.concretePowder, new ArrayList<>(CONCRETE_POWDERS));
            }
            if(config.getBoolean("terracotta.single")){
                addSingleShapelessRecipe(dye.dye, dye.terracotta, new ArrayList<>(TERRACOTTAS));
            }
            if(config.getBoolean("stained_glass.single")){
                addSingleShapelessRecipe(dye.dye, dye.stainedGlass, new ArrayList<>(stainedGlasses));
            }
            if(config.getBoolean("stained_glass_pane.single")){
                addSingleShapelessRecipe(dye.dye, dye.stainedGlassPane, new ArrayList<>(STAINED_GLASS_PANES));
            }
            if(config.getBoolean("bed.single")){
                addSingleShapelessRecipe(dye.dye, dye.bed, new ArrayList<>(Tag.BEDS.getValues()));
            }
            if(config.getBoolean("carpet.single")){
                addSingleShapelessRecipe(dye.dye, dye.carpet, new ArrayList<>(Tag.CARPETS.getValues()));
            }
            if(config.getBoolean("candle.single")){
                addSingleShapelessRecipe(dye.dye,dye.candle,new ArrayList<>(Tag.CANDLES.getValues()));
            }


            // sorounded
            if(config.getBoolean("wool.sorounded")){
                addSoroundedShapedRecipe(dye.dye, dye.wool, new ArrayList<>(Tag.WOOL.getValues()));
            }
            if(config.getBoolean("concrete.sorounded")){
                addSoroundedShapedRecipe(dye.dye, dye.concrete, new ArrayList<>(CONCRETES));
            }
            if(config.getBoolean("concrete_powder.sorounded")){
                addSoroundedShapedRecipe(dye.dye, dye.concretePowder, new ArrayList<>(CONCRETE_POWDERS));
            }
            if(config.getBoolean("terracotta.sorounded")){
                addSoroundedShapedRecipe(dye.dye, dye.terracotta, new ArrayList<>(TERRACOTTAS));
            }
            if(config.getBoolean("stained_glass.sorounded")){
                addSoroundedShapedRecipe(dye.dye, dye.stainedGlass, new ArrayList<>(stainedGlasses));
            }
            if(config.getBoolean("stained_glass_pane.sorounded")){
                addSoroundedShapedRecipe(dye.dye, dye.stainedGlassPane, new ArrayList<>(STAINED_GLASS_PANES));
            }
            if(config.getBoolean("bed.sorounded")){
                addSoroundedShapedRecipe(dye.dye, dye.bed, new ArrayList<>(Tag.BEDS.getValues()));
            }
            if(config.getBoolean("carpet.sorounded")){
                addSoroundedShapedRecipe(dye.dye, dye.carpet, new ArrayList<>(Tag.CARPETS.getValues()));
            }
            if(config.getBoolean("candle.sorounded")){
                addSoroundedShapedRecipe(dye.dye,dye.candle,new ArrayList<>(Tag.CANDLES.getValues()));
            }

        }
    }


    private void addSingleShapelessRecipe(Material dye,Material result,List<Material> ing){
        NamespacedKey key = Tweakin.getKey(this.name+"_"+dye.toString().toLowerCase()+"_"+result.toString().toLowerCase()+"_single");
        ing.remove(result);
        ShapelessRecipe recipe = new ShapelessRecipe(key,new ItemStack(result)).addIngredient(dye).addIngredient(new MaterialChoice(ing));
        addRecipe(key, recipe);
    }

    private void addSoroundedShapedRecipe(Material dye,Material result,List<Material> ing){
        NamespacedKey key = Tweakin.getKey(this.name+"_"+dye.toString().toLowerCase()+"_"+result.toString().toLowerCase()+"_sorounded");
        ing.remove(result);
        ShapedRecipe recipe = new ShapedRecipe(key,new ItemStack(result,8)).shape("XXX", "XYX", "XXX").setIngredient('Y', dye).setIngredient('X', new MaterialChoice(ing));
        addRecipe(key, recipe);
    }
    
}

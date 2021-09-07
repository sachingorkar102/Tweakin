package com.github.sachin.tweakin.betterarmorstands;

import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;

public class ArmorStandWandItem extends TweakItem{

    private BetterArmorStandTweak instance;

    public ArmorStandWandItem(Tweakin plugin,BetterArmorStandTweak instance) {
        super(plugin, "armorstand-wand");
        this.instance = instance;
    }


    @Override
    public void register() {
        super.register();
        registerRecipe();
    }

    @Override
    public void unregister() {
        super.unregister();
        unregisterRecipe();
    }
    
}

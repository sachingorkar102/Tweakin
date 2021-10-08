package com.github.sachin.tweakin.modules.bettersignedit;

import com.github.sachin.tweakin.BaseFlag;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;

public class BSEFlag extends BaseFlag{

    public BSEFlag(Tweakin plugin) {
        super(plugin, TConstants.BSE_FLAG);
        register();
    }



}
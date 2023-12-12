package com.github.sachin.tweakin.modules.shearitemframe;

import com.github.sachin.tweakin.BaseFlag;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;

public class SIFFlag extends BaseFlag{

    public SIFFlag(Tweakin plugin) {
        super(plugin, TConstants.SIF_FLAG);
        register();
    }
    
}

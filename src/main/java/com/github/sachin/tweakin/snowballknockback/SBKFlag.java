package com.github.sachin.tweakin.snowballknockback;

import com.github.sachin.tweakin.BaseFlag;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;

public class SBKFlag extends BaseFlag{

    public SBKFlag(Tweakin plugin) {
        super(plugin, TConstants.SBK_FLAG);
        register();
    }
    
}

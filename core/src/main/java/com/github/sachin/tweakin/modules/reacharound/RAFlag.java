package com.github.sachin.tweakin.modules.reacharound;

import com.github.sachin.tweakin.BaseFlag;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;

public class RAFlag extends BaseFlag {


    public RAFlag(Tweakin plugin) {
        super(plugin, TConstants.RA_FLAG);
        register();
    }
}

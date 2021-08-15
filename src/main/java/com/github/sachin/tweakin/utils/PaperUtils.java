package com.github.sachin.tweakin.utils;

import com.destroystokyo.paper.entity.ai.MobGoals;
import com.destroystokyo.paper.entity.ai.VanillaGoal;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;

public class PaperUtils {

    public static void removePanicGoal(Entity entity){
        Bukkit.getMobGoals().removeGoal((Creature)entity, VanillaGoal.PANIC);
    }
    
}

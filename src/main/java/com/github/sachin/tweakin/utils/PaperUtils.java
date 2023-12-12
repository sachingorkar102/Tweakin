package com.github.sachin.tweakin.utils;

import com.destroystokyo.paper.entity.ai.MobGoals;
import com.destroystokyo.paper.entity.ai.VanillaGoal;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class PaperUtils {

    public static void removePanicGoal(Entity entity){
        try {
            Method method = Class.forName("org.bukkit.Bukkit").getMethod("getMobGoals");
            ((MobGoals)method.invoke(null)).removeGoal(((Creature)entity), VanillaGoal.PANIC);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            
        }
    }
    
}

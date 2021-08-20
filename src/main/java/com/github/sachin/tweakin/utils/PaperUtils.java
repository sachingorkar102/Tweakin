package com.github.sachin.tweakin.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.destroystokyo.paper.entity.ai.MobGoals;
import com.destroystokyo.paper.entity.ai.VanillaGoal;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;


public class PaperUtils {

    public static void removePanicGoal(Entity entity){
        try {
<<<<<<< HEAD
=======
            
>>>>>>> b4d7df9fe24db8bf1614bbc05a5429f8a82571e0
            Method method = Class.forName("org.bukkit.Bukkit").getMethod("getMobGoals");
            ((MobGoals)method.invoke(null)).removeGoal(((Creature)entity), VanillaGoal.PANIC);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            
        }
    }
    
}

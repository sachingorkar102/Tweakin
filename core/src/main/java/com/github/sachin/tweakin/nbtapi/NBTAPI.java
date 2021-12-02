package com.github.sachin.tweakin.nbtapi;

import com.github.sachin.tweakin.nbtapi.nms.NMSHelper;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;

public class NBTAPI {

    private static NBTAPI instance;
    private NMSHelper NMSHelper;

    public NBTAPI(){
        instance = this;
    }

    public boolean loadVersions(@Nonnull JavaPlugin plugin,String version){

        try {
            //abstractNmsHandler = (AbstractNMSHandler) Class.forName(packageName + ".internal.nms." + internalsName + ".NMSHandler").newInstance();
            NMSHelper = (NMSHelper) Class.forName("com.github.sachin.tweakin.nms."+version+".NMSHandler").getDeclaredConstructor().newInstance(null);
            return true;
        } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException | NoSuchMethodException | InvocationTargetException exception) {
//            plugin.getLogger().severe("The included JeffLib version (" + version + ")does not fully support the Minecraft version you are currently running:");
            exception.printStackTrace();
            return false;
        }
        
    }

    public NMSHelper getNMSHelper() {
        return NMSHelper;
    }

    public static NBTAPI getInstance() {
        return instance;
    }

}

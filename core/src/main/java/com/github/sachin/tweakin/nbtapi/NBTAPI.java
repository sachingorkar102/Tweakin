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

    public boolean loadVersions(@Nonnull JavaPlugin plugin,String version,String mcVersion){
        String newVersion = version;
        if(mcVersion.equals("1.19")){
            newVersion = "v1_19_R1";
        }
        else if(mcVersion.equals("1.19.1") || mcVersion.equals("1.19.2")){
            newVersion = "v1_19_R11";
        }
        try {
            NMSHelper = (NMSHelper) Class.forName("com.github.sachin.tweakin.nms."+newVersion+".NMSHandler").getDeclaredConstructor().newInstance(null);
            return true;
        } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException | NoSuchMethodException | InvocationTargetException exception) {
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

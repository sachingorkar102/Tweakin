package com.github.sachin.tweakin;

import co.aikar.commands.BaseCommand;
import com.github.sachin.tweakin.compat.TeaksTweaksCompat;
import com.github.sachin.tweakin.manager.TweakManager;
import com.github.sachin.tweakin.utils.TConstants;
import com.github.sachin.tweakin.utils.annotations.Config;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.permissions.Permission;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * When inheriting BaseTweak, remember to add a @Tweak annotation above your tweak class
 */
public abstract class BaseTweak {

    protected final Tweakin plugin;
    private TweakManager tweakManager;


    private boolean shouldEnable;
    private ConfigurationSection config;
    private final String configKey;
    public boolean registered;


    public BaseTweak(){
        this.plugin = Tweakin.getPlugin();
        this.configKey = getClass().getAnnotation(Tweak.class).name();
        load();
    }

    protected void load(){
        this.tweakManager = plugin.getTweakManager();
        this.config = plugin.getConfig().getConfigurationSection(configKey);
        if(config == null || !config.contains("enabled")){
            plugin.getLogger().info("Could not found config section for "+configKey+", ignoring the tweak module..");
            this.shouldEnable = false;
            return;
        }
        this.onLoad();
        this.shouldEnable = config.getBoolean("enabled",true);
    }


    public void reload() {
        this.config = plugin.getConfig().getConfigurationSection(configKey);
        this.shouldEnable = config.getBoolean("enabled");

//        reloads the field having @Config annotation
        for(Field field : getClass().getDeclaredFields()){
            Config ann = field.getDeclaredAnnotation(Config.class);
            if(ann == null) continue;
            if(!config.contains(ann.key())) continue;
            try {
                field.setAccessible(true);
                Object value = config.get(ann.key(),field.get(this));

                field.set(this,value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        String clashingTT = getClass().getAnnotation(Tweak.class).clashingTeaksTweak();
        if(this.shouldEnable && !clashingTT.equalsIgnoreCase("") && TeaksTweaksCompat.isEnabled){
            if(TeaksTweaksCompat.isPackEnabled(clashingTT)){
                plugin.getServer().getConsoleSender().sendMessage(TConstants.PREFIX+ChatColor.RED+"TeaksTweaks has already enabled the tweak "+clashingTT+", cannot enable "+getName()+" from Tweakin");
                this.shouldEnable = false;
            }
        }
    }

    public BaseTweak getInstance(){
        return this;
    }



    public TweakManager getTweakManager() {
        return tweakManager;
    }

    public boolean shouldEnable() {
        return shouldEnable;
    }

    public boolean isPluginEnabled(String name){
        return plugin.getServer().getPluginManager().isPluginEnabled(name);
    }


    public ConfigurationSection getConfig() {
        return config;
    }

    public String getName() {
        return configKey;
    }

    public List<String> getBlackListWorlds(){
        if(config.contains("black-list-worlds")){
            List<String> list = config.getStringList("black-list-worlds");
            if(list != null){
                return list;
            }
        }
        return new ArrayList<>();
    }

    public boolean containsWorld(World world){
        return containsWorld(world.getName());
    }

    public boolean containsWorld(String worldName){
        return getBlackListWorlds().contains(worldName);
    }


    public boolean hasPermission(Player player, Permission permission){
        if(config.getBoolean("check-permissions",true)){
            return player.hasPermission(permission);
        }
        else{
            return true;
        }
    }


    public boolean matchString(String str,List<String> matcher){
        for(String s : matcher){
            if(s.startsWith("^") && str.startsWith(s.replace("^", ""))){
                return true;
            }
            if(s.endsWith("$") && str.endsWith(s.replace("$", ""))){
                return true;
            }
            if(str.equals(s)) return true;

        }
        return false;
    }

    public void swingHand(EquipmentSlot hand,Player player){
        if(hand==EquipmentSlot.HAND){
            player.swingMainHand();
        }
        else if(hand==EquipmentSlot.OFF_HAND){
            player.swingOffHand();
        }
    }

    public boolean matchTag(Material mat,List<String> matcher){
        for(String s : matcher){
            Tag<Material> tag = Bukkit.getTag("blocks", NamespacedKey.minecraft(s.toLowerCase()),Material.class);
            if(tag != null && tag.getValues().contains(mat)){
                return true;
            }
        }
        return false;
    }




    protected void registerEvents(Listener listener) {
        this.plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    protected void unregisterEvents(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    protected void registerCommands(BaseCommand command){
        plugin.getCommandManager().registerCommand(command);
    }

    protected void unregisterCommands(BaseCommand command){
        plugin.getCommandManager().unregisterCommand(command);
    }

    public Tweakin getPlugin() {
        return plugin;
    }

    public void register(){
        if(this instanceof Listener){
            registerEvents((Listener)this);
        }
        registered = true;
    }

    public void unregister(){
        if(this instanceof Listener){
            unregisterEvents((Listener)this);
        }
        registered = false;
    }


    public void onDisable(){}

    public void onLoad(){}
}

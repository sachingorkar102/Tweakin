package com.github.sachin.tweakin.manager;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Message;
import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.autorecipeunlock.AutoRecipeUnlockTweak;
import com.github.sachin.tweakin.betterelytrarocket.BetterElytraRocketTweak;
import com.github.sachin.tweakin.betterflee.AnimalFleeTweak;
import com.github.sachin.tweakin.betterladder.BetterLadderTweak;
import com.github.sachin.tweakin.bettersignedit.BetterSignEditTweak;
import com.github.sachin.tweakin.bossspawnsounds.BroadCastSoundTweak;
import com.github.sachin.tweakin.bottledcloud.BottledCloudItem;
import com.github.sachin.tweakin.burnvinetip.BurnVineTipTweak;
import com.github.sachin.tweakin.compassworkeverywhere.CompassEveryWhereTweak;
import com.github.sachin.tweakin.confetticreepers.ConfettiCreepers;
import com.github.sachin.tweakin.controlledburn.ControlledBurnTweak;
import com.github.sachin.tweakin.coordinatehud.CoordinateHUDTweak;
import com.github.sachin.tweakin.customportals.CustomPortalTweak;
import com.github.sachin.tweakin.fastleafdecay.FastLeafDecayTweak;
import com.github.sachin.tweakin.hoeharvesting.HoeHarvestingTweak;
import com.github.sachin.tweakin.infinitybucket.InfinityWaterBucketTweak;
import com.github.sachin.tweakin.lapisintable.LapisInTableTweak;
import com.github.sachin.tweakin.lavabucketcan.LavaBucketTrashCan;
import com.github.sachin.tweakin.netherportalcoords.NetherPortalCoordsTweak;
import com.github.sachin.tweakin.noteblock.NoteBlockHeadsTweak;
import com.github.sachin.tweakin.patdogs.PatDogTweak;
import com.github.sachin.tweakin.poisonpotatousage.PoisonPotatoUsageTweak;
import com.github.sachin.tweakin.reacharound.ReachAroundTweak;
import com.github.sachin.tweakin.rightclickarmor.RightClickArmor;
import com.github.sachin.tweakin.rightclickshulker.RightClickShulkerBox;
import com.github.sachin.tweakin.rotationwrench.RotationWrenchItem;
import com.github.sachin.tweakin.shearitemframe.ShearItemFrameTweak;
import com.github.sachin.tweakin.silencemobs.SilenceMobsTweak;
import com.github.sachin.tweakin.slimebucket.SlimeInBucket;
import com.github.sachin.tweakin.snowballknockback.SnowBallKnockBackTweak;
import com.github.sachin.tweakin.swingthroughgrass.SwingThroughGrassTweak;
import com.github.sachin.tweakin.trowel.TrowelItem;
import com.github.sachin.tweakin.utils.ConfigUpdater;
import com.github.sachin.tweakin.villagerfollowemerald.VillagerFollowEmraldTweak;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TweakManager {

    private final Tweakin plugin;
    private Message messageManager;
    private List<BaseTweak> tweakList = new ArrayList<>();
    private List<TweakItem> registeredItems = new ArrayList<>();
    private Map<BaseTweak,Boolean> guiMap = new HashMap<>();
    private FileConfiguration recipeConfig;
    


    public TweakManager(Tweakin plugin){
        this.plugin = plugin;
    }

    public void load(){
        plugin.getLogger().info("Loading tweakin...");
        reload(false);
    }

    public void reload(){
        plugin.getLogger().info("Reloading tweakin...");
        reload(true);
    }

    private void reload(boolean unregister){
        plugin.saveDefaultConfig();
        if(!registeredItems.isEmpty()){
            registeredItems.clear();
        }
        int registered = 0;
        File configFile = new File(plugin.getDataFolder(),"config.yml");
        File recipeFile = new File(plugin.getDataFolder(),"recipes.yml");
        plugin.reloadMiscItems();
        if(!recipeFile.exists()){
            plugin.saveResource("recipes.yml", false);
        }
        this.recipeConfig = YamlConfiguration.loadConfiguration(recipeFile);
        try {
            ConfigUpdater.update(plugin, "recipes.yml", recipeFile, new ArrayList<>(),unregister);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            ConfigUpdater.update(plugin, "config.yml", configFile, new ArrayList<>(),unregister);
        } catch (IOException e) {
            e.printStackTrace();
        }
        plugin.reloadConfig();
        FirstInstallListener listener = new FirstInstallListener();
        if(plugin.isFirstInstall){
            sendConsoleMessage("&a-----------Tweakin------------");
            sendConsoleMessage("&eThank you for installing &6Tweakin!!");
            sendConsoleMessage("&eTweakin is installed on server for the first time..");
            sendConsoleMessage("&e&lAll tweaks are disabled by default, they can be enabled by using &6&l/tweakin toggle &e&lingame or &6&l/tweakin toggle [tweak-name] &e&lin console");
            sendConsoleMessage("&a------------------------------");
            if(!unregister){
                plugin.getServer().getPluginManager().registerEvents(listener, plugin);
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if(p.isOp()){
                        sendFirstInstallMessage(p);
                        listener.flaggedPlayers.add(p.getUniqueId());
                    }
                });
            }

        }
        this.messageManager = new Message(plugin);
        messageManager.reload();
        for (BaseTweak t : getTweakList()) {
            try {
                t.reload();
                if(unregister){
    
                    if(t.registered){
                        t.unregister();
                    }
                }
                if(t.shouldEnable()){
                    t.register();
                    if(t instanceof TweakItem){
                        registeredItems.add((TweakItem)t);
                    }
                    registered++;
                }
                guiMap.put(t, t.shouldEnable());
            } catch (Exception e) {
                plugin.getLogger().info("Error occured while registering "+t.getName()+" tweak..");
                plugin.getLogger().info("Report this error on discord or at spigot page in discussion section.");
                e.printStackTrace();
                
            }
        }
        if(registered == 0 && !plugin.isFirstInstall){
            if(plugin.getConfig().getBoolean("op-notifications",true)){
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if(p.isOp()){
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a-----------Tweakin------------"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eAll tweaks seems to be disabled, use &6/tw toggle &eor &6/tw &6toggle [tweak-name]"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eto enable some!!"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDo not use &6config.yml &cto toggle tweaks"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a------------------------------"));
                    }
                });
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a-----------Tweakin------------\n&eAll tweaks seems to be disabled, use &6/tw toggle &eor &6/tw &6toggle [tweak-name]\n&eto enable some!!\n&cDo not use &6config.yml &cto toggle tweaks\n&a------------------------------"));
            }
        }
        plugin.getLogger().info("Registered "+registered+" tweaks successfully");
        Bukkit.getOnlinePlayers().forEach(p -> p.updateCommands());
    }
    public List<BaseTweak> getTweakList() {
        if(tweakList.isEmpty()){
            tweakList.add(new RightClickArmor(plugin));
            tweakList.add(new RightClickShulkerBox(plugin));
            tweakList.add(new NoteBlockHeadsTweak(plugin));

            tweakList.add(new ReachAroundTweak(plugin));
            tweakList.add(new FastLeafDecayTweak(plugin));
            tweakList.add(new BetterLadderTweak(plugin));
            tweakList.add(new LapisInTableTweak(plugin));
            tweakList.add(new CustomPortalTweak(plugin));
            // tweakList.add(new ControlledBurnTweak(plugin)); work in progress
            tweakList.add(new AutoRecipeUnlockTweak(plugin));
            tweakList.add(new NetherPortalCoordsTweak(plugin));
            tweakList.add(new SwingThroughGrassTweak(plugin));
            tweakList.add(new CoordinateHUDTweak(plugin));
            tweakList.add(new PoisonPotatoUsageTweak(plugin));
            tweakList.add(new BurnVineTipTweak(plugin));
            tweakList.add(new SilenceMobsTweak(plugin));
            tweakList.add(new RotationWrenchItem(plugin));
            tweakList.add(new BottledCloudItem(plugin));
            tweakList.add(new TrowelItem(plugin));
            tweakList.add(new ConfettiCreepers(plugin));
            tweakList.add(new LavaBucketTrashCan(plugin));
            tweakList.add(new SlimeInBucket(plugin));
            // tweakList.add(new BetterElytraRocketTweak(plugin)); could not done
            tweakList.add(new PatDogTweak(plugin));
            tweakList.add(new CompassEveryWhereTweak(plugin));
            if(plugin.isProtocolLibEnabled){
                tweakList.add(new BroadCastSoundTweak(plugin));
                tweakList.add(new BetterSignEditTweak(plugin));
            }
            else{
                plugin.getLogger().info("ProtocolLib not found,ignoring boss-spawn-sounds and better-sign-edit...");
            }
            tweakList.add(new HoeHarvestingTweak(plugin));
            tweakList.add(new VillagerFollowEmraldTweak(plugin));
            tweakList.add(new AnimalFleeTweak(plugin));
            tweakList.add(new ShearItemFrameTweak(plugin));
            tweakList.add(new SnowBallKnockBackTweak(plugin));
            tweakList.add(new InfinityWaterBucketTweak(plugin));
        }
        return tweakList;
    }

    private void sendConsoleMessage(String message){
        plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendFirstInstallMessage(Player player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a-----------Tweakin------------"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eThank you for installing &6Tweakin!!"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eTweakin is installed on server for the first time.."));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lAll tweaks are disabled by default, they can be enabled by using &6&l/tweakin toggle &e&lingame or &6&l/tweakin toggle [tweak-name] &e&lin console"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a------------------------------"));
    }

    public Message getMessageManager() {
        return messageManager;
    }

    public List<TweakItem> getRegisteredItems() {
        return registeredItems;
    }

    public Map<BaseTweak, Boolean> getGuiMap() {
        return guiMap;
    }

    public List<String> getTweakNames(){
        List<String> list = new ArrayList<>();
        for(BaseTweak t : getTweakList()){
            list.add(t.getName());
        }
        return list;
    }



    public List<String> getRegisteredItemNames(){
        List<String> list = new ArrayList<>();
        for (TweakItem i : registeredItems) {
            list.add(i.getName());
        }
        return list;
    }

    public TweakItem getTweakItem(String name){
        for (TweakItem tweakItem : registeredItems) {
            if(tweakItem.getName().equals(name)){
                return tweakItem;
            }
        }
        return null;
    }

    public BaseTweak getTweakFromName(String name){
        for(BaseTweak tweak : getTweakList()){
            if(tweak.getName().equals(name)){
                return tweak;
            }
        }
        return null;
    }

    public FileConfiguration getRecipeFile(){
        return recipeConfig;
    }
}



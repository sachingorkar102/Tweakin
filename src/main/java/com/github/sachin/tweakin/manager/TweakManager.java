package com.github.sachin.tweakin.manager;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Message;
import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.modules.anvilrepair.AnvilRepairTweak;
import com.github.sachin.tweakin.modules.armoredelytra.ArmoredElytraTweak;
import com.github.sachin.tweakin.modules.autorecipeunlock.AutoRecipeUnlockTweak;
import com.github.sachin.tweakin.modules.betterarmorstands.BetterArmorStandTweak;
import com.github.sachin.tweakin.modules.betterbonemeal.BetterBoneMealTweak;
import com.github.sachin.tweakin.modules.betterflee.AnimalFleeTweak;
import com.github.sachin.tweakin.modules.bettergrindstone.BetterGrindStoneTweak;
import com.github.sachin.tweakin.modules.betterladder.BetterLadderTweak;
import com.github.sachin.tweakin.modules.betterrecoverycompass.BetterRecoveryCompassTweak;
import com.github.sachin.tweakin.modules.bettersignedit.BetterSignEditTweak;
import com.github.sachin.tweakin.modules.blockalwaysdrops.BlockAlwaysDropsTweak;
import com.github.sachin.tweakin.modules.bossspawnsounds.BroadCastSoundTweak;
import com.github.sachin.tweakin.modules.bottledcloud.BottledCloudItem;
import com.github.sachin.tweakin.modules.burnvinetip.BurnVineTipTweak;
import com.github.sachin.tweakin.modules.cauldronconcrete.CauldronConcreteTweak;
import com.github.sachin.tweakin.modules.cauldronmud.CauldronMudTweak;
import com.github.sachin.tweakin.modules.chickenshearing.ChickenShearingTweak;
import com.github.sachin.tweakin.modules.compassworkeverywhere.CompassEveryWhereTweak;
import com.github.sachin.tweakin.modules.confetticreepers.ConfettiCreepers;
import com.github.sachin.tweakin.modules.coordinatehud.CoordinateHUDTweak;
import com.github.sachin.tweakin.modules.craftingtableonstick.CraftTableOnStick;
import com.github.sachin.tweakin.modules.customportals.CustomPortalTweak;
import com.github.sachin.tweakin.modules.elytrabombing.ElytraBombingTweak;
import com.github.sachin.tweakin.modules.fastleafdecay.FastLeafDecayTweak;
import com.github.sachin.tweakin.modules.hoeharvesting.HoeHarvestingTweak;
import com.github.sachin.tweakin.modules.infinitefirework.InfiniteFireworkItem;
import com.github.sachin.tweakin.modules.infinitybucket.InfinityWaterBucketTweak;
import com.github.sachin.tweakin.modules.jumpyboats.JumpyBoatsTweak;
import com.github.sachin.tweakin.modules.lapisintable.LapisInTableTweak;
import com.github.sachin.tweakin.modules.lavabucketcan.LavaBucketTrashCan;
import com.github.sachin.tweakin.modules.lecternpagereset.LecternPageResetTweak;
import com.github.sachin.tweakin.modules.miniblocks.MiniBlocksTweak;
import com.github.sachin.tweakin.modules.mobheads.MobHeadsTweak;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;
import com.github.sachin.tweakin.modules.netherportalcoords.NetherPortalCoordsTweak;
import com.github.sachin.tweakin.modules.noteblock.NoteBlockHeadsTweak;
import com.github.sachin.tweakin.modules.patdogs.PatDogTweak;
import com.github.sachin.tweakin.modules.poisonpotatousage.PoisonPotatoUsageTweak;
import com.github.sachin.tweakin.modules.reacharound.ReachAroundTweak;
import com.github.sachin.tweakin.modules.recyclablewax.RecyclableWaxTweak;
import com.github.sachin.tweakin.modules.rightclickarmor.RightClickArmor;
import com.github.sachin.tweakin.modules.shulkerboxpreview.ShulkerBoxPreview;
import com.github.sachin.tweakin.modules.rotationwrench.RotationWrenchItem;
import com.github.sachin.tweakin.modules.shearitemframe.ShearItemFrameTweak;
import com.github.sachin.tweakin.modules.shearnametag.ShearNameTagTweak;
import com.github.sachin.tweakin.modules.silencemobs.SilenceMobsTweak;
import com.github.sachin.tweakin.modules.slimebucket.SlimeInBucket;
import com.github.sachin.tweakin.modules.snowballknockback.SnowBallKnockBackTweak;
import com.github.sachin.tweakin.modules.stormchanneling.StormChannelingTweak;
import com.github.sachin.tweakin.modules.swingthroughgrass.SwingThroughGrassTweak;
import com.github.sachin.tweakin.modules.trowel.TrowelItem;
import com.github.sachin.tweakin.modules.villagerdeathmessage.VillagerDeathMessageTweak;
import com.github.sachin.tweakin.modules.villagerfollowemerald.VillagerFollowEmraldTweak;
import com.github.sachin.tweakin.modules.wanderingtraderannouncements.WanderingTraderAnnouncementTweak;
import com.github.sachin.tweakin.modules.waterextinguish.WaterExtinguishTweak;
import com.github.sachin.tweakin.utils.ConfigUpdater;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TweakManager {

    private final Tweakin plugin;
    private Message messageManager;
    private List<BaseTweak> tweakList = new ArrayList<>();
    private List<TweakItem> registeredItems = new ArrayList<>();

    private List<String> incompaitableTweaks = new ArrayList<>();
    private Map<BaseTweak,Boolean> guiMap = new HashMap<>();

    private FileConfiguration recipeConfig;
    


    public TweakManager(Tweakin plugin){
        this.plugin = plugin;
        this.messageManager = plugin.getMessageManager();
    }

    public void load(){
        plugin.getLogger().info("Loading tweakin...");
        addCoreTweaks();
        sort();
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
        ConfigUpdater.updateWithoutComments(plugin, "recipes.yml", recipeFile);
        try {
            ConfigUpdater.update(plugin, "config.yml", configFile, new ArrayList<>(),unregister);
        } catch (IOException e) {
            e.printStackTrace();
        }
        plugin.reloadConfig();
        if(unregister){
            plugin.reloadMessageManager();
        }
        this.messageManager = plugin.getMessageManager();

        FirstInstallListener listener = new FirstInstallListener();
        if(plugin.isFirstInstall){
            plugin.isFirstInstall = false;
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

        for (BaseTweak t : tweakList) {
            try {
                if(unregister){

                    if(t.registered){
                        t.unregister();
                    }
                }
                t.reload();
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
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a-----------Tweakin------------"));
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eAll tweaks seems to be disabled, use &6/tw toggle &eor &6/tw &6toggle [tweak-name]"));
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eto enable some!!"));
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDo not use &6config.yml &cto toggle tweaks"));
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a------------------------------"));
            }
        }
        plugin.getLogger().info("Registered "+registered+" tweaks successfully");

        Bukkit.getOnlinePlayers().forEach(p -> p.updateCommands());
    }

    private void addCoreTweaks(){

        tweakList.add(new ShulkerBoxPreview());
        tweakList.add(new NoteBlockHeadsTweak());

        tweakList.add(new FastLeafDecayTweak());
        tweakList.add(new BetterLadderTweak());
        tweakList.add(new LapisInTableTweak());
        tweakList.add(new CustomPortalTweak());
        tweakList.add(new NetherPortalCoordsTweak());
        tweakList.add(new SwingThroughGrassTweak());
        tweakList.add(new CoordinateHUDTweak());
        tweakList.add(new PoisonPotatoUsageTweak());
        tweakList.add(new BurnVineTipTweak());
        tweakList.add(new SilenceMobsTweak());
        tweakList.add(new RotationWrenchItem());
        tweakList.add(new TrowelItem());
        tweakList.add(new ConfettiCreepers());
        tweakList.add(new LavaBucketTrashCan());
        tweakList.add(new SlimeInBucket());
        tweakList.add(new PatDogTweak());
        tweakList.add(new CompassEveryWhereTweak());
        tweakList.add(new HoeHarvestingTweak());
        tweakList.add(new AnimalFleeTweak());
        tweakList.add(new ShearItemFrameTweak());
        tweakList.add(new SnowBallKnockBackTweak());
        tweakList.add(new InfinityWaterBucketTweak());
        tweakList.add(new BetterGrindStoneTweak());
        tweakList.add(new ArmoredElytraTweak());
        tweakList.add(new VillagerDeathMessageTweak());
        tweakList.add(new BetterArmorStandTweak());
        tweakList.add(new ShearNameTagTweak());
        tweakList.add(new LecternPageResetTweak());
        tweakList.add(new CraftTableOnStick());
        tweakList.add(new InfiniteFireworkItem());
        tweakList.add(new MoreRecipesTweak());
        tweakList.add(new AutoRecipeUnlockTweak());
        tweakList.add(new AnvilRepairTweak());
        tweakList.add(new WaterExtinguishTweak());
        tweakList.add(new BetterBoneMealTweak());
        tweakList.add(new ElytraBombingTweak());
        tweakList.add(new ChickenShearingTweak());
        tweakList.add(new RecyclableWaxTweak());
        tweakList.add(new BlockAlwaysDropsTweak());
        tweakList.add(new WanderingTraderAnnouncementTweak());
        tweakList.add(new StormChannelingTweak());
        tweakList.add(new MobHeadsTweak());
        tweakList.add(new BottledCloudItem());
        tweakList.add(new MiniBlocksTweak());

        if(plugin.isProtocolLibEnabled){
            if(!plugin.isPost1_19_3()){
                tweakList.add(new BroadCastSoundTweak());
            }
            if(!plugin.isPost1_20()){
                tweakList.add(new BetterSignEditTweak());
            }
            tweakList.add(new ReachAroundTweak());
            tweakList.add(new JumpyBoatsTweak());
        }
        else{
            plugin.getLogger().severe("Not running ProtocolLib, ignoring boss-spawn-sounds, better-sign-edit, reach-around, jumpy-boats...");
            incompaitableTweaks.add("boss-spawn-sounds");
            incompaitableTweaks.add("jumpy-boats");
            incompaitableTweaks.add("reach-around");
            incompaitableTweaks.add("better-sign-edit");
        }
        if(plugin.isRunningPaper){
            tweakList.add(new CauldronConcreteTweak());
            tweakList.add(new VillagerFollowEmraldTweak());
            if(plugin.isPost1_19()){
                tweakList.add(new CauldronMudTweak());
            }
        }
        else{
            plugin.getLogger().severe("Not running PaperMC as server software, ignoring cauldron concrete and villager follow emerald tweak...");
            incompaitableTweaks.add("cauldron-concrete");
            incompaitableTweaks.add("villager-follow-emerald");
        }
        if(plugin.isPost1_19()){
            tweakList.add(new BetterRecoveryCompassTweak());
        }


        else{
            incompaitableTweaks.add("better-recovery-compass");
        }
        if(!plugin.isPost1_19_3()){
            tweakList.add(new RightClickArmor());
        }
        else{
            incompaitableTweaks.add("armor-right-click");
        }
    }

    public List<BaseTweak> getTweakList() {
        return tweakList;
    }

    public List<String> getIncompaitableTweaks() {
        return incompaitableTweaks;
    }

    private void sort(){
        TreeMap<String,BaseTweak> treeMap = new TreeMap<>(Comparator.naturalOrder());
        for(BaseTweak tweak : tweakList){
            treeMap.put(tweak.getName(),tweak);
        }
        tweakList.clear();
        tweakList.addAll(treeMap.values());
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
        return this.messageManager;
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

    public void addTweak(BaseTweak tweak){
        tweakList.add(tweak);
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



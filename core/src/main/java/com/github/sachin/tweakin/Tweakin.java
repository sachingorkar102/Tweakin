package com.github.sachin.tweakin;

import co.aikar.commands.CommandReplacements;
import co.aikar.commands.PaperCommandManager;
import com.github.sachin.tweakin.bstats.Metrics;
import com.github.sachin.tweakin.bstats.Metrics.AdvancedPie;
import com.github.sachin.tweakin.commands.CoreCommand;
import com.github.sachin.tweakin.compat.CombatLogXCompat;
import com.github.sachin.tweakin.compat.grief.*;
import com.github.sachin.tweakin.gui.GuiListener;
import com.github.sachin.tweakin.manager.TweakManager;
import com.github.sachin.tweakin.modules.lapisintable.LapisData;
import com.github.sachin.tweakin.modules.mobheads.Head;
import com.github.sachin.tweakin.modules.morerecipes.MoreRecipesTweak;
import com.github.sachin.tweakin.nbtapi.NBTAPI;
import com.github.sachin.tweakin.nbtapi.nms.NMSHelper;
import com.github.sachin.tweakin.utils.MiscItems;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.TConstants;
import com.github.sachin.tweakin.compat.VulcanListenerCompat;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public final class Tweakin extends JavaPlugin {

    private static Tweakin plugin;
    private Metrics metrics;
    public final Random RANDOM = new Random();
    public boolean isRunningPaper;
    private WGFlagManager wgFlagManager;
    public boolean isWorldGuardEnabled;
    private String version;

    private String mcVersion;
    private PaperCommandManager commandManager;

    public BaseGriefCompat griefCompat;
    public CommandReplacements replacements;
    private TweakManager tweakManager;

    private Message messageManager;
    private NMSHelper nmsHelper;
    private boolean isEnabled;
    public boolean isProtocolLibEnabled;
    public boolean isFirstInstall;
    private MiscItems miscItems;
    public final Map<String,Integer> placedBlocksMap = new HashMap<>();

    private List<Permission> permissions;
    private List<Player> placedPlayers = new ArrayList<>();


    @Override
    public void onLoad() {
        plugin = this;
        isWorldGuardEnabled = Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
        if(isWorldGuardEnabled){
            wgFlagManager = new WGFlagManager(this);
            plugin.getLogger().info("Found WorldGuard, initializing flags support");
            wgFlagManager.registerFlags();
        }
    }


    @Override
    public void onEnable() {
        this.isEnabled = true;
        
        this.isFirstInstall = false;
        try {
            Class.forName("com.destroystokyo.paper.utils.PaperPluginLogger");
            this.isRunningPaper = true;
            getLogger().info("Running papermc..");
        } catch (ClassNotFoundException e) {
            this.isRunningPaper = false;
        }
        registerGriefCompat();
        if(isPluginEnabled(TConstants.VULCAN)){
            new VulcanListenerCompat().registerEvents();
            getLogger().info("Running Vulcan, registering listener for reacharound");
        }
        if(isPluginEnabled(TConstants.COMBATLOGX)){
            new CombatLogXCompat().registerEvents();
            getLogger().info("Running CombatLogX, registering listener for better-ladders");
        }
        int currentMajor = Integer.parseInt(Bukkit.getBukkitVersion().split("\\.")[0]);
        int currentMinor = Integer.parseInt(Bukkit.getBukkitVersion().split("\\.")[1].split("-")[0]);
        int currentPatch = Bukkit.getBukkitVersion().chars().filter(ch -> ch == '.').count() == 2 ? 0 : Integer.parseInt(Bukkit.getBukkitVersion().split("\\.")[2].split("-")[0]);
        mcVersion = currentMajor+"."+currentMinor;
        if(currentPatch>0){
            mcVersion = mcVersion+"."+currentPatch;
        }
        this.version = plugin.getServer().getClass().getPackage().getName().split("\\.")[3];
        getLogger().info("Running "+mcVersion+" minecraft version...");
        NBTAPI nbtapi = new NBTAPI();
        if(!nbtapi.loadVersions(this,version,mcVersion)){
            getLogger().warning("Running incompataible minecraft version, stopping tweakin");
            this.isEnabled = false;
            this.getServer().getPluginManager().disablePlugin(this);
            return;
            
        }

        reloadMiscItems();
        reloadMessageManager();
        this.getServer().getPluginManager().registerEvents(new GuiListener(plugin), plugin);
        this.isProtocolLibEnabled = plugin.getServer().getPluginManager().isPluginEnabled("ProtocolLib");
        this.nmsHelper = nbtapi.getNMSHelper();
        this.saveDefaultConfig();
        this.reloadConfig();
        this.commandManager = new PaperCommandManager(this);
        this.replacements = commandManager.getCommandReplacements();
        this.tweakManager = new TweakManager(this);
        tweakManager.load();
        Permissions.reload();
        Tweakin.getPlugin().getLogger().info("Registering permissions");
        this.permissions = Arrays.asList(Permissions.BETTERRECOVERYCOMPASS_USE, Permissions.ARMOREDELYTRA_CRAFT, Permissions.AUTORECIPEUNLOCK, Permissions.BETTERARMORSTAND_PARENT
                , Permissions.BETTERARMORSTAND_COMMAND, Permissions.BETTERARMORSTAND_UUIDBYPASS, Permissions.BETTERARMORSTAND_ARMORSWAP, Permissions.ARMORSTANDWAND
                , Permissions.FLEEMOBS, Permissions.BETTER_GRINDSTONE, Permissions.BETTERLADDER_PARENT, Permissions.BETTERLADDER_QUICKCLIMB, Permissions.BETTERLADDER_DROPDOWN
                , Permissions.BETTERSIGNEDIT, Permissions.BOTTLED_CLOUD_PARENT, Permissions.BOTTLEDCLOUD_USE, Permissions.BOTTLEDCLOUD_PICKUP, Permissions.BURNVINETIP_USE
                , Permissions.COMPASSTRACK, Permissions.HUD_PARENT, Permissions.HUD_COMPASSBYPASS, Permissions.HUD_COMMAND, Permissions.CRAFTINGTABLE_USE, Permissions.CUSTOMPORTAL_USE
                , Permissions.INFIFIREWORK_USE, Permissions.INFBUCKET_PARENT, Permissions.INFIBUCKET_CRAFT, Permissions.INFIBUCKET_USE, Permissions.LAVATRASHCAN, Permissions.MOBHEADS
                , Permissions.NETHERCOORDS, Permissions.PAT_DOG, Permissions.PAT_CAT, Permissions.POISONPOTATO, Permissions.REACHAROUND_PARENT, Permissions.REACHAROUND_HIGHLIGHT
                , Permissions.REACHAROUND_VERT, Permissions.REACHAROUND_HORI, Permissions.REACHAROUND_TOGGLE,
                Permissions.ARMORCLICK,Permissions.SHULKERBOX_CLICK,Permissions.ENDERCHEST_CLICK,Permissions.ROTATION_WRENCH,Permissions.SHEARITEMFRAME,Permissions.SHEARNAMETAG,
                Permissions.SILENCEMOBS_PARENT,Permissions.SILENCEMOBS_SILENCE,Permissions.SILENCEMOBS_UNSILENCE,Permissions.SLIMEBUCKET_PARENT,
                Permissions.SLIMEBUCKET_PICKUP,Permissions.SLIMEBUCKET_DETECT,Permissions.SWINGGRASS,Permissions.TROWEL,Permissions.VIL_DTH_MSG,Permissions.ANVIL_REPAIR
                ,Permissions.WATER_EX,Permissions.BETTER_BONEMEAL,Permissions.JUMPYBOATS,Permissions.CAULDRON_CONCRETE
                ,Permissions.CHICKEN_SHEARING,Permissions.HOE_HARVESTING);


        for(Permission perm : this.permissions){
            if(perm != null){
                Bukkit.getServer().getPluginManager().addPermission(perm);
            }
        }
        Tweakin.getPlugin().getLogger().info("Registered "+permissions.size()+" permissions");
        ConfigurationSerialization.registerClass(LapisData.class,"LapisData");
        
        commandManager.getCommandCompletions().registerCompletion("tweakitems", c -> tweakManager.getRegisteredItemNames());
        commandManager.getCommandCompletions().registerCompletion("tweaklist", c -> tweakManager.getTweakNames());
        
        List<String> headList = Arrays.asList(Head.values()).stream().map(h -> h.toString()).collect(Collectors.toList());
        commandManager.getCommandCompletions().registerCompletion("tweakinheads",c -> headList);
        commandManager.registerCommand(new CoreCommand(this));
        enabledBstats();
        getLogger().info("Tweakin loaded successfully");
    }

    public MiscItems getMiscItems() {
        return miscItems;
    }

    public void reloadMiscItems(){
        this.miscItems = new MiscItems(this);
    }

    public void reloadMessageManager(){
        this.messageManager = new Message(plugin);
        messageManager.reload();
    }

    @Override
    public void onDisable() {
        if(!isEnabled) return;
        for(Permission perm : this.permissions){
            if(perm != null){
                Bukkit.getServer().getPluginManager().removePermission(perm);
            }
        }
        for(BaseTweak t : tweakManager.getTweakList()){
            if(t.registered){
                t.onDisable();
            }
        }
        if(metrics != null){
            this.metrics.addCustomChart(new AdvancedPie("Placed-Blocks", new Callable<Map<String, Integer>>() {
                @Override
                public Map<String, Integer> call() throws Exception {
                    return placedBlocksMap;
                }
            }));
        }
        
    }

    private void registerGriefCompat(){
        if(!getConfig().getBoolean("grief-plugin-support")) return;
        if(isPluginEnabled(TConstants.GRIEF_PREVENTION)){
            this.griefCompat = new GriefPreventionCompat();
        }
        else if(isPluginEnabled(TConstants.LANDS)){
            this.griefCompat = new LandsCompat();
        }
        else if(isPluginEnabled(TConstants.RESIDENCE)){
            this.griefCompat = new ResidenceCompat();
        }
        else if(isPluginEnabled(TConstants.CRASHCLAIM)){
            this.griefCompat = new CrashClaimCompat();
        }
        else if(isPluginEnabled(TConstants.TOWNY)){
            this.griefCompat = new TownyCompat();
        }

    }

    public boolean isPluginEnabled(String name){
        return this.getServer().getPluginManager().isPluginEnabled(name);
    }

    public String getVersion() {
        return version;
    }

    private void enabledBstats(){
        if(getConfig().getBoolean("metrics",true)){
            this.metrics = new Metrics(this,11786);
            getLogger().info("Enabling bstats...");
            
            this.metrics.addCustomChart(new AdvancedPie("Enabled-Tweaks", new Callable<Map<String, Integer>>() {
                @Override
                public Map<String, Integer> call() throws Exception {
                    Map<String, Integer> map = new HashMap<>();
                    for(BaseTweak tweak : getTweakManager().getTweakList()){
                        if(tweak.shouldEnable()){
                            map.put(tweak.getName(), 1);
                        }
                        
                    }
                    return map;
                }
            }));

            ((MoreRecipesTweak)tweakManager.getTweakFromName("more-recipes")).addBstatsGraph(metrics);


        }
    }

    public void addPlacedPlayer(Player player){
        placedPlayers.add(player);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,() -> {placedPlayers.remove(player);}, 3);
    }

    public boolean isPost1_17(){
        return Arrays.asList("v1_17_R1","v1_18_R1","v1_18_R2","v1_19_R1","v1_19_R2","v1_19_R3").contains(version);
    }

    public boolean isPost1_18() {return Arrays.asList("v1_18_R1","v1_18_R2","v1_19_R1","v1_19_R2","v1_19_R3").contains(version);}

    public boolean isPost1_19() {return Arrays.asList("v1_19_R1","v1_19_R2","v1_19_R3").contains(version);}

    public boolean isPost1_19_3() {return Arrays.asList("v1_19_R3").contains(version);}

    public List<Player> getPlacedPlayers() {
        return placedPlayers;
    }

    public NMSHelper getNmsHelper() {
        return nmsHelper;
    }

    public static Tweakin getPlugin() {
        return plugin;
    }

    public PaperCommandManager getCommandManager() {
        return commandManager;
    }

    public TweakManager getTweakManager() {
        return tweakManager;
    }

    public Message getMessageManager() {
        return messageManager;
    }

    public static NamespacedKey getKey(String key){
        return new NamespacedKey(plugin, key);
    }

    public WGFlagManager getWGFlagManager() {
        return wgFlagManager;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    
}

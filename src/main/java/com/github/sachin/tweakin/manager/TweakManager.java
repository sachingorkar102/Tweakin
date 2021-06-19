package com.github.sachin.tweakin.manager;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.autorecipeunlock.AutoRecipeUnlockTweak;
import com.github.sachin.tweakin.betterladder.BetterLadderTweak;
import com.github.sachin.tweakin.controlledburn.ControlledBurnTweak;
import com.github.sachin.tweakin.customportals.CustomPortalTweak;
import com.github.sachin.tweakin.fastleafdecay.FastLeafDecayTweak;
import com.github.sachin.tweakin.lapisintable.LapisInTableTweak;
import com.github.sachin.tweakin.netherportalcoords.NetherPortalCoordsTweak;
import com.github.sachin.tweakin.noteblock.NoteBlockHeadsTweak;
import com.github.sachin.tweakin.reacharound.ReachAroundTweak;
import com.github.sachin.tweakin.rightclickarmor.RightClickArmor;
import com.github.sachin.tweakin.rightclickshulker.RightClickShulkerBox;
import com.github.sachin.tweakin.swingthroughgrass.SwingThroughGrassTweak;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class TweakManager {

    private final Tweakin plugin;
    private List<BaseTweak> tweakList = new ArrayList<>();


    public TweakManager(Tweakin plugin){
        this.plugin = plugin;
    }

    public void load(){
        reload(false);
    }

    public void reload(){
        reload(true);
    }

    private void reload(boolean unregister){
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        getTweakList().forEach(t -> {
            if(unregister){
                t.reload();
                if(t.registered){
                    t.unregister();
                }
            }
            if(t.shouldEnable()){
                t.register();
            }
        });
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
        }
        return tweakList;
    }
}



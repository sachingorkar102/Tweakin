package com.github.sachin.tweakin.modules.betterarmorstands;

import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class ArmorStandWandItem extends TweakItem implements Listener{

    private BetterArmorStandTweak instance;

    public ArmorStandWandItem(Tweakin plugin,BetterArmorStandTweak instance) {
        super(plugin, "armorstand-wand");
        this.instance = instance;
    }


    @Override
    public void register() {
        super.register();
        registerRecipe();
    }

    @Override
    public void unregister() {
        super.unregister();
        unregisterRecipe();
    }
    

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(hasItem(player, EquipmentSlot.HAND) && player.hasPermission("tweakin.armorstandwand.use")){
            if(e.getAction()==Action.RIGHT_CLICK_AIR && player.isSneaking()){
                instance.openArmorStandLast(player);
            }
            else if(e.getAction() == Action.RIGHT_CLICK_AIR && !player.isSneaking()){
                instance.openArmorStandNear(player);
            }
        }
    }

}

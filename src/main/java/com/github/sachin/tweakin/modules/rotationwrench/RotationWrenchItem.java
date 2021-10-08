package com.github.sachin.tweakin.modules.rotationwrench;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class RotationWrenchItem extends TweakItem implements Listener{

    private List<String> rotateableMaterials = new ArrayList<>();
    private List<Player> cooldownPlayers = new ArrayList<>();

    public RotationWrenchItem(Tweakin plugin) {
        super(plugin,"rotation-wrench");
        
    }

    @Override
    public void reload() {
        super.reload();
        this.rotateableMaterials = getConfig().getStringList("rotatable-materials");
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
    public void interactEvent(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        
        if(!player.hasPermission("tweakin.rotationwrench.use")) return;
        if(!hasItem(player, e.getHand())) return;
        Block block = e.getClickedBlock();
        if(!matchString(block.getType().toString(), rotateableMaterials)) return;
        boolean success = false;
        Block relativeBlock = block.getRelative(e.getBlockFace());
        if(relativeBlock.getType() != Material.AIR) return;
        if(cooldownPlayers.contains(player)) return;
        if(block.getBlockData() instanceof Directional){
            Directional directional = (Directional) block.getBlockData();
            BlockFace currentFace = directional.getFacing();
            List<BlockFace> rotations = new ArrayList<>(directional.getFaces());
            boolean placed = getPlugin().getNmsHelper().placeItem(player,relativeBlock.getLocation(), new ItemStack(block.getType()), e.getBlockFace(),null,false);
            if(placed){
                success = true;
                e.setCancelled(true);
                relativeBlock.setType(Material.AIR);
                if(rotations.indexOf(currentFace) == rotations.size()-1){
                    directional.setFacing(rotations.get(0));
                    
                }else{
                    directional.setFacing(rotations.get(rotations.indexOf(currentFace)+1));
                }
                block.setBlockData(directional);
                block.getState().update(true,true);
            }
        }
        else if( block.getBlockData() instanceof Orientable){
            Orientable orientable = (Orientable) block.getBlockData();
            List<Axis> axises = new ArrayList<>(orientable.getAxes());
            Axis currentFace = orientable.getAxis();
            boolean placed = getPlugin().getNmsHelper().placeItem(player, relativeBlock.getLocation(), new ItemStack(Material.DIRT), e.getBlockFace(),null,false);
            if(placed){
                success = true;
                relativeBlock.setType(Material.AIR);
                if(axises.indexOf(currentFace) == axises.size()-1){
                    orientable.setAxis(axises.get(0));
                }
                else {
                    orientable.setAxis(axises.get(axises.indexOf(currentFace)+1));
                }
                
                block.setBlockData(orientable);
                block.getState().update(true, true);
            }

            
            
        }
        if(success){
            cooldownPlayers.add(player);
            new BukkitRunnable(){

                @Override
                public void run() {
                    cooldownPlayers.remove(player);
                };
            }.runTaskLater(getPlugin(), 5);
        }

        
    }

    // NOT IN USE
    // public boolean isRotatable(Block block){
    //     String blockName = block.getType().toString();
    //     for (String string : rotateableMaterials) {
    //         if(string.startsWith("^")){
    //             if(blockName.endsWith(string.replace("^", ""))){
    //                 return true;
    //             }
    //         }
    //         if(blockName.equals(string)){
    //             return true;
    //         }
    //     }
    //     return false;
    // }
    
}

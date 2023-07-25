package com.github.sachin.tweakin.modules.rotationwrench;

import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.utils.annotations.Config;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;
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
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

@Tweak(name = "rotation-wrench",clashingTeaksTweak = "Rotation Wrench")
public class RotationWrenchItem extends TweakItem implements Listener{

    @Config(key = "rotatable-materials")
    private List<String> rotateableMaterials = new ArrayList<>();
    private List<Player> cooldownPlayers = new ArrayList<>();


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
    public void onDispense(BlockDispenseEvent e){
        if(e.getBlock().getType() == Material.DISPENSER && getConfig().getBoolean("dispenser-usable")){
            if(isSimilar(e.getItem())){
                Directional dispenserDirection = (Directional) e.getBlock().getBlockData();
                Block block = e.getBlock().getRelative(dispenserDirection.getFacing());
                if(matchString(block.getType().toString(), rotateableMaterials)){
                    e.setCancelled(true);
                    if(block.getBlockData() instanceof Directional){
                        Directional directional = (Directional) block.getBlockData();
                        BlockFace currentFace = directional.getFacing();
                        List<BlockFace> rotations = new ArrayList<>(directional.getFaces());
                        if(rotations.indexOf(currentFace) == rotations.size()-1){
                            directional.setFacing(rotations.get(0));
                            
                        }else{
                            directional.setFacing(rotations.get(rotations.indexOf(currentFace)+1));
                        }
                        block.setBlockData(directional);
                        block.getState().update(true,true);
                    }
                    else if( block.getBlockData() instanceof Orientable){
                        Orientable orientable = (Orientable) block.getBlockData();
                        List<Axis> axises = new ArrayList<>(orientable.getAxes());
                        Axis currentFace = orientable.getAxis();
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
            }
        }
    }

    @EventHandler
    public void interactEvent(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        
        if(!hasPermission(player, Permissions.ROTATION_WRENCH)) return;
        if(!hasItem(player, e.getHand())) return;
        Block block = e.getClickedBlock();
        if(!matchString(block.getType().toString(), rotateableMaterials)) return;
        boolean success = false;
        if(cooldownPlayers.contains(player)) return;
        ItemStack item = e.getItem().clone();
        if(plugin.griefCompat != null && !plugin.griefCompat.canBuild(player,block.getLocation(),block.getType())) return;
        if(block.getBlockData() instanceof Directional){
            Directional directional = (Directional) block.getBlockData();
            BlockFace currentFace = directional.getFacing();
            List<BlockFace> rotations = new ArrayList<>(directional.getFaces());

            success = true;
            e.setCancelled(true);
            if(rotations.indexOf(currentFace) == rotations.size()-1){
                directional.setFacing(rotations.get(0));

            }else{
                directional.setFacing(rotations.get(rotations.indexOf(currentFace)+1));
            }
            block.setBlockData(directional);
            block.getState().update(true,true);
        }
        else if( block.getBlockData() instanceof Orientable){
            Orientable orientable = (Orientable) block.getBlockData();
            List<Axis> axises = new ArrayList<>(orientable.getAxes());
            Axis currentFace = orientable.getAxis();
            success = true;
            if(axises.indexOf(currentFace) == axises.size()-1){
                orientable.setAxis(axises.get(0));
            }
            else {
                orientable.setAxis(axises.get(axises.indexOf(currentFace)+1));
            }

            block.setBlockData(orientable);
            block.getState().update(true, true);


            
            
        }
        if(success){
            if(plugin.isPost1_18()){
                player.getInventory().setItemInMainHand(item);
            }
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

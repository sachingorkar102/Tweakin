package com.github.sachin.tweakin.modules.betterbonemeal;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.annotations.Config;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;

import java.util.*;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

@Tweak(name = "better-bone-meal")
public class BetterBoneMealTweak extends BaseTweak implements Listener {

    @Config(key = "growable-blocks")
    private List<String> growableBlocks = new ArrayList<>();

    @Config(key = "dispenser-usable")
    private boolean dispenserUsable = true;

    @Config(key = "lilypad-bonemeal-radius")
    private int lilypadBMRadius = 2;

    @Config(key = "growth-limit")
    private int growthLimit = 10;

    private boolean hasSugarCane;
    private boolean hasCactus;
    private boolean hasLilyPad;
    private boolean hasNetherWart;

    @Override
    public void reload() {
        super.reload();
        hasSugarCane = growableBlocks.contains("SUGARCANE");
        hasCactus = growableBlocks.contains("CACTUS");
        hasNetherWart = growableBlocks.contains("NETHERWART");
        hasLilyPad = growableBlocks.contains("LILYPAD");

    }



    @EventHandler
    public void onDispenserDispenseBonemeal(BlockDispenseEvent e){
        if(e.getBlock().getType()==Material.DISPENSER && e.getItem().getType()==Material.BONE_MEAL && dispenserUsable && !containsWorld(e.getBlock().getWorld())){
            Directional directional = (Directional) e.getBlock().getBlockData();
            Block relative = e.getBlock().getRelative(directional.getFacing());
            boolean growed = applyBoneMeal(relative,e.getItem(),null);
            if(growed){
                e.setCancelled(true);
                Dispenser dispenser = (Dispenser) e.getBlock().getState();
                int slot = dispenser.getInventory().first(Material.BONE_MEAL);
                if(slot == -1) return;
                ItemStack item = dispenser.getInventory().getItem(slot).clone();
                item.setAmount(item.getAmount()-1);
                Inventory inv = dispenser.getInventory();
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        inv.setItem(slot,item);
                    }
                }, 1);

            }
        }
    }


    @EventHandler
    public void onUseBoneMeal(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = e.getClickedBlock();
        ItemStack item = e.getItem();
        Player player = e.getPlayer();
        if(item != null && item.getType()== Material.BONE_MEAL){
            if((plugin.griefCompat != null && !plugin.griefCompat.canBuild(player,block.getLocation(),item.getType())) || !hasPermission(player, Permissions.BETTER_BONEMEAL) || containsWorld(player.getWorld())) return;
            boolean growed = applyBoneMeal(block,item,player);
            if(growed){
                if(player.getGameMode() != GameMode.CREATIVE){
                    item.setAmount(item.getAmount()-1);
                }
                if(e.getHand()== EquipmentSlot.HAND){
                    player.swingMainHand();
                }else{
                    player.swingOffHand();
                }
            }
        }
    }

    private boolean applyBoneMeal(Block block,ItemStack item, @Nullable Player player){
        Block baseBlock = getTopBlock(block);
        Block topperBlock = isGrowable(baseBlock);
        int height = getHeightFromDown(baseBlock,0);
        if(growthLimit==height+1){
            return false;
        }
        if(topperBlock != null){
            if(block.getType()==Material.SUGAR_CANE && hasSugarCane){
                topperBlock.setType(Material.SUGAR_CANE);
                playBoneMealEffect(block.getLocation().add(0.5,0.5,0.5));
                return true;
            }
            else if(block.getType()==Material.CACTUS && hasCactus){
                topperBlock.setType(Material.CACTUS);
                playBoneMealEffect(block.getLocation().add(0.5,0.5,0.5));
                return true;
            }
            else if(block.getType()==Material.NETHER_WART && hasNetherWart){
                Ageable ageable = (Ageable) block.getBlockData();
                if(ageable.getAge()<ageable.getMaximumAge()){
                    ageable.setAge(ageable.getAge()+1);
                    block.setBlockData(ageable);
                    playBoneMealEffect(block.getLocation().add(0.5,0.5,0.5));
                    return true;
                }
            }
            else if(block.getType()==Material.LILY_PAD && hasLilyPad){
                List<Block> list = getNearbyBlocks(block);
                if(!list.isEmpty()){
                    for(Block b : list){
                        if(plugin.RANDOM.nextBoolean()) continue;
                        b.setType(Material.LILY_PAD);
                        playBoneMealEffect(b.getLocation().add(0.5,-0.3,0.5));
                    }
                    playBoneMealEffect(block.getLocation().add(0.5,-0.3,0.5));
                    return true;
                }
            }
        }
        return false;

    }

    private List<Block> getNearbyBlocks(Block block){
        List<Block> blocks = new ArrayList<>();
        for(int x = block.getX()-lilypadBMRadius;x <=block.getX()+lilypadBMRadius;x++){
            for(int z = block.getZ()-lilypadBMRadius;z<=block.getZ()+lilypadBMRadius;z++){
                Block target = block.getWorld().getBlockAt(x,block.getY(),z);
                if(target.getRelative(BlockFace.DOWN).getType()==Material.WATER && (target.getType()==Material.AIR || target.getType()==Material.CAVE_AIR)){
                    blocks.add(target);
                }
            }
        }
        blocks.sort(Comparator.comparingDouble(b -> b.getLocation().distance(block.getLocation())));
        List<Block> closestBlocks = new ArrayList<>();
        int maxSelection = Math.min(2,blocks.size());
        while (closestBlocks.size() < maxSelection) {
            Block randomBlock = blocks.get(plugin.RANDOM.nextInt(maxSelection));
            if (!closestBlocks.contains(randomBlock)) {
                closestBlocks.add(randomBlock);
            }
        }
        return closestBlocks;
    }


    private int getHeightFromDown(Block block,int fromDown){
        Block blockBelow = block.getRelative(BlockFace.DOWN);
        if(blockBelow.getType()==block.getType()){
            fromDown++;
            return getHeightFromDown(blockBelow,fromDown);
        }
        return fromDown;
    }



    private void playBoneMealEffect(Location location){
        location.getWorld().spawnParticle(Particle.valueOf("HAPPY_VILLAGER"), location, 12, 0.3, 0.3, 0.3, 0.1);
        location.getWorld().playSound(location, Sound.ITEM_BONE_MEAL_USE, 1, 1);
    }


    private Block getTopBlock(Block block){
        Block upperBlock = block.getRelative(BlockFace.UP);
        if(upperBlock.getType()==block.getType()){
            return getTopBlock(upperBlock);
        }
        return block;
    }

    private Block isGrowable(Block block){
        Block topperBlock = block.getRelative(BlockFace.UP);
        if(topperBlock.getType()==Material.AIR || topperBlock.getType()==Material.CAVE_AIR){
            return topperBlock;
        }
        return null;
    }
}

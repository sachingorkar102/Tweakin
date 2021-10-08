package com.github.sachin.tweakin.modules.betterladder;


import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

// permission: tweakin.betterladder.dropdown, tweakin.betterladder.quickclimb
public class BetterLadderTweak extends BaseTweak implements Listener{

    public BetterLadderTweak(Tweakin plugin) {
        super(plugin, "better-ladder");
    }


    @EventHandler
    public void ladderClickEvent(PlayerInteractEvent e){

        if(getBlackListWorlds().contains(e.getPlayer().getWorld().getName())) return;
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getHand() != EquipmentSlot.HAND) return;
        if(e.getClickedBlock().getType() != Material.LADDER) return;
        Player player = e.getPlayer();
        Block clickedLadder = e.getClickedBlock(); 
        if(e.getItem() == null){
            if(!player.hasPermission("tweakin.betterladder.quickclimb") || !getConfig().getBoolean("quickclimb",true)) return;
            if(!player.isSneaking()) return;
            Vector face = player.getEyeLocation().getDirection().clone();
            if(clickedLadder.getRelative(BlockFace.DOWN).getType() != Material.LADDER){
                Block topBlock = getFirstNonLadderBlock(clickedLadder, BlockFace.UP);
                if(topBlock != null){
                    if(topBlock.getRelative(BlockFace.UP).getType() == Material.AIR){
                        player.teleport(topBlock.getLocation().setDirection(face).subtract(-0.5,1,-0.5));
                        player.playSound(player.getLocation(), Sound.BLOCK_LADDER_STEP, 1, 1);
                    }
                }
            }
            else if(clickedLadder.getRelative(BlockFace.UP).getType() != Material.LADDER){
                Block downBlock = getFirstNonLadderBlock(clickedLadder, BlockFace.DOWN);
                if(downBlock != null){
                    player.teleport(downBlock.getLocation().setDirection(face).add(0.5,1,0.5));
                    player.playSound(player.getLocation(), Sound.BLOCK_LADDER_STEP, 1, 1);
                }
            }
        }
        else if(e.getItem().getType() == Material.LADDER){
            if(!player.hasPermission("tweakin.betterladder.dropdown") || !getConfig().getBoolean("dropdown",true)) return;
            Block downLadder = getLadderBlock(clickedLadder);
            
            if(downLadder == null) return;
            if(downLadder.getType() == Material.AIR){
                boolean fakePlaced = plugin.getNmsHelper().placeItem(player, downLadder.getLocation(), new ItemStack(Material.DIRT), e.getBlockFace(),null,false);
                if(fakePlaced){
                    downLadder.getLocation().getBlock().setType(Material.LADDER);
                    downLadder.getLocation().getBlock().setBlockData(e.getClickedBlock().getBlockData());
                    ItemStack ladder = player.getInventory().getItemInMainHand();

                    if(player.getGameMode() != GameMode.CREATIVE){
                        ladder.setAmount(ladder.getAmount()-1);
                    }
                }
                // boolean placed = getPlugin().getNmsHelper().placeItem(player, downLadder.getLocation(), ladder, e.getBlockFace());
                // downLadder.setType(Material.LADDER);
                // downLadder.setBlockData(clickedLadder.getBlockData());
                // if(player.getGameMode() == GameMode.SURVIVAL){
                // }
            }
        }
        
    }

    private Block getFirstNonLadderBlock(Block block,BlockFace direction){
        Block upBlock = block.getRelative(direction);
        if(upBlock.getType() == Material.LADDER){
            return getFirstNonLadderBlock(upBlock, direction);
        }
        else if(upBlock.getType() == Material.AIR){
            return upBlock;
        }
        else if(direction == BlockFace.DOWN){
            return upBlock;
        }
        
        return null;
    }

    

    private Block getLadderBlock(Block clickedBlock){
        Block downBlock = clickedBlock.getRelative(BlockFace.DOWN);
        if(downBlock.getType() == Material.AIR){
           return downBlock;
        }
        
        else if(downBlock.getType() == Material.LADDER){
            return getLadderBlock(downBlock);
        }
        return null;
    }
    
}

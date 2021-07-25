package com.github.sachin.tweakin.customportals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;


// permission: tweakin.customportal.use
public class CustomPortalTweak extends BaseTweak implements Listener{


    public CustomPortalTweak(Tweakin plugin) {
        super(plugin, "custom-portal");
    }


    private List<Material> getValidPortalBlocks(){
        List<String> names = getConfig().getStringList("valid-portal-blocks");
        List<Material> mats = new ArrayList<>();
        for (String string : names) {
            if(Material.matchMaterial(string) != null){
                mats.add(Material.matchMaterial(string));
            }
        }
        return mats;
    }

    @EventHandler
    public void netherPortalLitEvent(PlayerInteractEvent e){
        if(getBlackListWorlds().contains(e.getPlayer().getWorld().getName())) return;
        
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getHand() != EquipmentSlot.HAND) return;
        if(e.getItem() == null) return;
        if(e.getClickedBlock() == null) return;
        Player player = e.getPlayer();
        Block clickedBlock = e.getClickedBlock();
        if(player.getWorld().getEnvironment() == Environment.THE_END) return;
        if(e.getItem().getType() != Material.FLINT_AND_STEEL) return;
        if(!getValidPortalBlocks().contains(e.getClickedBlock().getType())) return;
        if(e.getBlockFace() != BlockFace.UP) return;
        boolean facingEast = player.getFacing() == BlockFace.EAST || player.getFacing() == BlockFace.WEST;
        if(!player.hasPermission("tweakin.customportal.use")) return;
        Set<Block> bs = getValidPortal(clickedBlock, clickedBlock, facingEast ? Axis.Z : Axis.X , new HashSet<>(), true);
        // List<Block> bs = getFrame(clickedBlock, clickedBlock, new ArrayList<>(),facingEast,0); 
        
        if(bs != null){
            e.setCancelled(true);
            if(!bs.contains(clickedBlock)){
                bs.add(clickedBlock);
            }
            List<Block> airBlocks = getNearbyBlocks(clickedBlock.getLocation(),getConfig().getInt("radius",10), facingEast);

            for (Block block : airBlocks) {
                
                List<Block> adjObs = new ArrayList<>();
                Location loc = block.getLocation();
                boolean aUp = false;
                boolean aDown = false;
                boolean aLeft = false;
                boolean aRight = false;
                int airX = facingEast ? block.getLocation().getBlockZ() : block.getLocation().getBlockX();
                for(Block o : bs){
                    int oX = facingEast ? o.getLocation().getBlockZ() : o.getLocation().getBlockX();
                    Location oLoc = o.getLocation();
                    if(airX == oX && !aUp && loc.getBlockY() > oLoc.getBlockY()){
                        aUp = true;
                        adjObs.add(o);
                    }
                    else if(airX == oX && !aDown && loc.getBlockY() < oLoc.getBlockY()){
                        aDown = true;
                        adjObs.add(o);
                    }
                    else if(loc.getBlockY() == oLoc.getBlockY() && !aRight && airX > oX){
                        aRight = true;
                        adjObs.add(o);
                    }
                    else if(loc.getBlockY() == oLoc.getBlockY() && !aLeft && airX < oX){
                        aLeft = true;
                        adjObs.add(o);
                    }
                }
                if(adjObs != null){
                    if(adjObs.size() == 4){
                        block.setType(Material.NETHER_PORTAL);
                        BlockData bd = block.getBlockData();
                        Orientable orientable = (Orientable) bd;
                        orientable.setAxis(facingEast ? Axis.Z : Axis.X);
                        block.setBlockData(orientable);
                    }
                }
                
                
            }
        }


    }


    // private boolean hasFutureBlock(Block block,List<Block> orignalList,boolean facingEast){
    //     List<BlockSides> sides = Arrays.asList(BlockSides.values());
    //     // Collections.shuffle(sides);
    //     for(BlockSides side: sides){
    //         Block sideBlock = block.getLocation().add(facingEast ? 0 : side.getX(), side.getY(), facingEast ? side.getX() : 0).getBlock();
    //         if(getValidPortalBlocks().contains(block.getType()) && !orignalList.contains(sideBlock)){
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    // Legacy method
    // private List<Block> getFrame(Block clickedBlock,Block starterBlock,List<Block> orignalList,boolean facingEast,int rCount){
    //     if(rCount > 200){
    //         return null;
    //     }
    //     List<Material> validBlocks = getValidPortalBlocks();
    //     Block sideBlock = null;
    //     List<BlockSides> sides = Arrays.asList(BlockSides.values());
    //     // Collections.shuffle(sides);
    //     for(BlockSides side: sides){
    //         Block b = starterBlock.getLocation().add(facingEast ? 0 : side.getX(), side.getY(), facingEast ? side.getX() : 0).getBlock();
    //         if(!validBlocks.contains(b.getType())) continue;
    //         if(orignalList.contains(b)) continue;
    //         if(hasFutureBlock(b, orignalList, facingEast)){
    //             sideBlock = b;
    //             break;
    //         }
    //     }
    //     if(sideBlock != null){
    //         if(sideBlock.getLocation().getBlockY()-clickedBlock.getLocation().getBlockY() > getConfig().getInt("radius",10)){
    //             return null;
    //         }
    //         boolean equalX = facingEast ? sideBlock.getLocation().getBlockZ() == clickedBlock.getLocation().getBlockZ() : sideBlock.getLocation().getBlockX() == clickedBlock.getLocation().getBlockX();
    //         if(equalX && sideBlock.getLocation().getBlockY() == clickedBlock.getLocation().getBlockY() && rCount>5){
    //             orignalList.add(sideBlock);
    //             return orignalList;
    //         }
    //         if(!orignalList.contains(sideBlock)){
    //             rCount++;
    //             orignalList.add(sideBlock);
    //             return getFrame(clickedBlock, sideBlock, orignalList, facingEast, rCount);
    //         }
            
        
    //     }
    //     return null;
    // }

    public static List<Block> getNearbyBlocks(Location location, int radius,boolean facingEast) {
        List<Block> blocks = new ArrayList<Block>();
        int sx = location.getBlockX();
        int sz =location.getBlockZ();
        for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
            // if(y<sy+1) continue;
            if(facingEast){
                for(int z = sz - radius; z <= sz + radius; z++) {
                   Block block = location.getWorld().getBlockAt(sx, y, z);
                   if(block.getType() == Material.AIR){
                       blocks.add(block);
                   } 
                }
            }
            else{
                for(int x = sx - radius; x <= sx + radius; x++) {
                    Block block = location.getWorld().getBlockAt(x, y, sz);
                   if(block.getType() == Material.AIR){
                       blocks.add(block);
                   }
                }
            }
        }
        return blocks;
    }

    public Set<Block> getValidPortal(Block block, Block starting, Axis axis, Set<Block> alreadyFound, boolean isFirst) {
        if(alreadyFound == null) alreadyFound = new HashSet<>();
        List<Material> validPortalBlocks = getValidPortalBlocks();
        if(block.getLocation().equals(starting.getLocation()) && !isFirst) return alreadyFound;
        final Set<Block> checked = alreadyFound;
        Block[] nearbyBlocks = null;
        if(axis == Axis.X) {
            nearbyBlocks = Arrays.stream(new Block[]{block.getLocation().add(1,0,0).getBlock(), block.getLocation().add(1,1,0).getBlock(), block.getLocation().add(1,-1,0).getBlock(), block.getLocation().add(0,1,0).getBlock(), block.getLocation().add(0,-1,0).getBlock(), block.getLocation().add(-1,0,0).getBlock(), block.getLocation().add(-1,1,0).getBlock(), block.getLocation().add(-1,-1,0).getBlock()}).filter(b -> !checked.contains(b)).toArray(Block[]::new);
        } else if(axis == Axis.Z) {
            nearbyBlocks = Arrays.stream(new Block[]{block.getLocation().add(0,0,1).getBlock(), block.getLocation().add(0,1,1).getBlock(), block.getLocation().add(0,-1,1).getBlock(), block.getLocation().add(0,1,0).getBlock(), block.getLocation().add(0,-1,0).getBlock(), block.getLocation().add(0,0,-1).getBlock(), block.getLocation().add(0,1,-1).getBlock(), block.getLocation().add(0,-1,-1).getBlock()}).filter(b -> !checked.contains(b)).toArray(Block[]::new);
        }
        if(axis != null){
            for(Block nearbyBlock : nearbyBlocks) {
                if(validPortalBlocks.contains(nearbyBlock.getType()) && !alreadyFound.contains(nearbyBlock)) {
                    alreadyFound.add(nearbyBlock);
                    return getValidPortal(nearbyBlock, starting, axis, alreadyFound,false);
                }
            }
        }
        return null;
    }

    
}

enum BlockSides{
    UP(0,1)
    ,DOWN(0,-1)
    ,UP_LEFT(-1,1)
    ,UP_RIGHT(1,1)
    ,DOWN_LEFT(-1,-1)
    ,DOWN_RIGHT(1,-1)
    ,MIDDLE_LEFT(1,0)
    ,MIDDLE_RIGHT(-1,0);

    private BlockSides(int x,int y){
        this.x = x;
        this.y = y;
    }

    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }



}

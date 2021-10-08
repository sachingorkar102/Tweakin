package com.github.sachin.tweakin.modules.customportals;

import java.text.MessageFormat;
import java.util.*;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Axis;
import org.bukkit.ChatColor;
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
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EquipmentSlot;


// permission: tweakin.customportal.use
public class CustomPortalTweak extends BaseTweak implements Listener {


    public CustomPortalTweak(Tweakin plugin) {
        super(plugin, "custom-shaped-portals");
    }

    // A little helper class which contains info about portal boundaries and does all the work to check if we exceed these.
    private class PortalBounds {
        int minX;
        int maxX;
        int minY;
        int maxY;

        boolean facingEast;

        int maxWidth;
        int maxHeight;
        int maxSize;

        public PortalBounds(Location loc, boolean facingEast) {
            this.maxWidth = getConfig().getInt("max-width", 20);
            this.maxHeight = getConfig().getInt("max-width", 40);
            this.maxSize = this.maxWidth * this.maxHeight;

            this.facingEast = facingEast;

            this.minX = facingEast ? loc.getBlockZ() : loc.getBlockX();
            this.maxX = facingEast ? loc.getBlockZ() : loc.getBlockX();
            this.minY = loc.getBlockY();
            this.maxY = loc.getBlockY();
        }

        public boolean updateBounds(Location loc) {
            boolean valueChanged = false;
            int curX = facingEast ? loc.getBlockZ() : loc.getBlockX();
            int curY = loc.getBlockY();
            if (curX < minX) {
                minX = curX;
                valueChanged = true;
            }
            if (curX >= maxX) {
                maxX = curX;
                valueChanged = true;
            }
            if (curY < minY) {
                minY = curY;
                valueChanged = true;
            }
            if (curY >= maxY) {
                maxY = curY;
                valueChanged = true;
            }
            return valueChanged;
        }

        public boolean withinBounds() {
            if ((maxX - minX) > maxWidth)
                return false;
            if ((maxY - minY) > maxHeight)
                return false;
            return true;
        }
    }

    private List<Material> getValidPortalMaterials(){
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
        if(e.getItem().getType() != Material.FLINT_AND_STEEL) return;
        Player player = e.getPlayer();
        
        if(player.getWorld().getEnvironment() == Environment.THE_END) return;
        if(!player.hasPermission("tweakin.customportal.use")) return;
        Block clickedBlock = e.getClickedBlock();
        List<Material> validPortalMaterials = getValidPortalMaterials();
        if(!validPortalMaterials.contains(e.getClickedBlock().getType())) return;
        if(clickedBlock.getRelative(e.getBlockFace()).getType() != Material.AIR) return;

        boolean facingEast = player.getFacing() == BlockFace.EAST || player.getFacing() == BlockFace.WEST;

        Set<Block> checked = new HashSet<>();
        BlockFace[] faces = getRelativeBlockFaces(facingEast);

        Block block = clickedBlock.getRelative(e.getBlockFace());
        PortalBounds bounds = new PortalBounds(block.getLocation(), facingEast);

        if (checkBlock(block, validPortalMaterials, checked, faces, bounds)) {
            e.setCancelled(true);
            buildPortal(checked, facingEast);
            return;
        }
        facingEast = !facingEast;
        bounds = new PortalBounds(block.getLocation(), facingEast);
        faces = getRelativeBlockFaces(facingEast);
        checked.clear();
        if (checkBlock(block, validPortalMaterials, checked, faces, bounds)) {
            e.setCancelled(true);
            buildPortal(checked, facingEast);
            return;
        }
    }

    private boolean checkBlock(Block block, List<Material> validPortalMaterials, Set<Block> checked, BlockFace[] faces, PortalBounds bounds) {
        if (validPortalMaterials.contains(block.getType()) || checked.contains(block))
            return true;
        if (block.getType() != Material.AIR && block.getType() != Material.FIRE)
            return false;
        checked.add(block);
        if (bounds.updateBounds(block.getLocation()))
            if (!bounds.withinBounds())
                return false;
        if (!checkBlock(block.getRelative(faces[2]), validPortalMaterials, checked, faces, bounds) ||
                !checkBlock(block.getRelative(faces[1]), validPortalMaterials, checked, faces, bounds) ||
                !checkBlock(block.getRelative(faces[3]), validPortalMaterials, checked, faces, bounds) ||
                !checkBlock(block.getRelative(faces[0]), validPortalMaterials, checked, faces, bounds)
        )
            return false;
        return true;
    }

    private void buildPortal(Set<Block> portalBlocks, boolean facingEast) {
        for (Block b : portalBlocks) {
            b.setType(Material.NETHER_PORTAL);
            BlockData bd = b.getBlockData();
            Orientable orientable = (Orientable) bd;
            orientable.setAxis(facingEast ? Axis.Z : Axis.X);
            b.setBlockData(orientable);
        }
    }

    private BlockFace[] getRelativeBlockFaces(boolean facingEast) {
        return new BlockFace[]{ BlockFace.UP,
                facingEast ? BlockFace.SOUTH : BlockFace.EAST,
                BlockFace.DOWN,
                facingEast ? BlockFace.NORTH : BlockFace.WEST
        };
    }
}
package com.github.sachin.tweakin.modules.anvilrepair;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.annotations.Config;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import com.github.sachin.tweakin.utils.Permissions;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

//Permission: tweakin.anvilrepair.use
@Tweak(name = "anvil-repair")
public class AnvilRepairTweak extends BaseTweak implements Listener {


    @Config(key = "repairable-blocks")
    private List<String> repairableBlocks = new ArrayList<>();

    @EventHandler
    public void onAnvilBlockClick(PlayerInteractEvent e){
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        if(getBlackListWorlds().contains(player.getWorld().getName())) return;
        if(player.isSneaking() || !hasPermission(player, Permissions.ANVIL_REPAIR) || e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block anvil = e.getClickedBlock();
        Material type = anvil.getType();

        if(item != null && repairableBlocks.contains(item.getType().toString())){
            if(type == Material.CHIPPED_ANVIL || type == Material.DAMAGED_ANVIL){
                e.setUseInteractedBlock(Event.Result.DENY);
                BlockFace currentFace = ((Directional)anvil.getBlockData()).getFacing();
                if(type==Material.CHIPPED_ANVIL){
                    anvil.setType(Material.ANVIL);
                }
                if(type==Material.DAMAGED_ANVIL){
                    anvil.setType(Material.CHIPPED_ANVIL);
                }
                Directional directional = (Directional) anvil.getBlockData();

                directional.setFacing(currentFace);
                anvil.setBlockData(directional);
                if(player.getGameMode() != GameMode.CREATIVE){
                    item.setAmount(item.getAmount()-1);
                }
                player.getWorld().playSound(anvil.getLocation(),Sound.BLOCK_ANVIL_PLACE,1F,1F);
            }
        }
    }
}

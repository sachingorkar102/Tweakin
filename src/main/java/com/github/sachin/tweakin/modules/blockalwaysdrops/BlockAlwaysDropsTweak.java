package com.github.sachin.tweakin.modules.blockalwaysdrops;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;
// tweakin.blockalwaysdrops.use
@Tweak(name = "block-always-drops")
public class BlockAlwaysDropsTweak extends BaseTweak implements Listener {

    private Map<String,Boolean> blockMap = new HashMap<>();


    @Override
    public void reload() {
        super.reload();
        if(!blockMap.isEmpty()) blockMap.clear();
        for(String s : getConfig().getStringList("blocks")){
            String[] components = s.split("\\|");
            if(components.length==2){
                blockMap.put(components[0],Boolean.parseBoolean(components[1]));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e){
        if(e.isCancelled()) return;
        if(containsWorld(e.getPlayer().getWorld())) return;
        if(!hasPermission(e.getPlayer(), Permissions.BLOCKS_ALWAYS_DROPS) || e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
        String key = matchMaterial(e.getBlock().getType().toString(),new ArrayList<String>(blockMap.keySet()));
        if(key != null){
            boolean useSilkTouch = blockMap.get(key);
            ItemStack itemInUse = e.getPlayer().getEquipment().getItem(EquipmentSlot.HAND);
            if(useSilkTouch && itemInUse.getEnchantmentLevel(Enchantment.SILK_TOUCH)==0) return;
            e.setDropItems(false);
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(),new ItemStack(e.getBlock().getType()));
        }
    }

    private String matchMaterial(String str,List<String> matcher){
        for(String s : matcher){
            if(s.startsWith("^") && str.startsWith(s.replace("^", ""))){
                return s;
            }
            if(s.endsWith("$") && str.endsWith(s.replace("$", ""))){
                return s;
            }
            if(str.equals(s)) return s;

        }
        return null;
    }
}

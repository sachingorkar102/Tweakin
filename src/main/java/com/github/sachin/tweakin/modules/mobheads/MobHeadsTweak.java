package com.github.sachin.tweakin.modules.mobheads;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.prilib.nms.NBTItem;
import com.github.sachin.tweakin.utils.ConfigUpdater;
import com.github.sachin.tweakin.utils.CustomBlockData;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.TConstants;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import com.github.sachin.tweakin.utils.annotations.TweakFile;
import com.google.common.base.Enums;
import com.google.common.base.Optional;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

// Permission: tweakin.mobheads.drops
@Tweak(name = "mob-heads",clashingTeaksTweak = "More Mob Heads")
public class MobHeadsTweak extends BaseTweak implements Listener{

    @TweakFile(fileName = "heads.yml")
    public static FileConfiguration headConfig;
    private final NamespacedKey key = Tweakin.getKey("mob-head-enum");
    private HeadManager manager;


    @Override
    public void register() {
        super.register();
        manager = new HeadManager(this);
    }

    @EventHandler
    public void onHeadDrops(ItemSpawnEvent e){
        if(e.getEntity().getItemStack().getType() == Material.PLAYER_HEAD || e.getEntity().getItemStack().getType() == Material.PLAYER_WALL_HEAD){
            CustomBlockData data = new CustomBlockData(e.getEntity().getLocation());
            if(data.has(key, PersistentDataType.STRING)){
                Optional<Head> head = Enums.getIfPresent(Head.class, data.get(key, PersistentDataType.STRING));
                if(head.isPresent()){
                    data.remove(key);
                    e.getEntity().setItemStack(head.get().getSkull());
                }
            }
        }
    }

    @EventHandler
    public void onHeadBreak(BlockBreakEvent e){
        if(e.getBlock().getType() == Material.PLAYER_HEAD || e.getBlock().getType() == Material.PLAYER_WALL_HEAD){
            if(!e.isDropItems()){
                CustomBlockData data = new CustomBlockData(e.getBlock().getLocation());
                if(data.has(key, PersistentDataType.STRING)){
                    data.remove(key);
                }
            }
        }
    }

    @EventHandler
    public void onHeadPlace(BlockPlaceEvent e){
        ItemStack item = e.getItemInHand();
        NBTItem nbti = plugin.getNMSHandler().newItem(item);
        if(item.getType()==Material.PLAYER_HEAD){
            ItemMeta meta = item.getItemMeta();
            CustomBlockData data = new CustomBlockData(e.getBlockPlaced().getLocation());
            if(nbti.hasKey("mob-head-item")){
                data.set(key, PersistentDataType.STRING, nbti.getString("mob-head-item"));
            }
            if(meta.getPersistentDataContainer().has(key,PersistentDataType.STRING)){
                data.set(key,PersistentDataType.STRING,meta.getPersistentDataContainer().get(key,PersistentDataType.STRING));
            }
        }
    }


    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        LivingEntity killed = e.getEntity();
        if(getBlackListWorlds().contains(killed.getWorld().getName())) return;
        if(!(killed instanceof Player)){
            
            List<Head> heads = manager.getMultiHeadMap().getOrDefault(killed.getType(), new ArrayList<>());
            if(!heads.isEmpty()){
                Head validHead = null;
                if(killed.getType() == EntityType.WITHER){
                    double total = 0.0;
                    TreeMap<Double,Head> map = new TreeMap<>();
                    for(Head h : heads){
                        map.put(total+=h.getChance(), h);
                    }
                    Entry<Double,Head> entry = null;
                    double value = Math.random();
                    entry = map.ceilingEntry(value);
                    if(entry != null){
                        validHead = entry.getValue();
                    }
                }
                else{
                    for(Head h : heads){
                        if(h.check(killed)){
                            validHead = h;
                            break;
                        }
                    }
                }

                if(validHead != null){
                    boolean requirePlayerKill = getConfig().getBoolean("require-player-kill");
                    if(requirePlayerKill && killed.getKiller() != null){
                        Player killer = killed.getKiller();
                        if(!hasPermission(killer, Permissions.MOBHEADS)) return;
                        ItemStack item1 = killer.getInventory().getItemInMainHand();
                        ItemStack item2 = killer.getInventory().getItemInOffHand();
                        if(item1 != null){
                            if(validHead.hasChance(item1.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS))){
                                e.getDrops().add(validHead.getSkull());
                            }
                        }
                        else if(item2 != null){
                            if(validHead.hasChance(item2.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS))){
                                e.getDrops().add(validHead.getSkull());
                            }
                        }
                        else if(validHead.hasChance()){
                            e.getDrops().add(validHead.getSkull());
                        }
                    }
                    else if(!requirePlayerKill && validHead.hasChance()){
                        e.getDrops().add(validHead.getSkull());
                    }      
                }
            }
        }
    }

    

    

}

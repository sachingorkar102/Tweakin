package com.github.sachin.tweakin.modules.betterarmorstands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Message;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import de.jeff_media.morepersistentdatatypes.DataType;

// tweakin.betterarmorstands.armorswap,tweakin.betterarmorstands.uuidlockbypass,tweakin.betterarmorstands.command
public class BetterArmorStandTweak extends BaseTweak implements Listener{

    private ArmorStandCommand command;
    private final PoseManager poseManager;
    private final ArmorStandWandItem wandItem;
    private Message messageManager;
    protected final Map<UUID,UUID> cachedAsList = new HashMap<>();

    public BetterArmorStandTweak(Tweakin plugin) {
        super(plugin, "better-armorstands");
        this.wandItem = new ArmorStandWandItem(plugin,this);
        getTweakManager().registerTweak(wandItem);
        this.poseManager = new PoseManager(this);
        this.command = new ArmorStandCommand(this);
        this.messageManager = plugin.getTweakManager().getMessageManager();
    }
    
    public PoseManager getPoseManager() {
        return poseManager;
    }
    
    @Override
    public void register() {
        super.register();
        poseManager.loadPoses();
        registerCommands(command);
    }


    @Override
    public void unregister() {
        super.unregister();
        unregisterCommands(command);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArmorStandSpawn(EntitySpawnEvent e){
        if(e.isCancelled() || !getConfig().getBoolean("spawn-with-arms")) return;
        if(e.getEntity() instanceof ArmorStand){
            ArmorStand as = (ArmorStand) e.getEntity();
            if(getBlackListWorlds().contains(as.getWorld().getName())) return;
            as.setArms(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArmorstandDeath(EntityDamageEvent e){
        if(e.getEntity() instanceof ArmorStand){
            ArmorStand as = (ArmorStand) e.getEntity();
            
            if(as.getPersistentDataContainer().has(TConstants.ARMORSTAND_EDITED, PersistentDataType.INTEGER) || as.getPersistentDataContainer().has(TConstants.UUID_LOCK_KEY, DataType.UUID)){
                e.setCancelled(true);
            } 
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
    public void onArmorStandInteract(PlayerInteractAtEntityEvent e){
        if(e.isCancelled()) return;
        Player player = e.getPlayer();
        if(getBlackListWorlds().contains(player.getWorld().getName())) return;
        if(e.getRightClicked() instanceof ArmorStand){
            ArmorStand as = (ArmorStand) e.getRightClicked();
            ItemStack clickedItem = player.getInventory().getItem(e.getHand());
            // security checks
            if(as.getPersistentDataContainer().has(TConstants.UUID_LOCK_KEY, DataType.UUID) && !player.getUniqueId().equals(as.getPersistentDataContainer().get(TConstants.UUID_LOCK_KEY,DataType.UUID)) && !player.hasPermission("tweakin.betterarmorstands.uuidlockbypass")){
                player.sendMessage(plugin.getTweakManager().getMessageManager().getMessage("armorstand-locked").replace("%player%", Bukkit.getOfflinePlayer(as.getPersistentDataContainer().get(TConstants.UUID_LOCK_KEY,DataType.UUID)).getName()));
            }
            else if(as.getPersistentDataContainer().has(TConstants.ARMORSTAND_EDITED, PersistentDataType.INTEGER)){
                player.sendMessage(messageManager.getMessage("armorstand-edited"));
                e.setCancelled(true);
            }
            else if(clickedItem.getType()==Material.SHEARS && player.isSneaking()) return;
            // NameTagChecks
            else if(clickedItem.getType()==Material.NAME_TAG && player.isSneaking() && clickedItem.getItemMeta().hasDisplayName()){
                as.setCustomNameVisible(true);
                as.setCustomName(clickedItem.getItemMeta().getDisplayName());
                as.getPersistentDataContainer().set(TConstants.NAMETAGED_MOB, PersistentDataType.INTEGER, 1);
                clickedItem.setAmount(clickedItem.getAmount()-1);
            }
            // wand checks
            else if(wandItem.registered && clickedItem != null && wandItem.isSimilar(clickedItem) && player.hasPermission("tweakin.armorstandwand.use") && player.isSneaking()){
                e.setCancelled(true);
                ASGuiHolder.openGui(player, as);
            }
            // replacement for setMarker
            else if(as.getPersistentDataContainer().has(TConstants.INTERACTABLE_AS, PersistentDataType.INTEGER)){
                e.setCancelled(true);;
            }
            else if(e.getHand()==EquipmentSlot.HAND && player.isSneaking() && getConfig().getBoolean("armor-swap") && player.hasPermission("tweakin.betterarmorstands.armorswap")){
                e.setCancelled(true);
                for(EquipmentSlot slot : EquipmentSlot.values()){
                    ItemStack asItem = as.getEquipment().getItem(slot);
                    ItemStack playerItem = player.getEquipment().getItem(slot);
                    as.getEquipment().setItem(slot, playerItem);
                    player.getEquipment().setItem(slot, asItem);
                    
                }
            }

        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getClickedInventory() == null) return;
        if(e.getInventory().getHolder() instanceof ASGuiHolder && e.isShiftClick()){
            e.setCancelled(true);
        }
        if(e.getClickedInventory().getHolder() instanceof ASGuiHolder){
            ASGuiHolder holder = (ASGuiHolder) e.getClickedInventory().getHolder();
            Inventory inv = holder.getInventory();
            ArmorStand as = holder.armorStand;
            ItemStack cItem = e.getCurrentItem();
            if(!Arrays.asList(1,9,10,11,19,28).contains(e.getSlot()) || e.isShiftClick()){
                e.setCancelled(true);
            }
            if(cItem != null){
                GuiItems gItem = GuiItems.getGuiItem(cItem);
                if(gItem != null)
                    gItem.handleClick(e, as,e.getClick(),inv,e.getSlot(),as.getLocation(),0.0,null);
            }

            new BukkitRunnable(){
                public void run() {
                    int slot = e.getSlot();
                    ItemStack item = inv.getItem(slot);
                    if(slot == 1)
                        as.getEquipment().setHelmet(item);
                    else if(slot == 10)
                        as.getEquipment().setChestplate(item);
                    else if(slot == 19)
                        as.getEquipment().setLeggings(item);
                    else if(slot == 28)
                        as.getEquipment().setBoots(item);
                    else if(slot ==9)
                        as.getEquipment().setItemInMainHand(item);
                    else if(slot == 11)
                        as.getEquipment().setItemInOffHand(item);        
                    
                };
            }.runTaskLater(plugin, 1);    
            
        }

        if(e.getClickedInventory().getHolder() instanceof PresetPoseGui){
            PresetPoseGui gui = (PresetPoseGui) e.getClickedInventory().getHolder();
            gui.handlePageClicks(e);
        }
    }

    @EventHandler
    public void onGuiClose(InventoryCloseEvent e){
        if(e.getInventory().getHolder() instanceof ASGuiHolder){
            ASGuiHolder gui = (ASGuiHolder) e.getInventory().getHolder();
            cachedAsList.put(e.getPlayer().getUniqueId(), gui.armorStand.getUniqueId());
            gui.armorStand.getPersistentDataContainer().remove(TConstants.ARMORSTAND_EDITED);
            
        }
    }


    public void openArmorStandNear(Player player){
        List<Entity> stands = player.getNearbyEntities(5, 5, 5).stream().filter(e -> (e instanceof ArmorStand) && !((ArmorStand)e).isMarker()).collect(Collectors.toList());
        if(!stands.isEmpty()){
            TreeMap<Integer,Entity> map = new TreeMap<>();
            for(Entity en : stands){
                map.put((int)Math.round(en.getLocation().distanceSquared(player.getLocation())),en);
            }
            ArmorStand as = (ArmorStand) map.get(map.firstKey());
            if(canBuild(player, as)){
                ASGuiHolder.openGui(player, as);
            }
        }
        else{
            player.sendMessage(messageManager.getMessage("no-armorstand-near"));
        }
    }


    public void openArmorStandLast(Player player){
        UUID uuid = cachedAsList.get(player.getUniqueId());
        if(uuid != null){
            Entity en = Bukkit.getEntity(uuid);
            if(en != null && !en.isDead()){
                ArmorStand as = (ArmorStand) en;
                if(canBuild(player, as)){
                    ASGuiHolder.openGui(player, as);
                }

            }
        }
        else{
            player.sendMessage(messageManager.getMessage("armorstand-dead"));
        }
    }

    @Override
    public void onDisable() {
        poseManager.savePoses();
    }


    private boolean canBuild(Player player,ArmorStand as){
        PlayerInteractEntityEvent event = new PlayerInteractEntityEvent(player, as);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }


    
}

package com.github.sachin.tweakin.modules.betterarmorstands;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Message;
import com.github.sachin.tweakin.compat.ItemsAdderCompat;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.TConstants;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import de.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

// tweakin.betterarmorstands.armorswap,tweakin.betterarmorstands.uuidlockbypass,tweakin.betterarmorstands.command
@Tweak(name = "better-armorstands")
public class BetterArmorStandTweak extends BaseTweak implements Listener{

    private ArmorStandCommand command;
    private final PoseManager poseManager;
    private final ArmorStandWandItem wandItem;
    private Message messageManager;
    protected final Map<UUID,UUID> cachedAsList = new HashMap<>();

    private final List<EquipmentSlot> disabledArmorSlots = new ArrayList<>();
    public static final Map<Integer,EquipmentSlot> SLOT_TO_EQUIPMENT_MAP = new HashMap<Integer,EquipmentSlot>(){{
        put(1,EquipmentSlot.HEAD);
        put(10,EquipmentSlot.CHEST);
        put(19,EquipmentSlot.LEGS);
        put(28,EquipmentSlot.FEET);
        put(9,EquipmentSlot.HAND);
        put(11,EquipmentSlot.OFF_HAND);}};

    public BetterArmorStandTweak() {
        super();
        this.wandItem = new ArmorStandWandItem(this);
        getTweakManager().addTweak(wandItem);
        this.poseManager = new PoseManager(this);
        this.command = new ArmorStandCommand(this);
        this.messageManager = plugin.getTweakManager().getMessageManager();
    }

    @Override
    public void reload() {
        super.reload();
        disabledArmorSlots.clear();
        for(String s : getConfig().getStringList("disabled-armor-slots")){
            if(EquipmentSlot.valueOf(s) != null){
                disabledArmorSlots.add(EquipmentSlot.valueOf(s));
            }
        }
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
            if(plugin.griefCompat != null && !(plugin.griefCompat.canBuild(player,as.getLocation(),Material.ARMOR_STAND) || plugin.griefCompat.canUseArmorStand(player,as))) return;
            ItemStack clickedItem = player.getInventory().getItem(e.getHand());
            if(isItemsAdderArmorStand(as)){
                return;
            }
            // security checks
            if(as.getPersistentDataContainer().has(TConstants.UUID_LOCK_KEY, DataType.UUID) && !player.getUniqueId().equals(as.getPersistentDataContainer().get(TConstants.UUID_LOCK_KEY,DataType.UUID)) && !player.hasPermission(Permissions.BETTERARMORSTAND_UUIDBYPASS)){
                player.sendMessage(plugin.getTweakManager().getMessageManager().getMessage("armorstand-locked").replace("%player%", Bukkit.getOfflinePlayer(as.getPersistentDataContainer().get(TConstants.UUID_LOCK_KEY,DataType.UUID)).getName()));
                e.setCancelled(true);
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
            else if(wandItem.registered && clickedItem != null && wandItem.isSimilar(clickedItem) && hasPermission(player, Permissions.ARMORSTANDWAND) && player.isSneaking()){
                e.setCancelled(true);
                ASGuiHolder.openGui(player, as,this);
            }
            // replacement for setMarker
            else if(as.getPersistentDataContainer().has(TConstants.INTERACTABLE_AS, PersistentDataType.INTEGER)){
                e.setCancelled(true);;
            }
            else if(e.getHand()==EquipmentSlot.HAND && player.isSneaking() && getConfig().getBoolean("armor-swap") && hasPermission(player, Permissions.BETTERARMORSTAND_ARMORSWAP)){
                e.setCancelled(true);
                for(EquipmentSlot slot : EquipmentSlot.values()){
                    if(slot.name().equalsIgnoreCase("BODY")) continue;
                    ItemStack asItem = as.getEquipment().getItem(slot);
                    ItemStack playerItem = player.getEquipment().getItem(slot);
                    PlayerArmorStandManipulateEvent event = new PlayerArmorStandManipulateEvent(player,as,playerItem,asItem,slot);
                    plugin.getServer().getPluginManager().callEvent(event);
                    if(!event.isCancelled()){
                        as.getEquipment().setItem(slot, playerItem);
                        player.getEquipment().setItem(slot, asItem);
                    }

                    
                }
            }

        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e){
        if(!(e.getInventory().getHolder() instanceof ASGuiHolder)) return;
        ASGuiHolder holder = (ASGuiHolder) e.getInventory().getHolder();
        ArmorStand as = holder.armorStand;
        Player player = holder.player;
        for(int slot : e.getInventorySlots()){
            if(SLOT_TO_EQUIPMENT_MAP.keySet().contains(slot))
            {
                new BukkitRunnable()
                {
                    public void run()
                    {
                        ItemStack item = e.getNewItems().get(slot);
                        ItemStack playerItem = e.getOldCursor();
                        if(item == null){
                            item = new ItemStack(Material.AIR);
                        }
                        PlayerArmorStandManipulateEvent event = new PlayerArmorStandManipulateEvent(player,as,item,playerItem, SLOT_TO_EQUIPMENT_MAP.get(slot));
                        plugin.getServer().getPluginManager().callEvent(event);
                        if(!event.isCancelled()){
                            as.getEquipment().setItem(SLOT_TO_EQUIPMENT_MAP.get(slot),item);
                        }
                        else{
                            e.setCancelled(true);
                        }

                    };
                }.runTaskLater(plugin, 1);
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
            Player player = holder.player;
            int slot = e.getSlot();
            if(!SLOT_TO_EQUIPMENT_MAP.keySet().contains(slot) || e.isShiftClick() || disabledArmorSlots.contains(SLOT_TO_EQUIPMENT_MAP.get(slot))){
                e.setCancelled(true);

            }

            if(cItem != null){
                GuiItems gItem = GuiItems.getGuiItem(cItem);
                if(gItem != null)
                    gItem.handleClick(e, as,e.getClick(),inv,e.getSlot(),as.getLocation(),0.0,null);
            }

            if(SLOT_TO_EQUIPMENT_MAP.keySet().contains(slot)){
                if(getDisabledArmorSlots().contains(SLOT_TO_EQUIPMENT_MAP.get(slot))) return;
                new BukkitRunnable(){
                    public void run() {

                        ItemStack item = inv.getItem(slot);
                        ItemStack playerItem = e.getCursor();
                        if(item == null){
                            item = new ItemStack(Material.AIR);
                        }
                        if(playerItem == null){
                            playerItem = new ItemStack(Material.AIR);
                        }
                        PlayerArmorStandManipulateEvent event = new PlayerArmorStandManipulateEvent(player,as,item,playerItem, SLOT_TO_EQUIPMENT_MAP.get(slot));
                        plugin.getServer().getPluginManager().callEvent(event);
                        if(!event.isCancelled()){
                            as.getEquipment().setItem(SLOT_TO_EQUIPMENT_MAP.get(slot),item);

                        }
                        else{
                            e.setCancelled(true);
                        }

                    };
                }.runTaskLater(plugin, 1);
            }

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
            for(int slot : SLOT_TO_EQUIPMENT_MAP.keySet()){
                if(getDisabledArmorSlots().contains(SLOT_TO_EQUIPMENT_MAP.get(slot))) continue;
                gui.armorStand.getEquipment().setItem(SLOT_TO_EQUIPMENT_MAP.get(slot),gui.inventory.getItem(slot),true);
            }
            
        }
    }


    public void openArmorStandNear(Player player){
        List<Entity> stands = player.getNearbyEntities(5, 5, 5).stream().filter(e -> (e instanceof ArmorStand) && !((ArmorStand)e).isMarker()).collect(Collectors.toList());
        if(!stands.isEmpty()){
            TreeMap<Integer,Entity> map = new TreeMap<>();
            for(Entity en : stands){
                if(isItemsAdderArmorStand((ArmorStand)en)){
                    continue;
                }
                map.put((int)Math.round(en.getLocation().distanceSquared(player.getLocation())),en);
            }
            ArmorStand as = (ArmorStand) map.get(map.firstKey());
            if(canBuild(player, as)){
                ASGuiHolder.openGui(player, as,this);
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
                if(isItemsAdderArmorStand(as)){return;}
                if(canBuild(player, as)){
                    ASGuiHolder.openGui(player, as,this);
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


    public boolean isItemsAdderArmorStand(ArmorStand as){
        return ItemsAdderCompat.isEnabled && ItemsAdderCompat.isCustomFurniture(as);
    }

    public List<EquipmentSlot> getDisabledArmorSlots() {
        return disabledArmorSlots;
    }
}

package com.github.sachin.tweakin.modules.bottledcloud;

import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.utils.annotations.Config;
import com.github.sachin.tweakin.utils.CustomBlockData;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Permission: tweakin.bottledcloud.pickup,tweakin.bottledcloud.use
@Tweak(name = "bottled-cloud")
public class BottledCloudItem extends TweakItem implements Listener{

    private ItemStack cloudItem;
    private NamespacedKey placedBlock;
    public Map<Location,CloudEntity> clouds = new HashMap<>();
    @Config(key = "minimum-height")
    private int miniHeight = 126;
    @Config(key = "maximum-height")
    private int maxHeight = 132;



    @Override
    public void onLoad() {
        super.onLoad();
        this.cloudItem = new ItemStack(Material.PLAYER_HEAD);
        this.placedBlock = new NamespacedKey(plugin, "cloud-occupied");
        ItemMeta meta = cloudItem.getItemMeta();
        SkullMeta skullMeta = (SkullMeta) meta;
        plugin.getNmsHelper().applyHeadTexture(skullMeta, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY2YTVjOTg5MjhmYTVkNGI1ZDViOGVmYjQ5MDE1NWI0ZGRhMzk1NmJjYWE5MzcxMTc3ODE0NTMyY2ZjIn19fQ==");
        cloudItem.setItemMeta(meta);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(!hasPermission(player, Permissions.BOTTLEDCLOUD_USE)) return;
        if(getBlackListWorlds().contains(player.getWorld().getName())) return;
        if(!hasItem(player, EquipmentSlot.HAND)) return;
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) e.setCancelled(true);
        if(e.getAction() != Action.RIGHT_CLICK_AIR) return;
        e.setCancelled(true);
        ItemStack item = e.getItem();
        // player.getWorld().ray
        RayTrace trace = new RayTrace(player.getEyeLocation().toVector(), player.getEyeLocation().getDirection());
        // System.out.println(trace.getPostion(5));
        ArrayList<Vector> positions = trace.traverse(4, 0.01);
        if(positions != null){

            Location loc = positions.get(positions.size()-1).toLocation(player.getWorld()).getBlock().getLocation();
            CustomBlockData data = new CustomBlockData(loc);
            if(loc.getBlock().getType().isAir() && !clouds.containsKey(loc)){
                if(!data.has(placedBlock, PersistentDataType.INTEGER)){
                    // loc.getBlock().setType(Material.GLASS);
                    CloudEntity entity = new CloudEntity(loc);
                    item.setAmount(item.getAmount()-1);
                    player.getInventory().addItem(new ItemStack(Material.GLASS_BOTTLE));
                    player.swingMainHand();
                    clouds.put(loc, entity);
                    entity.initTicker();
                }
            }
        }
    }

    @EventHandler
    public void onGlassBottleClick(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(getBlackListWorlds().contains(player.getWorld().getName())) return;
        if(!hasPermission(player, Permissions.BOTTLEDCLOUD_PICKUP)) return;
        if(e.getAction() != Action.RIGHT_CLICK_AIR) return;
        if(e.getHand() != EquipmentSlot.HAND) return;
        if(e.getItem() == null) return;
        ItemStack item = e.getItem();
        if(!e.getItem().isSimilar(new ItemStack(Material.GLASS_BOTTLE))) return;
        if(player.getLocation().getBlockY() >= miniHeight && player.getLocation().getBlockY() <= maxHeight){
            giveCloudItem(player, item);
            e.setCancelled(true);
        }
    }

    public void giveCloudItem(Player player,ItemStack item){
        player.swingMainHand();
        player.getInventory().addItem(getItem());
        player.getWorld().playSound(player.getLocation(),Sound.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.PLAYERS, 0.3F, 0.3F);
        if(player.getGameMode() != GameMode.CREATIVE){
            item.setAmount(item.getAmount()-1);
        }
    }


    @EventHandler
    public void onDispenseCloud(BlockDispenseEvent e){
        if(e.getItem() != null){
            if(e.getItem().isSimilar(getItem())){
                e.setCancelled(true);
            }
        }
    }



    @EventHandler
    public void onCloudClick(PlayerInteractEntityEvent e){
        if(e.getHand() != EquipmentSlot.HAND) return;
        if(!(e.getRightClicked() instanceof MagmaCube)) return;
        Player player = e.getPlayer();
        if(hasItem(player, EquipmentSlot.HAND)) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item == null) return;
        // if(!item.getType().isBlock()) return;
        MagmaCube cube = (MagmaCube) e.getRightClicked();
        CloudEntity entity = null;
        for(Location loc : clouds.keySet()){
            CloudEntity en = clouds.get(loc);
            if(cube.getUniqueId() ==  en.cube.getUniqueId()){
                entity = en;
                break;
            }
        }
        if(entity != null){
            if(item.getType() == Material.GLASS_BOTTLE){
                giveCloudItem(player, item);
                entity.ticker.removeAll();
                clouds.remove(entity.loc);
                return;
            }
            entity.ticker.removeAll();
            clouds.remove(entity.loc);
            boolean placed = getPlugin().getNmsHelper().placeItem(player, entity.loc,item,BlockFace.DOWN,getName(),true);
            if(placed && player.getGameMode() != GameMode.CREATIVE && !plugin.isPost1_18()){
                item.setAmount(item.getAmount()-1);
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
    public void oncloudEntitySpawnEvent(EntitySpawnEvent e){
        Entity entity = e.getEntity();
        if(e.isCancelled() && entity.getType()== EntityType.MAGMA_CUBE && entity.getPersistentDataContainer().has(placedBlock,PersistentDataType.INTEGER)){
            e.setCancelled(false);
        }
    }

    @Override
    public void onDisable() {
        for(CloudEntity entity : clouds.values()){
            entity.ticker.removeAll();
        }
    }


    public class CloudEntity {

        public ArmorStand armorStand;
        public MagmaCube cube;
        public Location loc;
        public CloudTicker ticker;
        public CustomBlockData data;


        public CloudEntity(Location loc){
            this.loc = loc;
            this.armorStand = loc.getWorld().spawn(loc.clone().add(0.5, -0.5, 0.5), ArmorStand.class);
            armorStand.getEquipment().setHelmet(cloudItem);
            armorStand.setGravity(false);
            armorStand.setSmall(true);
            armorStand.setMarker(true);
            armorStand.setInvisible(true);
            this.cube = loc.getWorld().spawn(armorStand.getEyeLocation(), MagmaCube.class);
            cube.setSize(2);
            cube.setInvisible(true);
            cube.setAI(false);
            cube.setInvulnerable(true);
            cube.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(200);
            cube.getPersistentDataContainer().set(placedBlock,PersistentDataType.INTEGER,1);
            cube.setSilent(true);
            this.ticker = new CloudTicker(this);
            this.data = new CustomBlockData(loc);
            tagBlock();
        }

        public void tagBlock(){
            data.set(placedBlock, PersistentDataType.INTEGER, 1);
        }

        public void removeTag(){
            if(data.has(placedBlock, PersistentDataType.INTEGER)){
                data.remove(placedBlock);
            }
        }

        public void initTicker(){
            ticker.runTaskTimer(getPlugin(), 0, 20);
        }
    }

    public class CloudTicker extends BukkitRunnable{

        private CloudEntity entity;
        private int count;

        public CloudTicker(CloudEntity entity){
            this.entity = entity;
            this.count = 0;
        }

        public void removeAll(){
            entity.armorStand.remove();
            entity.cube.remove();
            entity.removeTag();
            this.cancel();
        }

        @Override
        public void run() {
            if(count == 5){
                removeAll();
                clouds.remove(entity.loc);
                return;
            }
            if(entity.armorStand.isDead() || entity.cube.isDead()){
                removeAll();
                clouds.remove(entity.loc);
                return;
            }
            entity.loc.getWorld().spawnParticle(Particle.CLOUD, entity.armorStand.getEyeLocation().add(0,0.7,0), 4, 0, 0, 0, 0.01);
            count++;
        }
    }



    
}


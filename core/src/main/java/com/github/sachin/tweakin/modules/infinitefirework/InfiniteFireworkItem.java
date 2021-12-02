package com.github.sachin.tweakin.modules.infinitefirework;

import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.TConstants;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;


public class InfiniteFireworkItem extends TweakItem implements Listener{

    public InfiniteFireworkItem(Tweakin plugin) {
        super(plugin, "infinite-firework");
    }

    @Override
    public void register() {
        super.register();
        registerRecipe();
    }

    @Override
    public void unregister() {
        super.unregister();
        unregisterRecipe();
    }

    @EventHandler
    public void onDispense(BlockDispenseEvent e){
        if(isSimilar(e.getItem()) && e.getBlock().getType()==Material.DISPENSER){
            if(!getConfig().getBoolean("dispenser-usable")){
                e.setCancelled(true);
                return;
            }
            ItemStack item = new ItemStack(Material.FIREWORK_ROCKET);
            FireworkMeta meta = (FireworkMeta) item.getItemMeta();
            item.setItemMeta(getRandomFireworkEffect(meta));
            
            e.setItem(item);
        }
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(!isSimilar(e.getItem())) return;
        Player player = e.getPlayer();
        if(e.getAction()==Action.RIGHT_CLICK_AIR && player.isGliding()){
            e.setCancelled(true);
            return;
        }
        if(e.getAction()==Action.RIGHT_CLICK_BLOCK){
            e.setCancelled(true);
            if(!hasPermission(player,Permissions.INFINITEFIREWORK_USE)) return;
            RayTraceResult raytrace = player.rayTraceBlocks(4);
            if(raytrace != null && raytrace.getHitPosition() != null){
                Vector vec = raytrace.getHitPosition();
                BlockFace clickedFace = e.getBlockFace();
                getRandomFireWork(new Location(player.getWorld(),vec.getX() + clickedFace.getModX() * 0.15D,vec.getY() + clickedFace.getModY() * 0.15D,vec.getZ() + clickedFace.getModZ() * 0.15D), player);
            }
        }
    }

    public FireworkMeta getRandomFireworkEffect(FireworkMeta meta){
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        meta.setPower(rand.nextInt(getConfig().getInt("min-flight"),getConfig().getInt("max-flight")));
        for(int i=0;i<rand.nextInt(getConfig().getInt("min-effects"), getConfig().getInt("max-effects"));i++){
            FireworkEffect.Builder builder = FireworkEffect.builder();
            builder
            .flicker(plugin.RANDOM.nextBoolean())
            .trail(plugin.RANDOM.nextBoolean())
            .with(getRandomEnum(Type.class))
            .withColor(Color.fromBGR(rand.nextInt(0,254), rand.nextInt(0, 254), rand.nextInt(0, 254)));
            meta.addEffect(builder.build());
        }
        return meta;
    }

    public Firework getRandomFireWork(Location loc,ProjectileSource source){
        Firework entity = loc.getWorld().spawn(loc, Firework.class);
        entity.getPersistentDataContainer().set(TConstants.INFINITE_FIREWORK_KEY, PersistentDataType.INTEGER, 1);
        
        if(source != null){
            entity.setShooter(source);
        }
        
        entity.setFireworkMeta(getRandomFireworkEffect(entity.getFireworkMeta()));
        return entity;
    }

    @EventHandler
    public void onExplode(EntityDamageByEntityEvent e){
        if(e.getDamager().getPersistentDataContainer().has(TConstants.INFINITE_FIREWORK_KEY, PersistentDataType.INTEGER)){
            e.setCancelled(true);
        }
    }

    public <T extends Enum<T>> T getRandomEnum(Class<T> clazz){
        return Arrays.asList(clazz.getEnumConstants()).get(plugin.RANDOM.nextInt(clazz.getEnumConstants().length));
    }
}

package com.github.sachin.tweakin.modules.confetticreepers;

import java.util.ArrayList;
import java.util.List;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Tweakin;

import org.bukkit.Color;
import org.bukkit.EntityEffect;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class ConfettiCreepers extends BaseTweak implements Listener{

    private FireworkEffect.Builder builder;

    public ConfettiCreepers(Tweakin plugin) {
        super(plugin, "confetti-creepers");
    }

    @Override
    public void reload() {
        super.reload();
        this.builder = FireworkEffect.builder();
        builder.with(Type.BURST);
        List<Integer> colors = getConfig().getIntegerList("colors");
        List<Color> fireWorkColors = new ArrayList<>();
        if(!colors.isEmpty()){
            for (Integer color : colors) {
                fireWorkColors.add(Color.fromRGB(color));
            }
            builder.flicker(true);
            builder.withColor(fireWorkColors);
        }
    }


    @EventHandler
    public void onCreeperExplode(ExplosionPrimeEvent e){
        if(e.getEntityType() != EntityType.CREEPER) return;
        Creeper creeper = (Creeper) e.getEntity();
        if(getBlackListWorlds().contains(creeper.getWorld().getName())) return;
        if(Math.random() < getConfig().getDouble("chance",1))
        {
            e.setCancelled(true);
            Firework firework = creeper.getWorld().spawn(creeper.getLocation().clone().subtract(0,0.2,0), Firework.class);
            FireworkMeta meta = firework.getFireworkMeta();
            meta.addEffect(builder.build());
            firework.setFireworkMeta(meta);
            new BukkitRunnable(){
                public void run() {firework.detonate();};
            }.runTaskLater(getPlugin(), 2);
            creeper.remove();

        }
    }
    
}

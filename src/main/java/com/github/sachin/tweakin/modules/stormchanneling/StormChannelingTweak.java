package com.github.sachin.tweakin.modules.stormchanneling;

import com.github.sachin.prilib.utils.FastItemStack;
import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Config;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

// tweakin.stormchanneling.use
@Tweak(name = "storm-channeling")
public class StormChannelingTweak extends BaseTweak implements Listener {


    @Config(key = "durability-penalty")
    private int durabilityPenalty = 150;

    @Config(key = "player-height")
    private String playerHeightRange = "315-320";

    @Config(key = "trident-height")
    private String tridentHeightRange = "360-365";

    private int minPHeight = 315;
    private int maxPHeight = 320;

    private int minTHeight = 360;
    private int maxTHeight = 365;

    @Override
    public void reload() {
        super.reload();
        String[] split1 = playerHeightRange.split("-");
        String[] split2 = tridentHeightRange.split("-");
        if(split2.length==2){
            minTHeight = Integer.parseInt(split2[0]);
            maxTHeight = Integer.parseInt(split2[1]);
        }
        if(split1.length==2){
            minPHeight = Integer.parseInt(split1[0]);
            maxPHeight = Integer.parseInt(split1[1]);
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Trident) {
            Trident trident = (Trident) event.getEntity();
            if(containsWorld(trident.getWorld())) return;
            if (trident.getShooter() != null && trident.getShooter() instanceof Player) {
                Player player = (Player) trident.getShooter();
                if(!hasPermission(player, Permissions.STORM_CHANELLING)) return;
                World world = player.getWorld();
                if(!world.isThundering() || !world.hasStorm()){
                    ItemStack item = player.getInventory().getItemInMainHand();
                    FastItemStack fItem = new FastItemStack(trident.getItem());
                    if (item.getType() == Material.TRIDENT && item.containsEnchantment(Enchantment.CHANNELING) && fItem.getMaxDurability()-fItem.getDamage()>durabilityPenalty) {
                        if (player.getLocation().getY() >= minPHeight && player.getLocation().getY() <= maxPHeight) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (trident.isDead() || !trident.isValid()) {
                                        this.cancel();
                                        return;
                                    }
                                    Location tridentLocation = trident.getLocation();
                                    if (tridentLocation.getBlockY() >= minTHeight && tridentLocation.getBlockY() <= maxTHeight) {
                                        fItem.setDamage(durabilityPenalty);
                                        trident.setItem(fItem.get());
                                        world.setStorm(true);
                                        world.setThundering(true);
                                        for (int i = 0; i < 4; i++) {
                                            world.strikeLightningEffect(tridentLocation.clone().add(plugin.RANDOM.nextInt(2),plugin.RANDOM.nextInt(2),plugin.RANDOM.nextInt(2)));
                                        }
                                        this.cancel();


                                    }
                                }
                            }.runTaskTimer(plugin, 1L, 1L);
                        }
                    }
                }
            }
        }
    }
}

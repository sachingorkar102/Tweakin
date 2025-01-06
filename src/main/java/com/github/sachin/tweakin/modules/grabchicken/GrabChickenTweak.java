package com.github.sachin.tweakin.modules.grabchicken;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.utils.Permissions;
import com.github.sachin.tweakin.utils.annotations.Tweak;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityDismountEvent;

@Tweak(name = "grab-chicken")
public class GrabChickenTweak extends BaseTweak implements Listener {


    @EventHandler
    public void onRightClickChicken(PlayerInteractEntityEvent e){
        if(e.getRightClicked() instanceof Chicken){
            Chicken chicken = (Chicken) e.getRightClicked();
            Player player = e.getPlayer();
            if(
            chicken.isAdult() &&
            player.getInventory().getItemInMainHand().getType()== Material.AIR &&
            player.getPassengers().isEmpty() &&
            hasPermission(player, Permissions.GRAB_CHICKEN)){
                player.swingMainHand();
                player.addPassenger(chicken);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, -1, 0, false, false, false));
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getPassengers().isEmpty() || event.getClickedBlock()==null || player.getInventory().getItemInMainHand().getType() != Material.AIR) return;
        Entity chicken = player.getPassengers().get(0);
        for(Entity en : player.getPassengers()){
            if(en instanceof  Chicken){
                player.swingMainHand();
                chicken.leaveVehicle();
                chicken.teleport(event.getClickedBlock().getLocation().add(0.5, 1, 0.5));
            }
        }
    }


    @EventHandler
    public void onDismount(EntityDismountEvent event){
        Entity dismountedEntity = event.getEntity();
        Entity vehicle = event.getDismounted();
        if (vehicle instanceof Player) {
            Player player = (Player) vehicle;
            if (dismountedEntity instanceof Chicken && hasEffect(player)) {
                player.removePotionEffect(PotionEffectType.SLOW_FALLING);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (hasEffect(player) &&
                player.getPassengers().stream().noneMatch(entity -> entity instanceof Chicken)) {
            player.removePotionEffect(PotionEffectType.SLOW_FALLING);
        }
    }

    private boolean hasEffect(Player player){
        return player.hasPotionEffect(PotionEffectType.SLOW_FALLING) && player.getPotionEffect(PotionEffectType.SLOW_FALLING).getDuration()==-1;
    }
}

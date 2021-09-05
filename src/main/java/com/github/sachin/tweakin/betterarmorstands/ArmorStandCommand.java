package com.github.sachin.tweakin.betterarmorstands;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.github.sachin.tweakin.Message;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.utils.TConstants;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.RayTraceResult;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;

public class ArmorStandCommand extends BaseCommand{

    private BetterArmorStandTweak instance;
    private final Tweakin plugin = Tweakin.getPlugin();
    private Message messageManager;
    


    public ArmorStandCommand(BetterArmorStandTweak instance){
        this.instance = instance;
        
        this.messageManager = instance.getTweakManager().getMessageManager();
        plugin.replacements.addReplacement("tweakinarmorstandcommand", instance.getConfig().getString("alias","as|armorstand"));
    }

    @Default
    @CommandAlias("%tweakinarmorstandcommand")
    @CommandCompletion("last|near")
    public void onCommand(Player player,String[] args){
        if(instance.getBlackListWorlds().contains(player.getWorld().getName())) return;
        if(!player.hasPermission("tweakin.betterarmorstands.command")){
            player.sendMessage(messageManager.getMessage("no-permission"));
            return;
        }
        if(args.length==0){
            RayTraceResult result = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getEyeLocation().getDirection(), 5, (entity)-> (entity instanceof ArmorStand));
            if(result != null && result.getHitEntity() != null){
                ArmorStand as = (ArmorStand) result.getHitEntity();
                if(canBuild(player, as)){
                    ASGuiHolder.openGui(player, as);
                }
            }
            else{
                player.sendMessage(messageManager.getMessage("look-at-armorstand"));
            }
        }
        else if(args.length==1){
            if(args[0].equalsIgnoreCase("near")){
                List<Entity> stands = player.getNearbyEntities(5, 5, 5).stream().filter(e -> (e instanceof ArmorStand)).collect(Collectors.toList());
                if(!stands.isEmpty()){
                    ArmorStand as = (ArmorStand) stands.get(0);
                    if(canBuild(player, as)){
                        ASGuiHolder.openGui(player, as);
                    }
                }
                else{
                    player.sendMessage(messageManager.getMessage("no-armorstand-near"));
                }

            }
            else if(args[0].equalsIgnoreCase("last")){
                UUID uuid = instance.cachedAsList.get(player.getUniqueId());
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
        }
    }

    private boolean canBuild(Player player,ArmorStand as){
        PlayerInteractEntityEvent event = new PlayerInteractEntityEvent(player, as);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }
    
}

package com.github.sachin.tweakin.commands;

import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Message;
import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.gui.PagedGuiHolder;
import com.github.sachin.tweakin.modules.betterarmorstands.BetterArmorStandTweak;
import com.github.sachin.tweakin.modules.betterarmorstands.PresetPose;
import com.github.sachin.tweakin.modules.mobheads.Head;
import com.google.common.base.Enums;
import com.google.common.base.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;

@Description("core command for tweakin")
@CommandAlias("tw|tweakin")
public class CoreCommand extends BaseCommand{
 

    @Dependency
    private Tweakin plugin;

    private Message messageManager;


    public CoreCommand(Tweakin plugin){
        this.plugin = plugin;
        this.messageManager = plugin.getTweakManager().getMessageManager();
        
    }

    // /tweakin removepose [pose-name]
    @Subcommand("removepose")
    @CommandCompletion("@tweakinposes")
    public void onRemovePose(CommandSender sender,String[] args){
        if(args.length != 1) return;
        if(!sender.hasPermission("tweakin.command.removepose")){
            sender.sendMessage(messageManager.getMessage("no-permission"));
            return;
        }
        BetterArmorStandTweak tweak = (BetterArmorStandTweak) plugin.getTweakManager().getTweakFromName("better-armorstands");
        if(!tweak.registered){
            sender.sendMessage(messageManager.getMessage("tweak-is-disabled").replace("%tweak%", "better-armorstands"));
            return;
        }
        String id = args[0];
        if(tweak.getPoseManager().getPoses().remove(id) != null){
            sender.sendMessage(messageManager.getMessage("pose-removed"));
        }

    }

    // /tweakin addpose [id] [display]
    @Subcommand("addpose")
    public void onAddPose(Player player,String[] args){
        if(args.length != 2) return;
        if(!player.hasPermission("tweakin.command.addpose")){
            player.sendMessage(messageManager.getMessage("no-permission"));
            return;
        }
        BetterArmorStandTweak tweak = (BetterArmorStandTweak) plugin.getTweakManager().getTweakFromName("better-armorstands");
        if(!tweak.registered){
            player.sendMessage(messageManager.getMessage("tweak-is-disabled").replace("%tweak%", "better-armorstands"));
            return;
        }
        String id = args[0];
        String display = args[1];
        if(tweak.getPoseManager().getPoses().containsKey(id)){
            player.sendMessage(messageManager.getMessage("pose-exists").replace("%pose%", id));
            return;
        }
        RayTraceResult result = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getEyeLocation().getDirection(), 5, (entity)-> (entity instanceof ArmorStand));
        if(result != null && result.getHitEntity() != null){
            ArmorStand as = (ArmorStand) result.getHitEntity();
            PresetPose pose = new PresetPose(id,display, as.getHeadPose(), as.getBodyPose(), as.getLeftArmPose(), as.getRightArmPose(), as.getLeftLegPose(), as.getRightLegPose());
            tweak.getPoseManager().addPose(pose);
            player.sendMessage(messageManager.getMessage("pose-added"));
        }
        else{
            player.sendMessage(messageManager.getMessage("look-at-armorstand"));
        }
    }

    


    @Subcommand("reload")
    public void onReloadCommand(CommandSender sender){
        if(!sender.hasPermission("tweakin.command.reload")){
            sender.sendMessage(messageManager.getMessage("no-permission"));
            return;
        }
        plugin.getTweakManager().reload();
        sender.sendMessage(messageManager.getMessage("reloaded"));
    }


    @Subcommand("toggle")
    @CommandCompletion("@tweaklist")
    public void onConfigureCommand(CommandSender sender,String[] args){
        if(!sender.hasPermission("tweakin.command.toggle")){
            sender.sendMessage(messageManager.getMessage("no-permission"));
            return;
        }
        if(args.length == 0 && sender instanceof Player){
            PagedGuiHolder gui = new PagedGuiHolder(plugin,(Player) sender);
            gui.openPage();
        }
        else if(args.length == 1){
            BaseTweak t = plugin.getTweakManager().getTweakFromName(args[0]);
            if(t != null){
                if(t.shouldEnable()){
                    plugin.getTweakManager().getGuiMap().put(t, false);
                    sender.sendMessage(messageManager.getMessage("tweak-disabled").replace("%tweak%", args[0]));
                }
                else{
                    plugin.getTweakManager().getGuiMap().put(t, true);
                    sender.sendMessage(messageManager.getMessage("tweak-enabled").replace("%tweak%", args[0]));
                }
                plugin.getTweakManager().reload();
            }
            else{
                sender.sendMessage(messageManager.getMessage("invalid-tweak"));
            }
        }
    }


    @Subcommand("give")
    @CommandCompletion("@players @tweakitems @nothing")
    public void onGiveCommand(CommandSender sender,String[] args){
        if(!sender.hasPermission("tweakin.command.give")){
            sender.sendMessage(messageManager.getMessage("no-permission"));
            return;
        }
        if(args.length < 2) return;
        Player player = Bukkit.getPlayer(args[0]);
        TweakItem tItem = plugin.getTweakManager().getTweakItem(args[1]);
        if(player == null){
            sender.sendMessage(messageManager.getMessage("invalid-player"));
            return;
        }
        if(tItem == null){
            sender.sendMessage(messageManager.getMessage("invalid-item"));
            return;
        }
        int amount = 1;
        if(args.length == 3){
            amount = Integer.parseInt(args[2]);
        }
        ItemStack item = tItem.getItem().clone();
        item.setAmount(amount);
        player.getInventory().addItem(item);
        sender.sendMessage(messageManager.getMessage("gave-item").replace("%item%", args[1]).replace("%player%", player.getName()));
        
    }

    @Subcommand("tweak-list")
    public void onList(CommandSender sender){
        if(!sender.hasPermission("tweakin.command.list")){
            sender.sendMessage(messageManager.getMessage("no-permission"));
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append(ChatColor.YELLOW+"--------==Tweak-List==--------\n");
        builder.append(ChatColor.translateAlternateColorCodes('&', "&a&lEnabled Tweaks")+"\n");
        for(BaseTweak t : plugin.getTweakManager().getTweakList()){
            if(t.registered){
                builder.append(ChatColor.GREEN+t.getName()+ChatColor.RESET+", ");
            }
        }
        builder.append("\n");
        builder.append(ChatColor.translateAlternateColorCodes('&', "&c&lDisabled Tweaks")+"\n");
        for(BaseTweak t : plugin.getTweakManager().getTweakList()){
            if(!t.registered){
                builder.append(ChatColor.RED+t.getName()+ChatColor.RESET+", ");
            }
        }
        builder.append("\n");
        builder.append(ChatColor.YELLOW+"------------------------------");
        sender.sendMessage(builder.toString());

    }


    // /tw givehead [playername] [head-name] [amount]
    @Subcommand("givehead")
    @CommandCompletion("@players @tweakinheads @nothing")
    public void onGiveHeadCommand(CommandSender sender,String[] args){
        if(!sender.hasPermission("tweakin.command.givehead")){
            sender.sendMessage(messageManager.getMessage("no-permission"));
            return;
        }
        if(!plugin.getTweakManager().getTweakFromName("mob-heads").registered){
            sender.sendMessage(messageManager.getMessage("tweak-is-disabled").replace("%tweak%", "mob-heads"));
            return;
        }
        if(args.length < 2) return;
        Player player = Bukkit.getPlayer(args[0]);
        Optional<Head> oHead = Enums.getIfPresent(Head.class, args[1]);
        if(player == null){
            sender.sendMessage(messageManager.getMessage("invalid-player"));
            return;
        }
        if(args[1].equals("ALL")){
            for(Head h : Head.values()){
                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), h.getSkull().clone());
            }
            return;
        }
        if(!oHead.isPresent()){
            sender.sendMessage(messageManager.getMessage("invalid-item"));
            return;
        }
        int amount = 1;
        if(args.length == 3){
            amount = Integer.parseInt(args[2]);
        }
        ItemStack item = oHead.get().getSkull().clone();
        item.setAmount(amount);
        player.getInventory().addItem(item);
        sender.sendMessage(messageManager.getMessage("gave-head").replace("%head%", args[1]).replace("%player%", player.getName()));

    }


    
}

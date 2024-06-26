package com.github.sachin.tweakin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.github.sachin.tweakin.BaseTweak;
import com.github.sachin.tweakin.Message;
import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.gui.PagedGuiHolder;
import com.github.sachin.tweakin.modules.betterarmorstands.BetterArmorStandTweak;
import com.github.sachin.tweakin.modules.betterarmorstands.PresetPose;
import com.github.sachin.tweakin.modules.mobheads.Head;
import com.github.sachin.tweakin.utils.annotations.CommandInfo;
import com.google.common.base.Enums;
import com.google.common.base.Optional;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Description("core command for tweakin")
@CommandAlias("tw|tweakin")
public class CoreCommand extends BaseCommand{
 

    @Dependency
    private Tweakin plugin;

    private Message messageManager;

    @Default
    public void onHelp(CommandSender sender,String[] args){
        if(args.length==0 && hasPermission(sender,"command.help")){

            onHelp(sender);
        }
    }

    public void onHelp(CommandSender sender){
        StringBuilder builder = new StringBuilder();
        Message messageManager = plugin.getTweakManager().getMessageManager();
        if(sender instanceof Player){
            builder.append(ChatColor.translateAlternateColorCodes('&',"----------&aTweakin&f----------\n"));
        }
        else{
            builder.append("\n");
            builder.append(ChatColor.translateAlternateColorCodes('&',messageManager.getPrefix()+"----------&aTweakin&f----------\n"));
        }
        for(Method method : CoreCommand.class.getDeclaredMethods()){
            if(method.isAnnotationPresent(CommandInfo.class)){
                CommandInfo info = method.getAnnotation(CommandInfo.class);
                String format = null;
                if(sender instanceof Player){
                    format = messageManager.getMessageWithoutPrefix("help-command-format");
                }
                else{
                    format = messageManager.getMessage("help-command-format");
                }
                String command = format
                        .replace("%syntax%",info.syntax())
                        .replace("%perm%",info.perm())
                        .replace("%description%",info.description());
                builder.append(ChatColor.translateAlternateColorCodes('&',command));
                builder.append("\n");
            }
        }
        if(sender instanceof Player){
            builder.append(ChatColor.translateAlternateColorCodes('&',"---------------------------"));
        }
        else{
            builder.append(ChatColor.translateAlternateColorCodes('&',messageManager.getPrefix()+"---------------------------"));
        }
        sender.sendMessage(builder.toString());
    }



    public CoreCommand(Tweakin plugin){
        this.plugin = plugin;
        this.messageManager = plugin.getTweakManager().getMessageManager();
        
    }

    //    /tweakin reload
    @Subcommand("reload")
    @CommandInfo(syntax = "&a/tweakin reload",perm = "tweakin.command.reload",description = "reloads all config files")
    public void onReloadCommand(CommandSender sender){
        if(!hasPermission(sender,"command.reload")){
            sender.sendMessage(messageManager.getMessage("no-permission"));
            return;
        }
        plugin.getTweakManager().reload();
        int registeredTweaks = plugin.getTweakManager().getTweakList().stream().filter(t -> t.registered).collect(Collectors.toList()).size();
        sender.sendMessage(messageManager.getMessage("&6Registered &e"+registeredTweaks+" &6tweaks successfully"));

    }

    //    /tweakin toggle (tweak-name)
    @Subcommand("toggle")
    @CommandCompletion("@tweaklist")
    @CommandInfo(syntax = "&a/tweakin toggle &7(tweak)",perm = "tweakin.command.toggle",description = "toggles a tweak if specified or opens a gui")
    public void onConfigureCommand(CommandSender sender,String[] args){
        if(!hasPermission(sender,"command.toggle")){
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
                int registeredTweaks = plugin.getTweakManager().getTweakList().stream().filter(tweak -> tweak.registered).collect(Collectors.toList()).size();
                sender.sendMessage(messageManager.getMessage("&6Registered &e"+registeredTweaks+" &6tweaks successfully"));
            }
            else{
                sender.sendMessage(messageManager.getMessage("invalid-tweak"));
            }
        }
    }

    @Subcommand("tweak-list")
    @CommandInfo(syntax = "&a/tweakin tweak-list",perm = "tweakin.command.list",description = "lists enabled and disabled tweaks")
    public void onList(CommandSender sender){
        if(!hasPermission(sender,"command.list")){
            sender.sendMessage(messageManager.getMessage("no-permission"));
            return;
        }
        List<String> enabled = new ArrayList<>();
        List<String> disabled = new ArrayList<>();
        for(BaseTweak t : plugin.getTweakManager().getTweakList()){
            if(t.registered){
                enabled.add(t.getName());
            }
            else{
                disabled.add(t.getName());
            }
        }
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append(ChatColor.YELLOW+"--------==Tweak-List==--------\n");
        builder.append(ChatColor.translateAlternateColorCodes('&', "&a&lEnabled Tweaks("+enabled.size()+")")+"\n");
        for(String s : enabled){
            builder.append(ChatColor.GREEN+s+ChatColor.RESET+", ");
        }
        builder.append("\n");
        builder.append(ChatColor.translateAlternateColorCodes('&', "&c&lDisabled Tweaks("+disabled.size()+")")+"\n");
        for(String s : disabled){
            builder.append(ChatColor.RED+s+ChatColor.RESET+", ");
        }

        if(plugin.getTweakManager().getIncompaitableTweaks().size() > 0){
            builder.append("\n");
            builder.append(ChatColor.translateAlternateColorCodes('&', "&e&lIncompaitable Tweaks("+plugin.getTweakManager().getIncompaitableTweaks().size()+")")+"\n"+ChatColor.RESET+ChatColor.YELLOW+"Look into Tweak's description in config.yml for more info"+"\n");
            for(String s : plugin.getTweakManager().getIncompaitableTweaks()){
                builder.append(ChatColor.YELLOW+s+ChatColor.RESET+", ");
            }
        }
        builder.append("\n");
        builder.append(ChatColor.YELLOW+"------------------------------");
        sender.sendMessage(builder.toString());

    }

    //    /tweakin give [player] [item] (amount)
    @Subcommand("give")
    @CommandCompletion("@players @tweakitems @nothing")
    @CommandInfo(syntax = "&a/tweakin give &7[player] [item] (amount)",perm = "tweakin.command.give",description = "gives specified player a tweakin item")
    public void onGiveCommand(CommandSender sender,String[] args){
        if(!hasPermission(sender,"command.give")){
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

    // /tw givehead [player] [head] (amount)
    @Subcommand("givehead")
    @CommandCompletion("@players @tweakinheads @nothing")
    @CommandInfo(syntax = "&a/tweakin givehead &7[player] [head] (amount)",perm = "tweakin.command.givehead",description = "gives specified player a mob head")
    public void onGiveHeadCommand(CommandSender sender,String[] args){
        if(!hasPermission(sender,"command.givehead")){
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
        if(args[1].equals("WOLVES")){
            for(Head h : Head.values()){
                if(h.getEntityType().equalsIgnoreCase("WOLF")){
                    player.getLocation().getWorld().dropItemNaturally(player.getLocation(),h.getSkull().clone());
                }
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

    // /tweakin addpose [id] [display]
    @Subcommand("addpose")
    @CommandInfo(syntax = "&a/tweakin addpose &7[id] [display]",perm = "tweakin.command.addpose",description = "adds the pose of armorstand you are looking at to list")
    public void onAddPose(Player player,String[] args){
        if(args.length != 2) return;
        if(!hasPermission(player,"command.addpose")){
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

    // /tweakin removepose [pose-name]
    @Subcommand("removepose")
    @CommandInfo(syntax = "&a/tweakin removepose &7[pose]",perm="tweakin.command.removepose",description = "removes a armorstand pose from list")
    @CommandCompletion("@tweakinposes")
    public void onRemovePose(CommandSender sender,String[] args){
        if(args.length != 1) return;
        if(!hasPermission(sender,"command.removepose")){
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

    @Subcommand("help")
    @CommandInfo(syntax = "&a/tweakin help",perm = "tweakin.command.help",description="shows all tweakin commands, which you are looking at")
    public void onHelpCommand(CommandSender sender,String[] args){
        if(hasPermission(sender,"command.help")){
            onHelp(sender);
        }
    }



    // private boolean hasPermission(Player player,String permission){
    //     if(plugin.getConfig().getBoolean("check-permissions-for-core-commands")){
    //         return player.hasPermission(permission);
    //     }
    //     else{
    //         return player.isOp();
    //     }
    // }

    private boolean hasPermission(CommandSender sender,String permission){
        if(plugin.getConfig().getBoolean("check-permissions-for-core-commands")){
            return sender.hasPermission(permission);
        }
        else{
            return sender.isOp();
        }
    }


    
}

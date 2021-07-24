package com.github.sachin.tweakin.commands;

import com.github.sachin.tweakin.Message;
import com.github.sachin.tweakin.TweakItem;
import com.github.sachin.tweakin.Tweakin;
import com.github.sachin.tweakin.gui.PagedGuiHolder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
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

    @Subcommand("reload")
    public void onReloadCommand(CommandSender sender){
        if(!sender.hasPermission("tweakin.command.reload")){
            sender.sendMessage(messageManager.getMessage("no-permission"));
            return;
        }
        plugin.getTweakManager().reload();
        sender.sendMessage(messageManager.getMessage("reloaded"));
    }

    @Subcommand("configure")
    public void onConfigureCommand(Player player){
        if(!player.hasPermission("tweakin.command.configure")){
            player.sendMessage(messageManager.getMessage("no-permission"));
            return;
        }
        PagedGuiHolder gui = new PagedGuiHolder(plugin, player);
        gui.openPage();
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
}

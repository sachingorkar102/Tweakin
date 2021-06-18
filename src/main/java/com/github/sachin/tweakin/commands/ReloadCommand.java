package com.github.sachin.tweakin.commands;

import com.github.sachin.tweakin.Tweakin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;

@Description("reloads config files from tweakin")
@CommandAlias("tw|tweakin")
public class ReloadCommand extends BaseCommand{
 
    
    @Dependency
    private Tweakin plugin;

    public ReloadCommand(Tweakin plugin){
        this.plugin = plugin;
    }

    @Subcommand("reload")
    @CommandPermission("tweakin.command.reload")
    public void onCommand(CommandSender sender){
        plugin.getTweakManager().reload();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aConfig files successfully loaded"));
    }
}

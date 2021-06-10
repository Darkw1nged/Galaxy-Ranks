package me.darkwinged.GalaxyRankup.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.darkwinged.GalaxyRankup.Main;
import me.darkwinged.GalaxyRankup.Utils.Utils;

public class cmd_SetRank implements CommandExecutor {
	
	private Main plugin;
	public cmd_SetRank(Main plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setrank")) {
        	if (!(sender instanceof Player)) {
        		if (args.length != 2) {
            		sender.sendMessage(Utils.chat("&cUsage: /setrank [Player] [rank]"));
            		return true;
            	}
            	Player target = Bukkit.getPlayer(args[0]);
            	if (target == null) {
            		sender.sendMessage(Utils.chat("&cThat player could not be found."));
            		return true;
            	}
            	if (!plugin.Ranks.containsValue(args[1])) {
            		sender.sendMessage(Utils.chat("&cThat is not a valid rank!"));
            		return true;
            	}
            	plugin.Player_Rank.put(target.getName(), args[1]);
            	sender.sendMessage(Utils.chat("&aYou have set " + target.getName() + " rank to " + args[1]));
            	return true;
        	}
        	Player player = (Player)sender;
            if (player.hasPermission("GRankup.Set.Rank")) {
            	if (args.length != 2) {
            		player.sendMessage(Utils.chat("&cUsage: /setrank [Player] [rank]"));
            		return true;
            	}
            	Player target = Bukkit.getPlayer(args[0]);
            	if (target == null) {
            		player.sendMessage(Utils.chat("&cThat player could not be found."));
            		return true;
            	}
            	if (!plugin.Ranks.containsValue(args[1])) {
            		sender.sendMessage(Utils.chat("&cThat is not a valid rank!"));
            		return true;
            	}
            	plugin.Player_Rank.put(target.getName(), args[1]);
            	player.sendMessage(Utils.chat("&aYou have set " + target.getName() + " rank to " + args[1]));
            	
            } else {
                sender.sendMessage(Utils.chat("&cYou do not have permission to perform this command"));
            }
        }
        return false;
    }
}
package me.darkwinged.GalaxyRankup.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.darkwinged.GalaxyRankup.Main;
import me.darkwinged.GalaxyRankup.Utils.Utils;

public class cmd_SetPrestige implements CommandExecutor {
	
	private Main plugin;
	public cmd_SetPrestige(Main plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setprestige")) {
        	if (!(sender instanceof Player)) {
        		if (args.length != 2) {
            		sender.sendMessage(Utils.chat("&cUsage: /setprestige [Player] [rank]"));
            		return true;
            	}
            	Player target = Bukkit.getPlayer(args[0]);
            	if (target == null) {
            		sender.sendMessage(Utils.chat("&cThat player could not be found."));
            		return true;
            	}
            	if (!plugin.Prestige_Ranks.contains(args[1])) {
            		sender.sendMessage(Utils.chat("&cThat is not a valid rank!"));
            		return true;
            	}
            	plugin.Player_Prestige.put(target.getName(), args[1]);
            	sender.sendMessage(Utils.chat("&aYou have set " + target.getName() + " prestige rank to " + args[1]));
            	return true;
        	}
        	Player player = (Player)sender;
            if (player.hasPermission("GRankup.Set.Prestiges")) {
            	if (args.length != 2) {
            		player.sendMessage(Utils.chat("&cUsage: /setprestige [Player] [rank]"));
            		return true;
            	}
            	Player target = Bukkit.getPlayer(args[0]);
            	if (target == null) {
            		player.sendMessage(Utils.chat("&cThat player could not be found."));
            		return true;
            	}
            	if (!plugin.Prestige_Ranks.contains(args[1])) {
            		sender.sendMessage(Utils.chat("&cThat is not a valid rank!"));
            		return true;
            	}
            	plugin.Player_Prestige.put(target.getName(), args[1]);
            	player.sendMessage(Utils.chat("&aYou have set " + target.getName() + " prestige rank to " + args[1]));
            	
            } else {
                sender.sendMessage(Utils.chat("&cYou do not have permission to perform this command"));
            }
        }
        return false;
    }
	

}

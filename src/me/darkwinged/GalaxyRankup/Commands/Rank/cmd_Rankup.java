package me.darkwinged.GalaxyRankup.Commands.Rank;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.darkwinged.GalaxyRankup.Main;
import me.darkwinged.GalaxyRankup.Utils.RanksFile;
import me.darkwinged.GalaxyRankup.Utils.Utils;

public class cmd_Rankup implements CommandExecutor {
	
	private Main plugin;
	private RanksFile rf;
	public cmd_Rankup(Main plugin, RanksFile rf) {
		this.plugin = plugin;
		this.rf = rf;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rankup")) {
        	if (!(sender instanceof Player)) {
        		sender.sendMessage(Utils.chat("&cYou can not do this command in console."));
        		return true;
        	}
        	Player player = (Player)sender;
            if (player.hasPermission("GRankup.Rankup")) {
            	if (plugin.Player_Rank.containsKey(player.getName())) {
            		if (plugin.Ranks == null) return true;
            		if (plugin.Ranks.get(plugin.Ranks.size()).equalsIgnoreCase(plugin.Player_Rank.get(player.getName()))) {
            			player.sendMessage(Utils.chat("&cYou are at the max rank please use /prestiege"));
            			return true;
            		}
            		String current = plugin.Player_Rank.get(player.getName());
        			int next = plugin.Ranks_List.indexOf(current) + 2;
        			String next_rank = plugin.Ranks.get(next);
            		if (plugin.Player_Prestige.containsKey(player.getName())) {
            			int multiplier = rf.getConfig().getInt("Prestiges.Ranks." + plugin.Player_Prestige.get(player.getName()) + ".rank multiplier");
            			double amount = rf.getConfig().getInt("Ranks." + next_rank + ".cost") * multiplier;
            			if (amount <= plugin.getEconomy().getBalance(player)) {
         				   plugin.getEconomy().withdrawPlayer(player, amount);
         				   player.sendMessage(Utils.chat(plugin.getConfig().getString("Rankup Message")
         						   .replaceAll("%PreviousRank%", current)
         						   .replaceAll("%NewRank%", next_rank)
         						   .replaceAll("%Amount%", ""+amount)));
         				   plugin.Player_Rank.put(player.getName(), next_rank);
         				   
         			    } else {
         			    	player.sendMessage(Utils.chat("&cYou do not have enough money for this!"));
         			    }
            			return true;
            		}
        			double amount = rf.getConfig().getInt("Ranks." + next_rank + ".cost");
        			
        			if (amount <= plugin.getEconomy().getBalance(player)) {
        				plugin.getEconomy().withdrawPlayer(player, amount);
     				   	player.sendMessage(Utils.chat(plugin.getConfig().getString("Rankup Message")
     						   .replaceAll("%PreviousRank%", current)
     						   .replaceAll("%NewRank%", next_rank)
     						   .replaceAll("%Amount%", ""+amount)));
     				   	plugin.Player_Rank.put(player.getName(), next_rank);
     			    } else {
     			    	player.sendMessage(Utils.chat("&cYou do not have enough money for this!"));
     			    }
            	}
            } else {
                sender.sendMessage(Utils.chat("&cYou do not have permission to perform this command"));
            }
        }
        return false;
    }
	

}

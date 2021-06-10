package me.darkwinged.GalaxyRankup.Commands.Rank;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.darkwinged.GalaxyRankup.Main;
import me.darkwinged.GalaxyRankup.Utils.RanksFile;
import me.darkwinged.GalaxyRankup.Utils.Utils;

public class cmd_AutoRankup implements CommandExecutor {
	
	private Main plugin;
	private RanksFile rf;
	public cmd_AutoRankup(Main plugin, RanksFile rf) {
		this.plugin = plugin;
		this.rf = rf;
	}
	BukkitTask task;
	
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("autorankup")) {
        	if (!(sender instanceof Player)) {
        		sender.sendMessage(Utils.chat("&cYou can not do this command in console."));
        		return true;
        	}
        	Player player = (Player)sender;
            if (player.hasPermission("GRankup.Rankup.Auto")) {
            	if (plugin.Auto_Rank.contains(player.getUniqueId())) {
            		player.sendMessage(Utils.chat("&cYou now have disabled automatic rankup."));
            		plugin.Auto_Rank.remove(player.getUniqueId());
            		task.cancel();
            		return true;
            	}
            	plugin.Auto_Rank.add(player.getUniqueId());
            	player.sendMessage(Utils.chat("&aYou now have enabled automatic rankup."));
            	task = new BukkitRunnable() {
                    public void run() {
                    	if (!plugin.Auto_Rank.contains(player.getUniqueId())) return;
                    	if (plugin.Player_Rank.containsKey(player.getName())) {
                    		if (plugin.Ranks == null) return;
                    		if (plugin.Ranks.get(plugin.Ranks.size()).equalsIgnoreCase(plugin.Player_Rank.get(player.getName()))) {
                    			return;
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
                 			    }
                    			return;
                    		}
                			double amount = rf.getConfig().getInt("Ranks." + next_rank + ".cost");
                			
                			if (amount <= plugin.getEconomy().getBalance(player)) {
                				plugin.getEconomy().withdrawPlayer(player, amount);
             				   player.sendMessage(Utils.chat(plugin.getConfig().getString("Rankup Message")
             						   .replaceAll("%PreviousRank%", current)
             						   .replaceAll("%NewRank%", next_rank)
             						   .replaceAll("%Amount%", ""+amount)));
             				   plugin.Player_Rank.put(player.getName(), next_rank);
             			    }
                    	}
                    }
                }.runTaskTimer(plugin, 0L, 1L);
            } else {
                sender.sendMessage(Utils.chat("&cYou do not have permission to perform this command"));
            }
        }
        return false;
    }
	

}
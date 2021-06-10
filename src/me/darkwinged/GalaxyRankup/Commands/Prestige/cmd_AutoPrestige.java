package me.darkwinged.GalaxyRankup.Commands.Prestige;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.darkwinged.GalaxyRankup.Main;
import me.darkwinged.GalaxyRankup.Utils.RanksFile;
import me.darkwinged.GalaxyRankup.Utils.Utils;

public class cmd_AutoPrestige implements CommandExecutor {
	
	private Main plugin;
	private RanksFile rf;
	public cmd_AutoPrestige(Main plugin, RanksFile rf) {
		this.plugin = plugin;
		this.rf = rf;
	}
	BukkitTask task;
	
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("autoprestige")) {
        	if (!(sender instanceof Player)) {
        		sender.sendMessage(Utils.chat("&cYou can not do this command in console."));
        		return true;
        	}
        	Player player = (Player)sender;
            if (player.hasPermission("GRankup.Prestige.Auto")) {
            	if (plugin.Auto_Prestige.contains(player.getUniqueId())) {
            		player.sendMessage(Utils.chat("&cYou now have disabled automatic prestige."));
            		plugin.Auto_Prestige.remove(player.getUniqueId());
            		task.cancel();
            		return true;
            	}
            	plugin.Auto_Prestige.add(player.getUniqueId());
            	player.sendMessage(Utils.chat("&aYou now have enabled automatic prestige."));
            	task = new BukkitRunnable() {
                    public void run() {
                    	if (!plugin.Auto_Prestige.contains(player.getUniqueId())) return;
                    	if (plugin.Ranks.get(plugin.Ranks.size()).equalsIgnoreCase(plugin.Player_Rank.get(player.getName()))) {
                			String current = plugin.Player_Prestige.get(player.getName());
    	        			int next = plugin.Prestige_Ranks.indexOf(current) + 1;
    	        			String next_rank = plugin.Prestige_Ranks.get(next);
    	        			double amount = rf.getConfig().getInt("Prestiges.Ranks." + next_rank + ".cost");	
    	        			if (rf.getConfig().getBoolean("Prestiges.Settings.Rank Reset", true)) {
    	        				if (amount <= plugin.getEconomy().getBalance(player)) {
    		        				plugin.getEconomy().withdrawPlayer(player, amount);
    		     				   	player.sendMessage(Utils.chat(plugin.getConfig().getString("Prestige Message")
    		     						   .replaceAll("%Prestige%", next_rank)
    		     						   .replaceAll("%Amount%", ""+amount)));
    		     				   	plugin.Player_Prestige.put(player.getName(), next_rank);
    		     				   	String first = plugin.Ranks_List.get(0);
    		     				    plugin.Player_Rank.put(player.getName(), first);
    		     			    } else {
    		     			    	player.sendMessage(Utils.chat("&cYou do not have enough money for this!"));
    		     			    }
    	        				return;
    	        			}
    	        			if (amount <= plugin.getEconomy().getBalance(player)) {
    	        				plugin.getEconomy().withdrawPlayer(player, amount);
    	     				   	player.sendMessage(Utils.chat(plugin.getConfig().getString("Prestige Message")
    	     						   .replaceAll("%Prestige%", next_rank)
    	     						   .replaceAll("%Amount%", ""+amount)));
    	     				   	plugin.Player_Prestige.put(player.getName(), next_rank);
    	     			    } else {
    	     			    	player.sendMessage(Utils.chat("&cYou do not have enough money for this!"));
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
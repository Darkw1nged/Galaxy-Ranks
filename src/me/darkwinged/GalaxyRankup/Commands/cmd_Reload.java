package me.darkwinged.GalaxyRankup.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.darkwinged.GalaxyRankup.Main;
import me.darkwinged.GalaxyRankup.Utils.RanksFile;
import me.darkwinged.GalaxyRankup.Utils.Utils;

public class cmd_Reload implements CommandExecutor {
	
	private Main plugin;
	private RanksFile ranksFile;
	public cmd_Reload(Main plugin, RanksFile ranksFile) {
		this.plugin = plugin;
		this.ranksFile = ranksFile;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("grankup")) {
            if (sender.hasPermission("GRankup.Reload")) {
            	plugin.saveRanks();
            	plugin.saveConfig();
            	
                plugin.reloadConfig();
                ranksFile.reloadConfig();
                plugin.loadRank();
                sender.sendMessage(Utils.chat("&eConfiguration files have has been reloaded."));
            } else {
                sender.sendMessage(Utils.chat("&cYou do not have permission to perform this command"));
            }
        }
        return false;
    }
	

}

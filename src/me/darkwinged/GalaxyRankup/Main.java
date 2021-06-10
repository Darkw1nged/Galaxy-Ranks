package me.darkwinged.GalaxyRankup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.darkwinged.GalaxyRankup.Commands.cmd_Reload;
import me.darkwinged.GalaxyRankup.Commands.cmd_SetPrestige;
import me.darkwinged.GalaxyRankup.Commands.cmd_SetRank;
import me.darkwinged.GalaxyRankup.Commands.Prestige.cmd_AutoPrestige;
import me.darkwinged.GalaxyRankup.Commands.Prestige.cmd_Prestige;
import me.darkwinged.GalaxyRankup.Commands.Rank.cmd_AutoRankup;
import me.darkwinged.GalaxyRankup.Commands.Rank.cmd_Rankup;
import me.darkwinged.GalaxyRankup.Utils.PlaceHolders;
import me.darkwinged.GalaxyRankup.Utils.RanksFile;
import me.darkwinged.GalaxyRankup.Utils.Utils;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin implements Listener {

	private RanksFile ranksfile;
	private static Economy econ = null;

	public List<UUID> Auto_Rank = new ArrayList<>();
	public HashMap<String, String> Player_Rank = new HashMap<>();
	public HashMap<Integer, String> Ranks = new HashMap<>();
	public List<String> Ranks_List = new ArrayList<>();
	
	public List<UUID> Auto_Prestige = new ArrayList<>();
	public HashMap<String, String> Player_Prestige = new HashMap<>();
	public List<String> Prestige_Ranks = new ArrayList<>();
	public List<String> Prestige_List = new ArrayList<>();
	
	public void onEnable() {
		if (!setupEconomy() ) {
            getServer().getConsoleSender().sendMessage(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceHolders(this).register();
        }
		ranksfile = new RanksFile(this, "Ranks");
		ranksfile.saveDefaultConfig();
		loadCommands();
		loadRank();
		loadRanks();
		loadPrestiges();
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable() {
		saveRanks();
		saveConfig();
	}
	
	public void loadCommands() {
		getCommand("grankup").setExecutor(new cmd_Reload(this, ranksfile));
		getCommand("setprestige").setExecutor(new cmd_SetPrestige(this));
		getCommand("setrank").setExecutor(new cmd_SetRank(this));
		getCommand("rankup").setExecutor(new cmd_Rankup(this, ranksfile));
		getCommand("prestige").setExecutor(new cmd_Prestige(this, ranksfile));
		getCommand("autorankup").setExecutor(new cmd_AutoRankup(this, ranksfile));
		getCommand("autoprestige").setExecutor(new cmd_AutoPrestige(this, ranksfile));
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public Economy getEconomy() {
        return econ;
    }
	
	public void saveRanks() {
        for (Map.Entry<String, String> entry : Player_Rank.entrySet()) {
        	getConfig().set("Player Data.Ranks." + entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : Player_Prestige.entrySet()) {
        	getConfig().set("Player Data.Prestiges." + entry.getKey(), entry.getValue());
        }
    }
	
	public void loadRanks() {
		if (!ranksfile.getConfig().contains("Ranks")) return;
		int total = 0;
        for (String key : ranksfile.getConfig().getConfigurationSection("Ranks").getKeys(false)) {
        	total += 1;
        	Ranks.put(total, key);
        }
        for (String key : ranksfile.getConfig().getConfigurationSection("Ranks").getKeys(false)) {
        	Ranks_List.add(key);
        }
	}
	
	public void loadPrestiges() {
		if (!ranksfile.getConfig().contains("Prestiges.Ranks")) return;
        for (String key : ranksfile.getConfig().getConfigurationSection("Prestiges.Ranks").getKeys(false)) {
        	Prestige_Ranks.add(key);
        }
        for (String key : ranksfile.getConfig().getConfigurationSection("Prestiges.Ranks").getKeys(false)) {
        	Prestige_List.add(key);
        }
	}
	
    public void loadRank() {
        if (!getConfig().contains("Player Data.Ranks")) return;
        for (String key : getConfig().getConfigurationSection("Player Data.Ranks").getKeys(false)) {
        	Player_Rank.put(key, getConfig().getString("Player Data.Ranks." + key));
        }
        if (!getConfig().contains("Player Data.Prestiges")) return;
        for (String key : getConfig().getConfigurationSection("Player Data.Prestiges").getKeys(false)) {
        	Player_Prestige.put(key, getConfig().getString("Player Data.Prestiges." + key));
        }
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
    	Player player = event.getPlayer();
    	if (Player_Rank == null || !Player_Rank.containsKey(player.getName())) {
    		Player_Rank.put(player.getName(), Ranks.get(1));
    	}
    }

}

package me.hexiaranks.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import me.hexiaranks.HexiaRanks;

public class MasterCommand extends BukkitCommand {
	private HexiaRanks main = (HexiaRanks)Bukkit.getPluginManager().getPlugin("HexiaRanks");
	
	public MasterCommand(String commandName) {
		super(commandName);
		this.setDescription(main.getStringWithoutPAPI(main.configManager.commandsConfig.getString("commands." + commandName + ".description", "reborn")));
		this.setUsage(main.getStringWithoutPAPI(main.configManager.commandsConfig.getString("commands." + commandName + ".usage", "/master")));
		this.setPermission(main.configManager.commandsConfig.getString("commands." + commandName + ".permission", "HexiaRanks.master"));
		this.setPermissionMessage(main.getStringWithoutPAPI(main.configManager.commandsConfig.getString("commands." + commandName + ".permission-message", "&cYou don't have permission to execute this command.")));
		this.setAliases(main.configManager.commandsConfig.getStringList("commands." + commandName + ".aliases"));
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if(!sender.hasPermission(this.getPermission())) {
			sender.sendMessage(this.getPermissionMessage());
			return true;
		}
		if(!main.isMasterEnabled) {
			return true;
		}
		if(args.length == 0) {
	      if(!(sender instanceof Player)) {
	    	  return true;
	      } 
	      if(main.isBefore1_7) {
	    	  main.prxAPI.masterLegacy((Player)sender);
	    	  return true;
	      }
           // do master
	      main.prxAPI.master((Player)sender);
		} else if (args.length == 1) {
			// do force master
		}
		return true;
	}
}

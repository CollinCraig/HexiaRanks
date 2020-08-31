package me.hexiaranks.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import me.hexiaranks.HexiaRanks;

public class RankupCommand extends BukkitCommand {

	private HexiaRanks main = (HexiaRanks)Bukkit.getPluginManager().getPlugin("HexiaRanks");
	public RankupCommand(String commandName) {
		super(commandName);
		this.setDescription(main.getStringWithoutPAPI(main.configManager.commandsConfig.getString("commands." + commandName + ".description", "get promoted to the next rank")));
		this.setUsage(main.getStringWithoutPAPI(main.configManager.commandsConfig.getString("commands." + commandName + ".usage", "/rankup [player]")));
		this.setPermission(main.configManager.commandsConfig.getString("commands." + commandName + ".permission", "HexiaRanks.rankup"));
		this.setPermissionMessage(main.getStringWithoutPAPI(main.configManager.commandsConfig.getString("commands." + commandName + ".permission-message", "&cYou don't have permission to execute this command.")));
		this.setAliases(main.configManager.commandsConfig.getStringList("commands." + commandName + ".aliases"));
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if(!sender.hasPermission(this.getPermission())) {
			sender.sendMessage(this.getPermissionMessage());
			return true;
		}
		if(!main.isRankEnabled) {
			return true;
		}
		if(args.length == 0) {
	      if(!(sender instanceof Player)) {
	    	  String runFromConsole = main.messagesStorage.getStringMessage("runfromconsole");
	    	  sender.sendMessage(main.prxAPI.c(runFromConsole));
	    	  return true;
	      } 
	      if(main.isBefore1_7) {
	    	  main.prxAPI.rankupLegacy((Player)sender);
	    	  return true;
	      }
       main.prxAPI.rankup((Player)sender);
		} else if (args.length == 1) {
			if(args[0].equalsIgnoreCase("max")) {
				main.rankupMaxCommand.execute(sender, label, args);
			} else {
			// do rankup other
			}
		}
		return true;
	}

}

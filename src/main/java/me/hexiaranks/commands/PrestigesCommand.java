package me.hexiaranks.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import me.hexiaranks.HexiaRanks;

public class PrestigesCommand extends BukkitCommand{
	
private HexiaRanks main = (HexiaRanks)Bukkit.getPluginManager().getPlugin("HexiaRanks");
	
	public PrestigesCommand(String commandName) {
		super(commandName);
		this.setDescription(main.getStringWithoutPAPI(main.configManager.commandsConfig.getString("commands." + commandName + ".description", "shows a list of prison prestiges")));
		this.setUsage(main.getStringWithoutPAPI(main.configManager.commandsConfig.getString("commands." + commandName + ".usage", "/prestiges")));
		this.setPermission(main.configManager.commandsConfig.getString("commands." + commandName + ".permission", "HexiaRanks.prestiges"));
		this.setPermissionMessage(main.getStringWithoutPAPI(main.configManager.commandsConfig.getString("commands." + commandName + ".permission-message", "&cYou don't have permission to execute this command.")));
		this.setAliases(main.configManager.commandsConfig.getStringList("commands." + commandName + ".aliases"));
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if(!main.isPrestigeEnabled) {
			return true;
		}
		if(!sender.hasPermission(this.getPermission())) {
			sender.sendMessage(this.getPermissionMessage());
			return true;
		}
		if(!(sender instanceof Player)) {
			try {
			sender.sendMessage(main.prxAPI.c(main.prestigesAPI.prestigeListConsole));
			main.prxAPI.getPrestigesCollection().forEach(prestige -> {sender.sendMessage(prestige);});
			} catch (NullPointerException err) {
				
			}
			return true;
		}
		if(args.length == 0) {
				if(main.globalStorage.getBooleanData("Options.GUI-PRESTIGELIST")) {
					main.guiManager.openPrestigesGUI((Player)sender);
					return true;
				}
			main.prestigesAPI.send("1", sender);
		} else if (args.length == 1) {
            main.prestigesAPI.send(args[0], sender);
		}
		return true;
	}
}

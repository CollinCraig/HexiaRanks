package me.hexiaranks.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import me.hexiaranks.HexiaRanks;

public class MasteryCommand extends BukkitCommand{
private HexiaRanks main = (HexiaRanks)Bukkit.getPluginManager().getPlugin("HexiaRanks");
	
	public MasteryCommand(String commandName) {
		super(commandName);
		this.setDescription(main.getStringWithoutPAPI(main.configManager.commandsConfig.getString("commands." + commandName + ".description", "shows a list of prison mastery")));
		this.setUsage(main.getStringWithoutPAPI(main.configManager.commandsConfig.getString("commands." + commandName + ".usage", "/mastery")));
		this.setPermission(main.configManager.commandsConfig.getString("commands." + commandName + ".permission", "HexiaRanks.mastery"));
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
		if(!(sender instanceof Player)) {
			try {
			sender.sendMessage(main.prxAPI.c(main.masteryAPI.masterListConsole));
			main.prxAPI.getMasteryCollection().forEach(master -> {sender.sendMessage(master);});
			} catch (NullPointerException err) {
				
			}
			return true;
		}
		if(args.length == 0) {
			if(main.globalStorage.getBooleanData("Options.GUI-MASTERLIST")) {
				main.guiManager.openMasteryGUI((Player)sender);
				return true;
			}
			main.masteryAPI.send("1", sender);
		} else if (args.length == 1) {
            main.masteryAPI.send(args[0], sender);
		}
		return true;
	}
}

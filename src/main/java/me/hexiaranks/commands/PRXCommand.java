package me.hexiaranks.commands;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import me.hexiaranks.HexiaRanks;
import me.hexiaranks.data.PrestigeDataHandler;
import me.hexiaranks.data.RankDataHandler;
import me.hexiaranks.data.RankPath;
import me.hexiaranks.data.MasterDataHandler;
import me.hexiaranks.data.XUser;
import me.hexiaranks.events.PrestigeUpdateCause;
import me.hexiaranks.events.RankUpdateCause;
import me.hexiaranks.events.MasterUpdateCause;
import me.hexiaranks.events.XPrestigeUpdateEvent;
import me.hexiaranks.events.XRankUpdateEvent;
import me.hexiaranks.events.XMasterUpdateEvent;
import me.hexiaranks.utils.CollectionUtils;
import me.hexiaranks.utils.CollectionUtils.ReplaceableList;

public class PRXCommand extends BukkitCommand {
	
	private HexiaRanks main = (HexiaRanks)Bukkit.getPluginManager().getPlugin("HexiaRanks");
	private String ver = "1.0";
	private List<String> placeholders;
	private boolean is1_16;
	public PRXCommand(String commandName) {
		super(commandName);
		this.setDescription(main.configManager.commandsConfig.getString("commands." + commandName + ".description", "Manage ranks, prestiges, mastery settings"));
		this.setUsage(main.configManager.commandsConfig.getString("commands." + commandName + ".usage", "/hp help [page]"));
		this.setPermission(main.configManager.commandsConfig.getString("commands." + commandName + ".permission", "HexiaRanks.admin"));
		this.setPermissionMessage(main.configManager.commandsConfig.getString("commands." + commandName + ".permission-message", "&cYou don't have permission to execute this command."));
		this.setAliases(main.configManager.commandsConfig.getStringList("commands." + commandName + ".aliases"));
		ver = main.getDescription().getVersion();
		is1_16 = Bukkit.getVersion().contains("1.16");
		placeholders = Arrays.asList(
				"%hexiaranks_currentrank_name%", "%hexiaranks_currentrank_displayname%"
				,"%hexiaranks_rankup_percentage%", "%hexiaranks_rankup_percentage_decimal%"
				,"%hexiaranks_rankup_percentage_nolimit%", "%hexiaranks_rankup_percentage_decimal_nolimit%"
				,"%hexiaranks_rankup_percentage_plain%", "%hexiaranks_rankup_progress%"
				,"%hexiaranks_rankup_progress_double%", "%hexiaranks_rankup_name%"
				,"%hexiaranks_rankup_displayname%", "%hexiaranks_rankup_cost%"
				,"%hexiaranks_rankup_cost_formatted%", "%hexiaranks_rank_displayname_<rank>%"
				,"%hexiaranks_rank_cost_<rank>%", "%hexiaranks_rank_costformatted_<rank>%"
				,"%hexiaranks_currentrank_lastcolors%", "%hexiaranks_currentrank_afterbracketcolor%"
				,"%hexiaranks_currentrank_afterspacecolor%", "%hexiaranks_currentrank_colors%"
				,"%hexiaranks_currentrank_name_<playername>%", "%hexiaranks_rankup_name_<playername>%"
				,"%hexiaranks_rankup_cost_plain%", "%hexiaranks_rankup_cost_integer%"
				,"%hexiaranks_rankup_cost_integer_plain%", "%hexiaranks_prestige_name%"
				,"%hexiaranks_prestige_displayname%", "%hexiaranks_prestige_cost%"
				,"%hexiaranks_prestige_cost_formatted%", "%hexiaranks_nextprestige_name%"
				,"%hexiaranks_nextprestige_displayname%", "%hexiaranks_nextprestige_cost%"
				,"%hexiaranks_nextprestige_cost_formatted%", "%hexiaranks_prestige_displayname_<prestige>%"
				,"%hexiaranks_prestige_cost_<prestige%", "%hexiaranks_prestige_costformatted_<prestige>%"
				,"%hexiaranks_has_prestiged%", "%hexiaranks_prestige_name_<playername>%"
				,"%hexiaranks_nextprestige_name_<playername>%", "%hexiaranks_nextprestige_cost_plain%"
				,"%hexiaranks_nextprestige_cost_integer%", "%hexiaranks_nextprestige_cost_integer_plain%"
				,"%hexiaranks_master_name%", "%hexiaranks_master_displayname%"
				,"%hexiaranks_nextmaster_name%" ,"%hexiaranks_nextmaster_displayname%"
				,"%hexiaranks_nextmaster_cost%", "%hexiaranks_nextmaster_cost_formatted%"
				,"%prisonranksx_master_displayname_<master>%", "%prisonranksx_master_cost_<master>%"
				,"%prisonranksx_master_costformatted_<master>%", "%prisonranksx_has_mastered%"
				,"%prisonranksx_master_name_<playername>%", "%prisonranksx_nextmaster_name_<playername>%"
				,"%prisonranksx_nextmaster_cost_plain%", "%prisonranksx_nextmaster_cost_integer%"
				,"%prisonranksx_nextmaster_cost_integer_plain%", "%prisonranksx_name_rank_<number>%"
				,"%prisonranksx_value_rank_<number>%", "%prisonranksx_name_prestige_<number>%"
				,"%prisonranksx_value_prestige_<number>%", "%prisonranksx_name_master_<number>%"
				,"%prisonranksx_value_master_<number>%", "%prisonranksx_money%"
				,"%prisonranksx_money_nonformatted%", "%prisonranksx_money_decimalformatted%"
				,"%prisonranksx_next_percentage%", "%prisonranksx_next_percentage_decimal%"
				,"%prisonranksx_next_progress%", "%prisonranksx_next_progress_double%"
				,"%prisonranksx_plain_{placeholder}%", "%prisonranksx_current_displayname%"
				, "%prisonranksx_name_stage_<number>%", "%prisonranksx_value_stage_<number>%", ".");
		placeholders = CollectionUtils.columnizeList(placeholders, 3, ", ", ".");
	}

	public String getRandomHexColors() {
		StringBuilder sb = new StringBuilder("#");
		for(int i = 0 ; i < 6 ; i++) {
			sb.append(getRandomColor());
		}
		return sb.toString();
	}
	
	public String getRandomColor() {
		int rand = main.prxAPI.numberAPI.getRandomInteger(0, 15);
			switch (rand) {
			  case 10: return "a";
			  case 11: return "b";
			  case 12: return "c";
			  case 13: return "d";
			  case 14: return "e";
			  case 15: return "f";
			  default: return String.valueOf(rand);
			}
	}
	
	public String getRandomFormat() {
		int rand = main.prxAPI.numberAPI.getRandomInteger(0, 4);
		switch (rand) {
		case 0: return "l";
		case 1: return "n";
		case 2: return "m";
		case 3: return "o";
		default: return "k";
		}
	}
	
	public String getRandomBasicFormat() {
		int rand = main.prxAPI.numberAPI.getRandomInteger(0, 2);
		switch (rand) {
		case 0: return "l";
		case 1: return "n";
		default: return "o";
		}
	}
	
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if(!sender.hasPermission(this.getPermission())) {
			
			sender.sendMessage(this.getPermissionMessage());
			return true;
		}
        if(args.length == 0) {
        	sender.sendMessage(main.prxAPI.c("&3[&6Hexia&4Ranks&3] &av" + "1.0.0"));
        	sender.sendMessage(main.prxAPI.c("&7<> = required &8⎟ &7[] = optional &8⎟ &7() = optional prefix"));
            sender.sendMessage(main.prxAPI.c("&c&m                                                                      &c"));
        	sender.sendMessage(main.prxAPI.c("&c/&6hr help [page] &7⎟ &3show the available commands"));
        	sender.sendMessage(main.prxAPI.c("&c/&6hr reload &7⎟ &3reload the entire plugin"));
        	sender.sendMessage(main.prxAPI.c("&c/&6hr save &7⎟ &3save the ranks/prestiges/etc.. you created ingame"));
        	sender.sendMessage(main.prxAPI.c("&c/&6hr createrank <name> <cost> [displayname] (-path:)[pathname]"));
        	sender.sendMessage(main.prxAPI.c("&c/&6hr setrankcost <name> <cost>"));
        	sender.sendMessage(main.prxAPI.c("&c/&6hr setrankdisplay <name> <displayname>"));
        	sender.sendMessage(main.prxAPI.c("&c/&6hr setrankpath <name> <path>"));
        	sender.sendMessage(main.prxAPI.c("&c/&6hr delrank <name>"));
        	sender.sendMessage(main.prxAPI.c("&c/&6hr setdefaultrank <name> &7⎟ &3set the default rank in the default path"));
        	sender.sendMessage(main.prxAPI.c("&c/&6hr setlastrank <name> &7⎟ &3set the last rank in the default path"));
        	sender.sendMessage(main.prxAPI.c("&c/&6hr setrank <player> <rank> [pathname] &7⎟ &3set the player rank"));
        	sender.sendMessage(main.prxAPI.c("&c/&6hr resetrank <player> [pathname] &7⎟ &3reset the player rank"));
        	sender.sendMessage(main.prxAPI.c("&c/&6forcerankup <player>"));
            sender.sendMessage(main.prxAPI.c("&3[&6Page&3] &7(&f1&7/&f3&7)"));
            sender.sendMessage(main.prxAPI.c("&c&m                                                                      &c"));
        } else if (args.length == 1) {
        	if(args[0].equalsIgnoreCase("banana")) {
        		Bukkit.broadcastMessage(main.prxAPI.c("&e&lBANANA!"));
        	}
        	else if (args[0].equalsIgnoreCase("info")) {
        		sender.sendMessage(main.prxAPI.c("&6&lHR&r &7&lINFO:"));
        		sender.sendMessage(main.prxAPI.c("&eRanks: &b" + main.rankStorage.getEntireData().size()));
        		sender.sendMessage(main.prxAPI.c("&ePrestiges: &b" + main.prestigeStorage.getPrestigeData().size()));
        		sender.sendMessage(main.prxAPI.c("&eMastery: &b" + main.masterStorage.getMasterData().size()));
        		sender.sendMessage(main.prxAPI.c("&eRegistered players: &b" + main.playerStorage.getPlayerData().size()));
        	}
        	else if (args[0].equalsIgnoreCase("runtask")) {
        		main.prxAPI.allRankAddPermissions.forEach(string -> {
        			sender.sendMessage(string);
        		});
        	} else if (args[0].equalsIgnoreCase("placeholders")) {
        		if(is1_16) {
        			placeholders.forEach(placeholder -> {
        				sender.sendMessage(main.globalStorage.translateHexColorCodes("&" + getRandomHexColors() + placeholder));
        			});
        			return true;
        		}
        		placeholders.forEach(placeholder -> {
        			sender.sendMessage(main.prxAPI.c("&" + getRandomColor() + "&" + getRandomBasicFormat() + placeholder));
        		});
        	}
        	else if (args[0].equalsIgnoreCase("pathrank")) {
        		List<String> pathRanks = CollectionUtils.columnizeList(main.rankStorage.getPathRanksMap().get("default"), 3, ", ", ".");
        		pathRanks.forEach(line -> {sender.sendMessage(line);});
        	}
        	else if(args[0].equalsIgnoreCase("getprestiges")) {
        		sender.sendMessage("Your prestiges: " + String.valueOf(main.prxAPI.getPlayerPrestiges((Player)sender)));
        	}
        	else if(args[0].equalsIgnoreCase("players")) {
        		main.getPlayerStorage().getPlayerData().entrySet().forEach(entry -> {
        			sender.sendMessage(main.prxAPI.c("&7" + entry.getKey() + "&9:" + entry.getValue().getName()));
        		});
        	}
        	else if(args[0].equalsIgnoreCase("players2")) {
        		main.getPlayerStorage().getPlayerData().entrySet().forEach(entry -> {
        			sender.sendMessage(main.prxAPI.c("&7" + entry.getValue().getUUID().toString() + "&9:" + entry.getValue().getName()));
        		});
        	}
        	else if(args[0].equalsIgnoreCase("players_prestiges")) {
        		main.getPlayerStorage().getPlayerData().entrySet().forEach(entry -> {
        			sender.sendMessage(main.prxAPI.c("&7" + entry.getValue().getName() + "&9:" + entry.getValue().getPrestige()));
        		});
        	}
        	
        	else if(args[0].equalsIgnoreCase("players_ranks")) {
        		main.getPlayerStorage().getPlayerData().entrySet().forEach(entry -> {
        			sender.sendMessage(main.prxAPI.c("&7" + entry.getValue().getName() + "&9:" + entry.getValue().getRankPath().getRankName()));
        		});
        	}
        	else if(args[0].equalsIgnoreCase("players_mastery")) {
        		main.getPlayerStorage().getPlayerData().entrySet().forEach(entry -> {
        			sender.sendMessage(main.prxAPI.c("&7&m---------------------------------------"));
        			sender.sendMessage(main.prxAPI.c("&7" + entry.getValue().getName() + "&9:" + entry.getValue().getMaster()));
        			sender.sendMessage(main.prxAPI.c("&7&m---------------------------------------"));
        		});
        	}
        	else if(args[0].equalsIgnoreCase("playerdata")) {
        		main.playerStorage.getPlayerData().keySet().forEach(data -> {
                      sender.sendMessage(main.playerStorage.getPlayerData().get(data).toString());
        		});
        	}
        	else if(args[0].equalsIgnoreCase("playerdata2")) {
           		main.playerStorage.getPlayerData().keySet().forEach(data -> {
                    sender.sendMessage(main.playerStorage.getPlayerData().get(data).toString2());
      		});
        	}
        	else if(args[0].equalsIgnoreCase("dev")) {
        		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
        			try {
        				sender.sendMessage(main.prxAPI.c("&2Converting rankdata..."));
                  FileConfiguration oldRanked = YamlConfiguration.loadConfiguration(new File(main.getDataFolder() + "/ranked.yml"));
                  for(String uuids : oldRanked.getConfigurationSection("Players").getKeys(false)) {
                	  String uuid = uuids;
                	  String rank = oldRanked.getString("Players." + uuid);
                	  main.configManager.rankDataConfig.set("players." + uuid + ".rank", rank);
                	  main.configManager.rankDataConfig.set("players." + uuid + ".path", main.prxAPI.getDefaultPath());
                  }
                  main.configManager.saveRankDataConfig();
                  sender.sendMessage(main.prxAPI.c("&eRank data conversion success."));
        			} catch (Exception err) {
        				sender.sendMessage(main.prxAPI.c("&cRank data is already converted."));
        			}
        			try {
        				FileConfiguration oldPrestiged = YamlConfiguration.loadConfiguration(new File(main.getDataFolder() + "/prestiged.yml"));
        				for(String uuids : oldPrestiged.getConfigurationSection("Players").getKeys(false)) {
        					String prestige = oldPrestiged.getString("Players." + uuids);
        					main.configManager.prestigeDataConfig.set("players." + uuids, prestige);
        				}
        				main.configManager.savePrestigeDataConfig();
        				sender.sendMessage(main.prxAPI.c("&ePrestige data conversion success."));
        			} catch (Exception err) {
        				sender.sendMessage(main.prxAPI.c("&cPrestige data is already converted."));
        			}
        		});
        	}
        	else if(args[0].equalsIgnoreCase("terminate")) {
        		if(main.terminateMode) {
        		sender.sendMessage(main.prxAPI.c("&7Terminate mode &cdisabled&7."));
        		main.terminateMode = false;
        		} else {
        			sender.sendMessage(main.prxAPI.c("&7Terminate mode &aenabled&7."));
        			main.terminateMode = true;
        		}
        	}
        	else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
            	sender.sendMessage(main.prxAPI.c("&3[&6Hexia&4Ranks&3] &av" + "1.0.0"));
            	sender.sendMessage(main.prxAPI.c("&7<> = required &8⎟ &7[] = optional &8⎟ &7() = optional prefix"));
                sender.sendMessage(main.prxAPI.c("&c&m                                                                      &c"));
            	sender.sendMessage(main.prxAPI.c("&c/&6hr help [page] &7⎟ &3type member in the page for members help"));
            	sender.sendMessage(main.prxAPI.c("&c/&6hr reload &7⎟ &3reloads the entire plugin"));
            	sender.sendMessage(main.prxAPI.c("&c/&6hr save &7⎟ &3save the ranks/prestiges/etc.. you created ingame"));
            	sender.sendMessage(main.prxAPI.c("&c/&6hr createrank <name> <cost> [displayname] (-path:)[pathname]"));
            	sender.sendMessage(main.prxAPI.c("&c/&6hr setrankcost <name> <cost>"));
            	sender.sendMessage(main.prxAPI.c("&c/&6hr setrankdisplay <name> <displayname>"));
            	sender.sendMessage(main.prxAPI.c("&c/&6hr setrankpath <name> <path>"));
            	sender.sendMessage(main.prxAPI.c("&c/&6hr delrank <name>"));
            	sender.sendMessage(main.prxAPI.c("&c/&6hr setdefaultrank <name> &7⎟ &3set the default rank in the default path"));
            	sender.sendMessage(main.prxAPI.c("&c/&6hr setlastrank <name> &7⎟ &3set the last rank in the default path"));
            	sender.sendMessage(main.prxAPI.c("&c/&6hr setrank <player> <rank> [pathname] &7⎟ &3set the player rank"));
            	sender.sendMessage(main.prxAPI.c("&c/&6hr resetrank <player> [pathname] &7⎟ &3reset the player rank"));
                sender.sendMessage(main.prxAPI.c("&3[&6Page&3] &7(&f1&7/&f3&7)"));
                sender.sendMessage(main.prxAPI.c("&c&m                                                                      &c"));
        	} else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
        		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
        		sender.sendMessage(main.prxAPI.c("&eReloading..."));
        		main.manager.reload();
        		sender.sendMessage(main.prxAPI.g("reload"));
        		});
        	} else if (args[0].equalsIgnoreCase("reload-data")) {
        		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            		sender.sendMessage(main.prxAPI.c("&eReloading Data..."));
            		main.manager.reloadPlayerData();
            		sender.sendMessage(main.prxAPI.g("reload"));
        		});
        	} else if (args[0].equalsIgnoreCase("reenable") || args[0].equalsIgnoreCase("ree")) {
        		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
        		sender.sendMessage(main.prxAPI.c("&eEnabling..."));
        		main.onEnable();
        		sender.sendMessage(main.prxAPI.c("&eReloading..."));
        		main.manager.reload();
        		sender.sendMessage(main.prxAPI.c("&6Plugin Successfully re-enabled."));
        		});
        	} else if (args[0].equalsIgnoreCase("cleartask")) {
        		main.prxAPI.taskedPlayers.clear();
        		main.prestigeAPI.getTaskedPlayers().clear();
        		main.rankupAPI.getTaskedPlayers().clear();
        		main.rankupMaxAPI.rankupMaxProcess.clear();
        		sender.sendMessage(main.prxAPI.c("&c&k~&r &4&l&o&nTask limit cleared&r &c&k~"));
        	} else if (args[0].equalsIgnoreCase("errors")) {
        		if(main.errorInspector.getErrors().isEmpty()) {
        			if(is1_16) {
        			sender.sendMessage(main.globalStorage.translateHexColorCodes("&" + getRandomHexColors() + main.prxAPI.c("&l&oNo errors were found.")));
        			} else {
        			sender.sendMessage(main.prxAPI.c("&" + getRandomColor() + "&l&oNo errors were found."));
        			}
        			return true;
        		}
        		main.errorInspector.getErrors().forEach(error -> {
        			sender.sendMessage(main.prxAPI.c(error));
        		});
        	} else if (args[0].equalsIgnoreCase("save")) {
        		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
        		//sender.sendMessage(main.prxAPI.c("&eSaving data..."));
        		main.manager.save();
        		//sender.sendMessage(main.prxAPI.g("save"));
        		});
        	} else if (args[0].equalsIgnoreCase("debug")) {
        		if(main.debug) {
        			main.debug = false;
        			sender.sendMessage(main.prxAPI.c("&6Debug &cdisabled&6."));
        		} else {
        			main.debug = true;
        			sender.sendMessage(main.prxAPI.c("&6Debug &aenabled&6."));
        		}
        	} else if (args[0].equalsIgnoreCase("createrank")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr createrank &4<name> <cost> &c[displayname]"));
        	} else if (args[0].equalsIgnoreCase("createprestige")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr createprestige &4<name> <cost> &c[displayname]"));
        	} else if (args[0].equalsIgnoreCase("createmaster")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr createmaster &4<name> <cost> &c[displayname]"));
        	} else if (args[0].equalsIgnoreCase("setrankcost")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr setrankcost &4<name> <cost>"));
        	} else if (args[0].equalsIgnoreCase("setprestigecost")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr setprestigecost &4<name> <cost>"));
        	} else if (args[0].equalsIgnoreCase("setmastercost")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr setmastercost &4<name> <cost>"));
        	} else if (args[0].equalsIgnoreCase("setrankdisplay") || args[0].equalsIgnoreCase("setrankdisplayname")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr setrankdisplay &4<name> <displayname>"));
        	} else if (args[0].equalsIgnoreCase("setprestigedisplay") || args[0].equalsIgnoreCase("setprestigedisplayname")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr setprestigedisplay &4<name> <displayname>"));
        	} else if (args[0].equalsIgnoreCase("setmasterdisplay") || args[0].equalsIgnoreCase("setmasterdisplayname")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr setmasterdisplay &4<name> <displayname>"));
        	} else if (args[0].equalsIgnoreCase("setrankpath") || args[0].equalsIgnoreCase("setrankpathname")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr setrankpath &4<name> <path>"));
        	} else if (args[0].equalsIgnoreCase("delrank") || args[0].equalsIgnoreCase("deleterank")
        			|| args[0].equalsIgnoreCase("removerank") || args[0].equalsIgnoreCase("remrank")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr " + args[0] + " &4<name>"));
        	} else if (args[0].equalsIgnoreCase("delprestige") || args[0].equalsIgnoreCase("deleteprestige")
        			|| args[0].equalsIgnoreCase("removeprestige") || args[0].equalsIgnoreCase("remprestige")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr " + args[0] + " &4<name>"));
        	} else if (args[0].equalsIgnoreCase("delmaster") || args[0].equalsIgnoreCase("deletemaster")
        			|| args[0].equalsIgnoreCase("removemaster") || args[0].equalsIgnoreCase("remmaster")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr " + args[0] + " &4<name>"));
        	} else if (args[0].equalsIgnoreCase("setdefaultrank") || args[0].equalsIgnoreCase("setfirstrank")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr " + args[0] + " &4<name>"));
        	} else if (args[0].equalsIgnoreCase("setfirstprestige") || args[0].equalsIgnoreCase("setdefaultprestige")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr " + args[0] + " &4<name>"));
        	} else if (args[0].equalsIgnoreCase("setdefaultmaster") || args[0].equalsIgnoreCase("setfirstmaster")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr " + args[0] + " &4<name>"));
        	} else if (args[0].equalsIgnoreCase("setlastrank") || args[0].equalsIgnoreCase("setfinalrank")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr " + args[0] + " &4<name>"));
        	} else if (args[0].equalsIgnoreCase("setlastprestige") || args[0].equalsIgnoreCase("setfinalprestige")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr " + args[0] + " &4<name>"));
        	} else if (args[0].equalsIgnoreCase("setlastmaster") || args[0].equalsIgnoreCase("setfinalmaster")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr " + args[0] + " &4<name>"));
        	} else if (args[0].equalsIgnoreCase("setrank")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr setrank &4<player> <rankname>"));
        	} else if (args[0].equalsIgnoreCase("setprestige")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr setprestige &4<player> <prestigename>"));
        	} else if (args[0].equalsIgnoreCase("setmaster")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr setmaster &4<player> <mastername>"));
        	} else if (args[0].equalsIgnoreCase("resetrank")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr resetrank &4<player>"));
        	} else if (args[0].equalsIgnoreCase("resetprestige")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr resetprestige &4<player>"));
        	} else if (args[0].equalsIgnoreCase("resetmaster")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr resetmaster &4<player>"));
        	}
        } else if(args.length == 2) {
        	if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
        		if(args[1].equalsIgnoreCase("1")) {
                	sender.sendMessage(main.prxAPI.c("&3[&6Hexia&4Ranks&3] &av" + "1.0.0"));
                	sender.sendMessage(main.prxAPI.c("&7<> = required &8⎟ &7[] = optional &8⎟ &7() = optional prefix"));
                    sender.sendMessage(main.prxAPI.c("&c&m                                                                      &c"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr help [page] &7⎟ &3shows the available commands"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr reload &7⎟ &3reloads the entire plugin"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr save &7⎟ &3save the ranks/prestiges/etc.. you created ingame"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr createrank <name> <cost> [displayname] (-path:)[pathname]"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr setrankcost <name> <cost>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr setrankdisplay <name> <displayname>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr setrankpath <name> <path>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr delrank <name>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr setdefaultrank <name> &7⎟ &3set the default rank in the default path"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr setlastrank <name> &7⎟ &3set the last rank in the default path"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr setrank <player> <rank> [pathname] &7⎟ &3set the player rank"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr resetrank <player> [pathname] &7⎟ &3reset the player rank"));
                    sender.sendMessage(main.prxAPI.c("&3[&6Page&3] &7(&f1&7/&f3)"));
                    sender.sendMessage(main.prxAPI.c("&c&m                                                                      &c"));
        		} else if (args[1].equalsIgnoreCase("2")) {
                   	sender.sendMessage(main.prxAPI.c("&3[&6Hexia&4Ranks&3] &av" + "1.0.0"));
                	sender.sendMessage(main.prxAPI.c("&7<> = required &8⎟ &7[] = optional"));
                    sender.sendMessage(main.prxAPI.c("&c&m                                                                      &c"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr createprestige <name> <cost> [displayname]"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr setprestigecost <name> <cost>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr setprestigedisplay <name> <displayname>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr delprestige <name>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr setfirstprestige <name>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr setlastprestige <name>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr setprestige <player> <prestige>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr resetprestige <player>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr delplayerprestige <player>"));
                    sender.sendMessage(main.prxAPI.c("&3[&6Page&3] &7(&f2&7/&f3)"));
                    sender.sendMessage(main.prxAPI.c("&c&m                                                                      &c"));
        		} else if (args[1].equalsIgnoreCase("3")) {
                   	sender.sendMessage(main.prxAPI.c("&3[&6Hexia&4Ranks&3] &av" + "1.0.0"));
                	sender.sendMessage(main.prxAPI.c("&7<> = required &8⎟ &7[] = optional"));
                    sender.sendMessage(main.prxAPI.c("&c&m                                                                      &c"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr createmaster <name> <cost> [displayname]"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr setmastercost <name> <cost>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr setmasterdisplay <name> <displayname>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr delmaster <name>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr setfirstmaster <name>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr setlastmaster <name>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr setmaster <player> <master>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr resetmaster <player>"));
                	sender.sendMessage(main.prxAPI.c("&c/&6hr delplayermaster <player>"));
                    sender.sendMessage(main.prxAPI.c("&3[&6Page&3] &7(&f3&7/&f3)"));
                    sender.sendMessage(main.prxAPI.c("&c&m                                                                      &c"));
        		} else if (args[1].equalsIgnoreCase("member")) {
        			sender.sendMessage(main.prxAPI.c("&7- &9Prison Help &7-"));
        			sender.sendMessage(main.prxAPI.c("&6/rankup"));
        			sender.sendMessage(main.prxAPI.c("&6/rankupmax"));
        			sender.sendMessage(main.prxAPI.c("&6/ranks"));
        			sender.sendMessage(main.prxAPI.c("&6/prestige"));
        			sender.sendMessage(main.prxAPI.c("&6/prestiges"));
        			sender.sendMessage(main.prxAPI.c("&6/master"));
        			sender.sendMessage(main.prxAPI.c("&6/mastery"));
        			sender.sendMessage(main.prxAPI.c("&6/autorankup"));
        			sender.sendMessage(main.prxAPI.c("&6/autoprestige"));
        		}
        	} else if (args[0].equalsIgnoreCase("createrank")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr createrank <name> &4<cost> &c[displayname]"));
        	} else if (args[0].equalsIgnoreCase("createprestige")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr createprestige <name> &4<cost> &c[displayname]"));
        	} else if (args[0].equalsIgnoreCase("createmaster")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr createmaster <name> &4<cost> &c[displayname]"));
        	} else if (args[0].equalsIgnoreCase("setrank")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr setrank <player> &4<rank>"));
        	} else if (args[0].equalsIgnoreCase("setprestige")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr setprestige <player> &4<prestige>"));
        	} else if (args[0].equalsIgnoreCase("setmaster")) {
        		sender.sendMessage(main.prxAPI.c("&c/&6hr setmaster <player> &4<master>"));
        	}
        	else if(args[0].equalsIgnoreCase("delrank")) {
        		String matchedRank = main.manager.matchRank(args[1]);
        		main.manager.delRank(matchedRank);
        		sender.sendMessage(main.prxAPI.g("delrank").replace("%args1%", matchedRank));
        	} else if (args[0].equalsIgnoreCase("delprestige")) {
        		String matchedPrestige = main.manager.matchPrestige(args[1]);
        		main.manager.delPrestige(matchedPrestige);
        		if(main.prxAPI.prestigeExists(matchedPrestige)) {
        		sender.sendMessage(main.prxAPI.g("delprestige").replace("%args1%", matchedPrestige));
        		} else {
        			Player p = Bukkit.getPlayer(args[1]);
        			if(p == null) {
        				sender.sendMessage(main.prxAPI.g("prestige-notfound").replace("%prestige%", matchedPrestige));
        				return true;
        			}
            		XUser user = XUser.getXUser(p);
            		XPrestigeUpdateEvent e = new XPrestigeUpdateEvent(p, PrestigeUpdateCause.DELPRESTIGE);
            		Bukkit.getPluginManager().callEvent(e);
            		if(e.isCancelled()) {
            			return true;
            		}
            		main.manager.delPlayerPrestige(user);
            		if(main.globalStorage.getStringListMap().get("PrestigeOptions.prestige-delete-cmds").contains("[rankpermissions]")) {
            		    Set<String> perms = main.prxAPI.allRankAddPermissions;
            		    for(String perm : perms) {
            			    main.perm.delPermission(p, perm);
            		    }
            		} if (main.globalStorage.getStringListMap().get("PrestigeOptions.prestige-delete-cmds").contains("[prestigepermissions]")) {
            			Set<String> perms2 = main.prxAPI.allPrestigeAddPermissions;
            			for(String perm : perms2) {
            				main.perm.delPermission(p, perm);
            			}
            		}
            		for(String cmd : main.globalStorage.getStringListMap().get("PrestigeOptions.prestige-delete-cmds")) {
            			if(!cmd.endsWith("permissions] remove")) {
            				main.executeCommand(p, cmd);
            			}
            		}
            		sender.sendMessage(main.prxAPI.g("delplayerprestige").replace("%player%", p.getName()));
        		}
        	} else if (args[0].equalsIgnoreCase("delmaster")) {
        		String matchedMaster = main.manager.matchMaster(args[1]);
        		main.manager.delMaster(matchedMaster);
        		if(main.prxAPI.masterExists(matchedMaster)) {
        		sender.sendMessage(main.prxAPI.g("delmaster").replace("%args1%", matchedMaster));
        		} else {
        			Player p = Bukkit.getPlayer(args[1]);
        			if(p == null) {
        				sender.sendMessage(main.prxAPI.g("master-notfound").replace("%master%", matchedMaster));
        				return true;
        			}
        			XUser user = XUser.getXUser(p);
            		XMasterUpdateEvent e = new XMasterUpdateEvent(p, MasterUpdateCause.DELREBIRTH);
            		Bukkit.getPluginManager().callEvent(e);
            		if(e.isCancelled()) {
            			return true;
            		}
            		main.manager.delPlayerMaster(user);
            		if(main.globalStorage.getStringListMap().get("MasterOptions.master-delete-cmds").contains("[rankpermissions]")) {
            		    Set<String> perms = main.prxAPI.allRankAddPermissions;
            		    for(String perm : perms) {
            			    main.perm.delPermission(p, perm);
            		    }
            		} if (main.globalStorage.getStringListMap().get("MasterOptions.master-delete-cmds").contains("[prestigepermissions]")) {
            			Set<String> perms2 = main.prxAPI.allPrestigeAddPermissions;
            			for(String perm : perms2) {
            				main.perm.delPermission(p, perm);
            			}
            		}
            		if(main.globalStorage.getStringListMap().get("MasterOptions.master-delete-cmds").contains("[masterpermissions]")) {
            			Set<String> perms = main.prxAPI.allMasterAddPermissions;
            			for(String perm : perms) {
            				main.perm.delPermission(p, perm);
            			}
            		}
            		for(String cmd : main.globalStorage.getStringListMap().get("MasterOptions.master-delete-cmds")) {
            			if(!cmd.endsWith("permissions] remove")) {
            				main.executeCommand(p, cmd);
            			}
            		}
            		sender.sendMessage(main.prxAPI.g("delplayermaster").replace("%player%", p.getName()));
            	
        		}
        	} else  if (args[0].equalsIgnoreCase("resetrank")) {
        		String parsedPlayerName = args[1];
        		if(Bukkit.getPlayer(parsedPlayerName) == null) {
        			sender.sendMessage(main.prxAPI.g("playernotfound").replace("%player%", args[1]));
        			return true;
        		}
        		Player p = Bukkit.getPlayer(parsedPlayerName);
        		XRankUpdateEvent e = new XRankUpdateEvent(p, RankUpdateCause.RANKSET, main.prxAPI.getDefaultRank());
        		Bukkit.getServer().getPluginManager().callEvent(e);
        		if(e.isCancelled()) {
        			return true;
        		}
        		main.prxAPI.resetPlayerRank(p);
        		sender.sendMessage(main.prxAPI.g("resetrank").replace("%target%", p.getName())
        				.replace("%firstrank%", main.prxAPI.getDefaultRank()));
        		if(!main.getGlobalStorage().getStringListMap().containsKey("RankOptions.rank-reset-cmds")) {
        		  return true;
        		}
        		if(main.globalStorage.getStringListMap().get("RankOptions.rank-reset-cmds").contains("[rankpermissions]")) {
        		    Set<String> perms = main.prxAPI.allRankAddPermissions;
        		    for(String perm : perms) {
        			    main.perm.delPermission(p, perm);
        		    }
        		}
        		for(String command : main.globalStorage.getStringListMap().get("RankOptions.rank-reset-cmds")) {
        			if(!command.startsWith("[rankpermissions") && !command.startsWith("[prestigeperm") && !command.startsWith("[masterp")) {
        				main.executeCommand(p, command);
        			}
        		}
        		
        	} else if (args[0].equalsIgnoreCase("resetprestige")) {
        		String parsedPlayerName = args[1];
        		if(Bukkit.getPlayer(parsedPlayerName) == null) {
        			sender.sendMessage(main.prxAPI.g("playernotfound").replace("%player%", args[1]));
        			return true;
        		}
        		Player p = Bukkit.getPlayer(parsedPlayerName);
        		XPrestigeUpdateEvent e = new XPrestigeUpdateEvent(p, PrestigeUpdateCause.SETPRESTIGE);
        		Bukkit.getServer().getPluginManager().callEvent(e);
        		if(e.isCancelled()) {
        			return true;
        		}
        		main.prxAPI.setPlayerPrestige(p, main.prxAPI.getFirstPrestige());
        		sender.sendMessage(main.prxAPI.g("resetprestige").replace("%target%", p.getName())
        				.replace("%firstprestige%", main.globalStorage.getStringData("firstprestige")));
        		if(main.globalStorage.getStringListMap().get("PrestigeOptions.prestige-reset-cmds").contains("[rankpermissions]")) {
        		    Set<String> perms = main.prxAPI.allRankAddPermissions;
        		    for(String perm : perms) {
        			    main.perm.delPermission(p, perm);
        		    }
        		}
        		if(main.globalStorage.getStringListMap().get("PrestigeOptions.prestige-reset-cmds").contains("[prestigepermissions$1]")) {
        			Set<String> perms = main.prxAPI.allPrestigeAddPermissions;
        			main.prestigeStorage.getAddPermissionList(main.prxAPI.getFirstPrestige()).forEach(fperm -> {
        				perms.remove(fperm);
        			});
        			for(String perm : perms) {
        				main.perm.delPermission(p, perm);
        			}
        		}
        		for(String cmd : main.globalStorage.getStringListMap().get("PrestigeOptions.prestige-reset-cmds")) {
        			if(!cmd.endsWith("permissions] remove")) {
        				main.executeCommand(p, cmd);
        			}
        		}
        	} else if (args[0].equalsIgnoreCase("resetmaster")) {
        		String parsedPlayerName = args[1];
        		if(Bukkit.getPlayer(parsedPlayerName) == null) {
        			sender.sendMessage(main.prxAPI.g("playernotfound").replace("%player%", args[1]));
        			return true;
        		}
        		Player p = Bukkit.getPlayer(parsedPlayerName);
        		XMasterUpdateEvent e = new XMasterUpdateEvent(p, MasterUpdateCause.SETREBIRTH);
        		Bukkit.getServer().getPluginManager().callEvent(e);
        		if(e.isCancelled()) {
        			return true;
        		}
        		main.prxAPI.setPlayerMaster(p, main.prxAPI.getFirstMaster());
        		sender.sendMessage(main.prxAPI.g("resetmaster").replace("%target%", p.getName())
        				.replace("%firstmaster%", main.globalStorage.getStringData("firstmaster")));
        		if(main.globalStorage.getStringListMap().get("MasterOptions.master-reset-cmds").contains("[rankpermissions]")) {
        		    Set<String> perms = main.prxAPI.allRankAddPermissions;
        		    for(String perm : perms) {
        			    main.perm.delPermission(p, perm);
        		    }
        		}
        		if(main.globalStorage.getStringListMap().get("MasterOptions.master-reset-cmds").contains("[prestigepermissions]")) {
        			Set<String> perms = main.prxAPI.allPrestigeAddPermissions;
        			for(String perm : perms) {
        				main.perm.delPermission(p, perm);
        			}
        		}
        		if(main.globalStorage.getStringListMap().get("MasterOptions.master-reset-cmds").contains("[masterpermissions$1]")) {
        			Set<String> perms = main.prxAPI.allMasterAddPermissions;
        			main.masterStorage.getAddPermissionList(main.prxAPI.getFirstMaster()).forEach(fperm -> {
        				perms.remove(fperm);
        			});
        			for(String perm : perms) {
        				main.perm.delPermission(p, perm);
        			}
        		}
        		for(String cmd : main.globalStorage.getStringListMap().get("MasterOptions.master-reset-cmds")) {
        			if(!cmd.endsWith("permissions] remove")) {
        				main.executeCommand(p, cmd);
        			}
        		}
        		
        	} else if (args[0].equalsIgnoreCase("setdefaultrank") || args[0].equalsIgnoreCase("setfirstrank")) {
        		String rankn = main.manager.matchRank(args[1]);
        		main.manager.setDefaultRank(rankn, true);
        		sender.sendMessage(main.prxAPI.g("setdefaultrank").replace("%args1%", rankn));
        	} else if (args[0].equalsIgnoreCase("setlastrank")) {
        		String rankn = main.manager.matchRank(args[1]);
        		main.manager.setLastRank(rankn, true);
        		sender.sendMessage(main.prxAPI.g("setlastrank").replace("%args1%", rankn));
        	} else if (args[0].equalsIgnoreCase("setfirstprestige")) {
        		String prestigen = main.manager.matchPrestige(args[1]);
        	    main.manager.setFirstPrestige(prestigen, true);
                sender.sendMessage(main.prxAPI.g("setfirstprestige").replace("%args1%", prestigen));
        	} else if (args[0].equalsIgnoreCase("setlastprestige")) {
        		String prestigen = main.manager.matchPrestige(args[1]);
        		main.manager.setLastPrestige(prestigen, true);
        		sender.sendMessage(main.prxAPI.g("setlastprestige").replace("%args1%", prestigen));
        	} else if (args[0].equalsIgnoreCase("setfirstmaster")) {
        		String mastern = main.manager.matchMaster(args[1]);
        		main.manager.setFirstMaster(mastern, true);
        		sender.sendMessage(main.prxAPI.g("setfirstmaster").replace("%args1%", mastern));
        	} else if (args[0].equalsIgnoreCase("setlastmaster")) {
        		String mastern = main.manager.matchMaster(args[1]);
        		main.manager.setLastMaster(mastern, true);
        		sender.sendMessage(main.prxAPI.g("setlastmaster").replace("%args1%", mastern));
        	} else if (args[0].equalsIgnoreCase("delplayerprestige")) {
        		if(Bukkit.getPlayer(args[1]) == null) {
        			sender.sendMessage(main.prxAPI.g("playernotfound").replace("%player%", args[1]));
        			return true;
        		}
        		Player p = Bukkit.getPlayer(args[1]);
        		XUser user = XUser.getXUser(p);
        		XPrestigeUpdateEvent e = new XPrestigeUpdateEvent(p, PrestigeUpdateCause.DELPRESTIGE);
        		Bukkit.getPluginManager().callEvent(e);
        		if(e.isCancelled()) {
        			return true;
        		}
        		main.manager.delPlayerPrestige(user);
        		if(main.globalStorage.getStringListMap().get("PrestigeOptions.prestige-delete-cmds").contains("[rankpermissions]")) {
        		    Set<String> perms = main.prxAPI.allRankAddPermissions;
        		    for(String perm : perms) {
        			    main.perm.delPermission(p, perm);
        		    }
        		} if (main.globalStorage.getStringListMap().get("PrestigeOptions.prestige-delete-cmds").contains("[prestigepermissions]")) {
        			Set<String> perms2 = main.prxAPI.allPrestigeAddPermissions;
        			for(String perm : perms2) {
        				main.perm.delPermission(p, perm);
        			}
        		}
        		for(String cmd : main.globalStorage.getStringListMap().get("PrestigeOptions.prestige-delete-cmds")) {
        			if(!cmd.endsWith("permissions] remove")) {
        				main.executeCommand(p, cmd);
        			}
        		}
        		sender.sendMessage(main.prxAPI.g("delplayerprestige").replace("%player%", p.getName()));
        		
        	} else if (args[0].equalsIgnoreCase("delplayermaster")) {
        		if(Bukkit.getPlayer(args[1]) == null) {
        			sender.sendMessage(main.prxAPI.g("playernotfound").replace("%player%", args[1]));
        			return true;
        		}
        		Player p = Bukkit.getPlayer(args[1]);
        		XUser user = XUser.getXUser(p);
        		XMasterUpdateEvent e = new XMasterUpdateEvent(p, MasterUpdateCause.DELREBIRTH);
        		Bukkit.getPluginManager().callEvent(e);
        		if(e.isCancelled()) {
        			return true;
        		}
        		main.manager.delPlayerMaster(user);
        		if(main.globalStorage.getStringListMap().get("MasterOptions.master-delete-cmds").contains("[rankpermissions]")) {
        		    Set<String> perms = main.prxAPI.allRankAddPermissions;
        		    for(String perm : perms) {
        			    main.perm.delPermission(p, perm);
        		    }
        		} if (main.globalStorage.getStringListMap().get("MasterOptions.master-delete-cmds").contains("[prestigepermissions]")) {
        			Set<String> perms2 = main.prxAPI.allPrestigeAddPermissions;
        			for(String perm : perms2) {
        				main.perm.delPermission(p, perm);
        			}
        		}
        		if(main.globalStorage.getStringListMap().get("MasterOptions.master-delete-cmds").contains("[masterpermissions]")) {
        			Set<String> perms = main.prxAPI.allMasterAddPermissions;
        			for(String perm : perms) {
        				main.perm.delPermission(p, perm);
        			}
        		}
        		for(String cmd : main.globalStorage.getStringListMap().get("MasterOptions.master-delete-cmds")) {
        			if(!cmd.endsWith("permissions] remove")) {
        				main.executeCommand(p, cmd);
        			}
        		}
        		sender.sendMessage(main.prxAPI.g("delplayermaster").replace("%player%", p.getName()));
        	}
        } else if(args.length == 3) {
        	if(args[0].equalsIgnoreCase("createrank")) {
        		double costy;
        		if(!main.prxAPI.numberAPI.isNumber(args[2])) {
        			costy = main.prxAPI.numberAPI.parseBalance(args[2]);
        		} else {
        			costy = Double.valueOf(args[2]);
        		}
        		main.manager.createRank(args[1], Double.valueOf(costy));
        		main.configManager.saveRanksConfig();
        		sender.sendMessage(main.prxAPI.g("createrank").replace("%createdrank%", args[1])
        				.replace("%rankcost%", args[2]));
        	} else if (args[0].equalsIgnoreCase("createprestige")) {
        		double costy;
        		if(!main.prxAPI.numberAPI.isNumber(args[2])) {
        			costy = main.prxAPI.numberAPI.parseBalance(args[2]);
        		} else {
        			costy = Double.valueOf(args[2]);
        		}
        		main.manager.createPrestige(args[1], Double.valueOf(costy));
        		main.configManager.savePrestigesConfig();
        		sender.sendMessage(main.prxAPI.g("createprestige").replace("%createdprestige", args[1])
        				.replace("%prestigecost%", args[2]));
        	} else if (args[0].equalsIgnoreCase("createmaster")) {
        		double costy;
        		if(!main.prxAPI.numberAPI.isNumber(args[2])) {
        			costy = main.prxAPI.numberAPI.parseBalance(args[2]);
        		} else {
        			costy = Double.valueOf(args[2]);
        		}
        		main.manager.createMaster(args[1], Double.valueOf(costy));
        		main.configManager.saveMasteryConfig();
        		sender.sendMessage(main.prxAPI.g("createmaster").replace("%createdmaster%", args[1])
        		        .replace("%mastercost%", args[2]));
        	} else if (args[0].equalsIgnoreCase("setrankcost")) {
          		double costy;
        		if(!main.prxAPI.numberAPI.isNumber(args[2])) {
        			costy = main.prxAPI.numberAPI.parseBalance(args[2]);
        		} else {
        			costy = Double.valueOf(args[2]);
        		}
        		String matchedRank = main.manager.matchRank(args[1]);
        		main.manager.setRankCost(matchedRank, costy);
        		main.configManager.saveRanksConfig();
        		sender.sendMessage(main.prxAPI.g("setrankcost").replace("%args1%", matchedRank)
        				.replace("%args2%", args[2]));
        	} else if (args[0].equalsIgnoreCase("setprestigecost")) {
        		double costy;
        		if(!main.prxAPI.numberAPI.isNumber(args[2])) {
        			costy = main.prxAPI.numberAPI.parseBalance(args[2]);
        		} else {
        			costy = Double.valueOf(args[2]);
        		}
        		String matchedPrestige = main.manager.matchPrestige(args[1]);
        		main.manager.setPrestigeCost(matchedPrestige, costy);
        		main.configManager.savePrestigesConfig();
        		sender.sendMessage(main.prxAPI.g("setprestigecost").replace("%prestige%", matchedPrestige)
        				.replace("%prestigecost%", args[2]));
        	} else if (args[0].equalsIgnoreCase("setmastercost")) {
        		double costy;
        		if(!main.prxAPI.numberAPI.isNumber(args[2])) {
        			costy = main.prxAPI.numberAPI.parseBalance(args[2]);
        		} else {
        			costy = Double.valueOf(args[2]);
        		}
        		String matchedMaster = main.manager.matchMaster(args[1]);
        		main.manager.setMasterCost(matchedMaster, costy);
        		main.configManager.saveMasteryConfig();
        		sender.sendMessage(main.prxAPI.g("setmastercost").replace("%master%", matchedMaster)
        				.replace("%mastercost%", args[2]));
        	} else if (args[0].equalsIgnoreCase("setrankdisplay")) {
        		String matchedRank = main.manager.matchRank(args[1]);
        		String newDisplayName = main.getArgs(args, 2);
        		main.manager.setRankDisplayName(matchedRank, newDisplayName);
        		main.configManager.saveRanksConfig();
        		sender.sendMessage(main.prxAPI.g("setrankdisplay").replace("%args1%", matchedRank)
        				.replace("%args2%", newDisplayName + " §f=> " + newDisplayName));
        	} else if (args[0].equalsIgnoreCase("setprestigedisplay")) {
        		String matchedPrestige = main.manager.matchPrestige(args[1]);
        		String newDisplayName = main.getArgs(args, 2);
        		main.manager.setPrestigeDisplayName(matchedPrestige, newDisplayName);
        		main.configManager.savePrestigesConfig();
        		sender.sendMessage(main.prxAPI.g("setprestigedisplay").replace("%prestige%", matchedPrestige)
        				.replace("%changeddisplay%", newDisplayName + " §f=> " + newDisplayName));
        	} else if (args[0].equalsIgnoreCase("setmasterdisplay")) {
        		String matchedMaster = main.manager.matchMaster(args[1]);
        		String newDisplayName = main.getArgs(args, 2);
        		main.manager.setMasterDisplayName(matchedMaster, newDisplayName);
        		main.configManager.savePrestigesConfig();
        		sender.sendMessage(main.prxAPI.g("setmasterdisplay").replace("%master%", matchedMaster)
        				.replace("%changeddisplay%", newDisplayName + " §f=> " + newDisplayName));
        	} else if (args[0].equalsIgnoreCase("setrank")) {
        		if(Bukkit.getPlayer(args[1]) == null) {
            			sender.sendMessage(main.prxAPI.g("playernotfound").replace("%player%", args[1]));
        			return true;
        		}
        		Player p = Bukkit.getPlayer(args[1]);
        		String newRank = main.manager.matchRank(args[2]);
        		if(!main.prxAPI.rankExists(newRank)) {
        			sender.sendMessage(main.prxAPI.g("rank-notfound").replace("%rank%", newRank));
        			return true;
        		}
        		try {
        		XRankUpdateEvent e = new XRankUpdateEvent(p, RankUpdateCause.RANKSET, newRank);
        		Bukkit.getPluginManager().callEvent(e);
        		if(e.isCancelled()) {
        			return true;
        		}
        		main.prxAPI.setPlayerRank(p, newRank);
        		RankDataHandler rdh = main.prxAPI.getRank(RankPath.getRankPath(newRank, main.prxAPI.getDefaultPath()));
                main.executeCommands(p, rdh.getRankCommands()); 
                if(rdh.getCurrentAddPermissionList() != null) {rdh.getCurrentAddPermissionList().forEach(line -> {
                	main.perm.addPermission(p, line);
                });}
                if(rdh.getCurrentDelPermissionList() != null) {rdh.getCurrentDelPermissionList().forEach(line -> {
                	main.perm.delPermission(p, line);
                });}
        		} catch (Exception exception) {
        			main.getLogger().warning(p.getName() + " data went wrong. possible reasons:");
        			main.getLogger().warning("some ranks on ranks.yml has an invalid nextrank, (not matching case, rank doesn't exist)");
        			main.getLogger().warning("player has old/wrong rankdata in rankdata.yml therefore rankdata.yml must be deleted while the server is offline to prevent data loss");
        			main.getLogger().warning("rankup-vault-groups option on config.yml is enabled while you don't use it (not having groups in the permission plugin that match prisonranksx ranks on the main track)");
        		}
        		sender.sendMessage(main.prxAPI.g("setrank").replace("%target%", p.getName())
        				.replace("%settedrank%", newRank));
                
        	} else if (args[0].equalsIgnoreCase("setpath")) {
        		if(Bukkit.getPlayer(args[1]) == null) {
        			sender.sendMessage(main.prxAPI.g("playernotfound").replace("%player%", args[1]));
    			    return true;
    		    }
    		    Player p = Bukkit.getPlayer(args[1]);
    		    String newPath = main.manager.matchPath(args[2]);
    		    if(!main.prxAPI.pathExists(newPath)) {
    		    	sender.sendMessage(main.prxAPI.g("path-notfound").replace("%path%", newPath));
    		    	return true;
    		    }
    		    main.prxAPI.setPlayerPath(p, newPath);
    		    if(!main.prxAPI.rankExists(main.prxAPI.getPlayerRank(p), newPath)) {
    		    	sender.sendMessage(main.prxAPI.c("&cUnable to find player's rank within the new path."));
    		    	return true;
    		    }
        	} else if (args[0].equalsIgnoreCase("setprestige")) {
        		if(Bukkit.getPlayer(args[1]) == null) {
        			  sender.sendMessage(main.prxAPI.g("playernotfound").replace("%player%", args[1]));
        			return true;
        		}
        		Player p = Bukkit.getPlayer(args[1]);
        		String newPrestige = main.manager.matchPrestige(args[2]);
        		if(!main.prxAPI.prestigeExists(newPrestige)) {
        			if(newPrestige.equalsIgnoreCase("P0") || newPrestige.equalsIgnoreCase("0")) {
                		XUser user = XUser.getXUser(p);
                		XPrestigeUpdateEvent e = new XPrestigeUpdateEvent(p, PrestigeUpdateCause.DELPRESTIGE);
                		Bukkit.getPluginManager().callEvent(e);
                		if(e.isCancelled()) {
                			return true;
                		}
                		main.manager.delPlayerPrestige(user);
                		if(main.globalStorage.getStringListMap().get("PrestigeOptions.prestige-delete-cmds").contains("[rankpermissions]")) {
                		    Set<String> perms = main.prxAPI.allRankAddPermissions;
                		    for(String perm : perms) {
                			    main.perm.delPermission(p, perm);
                		    }
                		} if (main.globalStorage.getStringListMap().get("PrestigeOptions.prestige-delete-cmds").contains("[prestigepermissions]")) {
                			Set<String> perms2 = main.prxAPI.allPrestigeAddPermissions;
                			for(String perm : perms2) {
                				main.perm.delPermission(p, perm);
                			}
                		}
                		for(String cmd : main.globalStorage.getStringListMap().get("PrestigeOptions.prestige-delete-cmds")) {
                			if(!cmd.endsWith("permissions] remove")) {
                				main.executeCommand(p, cmd);
                			}
                		}
                		sender.sendMessage(main.prxAPI.g("delplayerprestige").replace("%player%", p.getName()));
        				return true;
        			} else if (main.prxAPI.numberAPI.isNumber(args[2])) {
        				String prestige = main.prxAPI.getPrestigeNameFromNumber(Integer.valueOf(args[2]));
        				if(prestige != null) {
        		       		XPrestigeUpdateEvent e = new XPrestigeUpdateEvent(p, PrestigeUpdateCause.SETPRESTIGE);
        	        		Bukkit.getPluginManager().callEvent(e);
        	        		if(e.isCancelled()) {
        	        			return true;
        	        		}
        	        		main.prxAPI.setPlayerPrestige(p, prestige);
        	        		PrestigeDataHandler pdh = main.prxAPI.getPrestige(prestige);
        	        		main.executeCommands(p, pdh.getPrestigeCommands());
        	        		if(pdh.getAddPermissionList() != null) {
        	        		pdh.getAddPermissionList().forEach(line -> {
        	        			main.perm.addPermission(p, line);
        	        		});
        	        		}
        	        		if(pdh.getDelPermissionList() != null) {
        	        			pdh.getDelPermissionList().forEach(line -> {
        	        				main.perm.delPermission(p, line);
        	        			});
        	        		}
        	        		sender.sendMessage(main.prxAPI.g("setprestige").replace("%target%", p.getName())
        	        				.replace("%settedprestige%", prestige));
        					return true;
        				}
        			}
        			sender.sendMessage(main.prxAPI.g("prestige-notfound").replace("%prestige%", newPrestige));
        			return true;
        		}
        		XPrestigeUpdateEvent e = new XPrestigeUpdateEvent(p, PrestigeUpdateCause.SETPRESTIGE);
        		Bukkit.getPluginManager().callEvent(e);
        		if(e.isCancelled()) {
        			return true;
        		}
        		main.prxAPI.setPlayerPrestige(p, newPrestige);
        		PrestigeDataHandler pdh = main.prxAPI.getPrestige(newPrestige);
        		main.executeCommands(p, pdh.getPrestigeCommands());
        		if(pdh.getAddPermissionList() != null) {
        		pdh.getAddPermissionList().forEach(line -> {
        			main.perm.addPermission(p, line);
        		});
        		}
        		if(pdh.getDelPermissionList() != null) {
        			pdh.getDelPermissionList().forEach(line -> {
        				main.perm.delPermission(p, line);
        			});
        		}
        		sender.sendMessage(main.prxAPI.g("setprestige").replace("%target%", p.getName())
        				.replace("%settedprestige%", newPrestige));
        		
        	} else if (args[0].equalsIgnoreCase("setmaster")) {
        		if(Bukkit.getPlayer(args[1]) == null) {
      			  sender.sendMessage(main.prxAPI.g("playernotfound").replace("%player%", args[1]));
      			return true;
      		}
      		Player p = Bukkit.getPlayer(args[1]);
      		String newMaster = main.manager.matchMaster(args[2]);
    		if(!main.prxAPI.masterExists(newMaster)) {
    			sender.sendMessage(main.prxAPI.g("master-notfound").replace("%master%", newMaster));
    			return true;
    		}
    		XMasterUpdateEvent e = new XMasterUpdateEvent(p, MasterUpdateCause.SETREBIRTH);
    		Bukkit.getPluginManager().callEvent(e);
    		if(e.isCancelled()) {
    			return true;
    		}
      		main.prxAPI.setPlayerMaster(p, newMaster);
      		MasterDataHandler rdh = main.prxAPI.getMaster(newMaster);
      		main.executeCommands(p, rdh.getMasterCommands());
      		if(rdh.getAddPermissionList() != null) {rdh.getAddPermissionList().forEach(line -> {
      			main.perm.addPermission(p, line);
      		});}
      		if(rdh.getDelPermissionList() != null) {rdh.getDelPermissionList().forEach(line -> {
      			main.perm.delPermission(p, line);
      		});}
      		sender.sendMessage(main.prxAPI.g("setmaster").replace("%target%", p.getName())
      				.replace("%settedmaster%", newMaster));
      		
        	} else if (args[0].equalsIgnoreCase("setrankpath")) {
            	String rank = main.manager.matchRank(args[1]);
            	String newPath = main.manager.matchPath(args[2]);
            	main.manager.setRankPathName(rank, newPath);
            	sender.sendMessage(main.prxAPI.g("setrankpath").replace("%args1%", rank)
            			.replace("%args2%", newPath));
            }
        } else if (args.length >= 4) {
        	if (args[0].equalsIgnoreCase("setrank")) {
        		if(Bukkit.getPlayer(args[1]) == null) {
            			sender.sendMessage(main.prxAPI.g("playernotfound").replace("%player%", args[1]));
        			return true;
        		}
        		Player p = Bukkit.getPlayer(args[1]);
        		String newRank = main.manager.matchRank(args[2]);
        		if(!main.prxAPI.rankExists(newRank, true)) {
        			sender.sendMessage(main.prxAPI.g("rank-notfound").replace("%rank%", newRank));
        			return true;
        		}
        		String newPath = main.manager.matchPath(args[3]);
        		RankPath rp = new RankPath(newRank, newPath);
        		if(!main.prxAPI.rankPathExists(rp)) {
        			sender.sendMessage(main.prxAPI.g("path-notfound").replace("%path%", newPath));
        			return true;
        		}
        		XRankUpdateEvent e = new XRankUpdateEvent(p, RankUpdateCause.RANKSET_BYCONVERT);
        		Bukkit.getPluginManager().callEvent(e);
        		if(e.isCancelled()) {
        			return true;
        		}
        		main.prxAPI.setPlayerRankPath(p, rp);
        		sender.sendMessage(main.prxAPI.g("setrank").replace("%target%", p.getName())
        				.replace("%settedrank%", newRank));
        		sender.sendMessage(main.prxAPI.c("&7Path: " + newPath));
        	}
        	if(args[0].equalsIgnoreCase("createrank")) {
        		double costy;
        		if(!main.prxAPI.numberAPI.isNumber(args[2])) {
        			costy = main.prxAPI.numberAPI.parseBalance(args[2]);
        		} else {
        			costy = Double.valueOf(args[2]);
        		}
        		String multiArgs = main.getArgs(args, 3);
        		String path = "";
        		if(multiArgs.contains("-path:")) {
        		for(String argument : multiArgs.split(" ")) {
        			if(argument.startsWith("-path:")) {
        				path = argument.substring(6);
        			}
        		}
        		path = main.manager.matchPath(path);
        		}
        		String displayName = multiArgs.replace("-path:" + path, "");
        		main.manager.createRank(args[1], Double.valueOf(costy), path, displayName);
        		main.configManager.saveRanksConfig();
        		sender.sendMessage(main.prxAPI.g("createrank").replace("%createdrank%", args[1])
        				.replace("%rankcost%", args[2]));
        		sender.sendMessage(main.prxAPI.c("&7Display: &r" + main.prxAPI.c(displayName)));
        		if(!path.equals("")) {
        			sender.sendMessage(main.prxAPI.c("&7Path: &6" + path));
        		}
        	} else if (args[0].equalsIgnoreCase("createprestige")) {
        		double costy;
        		if(!main.prxAPI.numberAPI.isNumber(args[2])) {
        			costy = main.prxAPI.numberAPI.parseBalance(args[2]);
        		} else {
        			costy = Double.valueOf(args[2]);
        		}
        		String displayName = main.getArgs(args, 3);
        		main.manager.createPrestige(args[1], Double.valueOf(costy), displayName);
        		main.configManager.savePrestigesConfig();
        		sender.sendMessage(main.prxAPI.g("createprestige").replace("%createdprestige", args[1])
        				.replace("%prestigecost%", args[2]));
        		sender.sendMessage(main.prxAPI.c("&7Display: " + main.prxAPI.c(displayName)));
        	} else if (args[0].equalsIgnoreCase("createmaster")) {
        		double costy;
        		if(!main.prxAPI.numberAPI.isNumber(args[2])) {
        			costy = main.prxAPI.numberAPI.parseBalance(args[2]);
        		} else {
        			costy = Double.valueOf(args[2]);
        		}
        		String displayName = main.getArgs(args, 3);
        		main.manager.createMaster(args[1], Double.valueOf(costy), displayName);
        		main.configManager.saveMasteryConfig();
        		sender.sendMessage(main.prxAPI.g("createmaster").replace("%createdmaster%", args[1])
        		        .replace("%mastercost%", args[2]));
        		sender.sendMessage(main.prxAPI.c("&7Display: " + main.prxAPI.c(displayName)));
        	}
        }
		return true;
	}
}

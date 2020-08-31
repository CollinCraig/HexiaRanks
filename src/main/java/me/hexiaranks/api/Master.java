package me.hexiaranks.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import io.samdev.actionutil.ActionUtil;
import me.hexiaranks.HexiaRanks;
import me.hexiaranks.data.MasterRandomCommands;
import me.hexiaranks.events.MasterUpdateEvent;
import me.hexiaranks.events.PrestigeUpdateCause;
import me.hexiaranks.events.RankUpdateCause;
import me.hexiaranks.events.MasterUpdateCause;
import me.hexiaranks.events.PrestigeUpdateEvent;
import me.hexiaranks.events.RankUpdateEvent;
import me.hexiaranks.utils.CompatibleSound.Sounds;

public class Master {
	
	private HexiaRanks main = (HexiaRanks)Bukkit.getPluginManager().getPlugin("HexiaRanks");
	private PRXAPI prxAPI;
	public Master() {
		this.prxAPI = main.prxAPI;
	}
	
	public void master(final Player player) {
		String name = player.getName();
		if(prxAPI.taskedPlayers.contains(name)) {
			if(prxAPI.g("commandspam") == null || prxAPI.g("commandspam").isEmpty()) {
				return;
			}
			player.sendMessage(prxAPI.g("commandspam"));
			return;
		}
		prxAPI.taskedPlayers.add(name);

		Player p = player;
		String master = prxAPI.getPlayerNextMaster(p);
		if(!p.hasPermission(main.masterCommand.getPermission()) && !p.hasPermission("*")) {
			if(prxAPI.g("nopermission") == null || prxAPI.g("nopermission").isEmpty()) {
				return;
			}
			p.sendMessage(prxAPI.g("nopermission"));
			prxAPI.taskedPlayers.remove(name);
			return;
		}
		if(master.equalsIgnoreCase("LASTREBIRTH")) {
			if(prxAPI.h("lastmaster") != null && !prxAPI.h("lastmaster").isEmpty()) {
			  for(String line : prxAPI.h("lastmaster")) {
				  p.sendMessage(prxAPI.c(line));
			  }
			}
			prxAPI.taskedPlayers.remove(name);
			return;
		}
		if(!prxAPI.isLastRank(p)) {
			if(prxAPI.g("nomaster") != null && !prxAPI.g("nomaster").isEmpty()) {
				p.sendMessage(prxAPI.cp(prxAPI.g("nomaster"), p));
			}
			prxAPI.taskedPlayers.remove(name);
			return;
		}
		if(prxAPI.getPlayerNextMasterCost(p) > prxAPI.getPlayerMoney(p)) {
			if(prxAPI.h("master-notenoughmoney") != null && !prxAPI.h("master-notenoughmoney").isEmpty()) {
			for(String line : prxAPI.h("master-notenoughmoney")) {
				p.sendMessage(prxAPI.c(line)
						.replace("%nextmaster%", prxAPI.getPlayerNextMaster(p)).replace("%nextmaster_display%", prxAPI.getPlayerNextMasterDisplayNoFallback(p))
						.replace("%nextmaster_cost%", prxAPI.s(prxAPI.getPlayerNextMasterCost(p))).replace("%nextmaster_cost_formatted%", prxAPI.getPlayerNextMasterCostFormatted(p)));
			}
			}
			prxAPI.taskedPlayers.remove(name);
			return;
		}
		int requiredPrestiges = prxAPI.getRequiredPrestiges(master);
		if(requiredPrestiges > 0) {
		if(requiredPrestiges > prxAPI.getPlayerPrestiges(p)) {
			// ouh
			int masterNumber = prxAPI.getPlayerMasterNumber(p) <= 0 ? 1 : prxAPI.getPlayerMasterNumber(p);
			int left = (requiredPrestiges - prxAPI.getPlayerPrestiges(p)) / masterNumber;
			p.sendMessage(prxAPI.g("master-failed").replace("%prestiges_amount_left%", String.valueOf(left))
					.replace("%prestiges_amount%", String.valueOf(requiredPrestiges)));
			prxAPI.taskedPlayers.remove(name);
			return;
		}
		} else {
			if(!prxAPI.hasNextPrestige(p)) {
				int masterNumber = prxAPI.getPlayerMasterNumber(p) <= 0 ? 1 : prxAPI.getPlayerMasterNumber(p);
				int left = (prxAPI.getPrestigesCollection().size() - prxAPI.getPlayerPrestiges(p) / masterNumber);
				p.sendMessage(prxAPI.g("master-failed").replace("%prestiges_amount_left%", String.valueOf(left))
						.replace("%prestiges_amount%", prxAPI.s(prxAPI.getPrestigesCollection().size())));
				prxAPI.taskedPlayers.remove(name);
				return;
			}
		}
		Map<String, String> stringRequirements = prxAPI.getMasterStringRequirements(master);
		Map<String, Double> numberRequirements = prxAPI.getMasterNumberRequirements(master);
		List<String> customRequirementMessage = prxAPI.getMasterCustomRequirementMessage(master);
		boolean failedRequirements = false;
		if(stringRequirements != null) {
			for(Entry<String, String> entry : stringRequirements.entrySet()) {
				String placeholder = prxAPI.cp(entry.getKey(), p);
				String value = prxAPI.cp(entry.getValue(), p);
				if(!placeholder.equalsIgnoreCase(value)) {
					failedRequirements = true;
				}
			}
		}
		if(numberRequirements != null) {
			for(Entry<String, Double> entry : numberRequirements.entrySet()) {
				String placeholder = prxAPI.cp(entry.getKey(), p);
				double value = entry.getValue();
				if(Double.valueOf(placeholder) < value) {
					failedRequirements = true;
				}
			}
		}
		if(failedRequirements) {
			if(customRequirementMessage != null) {
				customRequirementMessage.forEach(message -> {
					p.sendMessage(prxAPI.cp(message, p));
				});
			}
			prxAPI.taskedPlayers.remove(name);
			return;
		}
		MasterUpdateEvent e = new MasterUpdateEvent(player, MasterUpdateCause.REBIRTHUP);
		main.getServer().getPluginManager().callEvent(e);
		if(e.isCancelled()) {
			prxAPI.taskedPlayers.remove(name);
			return;
		}
		String masterMsg = prxAPI.g("master");
		if(masterMsg != null) {
			if(!masterMsg.isEmpty()) {
				if(main.globalStorage.getBooleanData("Options.send-mastermsg")) {
				p.sendMessage(prxAPI.cp(masterMsg
						.replace("%nextmaster%", prxAPI.getPlayerNextMaster(p))
						.replace("%nextmaster_display%", prxAPI.getPlayerNextMasterDisplayNoFallback(p)), p));
				}
			}
		}
		List<String> addPermissionList = main.masterStorage.getAddPermissionList(master);
		if(addPermissionList != null) {
			if(!addPermissionList.isEmpty()) {
				for(String permission : addPermissionList) {
				main.perm.addPermission(player, permission
						.replace("%player%", name)
						.replace("%nextmaster%", prxAPI.getPlayerNextMaster(p))
						.replace("%nextmaster_display%", prxAPI.getPlayerNextMasterDisplayNoFallback(p)));
				}
			}
		}
		List<String> delPermissionList = main.masterStorage.getDelPermissionList(master);
		if(delPermissionList != null) {
			if(!delPermissionList.isEmpty()) {
				for(String permission : delPermissionList) {
					main.perm.delPermission(p, permission
							.replace("%player%", name)
							.replace("%nextmaster%", prxAPI.getPlayerNextMaster(p))
							.replace("%nextmaster_display%", prxAPI.getPlayerNextMasterDisplayNoFallback(p)));
				}
			}
		}
		List<String> nextMasterCommands = main.masterStorage.getMasterCommands(master);
		if(nextMasterCommands != null) {
			if(!nextMasterCommands.isEmpty()) {
				List<String> newMasterCommands = new ArrayList<>();
				for(String command : nextMasterCommands) {
					newMasterCommands.add(command.replace("%nextmaster%", prxAPI.getPlayerNextMaster(p))
							.replace("%nextmaster_display%", prxAPI.getPlayerNextMasterDisplayNoFallback(p))
							.replace("%nextmaster_cost%", prxAPI.s(prxAPI.getPlayerNextMasterCost(p))));
				}
				main.executeCommands(p, newMasterCommands);
			}
		}
		List<String> actions = main.masterStorage.getActions(master);
		if(actions != null) {
			if(!actions.isEmpty() && main.isActionUtil) {
				ActionUtil.executeActions(p, actions);
			}
		}
		List<String> actionbarText = main.masterStorage.getActionbarMessages(master);
		if(actionbarText != null) {
			if(!actionbarText.isEmpty()) {
				List<String> newActionbarText = new LinkedList<>();
	            for(String line : actionbarText) {
	            	newActionbarText.add(line.replace("%nextmaster%", prxAPI.getPlayerNextMaster(p))
	            			.replace("%nextmaster_display%", prxAPI.getPlayerNextMasterDisplayNoFallbackR(p)));
	            }
			     int actionbarInterval = main.masterStorage.getActionbarInterval(master);
			     main.animateActionbar(p, actionbarInterval, newActionbarText);
			}
		}
		List<String> broadcastMessages = main.masterStorage.getBroadcast(master);
		if(broadcastMessages != null) {
			if(!broadcastMessages.isEmpty()) {
				for(String messageLine : broadcastMessages) {
					Bukkit.broadcastMessage(prxAPI.cp(messageLine
							.replace("%player%", name)
							.replace("%nextmaster%", prxAPI.getPlayerNextMaster(p))
							.replace("%nextmaster_display%", prxAPI.getPlayerNextMasterDisplayNoFallback(p)), p));
				}
			}
		}
		List<String> messages = main.masterStorage.getMsg(master);
		if(messages != null) {
			if(!messages.isEmpty()) {
				for(String messageLine : messages) {
					p.sendMessage(prxAPI.cp(messageLine
							.replace("%nextmaster%", prxAPI.getPlayerNextMaster(p))
							.replace("%nextmaster_display%", prxAPI.getPlayerNextMasterDisplayNoFallback(p)), p));
				}
			}
		}
		Map<String, Double> chances = new HashMap<String, Double>();
		MasterRandomCommands rrc = main.masterStorage.getRandomCommandsManager(master);
		if(rrc != null && rrc.getRandomCommandsMap() != null && !rrc.getRandomCommandsMap().isEmpty()) {
		for(String section : rrc.getRandomCommandsMap().keySet()) {
			Double chance = rrc.getChance(section);
			chances.put(section, chance);
		}
		String randomSection = prxAPI.numberAPI.getChanceFromWeightedMap(chances);
		if(rrc.getCommands(randomSection) != null) {
		List<String> commands = rrc.getCommands(randomSection);
		List<String> replacedCommands = new ArrayList<>();
		  for(String cmd : commands) {
			String pCMD = prxAPI.cp(cmd.replace("%player%", name).replace("%nextmaster%", prxAPI.getPlayerNextMaster(p)), p);
			replacedCommands.add(pCMD);
		  }
		main.executeCommands(p, replacedCommands);
		}
		}
		String nextMasterSoundName = main.globalStorage.getStringData("Options.masteryound-name");
		if(!nextMasterSoundName.isEmpty() && nextMasterSoundName.length() > 1) {
			float nextMasterSoundPitch = (float)main.globalStorage.getDoubleData("Options.masteryound-pitch");
		    float nextMasterSoundVolume = (float)main.globalStorage.getDoubleData("Options.masteryound-volume");
			p.playSound(p.getLocation(), Sounds.valueOf(nextMasterSoundName).bukkitSound(), nextMasterSoundVolume, nextMasterSoundPitch);
		}
		boolean nextMasterHologramIsEnable = main.globalStorage.getBooleanData("Holograms.master.enable");
		if(nextMasterHologramIsEnable && main.isholo) {
			int nextMasterHologramRemoveTime = main.globalStorage.getIntegerData("Holograms.master.remove-time");
			int nextMasterHologramHeight = main.globalStorage.getIntegerData("Holograms.master.height");
			List<String> nextMasterHologramFormat = main.globalStorage.getStringListData("Holograms.master.format");
			spawnHologram(nextMasterHologramFormat, nextMasterHologramRemoveTime, nextMasterHologramHeight, p);
		}
		main.sendMasterFirework(p);
		main.econ.withdrawPlayer(p, prxAPI.getPlayerNextMasterCost(p));
		if(main.globalStorage.getBooleanData("MasterOptions.ResetMoney")) {
			main.econ.withdrawPlayer(p, prxAPI.getPlayerMoney(p));
		}
		if(main.globalStorage.getBooleanData("MasterOptions.ResetRank")) {
			RankUpdateEvent xrue = new RankUpdateEvent(p, RankUpdateCause.RANKSET_BYREBIRTH, main.globalStorage.getStringData("defaultrank"));
			Bukkit.getScheduler().runTask(main, () -> {
				Bukkit.getPluginManager().callEvent(xrue);
			if(xrue.isCancelled()) {
				return;
			}
			});
			main.playerStorage.setPlayerRank(p, main.globalStorage.getStringData("defaultrank"));
		}
		if(main.globalStorage.getBooleanData("MasterOptions.ResetPrestige")) {
			PrestigeUpdateEvent xpue = new PrestigeUpdateEvent(p, PrestigeUpdateCause.SETPRESTIGE_BY_REBIRTH);
			Bukkit.getScheduler().runTask(main, () -> {
				Bukkit.getPluginManager().callEvent(xpue);
			if(xpue.isCancelled()) {
				return;
			}
			});
			main.playerStorage.setPlayerPrestige(p, prxAPI.getFirstPrestige());
		}
		List<String> masterCommands = main.globalStorage.getStringListData("MasterOptions.master-cmds");
		if(!masterCommands.isEmpty()) {
           masterCommands.forEach(cmd -> {
        	   if(cmd.startsWith("[rankpermissions]")) {
        		   prxAPI.allRankAddPermissions.forEach(permission -> {
        		   main.perm.delPermission(p, permission);
        		   });
        	   } else if (cmd.startsWith("[prestigepermissions]")) {
        		   prxAPI.allPrestigeAddPermissions.forEach(permission -> {
        			   main.perm.delPermission(p, permission);
        		   });
        	   } else if (cmd.startsWith("[masterpermissions]")) {
        		   prxAPI.allMasterAddPermissions.forEach(permission -> {
        			   main.perm.delPermission(p, permission);
        		   });
        	   } else {
        		   main.executeCommand(p, cmd);
        	   }
           });
		}
		Bukkit.getScheduler().runTaskLater(main, () -> {
		main.playerStorage.setPlayerMaster(p, master);
		prxAPI.taskedPlayers.remove(name);
		
		}, 1);
	}
	
	public void spawnHologram(List<String> format, int removeTime, int height, Player player) {
		Player p = player;
		String name = p.getName();
		Hologram hologram = HologramsAPI.createHologram(main, p.getLocation().add(0, height, 0));
		hologram.setAllowPlaceholders(true);
		for(String line : format) {
			String updatedLine = main.getString(line.replace("%player%", name)
					.replace("%player_display%", p.getDisplayName())
					.replace("%nextmaster%", prxAPI.getPlayerNextMaster(p))
					.replace("%nextmaster_display%", main.getString(prxAPI.getPlayerNextMasterDisplay(p)))
					, name);
			hologram.appendTextLine(updatedLine);
		}
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable () {public void run() {
        	hologram.delete();
        }}, 20L * removeTime);
	}
}

package me.hexiaranks.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import me.hexiaranks.HexiaRanks;

public class MasterRandomCommands {
	
	private String masterName;
	private boolean withKeys;
	private Map<String, Object> randomCommandsMap;
	private List<List<String>> commandsList;
	private List<Double> chances;
	
	private static HexiaRanks main = (HexiaRanks)Bukkit.getPluginManager().getPlugin("HexiaRanks");
	
	public MasterRandomCommands(String masterName, boolean withKeys) {this.setMasterName(masterName); this.withKeys = withKeys;
	this.commandsList = new ArrayList<>();
	this.chances = new ArrayList<>();
	this.randomCommandsMap = new HashMap<String, Object>();
	}
	
	public MasterRandomCommands(String masterName, boolean withKeys, boolean loadSections) {this.setMasterName(masterName); this.withKeys = withKeys;
	this.commandsList = new ArrayList<>();
	this.chances = new ArrayList<>();
	this.randomCommandsMap = new HashMap<String, Object>();
	  if(loadSections) {
		  loadSections(masterName);
	  }
	}
	
	public void loadSections(String masterName) {
		Map<String, Object> randomCommandsMap = new HashMap<String, Object>();
		if(main.getConfigManager().masteryConfig.getConfigurationSection("Mastery."  + masterName + ".randomcmds") != null &&
				!main.getConfigManager().masteryConfig.getConfigurationSection("Mastery." + masterName + ".randomcmds").getKeys(false).isEmpty()) {
		randomCommandsMap = main.getConfigManager().masteryConfig.getConfigurationSection("Mastery." + masterName + ".randomcmds").getValues(withKeys);
		}
		List<List<String>> commandsList = new ArrayList<>();
		List<Double> chances = new ArrayList<>();
		if(!randomCommandsMap.isEmpty()) {
		  for(String section : randomCommandsMap.keySet()) {
			  commandsList.add(main.getConfigManager().masteryConfig.getStringList("Mastery." + masterName + ".randomcmds." + section + ".commands"));
			  chances.add(main.getConfigManager().masteryConfig.getDouble("Mastery." + masterName + ".randomcmds." + section + ".chance"));
		  }
		}
		setRandomCommandsMap(randomCommandsMap);
		setCommandsList(commandsList);
		setChances(chances);
	}
	//public Map<String, Object> getCommandSections() {
		//return main.getConfigManager().ranksConfig.getConfigurationSection("Ranks." + pathName + "." + rankName + ".randomcmds").getValues(false);
	//}
	
	public void setRandomCommandsMap(Map<String, Object> randomCommandsMap) {
		this.randomCommandsMap = randomCommandsMap;
	}
	
	public void setCommandsList(List<List<String>> commandsList) {
		this.commandsList = commandsList;
	}
	
	public void setChances(List<Double> chances) {
		this.chances = chances;
	}
	
	public Map<String, Object> getRandomCommandsMap() {
		return randomCommandsMap;
	}
	
	public List<List<String>> getCommandsListCollection() {
		return commandsList;
	}
	
	public List<Double> getChancesCollection() {
		return chances;
	}
    
	public List<String> getCommands(String section) {
		if(randomCommandsMap.containsKey(section)) {
		return ((ConfigurationSection)randomCommandsMap.get(section)).getStringList("commands");
		}
		return null;
	}
	
	public Double getChance(String section) {
		return ((ConfigurationSection)randomCommandsMap.get(section)).getDouble("chance");
	}

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}
}

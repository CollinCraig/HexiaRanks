package me.hexiaranks.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import me.hexiaranks.HexiaRanks;

public class MasterDataStorage {
	
	public Map<String, MasterDataHandler> masterData;
	private HexiaRanks main;
	public MasterDataStorage(HexiaRanks main) {
		this.main = main;
		this.masterData = new LinkedHashMap<String, MasterDataHandler>();
	}
	
	public Map<String, MasterDataHandler> getMasterData() {
		return this.masterData;
	}
	
	public void putData(String name, MasterDataHandler masterDataHandler) {
		getMasterData().put(name, masterDataHandler);
	}
	
	public MasterDataHandler getDataHandler(String name) {
		return getMasterData().get(name);
	}
	
	public GlobalDataStorage gds() {
		return this.main.globalStorage;
	}
	/**
	 * Should only be used onEnable()
	 * can be used as a reload
	 */
	public void loadMasteryData() {
			for(String masterName : main.getConfigManager().masteryConfig.getConfigurationSection("Mastery").getKeys(false)) {
				String nextMasterName = main.getConfigManager().masteryConfig.getString("Mastery." + masterName + ".nextmaster");
				String masterDisplayName = main.getConfigManager().masteryConfig.getString("Mastery." + masterName + ".display");
				Double masterCost = main.getConfigManager().masteryConfig.getDouble("Mastery." + masterName + ".cost", 0.0);
				//Double nextMasterCost = main.getConfigManager().masteryConfig.getDouble("Mastery." + nextMasterName + ".cost", 0.0);
				//String nextMasterDisplayName = main.getConfigManager().masteryConfig.getString("Mastery." + nextMasterName + ".display");
				Double prestigeIncrease = 0.0;
                prestigeIncrease = loadDouble("Mastery." + masterName + ".prestige_cost_increase_percentage");
				List<String> masterCommands = main.getConfigManager().masteryConfig.getStringList("Mastery." + masterName + ".executecmds");
				//List<String> nextMasterCommands = main.getConfigManager().masteryConfig.getStringList("Mastery." + nextMasterName + ".executecmds");
				List<String> actionbarMessages = main.getConfigManager().masteryConfig.getStringList("Mastery." + masterName + ".actionbar.text");
				int actionbarInterval = main.getConfigManager().masteryConfig.getInt("Mastery." + masterName + ".actionbar.interval");
				List<String> broadcastMessages = main.getConfigManager().masteryConfig.getStringList("Mastery." + masterName + ".broadcast");
				List<String> messages = main.getConfigManager().masteryConfig.getStringList("Mastery." + masterName + ".msg");
				List<String> actions = main.getConfigManager().masteryConfig.getStringList("Mastery." + masterName + ".actions");
				List<String> addPermissionList = main.getConfigManager().masteryConfig.getStringList("Mastery." + masterName + ".addpermission");
				List<String> delPermissionList = main.getConfigManager().masteryConfig.getStringList("Mastery." + masterName + ".delpermission");
                int requiredPrestiges = 0;
                requiredPrestiges = loadInt("Mastery." + masterName + ".required_prestiges");
				MasterRandomCommands randomCommandsManager = new MasterRandomCommands(masterName, false, true);
				FireworkManager fireworkManager = new FireworkManager(masterName, LevelType.REBIRTH, "master");
				boolean sendFirework = main.getConfigManager().masteryConfig.getBoolean("Mastery." + masterName + ".send-firework");
				MasterDataHandler rbdh = new MasterDataHandler(masterName);
				Map<String, Double> numberRequirements = new HashMap<>();
				Map<String, String> stringRequirements = new HashMap<>();
				List<String> customRequirementMessage = new ArrayList<>();
				if(main.getConfigManager().masteryConfig.isSet("Mastery." + masterName + ".requirements")) {
					for(String requirementCondition : main.getConfigManager().masteryConfig.getStringList("Mastery." + masterName + ".requirements")) {
						if(requirementCondition.contains("->")) {
							String[] splitter = requirementCondition.split("->");
							String requirement = splitter[0];
							String value = splitter[1];
							stringRequirements.put(requirement, value);
						} else if (requirementCondition.contains(">>")) {
							String[] splitter = requirementCondition.split(">>");
							String requirement = splitter[0];
							double value = Double.valueOf(splitter[1]);
							numberRequirements.put(requirement, value);
						}
					}
				}
				if(main.getConfigManager().masteryConfig.isSet("Mastery." + masterName + ".custom-requirement-message")) {
					for(String messageLine : main.getConfigManager().masteryConfig.getStringList("Mastery." + masterName + ".custom-requirement-message")) {
						customRequirementMessage.add(gds().parseHexColorCodes(messageLine));
					}
				}
				if(!stringRequirements.isEmpty()) {
					rbdh.setStringRequirements(stringRequirements);
				}
				if(!numberRequirements.isEmpty()) {
					rbdh.setNumberRequirements(numberRequirements);
				}
				if(!customRequirementMessage.isEmpty()) {
					rbdh.setCustomRequirementMessage(customRequirementMessage);
				}
				rbdh.setName(masterName);
                rbdh.setDisplayName(gds().parseHexColorCodes(masterDisplayName));
                rbdh.setCost(masterCost);
                rbdh.setNextMasterName(nextMasterName);
                rbdh.setPrestigeCostIncreasePercentage(prestigeIncrease);
                //rbdh.setNextMasterCost(nextMasterCost);
                //rbdh.setNextMasterDisplayName(nextMasterDisplayName);
                rbdh.setMasterCommands(gds().parseHexColorCodes(masterCommands));
                //rbdh.setNextMasterCommands(nextMasterCommands);
                rbdh.setActionbarMessages(gds().parseHexColorCodes(actionbarMessages));
                rbdh.setActionbarInterval(actionbarInterval);
                rbdh.setBroadcastMessages(gds().parseHexColorCodes(broadcastMessages));
                rbdh.setMsg(gds().parseHexColorCodes(messages));
                rbdh.setActions(gds().parseHexColorCodes(actions));
                rbdh.setAddPermissionList(addPermissionList);
                rbdh.setDelPermissionList(delPermissionList);
                rbdh.setRandomCommandsManager(randomCommandsManager);
                rbdh.setFireworkManager(fireworkManager);
                rbdh.setSendFirework(sendFirework);
                rbdh.setRequiredPrestiges(requiredPrestiges);
                getMasterData().put(masterName, rbdh);
			}
	}
	
	public Double loadDouble(String node) {
		if(!main.getConfigManager().masteryConfig.isSet(node) || !main.getConfigManager().masteryConfig.isDouble(node)) {
			return 0.0;
		}
		return main.getConfigManager().masteryConfig.getDouble(node, 0.0);
	}
	
	public Integer loadInt(String node) {
		if(!main.getConfigManager().masteryConfig.isSet(node) || !main.getConfigManager().masteryConfig.isInt(node)) {
			return 0;
		}
		return main.getConfigManager().masteryConfig.getInt(node, 0);
	}
	
	public void loadMasterData(String masterName) {
		String nextMasterName = main.getConfigManager().masteryConfig.getString("Mastery." + masterName + ".nextmaster");
		String masterDisplayName = main.getConfigManager().masteryConfig.getString("Mastery." + masterName + ".display");
		Double masterCost = main.getConfigManager().masteryConfig.getDouble("Mastery." + masterName + ".cost", 0.0);
		Double nextMasterCost = main.getConfigManager().masteryConfig.getDouble("Mastery." + nextMasterName + ".cost", 0.0);
		String nextMasterDisplayName = main.getConfigManager().masteryConfig.getString("Mastery." + nextMasterName + ".display");
		List<String> masterCommands = main.getConfigManager().masteryConfig.getStringList("Mastery." + masterName + ".executecmds");
		//List<String> nextMasterCommands = main.getConfigManager().masteryConfig.getStringList("Mastery." + nextMasterName + ".executecmds");
		List<String> actionbarMessages = main.getConfigManager().masteryConfig.getStringList("Mastery." + masterName + ".actionbar.text");
		int actionbarInterval = main.getConfigManager().masteryConfig.getInt("Mastery." + masterName + ".actionbar.interval");
		List<String> broadcastMessages = main.getConfigManager().masteryConfig.getStringList("Mastery." + masterName + ".broadcast");
		List<String> messages = main.getConfigManager().masteryConfig.getStringList("Mastery." + masterName + ".text");
		List<String> actions = main.getConfigManager().masteryConfig.getStringList("Mastery." + masterName + ".actions");
		List<String> addPermissionList = main.getConfigManager().masteryConfig.getStringList("Mastery." + masterName + ".addpermission");
		List<String> delPermissionList = main.getConfigManager().masteryConfig.getStringList("Mastery." + masterName + ".delpermission");
		MasterRandomCommands randomCommandsManager = new MasterRandomCommands(masterName, true);
		FireworkManager fireworkManager = new FireworkManager(masterName, LevelType.REBIRTH, "master");
		boolean sendFirework = main.getConfigManager().masteryConfig.getBoolean("Mastery." + masterName + ".send-firework");
		MasterDataHandler rbdh = new MasterDataHandler(masterName);
		rbdh.setName(masterName);
        rbdh.setDisplayName(masterDisplayName);
        rbdh.setCost(masterCost);
        rbdh.setNextMasterName(nextMasterName);
        rbdh.setNextMasterCost(nextMasterCost);
        rbdh.setNextMasterDisplayName(nextMasterDisplayName);
        //rbdh.setNextMasterCommands(nextMasterCommands);
        rbdh.setMasterCommands(masterCommands);
        rbdh.setActionbarMessages(actionbarMessages);
        rbdh.setActionbarInterval(actionbarInterval);
        rbdh.setBroadcastMessages(broadcastMessages);
        rbdh.setMsg(messages);
        rbdh.setActions(actions);
        rbdh.setAddPermissionList(addPermissionList);
        rbdh.setDelPermissionList(delPermissionList);
        rbdh.setRandomCommandsManager(randomCommandsManager);
        rbdh.setFireworkManager(fireworkManager);
        rbdh.setSendFirework(sendFirework);
        getMasterData().put(masterName, rbdh);
	}
	
	/**
	 * 
	 * @return mastery collection
	 */
	public List<String> getMasteryCollection() {
		return new LinkedList<>(this.masterData.keySet());
	}
	
	public String getNextMasterName(String masterName) {
		return masterData.get(masterName).getNextMasterName();
	}
	
	public Double getCost(String masterName) {
		return masterData.get(masterName).getCost();
	}
	
	public String getDisplayName(String masterName) {
		return masterData.get(masterName).getDisplayName();
	}
	
	public Double getNextMasterCost(String masterName) {
		return masterData.get(masterData.get(masterName).getNextMasterName()).getCost();
	}
	
	public String getNextMasterDisplayName(String masterName) {
		return masterData.get(masterData.get(masterName).getNextMasterName()).getDisplayName();
	}
	
	public List<String> getMasterCommands(String masterName) {
		return masterData.get(masterName).getMasterCommands();
	}
	
	public List<String> getNextMasterCommands(String masterName) {
		return masterData.get(masterData.get(masterName).getNextMasterName()).getMasterCommands();
	}
	
	public int getActionbarInterval(String masterName) {
		return masterData.get(masterName).getActionbarInterval();
	}
	
	public int getNextActionbarInterval(String masterName) {
		return masterData.get(masterData.get(masterName).getNextMasterName()).getActionbarInterval();
	}
	
	public List<String> getActionbarMessages(String masterName) {
		return masterData.get(masterName).getActionbarMessages();
	}
	
	public List<String> getBroadcast(String masterName) {
		return masterData.get(masterName).getBroadcast();
	}
	
	public List<String> getMsg(String masterName) {
		return masterData.get(masterName).getMsg();
	}
	
	public List<String> getActions(String masterName) {
		return masterData.get(masterName).getActions();
	}
	
	public List<String> getAddPermissionList(String masterName) {
		return masterData.get(masterName).getAddPermissionList();
	}
	
	public List<String> getDelPermissionList(String masterName) {
		return masterData.get(masterName).getDelPermissionList();
	}
	
	public MasterRandomCommands getRandomCommandsManager(String masterName) {
		return masterData.get(masterName).getRandomCommandsManager();
	}
	
	public List<String> getRandomCommands(String masterName) {
		return masterData.get(masterName).getMasterCommands();
	}
	
	public FireworkManager getFireworkManager(String masterName) {
		return masterData.get(masterName).getFireworkManager();
	}
	
	public Map<String, Object> getFireworkBuilder(String masterName) {
		return masterData.get(masterName).getFireworkManager().getFireworkBuilder();
	}
	
	public boolean isSendFirework(String masterName) {
		return masterData.get(masterName).isSendFirework();
	}
	
	public Double getPrestigeCostIncreasePercentage(String masterName) {
		return masterData.get(masterName).getPrestigeCostIncreasePercentage();
	}
	
	public int getRequiredPrestiges(String masterName) {
		return masterData.get(masterName).getRequiredPrestiges();
	}
	
	@SuppressWarnings("unchecked")
	public void setData(String node, Object value) {
		if(value == null || node.contains("LASTREBIRTH")) {
			return;
		}
		if(value instanceof Integer) {
			if((int)value == 0) {
				return;
			}
		}
		if(value instanceof Double) {
			if((double)value == 0) {
				return;
			}
		}
		if(value instanceof Boolean) {
			if((boolean)value == false) {
				return;
			}
		}
		if(value instanceof List) {
			if(((List<String>)value).isEmpty()) {
				return;
			}
		}
		if(value instanceof Set) {
			if(((Set<String>)value).isEmpty()) {
				return;
			}
		}
		if(value instanceof Map) {
			if(((Map<String,Object>)value).isEmpty()) {
				return;
			}
		}
		main.getConfigManager().masteryConfig.set(node, value);
	}
	/**
	 * 
	 * Use when you want to update a Master option then
	 * use the method loadMasterData(String masterName) to load the data in game
	 */
	public void saveMasterData(String masterName) {
			
			String master = masterName;
            setData("Mastery." + master + ".nextmaster", masterData.get(masterName).getNextMasterName());
            setData("Mastery." + master + ".cost", masterData.get(masterName).getCost());
            setData("Mastery." + master + ".display", masterData.get(masterName).getDisplayName());
            // setData("Mastery." + master + ".executecmds", masterData.get(masterName).getMasterCommands());
            // setData("Mastery." + nextMaster + ".cost", masterData.get(masterName).getNextMasterCost());
            // setData("Mastery." + nextMaster + ".display", masterData.get(masterName).getNextMasterDisplayName());
            // setData("Mastery." + nextMaster + ".executecmds", masterData.get(masterName).getNextMasterCommands());
            // setData("Mastery." + masterData.get(masterName) + ".actionbar.interval", masterData.get(masterName).getActionbarInterval());
            // setData("Mastery." + masterData.get(masterName) + ".actionbar.text", masterData.get(masterName).getActionbarMessages());
            // setData("Mastery." + masterData.get(masterName) + ".broadcast", masterData.get(masterName).getBroadcast());
            // setData("Mastery." + masterData.get(masterName) + ".msg", masterData.get(masterName).getMsg());
            // setData("Mastery." + masterData.get(masterName) + ".actions", masterData.get(masterName).getActions());
            // setData("Mastery." + masterData.get(masterName) + ".addpermission", masterData.get(masterName).getAddPermissionList());
            // setData("Mastery." + masterData.get(masterName) + ".delpermission", masterData.get(masterName).getDelPermissionList());
            // setData("Mastery." + masterData.get(masterName) + ".randomcmds", masterData.get(masterName).getRandomCommandsManager().getRandomCommandsMap());
            // setData("Mastery." + masterData.get(masterName) + ".firework", masterData.get(masterName).getFireworkManager());
            // setData("Mastery." + masterData.get(masterName) + ".send-firework", masterData.get(masterName).isSendFirework());
	}
	/**
	 * Should only be used onDisable()
	 */
	public void saveMasteryData() {
		if(!main.isMasterEnabled) {
			return;
		}
			for(Entry<String, MasterDataHandler> master : masterData.entrySet()) {
				// String nextMaster = masterData.get(master.getKey()).getNextMasterName();
                 setData("Mastery." + master.getKey() + ".nextmaster", master.getValue().getNextMasterName());
                 setData("Mastery." + master.getKey() + ".cost", master.getValue().getCost());
                 setData("Mastery." + master.getKey() + ".executecmds", master.getValue().getMasterCommands());
                 //setData("Mastery." + nextMaster + ".cost", master.getValue().getNextMasterCost());
                 //setData("Mastery." + nextMaster + ".display", master.getValue().getNextMasterDisplayName());
                 //setData("Mastery." + nextMaster + ".executecmds", master.getValue().getNextMasterCommands());
                 setData("Mastery." + master.getKey() + ".actionbar.interval", master.getValue().getActionbarInterval());
                 setData("Mastery." + master.getKey() + ".actionbar.text", master.getValue().getActionbarMessages());
                 setData("Mastery." + master.getKey() + ".broadcast", master.getValue().getBroadcast());
                 setData("Mastery." + master.getKey() + ".msg", master.getValue().getMsg());
                 setData("Mastery." + master.getKey() + ".actions", master.getValue().getActions());
                 setData("Mastery." + master.getKey() + ".addpermission", master.getValue().getAddPermissionList());
                 setData("Mastery." + master.getKey() + ".delpermission", master.getValue().getDelPermissionList());
                 setData("Mastery." + master.getKey() + ".prestige_cost_increase_percentage", master.getValue().getPrestigeCostIncreasePercentage());
                 setData("Mastery." + master.getKey() + ".required_prestiges", master.getValue().getRequiredPrestiges());
                 if(master.getValue().getRandomCommandsManager() != null) {
                // setData("Mastery." + master.getKey() + ".randomcmds", master.getValue().getRandomCommandsManager().getRandomCommandsMap());
                 }
                 if(master.getValue().getFireworkManager() != null) {
                // setData("Mastery." + master.getKey() + ".firework", master.getValue().getFireworkManager());
                 }
                 if(master.getValue().isSendFirework()) {
                 setData("Mastery." + master.getKey() + ".send-firework", master.getValue().isSendFirework());
                 }
			}
	}
}

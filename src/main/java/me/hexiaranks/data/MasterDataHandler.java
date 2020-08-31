package me.hexiaranks.data;

import java.util.List;
import java.util.Map;

public class MasterDataHandler {
  
	private String masterName;
	private String masterDisplayName;
	private Double masterCost;
	private String nextMasterName;
	private String nextMasterDisplayName;
	private Double nextMasterCost;
	private Double prestigeCostIncreasePercentage;
	private List<String> masterCommands;
	private List<String> actionbarMessages;
	private int actionbarInterval;
	private List<String> actions;
	private List<String> broadcastMessages;
	private List<String> messages;
	private List<String> addPermissionList;
	private List<String> delPermissionList;
	private boolean sendFirework;
	private MasterRandomCommands randomCommandsManager;
	private FireworkManager fireworkManager;
	private int requiredPrestiges;
	private Map<String, Double> numberRequirements;
	private Map<String, String> stringRequirements;
	private List<String> customRequirementMessage;
	
	/**
	 * 
	 * @param masterName
	 */
	public MasterDataHandler(String masterName) {this.masterName = masterName;}
	
	public String getName() {
		return masterName;
	}
	
	public void setName(String masterName) {
		this.masterName = masterName;
	}
	
	public Double getCost() {
		return masterCost;
	}
	
	public void setCost(Double masterCost) {
		this.masterCost = masterCost;
	}
	
	public String getDisplayName() {
		return masterDisplayName;
	}
	
	public void setDisplayName(String masterDisplayName) {
		this.masterDisplayName = masterDisplayName;
	}
	
	public String getNextMasterName() {
		return nextMasterName;
	}
	
	public void setNextMasterName(String nextMasterName) {
		this.nextMasterName = nextMasterName;
	}
	
	public String getNextMasterDisplayName() {
		return nextMasterDisplayName;
	}
	
	public void setNextMasterDisplayName(String nextMasterDisplayName) {
		this.nextMasterDisplayName = nextMasterDisplayName;
	}
	
	public Double getNextMasterCost() {
		return nextMasterCost;
	}
	
	public void setNextMasterCost(Double nextMasterCost) {
		this.nextMasterCost = nextMasterCost;
	}
	
	public List<String> getMasterCommands() {
		return masterCommands;
	}
	
	public void setMasterCommands(List<String> masterCommands) {
		if(masterCommands != null && !masterCommands.isEmpty()) {
		this.masterCommands = masterCommands;
		}
	}
	
	
	public List<String> getActionbarMessages() {
		return actionbarMessages;
	}
	
	public void setActionbarMessages(List<String> actionbarMessages) {
		if(actionbarMessages != null && !actionbarMessages.isEmpty()) {
		this.actionbarMessages = actionbarMessages;
		}
	}
	
	public int getActionbarInterval() {
		return actionbarInterval;
	}
	
	public void setActionbarInterval(int actionbarInterval) {
		if(actionbarInterval != 0) {
		this.actionbarInterval = actionbarInterval;
		}
	}
	
	public List<String> getBroadcast() {
		return broadcastMessages;
	}
	
	public void setBroadcastMessages(List<String> broadcastMessages) {
		if(broadcastMessages != null && !broadcastMessages.isEmpty()) {
		this.broadcastMessages = broadcastMessages;
		}
	}
	
	public List<String> getMsg() {
		return messages;
	}
	
	public void setMsg(List<String> messages) {
		if(messages != null && !messages.isEmpty()) {
		this.messages = messages;
		}
	}
	
	public List<String> getActions() {
		return actions;
	}
	
	public void setActions(List<String> actions) {
		if(actions != null && !actions.isEmpty()) {
		this.actions = actions;
		}
	}
	
	public List<String> getAddPermissionList() {
		return addPermissionList;
	}
	
	public void setAddPermissionList(List<String> addPermissionList) {
		if(addPermissionList != null && !addPermissionList.isEmpty()) {
		this.addPermissionList = addPermissionList;
		}
	}
	
	public List<String> getDelPermissionList() {
		return delPermissionList;
	}
	
	public void setDelPermissionList(List<String> delPermissionList) {
		if(delPermissionList != null && !delPermissionList.isEmpty()) {
		this.delPermissionList = delPermissionList;
		}
	}
	
	public MasterRandomCommands getRandomCommandsManager() {
		return randomCommandsManager;
	}
	
	public void setRandomCommandsManager(MasterRandomCommands randomCommandsSet) {
		if(randomCommandsSet != null && randomCommandsSet.getRandomCommandsMap() != null
				&& !randomCommandsSet.getRandomCommandsMap().isEmpty()) {
		this.randomCommandsManager = randomCommandsSet;
		}
	}
	
	public FireworkManager getFireworkManager() {
		return fireworkManager;
	}
	
	public void setFireworkManager(FireworkManager fireworkManager) {
		if(fireworkManager != null) {
			if(fireworkManager.getFireworkBuilder() == null) {
				return;
			}
			if(fireworkManager.getFireworkBuilder().isEmpty()) {
				return;
			}
		this.fireworkManager = fireworkManager;
		}
	}
	
	public boolean isSendFirework() {
		return sendFirework;
	}
	
	public void setSendFirework(boolean sendFirework) {
		if(sendFirework) {
		this.sendFirework = sendFirework;
		}
	}
  
	public Double getPrestigeCostIncreasePercentage() {
		return prestigeCostIncreasePercentage;
	}
	
	public void setPrestigeCostIncreasePercentage(Double prestigeCostIncreasePercentage) {
		this.prestigeCostIncreasePercentage = prestigeCostIncreasePercentage;
	}
	
	public int getRequiredPrestiges() {
		return requiredPrestiges;
	}
	
	public void setRequiredPrestiges(int requiredPrestiges) {
		this.requiredPrestiges = requiredPrestiges;
	}

	public Map<String, Double> getNumberRequirements() {
		return numberRequirements;
	}

	public void setNumberRequirements(Map<String, Double> numberRequirements) {
		this.numberRequirements = numberRequirements;
	}

	public Map<String, String> getStringRequirements() {
		return stringRequirements;
	}

	public void setStringRequirements(Map<String, String> stringRequirements) {
		this.stringRequirements = stringRequirements;
	}

	public List<String> getCustomRequirementMessage() {
		return customRequirementMessage;
	}

	public void setCustomRequirementMessage(List<String> customRequirementMessage) {
		this.customRequirementMessage = customRequirementMessage;
	}
	
}

package me.hexiaranks.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;

import me.hexiaranks.HexiaRanks;

public interface GlobalDataStorage {
	
	Map<String, String> stringData = new HashMap<>();
	Map<String, Integer> integerData = new HashMap<>();
	Map<String, Double> doubleData = new HashMap<>();
	Map<String, Boolean> booleanData = new HashMap<>();
	Map<String, List<String>> stringListData = new HashMap<>();
	Map<String, Set<String>> stringSetData = new HashMap<>();
	Map<String, Map<String, Object>> mapData = new HashMap<>();
	Map<String, Object> globalData = new HashMap<>();
	HexiaRanks main = (HexiaRanks) Bukkit.getPluginManager().getPlugin("HexiaRanks");
	
	String translateHexColorCodes(final String message);
	String parseHexColorCodes(final String message);
	List<String> translateHexColorCodes(final List<String> message);
	List<String> parseHexColorCodes(final List<String> message);
	
	public default Map<String, String> getStringMap() {
		return this.stringData;
	}
	
	public default Map<String, Integer> getIntegerMap() {
		return this.integerData;
	}
	
	public default Map<String, Double> getDoubleMap() {
		return this.doubleData;
	}
	
	public default Map<String, Boolean> getBooleanMap() {
		return this.booleanData;
	}
	
	public default Map<String, List<String>> getStringListMap() {
		return this.stringListData;
	}
	
	public default Map<String, Set<String>> getStringSetMap() {
		return this.stringSetData;
	}
	
	public default Map<String, Map<String, Object>> getMap() {
		return this.mapData;
	}
	
	public default Map<String, Object> getGlobalMap() {
		return this.globalData;
	}
	
	/**
	 * 
	 * @param configNode
	 * @return loaded string
	 */
	public default String registerStringData(String configNode) {
		getStringMap().put(configNode, main.getConfig().getString(configNode));
		getGlobalMap().put(configNode, main.getConfig().getString(configNode));
		return main.getConfig().getString(configNode);
	}
	
	/**
	 * 
	 * @param configNode
	 * @return loaded integer
	 */
	public default Integer registerIntegerData(String configNode) {
		getIntegerMap().put(configNode, main.getConfig().getInt(configNode));
		getGlobalMap().put(configNode, main.getConfig().getInt(configNode));
		return main.getConfig().getInt(configNode);
	}
	
	/**
	 * 
	 * @param configNode
	 * @return loaded double
	 */
	public default double registerDoubleData(String configNode) {
		getDoubleMap().put(configNode, main.getConfig().getDouble(configNode));
		getGlobalMap().put(configNode, main.getConfig().getDouble(configNode));
		return main.getConfig().getDouble(configNode);
	}
	
	/**
	 * 
	 * @param configNode
	 * @return loaded boolean
	 */
	public default boolean registerBooleanData(String configNode) {
		getBooleanMap().put(configNode, main.getConfig().getBoolean(configNode));
		getGlobalMap().put(configNode, main.getConfig().getBoolean(configNode));
		return main.getConfig().getBoolean(configNode);
	}
	
	/**
	 * 
	 * @param configNode
	 * @return loaded string list
	 */
	public default List<String> registerStringListData(String configNode) {
		getStringListMap().put(configNode, main.getConfig().getStringList(configNode));
		getGlobalMap().put(configNode, main.getConfig().getStringList(configNode));
		return main.getConfig().getStringList(configNode);
	}
	
	/**
	 * 
	 * @param configNode
	 * @return loaded string set
	 */
	public default Set<String> registerStringSetData(String configNode) {
		getStringSetMap().put(configNode, main.getConfig().getConfigurationSection(configNode).getKeys(false));
		getGlobalMap().put(configNode, main.getConfig().getConfigurationSection(configNode).getKeys(false));
		return main.getConfig().getConfigurationSection(configNode).getKeys(false);
	}
	
	/**
	 * 
	 * @param configNode
	 * @param withKeys
	 * @return loaded string set
	 */
	public default Set<String> registerStringSetData(String configNode, boolean withKeys) {
		getStringSetMap().put(configNode, main.getConfig().getConfigurationSection(configNode).getKeys(withKeys));
		getGlobalMap().put(configNode, main.getConfig().getConfigurationSection(configNode).getKeys(withKeys));
		return main.getConfig().getConfigurationSection(configNode).getKeys(withKeys);
	}
	
	public default Map<String, Object> registerMapData(String configNode, boolean withKeys) {
		if(main.getConfig().getConfigurationSection(configNode) != null) {
		getMap().put(configNode, main.getConfig().getConfigurationSection(configNode).getValues(withKeys));
		getGlobalMap().put(configNode, main.getConfig().getConfigurationSection(configNode).getValues(withKeys));
		return main.getConfig().getConfigurationSection(configNode).getValues(withKeys);
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @param configNode
	 * @return loaded object
	 */
	public default Object registerData(String configNode) {
		getGlobalMap().put(configNode, main.getConfig().get(configNode));
		return main.getConfig().get(configNode);
	}
	
	/**
	 * must be run onEnable()
	 */
	@SuppressWarnings("unused")
	public default void loadGlobalData() {
		List<String> worlds = registerStringListData("worlds");
		//Under Options
		boolean isRankEnabled = registerBooleanData("Options.rank-enabled");
		boolean isPrestigeEnabled = registerBooleanData("Options.prestige-enabled");
		boolean isMasterEnabled = registerBooleanData("Options.master-enabled");
		String forceDisplayOrder = registerStringData("Options.force-display-order");
		boolean isForceRankDisplay = registerBooleanData("Options.force-rank-display");
		boolean isForcePrestigeDisplay = registerBooleanData("Options.force-prestige-display");
		boolean isAllWorldsBroadcast = registerBooleanData("Options.allworlds-broadcast");
		boolean isForceMasterDisplay = registerBooleanData("Options.force-master-display");
		String noPrestigeDispaly = registerStringData("Options.no-prestige-display");
		String noMasterDisplay = registerStringData("Options.no-master-display");
		boolean isSendRankupMsg = registerBooleanData("Options.send-rankupmsg");
		boolean isSendPrestigeMsg = registerBooleanData("Options.send-prestigemsg");
		boolean isSendMasterMsg = registerBooleanData("Options.send-mastermsg");
		boolean isSendRankupMaxMsg = registerBooleanData("Options.send-rankupmaxmsg");
		boolean isGuiRankList = registerBooleanData("Options.GUI-RANKLIST");
		boolean isGuiPrestigeList = registerBooleanData("Options.GUI-PRESTIGELIST");
		boolean isGuiMasterList = registerBooleanData("Options.GUI-REBIRTHLIST");
		String prestigeSoundName = registerStringData("Options.prestigesound-name");
		double prestigeSoundVolume = registerDoubleData("Options.prestigesound-volume");
		double prestigeSoundPitch = registerDoubleData("Options.prestigesound-pitch");
		String rankupSoundName = registerStringData("Options.rankupsound-name");
		double rankupSoundVolume = registerDoubleData("Options.rankupsound-volume");
		double rankupSoundPitch = registerDoubleData("Options.rankupsound-pitch");
		String masterSoundName = registerStringData("Options.masteryound-name");
		double masterSoundVolume = registerDoubleData("Options.masteryound-volume");
		double masterSoundPitch = registerDoubleData("Options.masteryound-pitch");
		boolean isPerRankPermission = registerBooleanData("Options.per-rank-permission");
		boolean isRankupMaxBroadcastLastRankOnly = registerBooleanData("Options.rankupmax-broadcastlastrankonly");
		boolean isRankupMaxMsgLastRankOnly = registerBooleanData("Options.rankupmax-msglastrankonly");
		boolean isRankupMaxRankupMsgLastRankOnly = registerBooleanData("Options.rankupmax-rankupmsglastrankonly");
		boolean isRankupVaultGroups = registerBooleanData("Options.rankup-vault-groups");
		String rankupVaultGroupsPlugin = registerStringData("Options.rankup-vault-groups-plugin");
		boolean isRankupVaultGroupsCheck = registerBooleanData("Options.rankup-vault-groups-check");
		boolean isAutoRankup = registerBooleanData("Options.autorankup");
		boolean rankupMaxWithPrestige = registerBooleanData("Options.rankupmax-with-prestige");
	    int autoRankupDelay = registerIntegerData("Options.autorankup-delay");
	    int autoPrestigeDelay = registerIntegerData("Options.autoprestige-delay");
	    int autoMasterDelay = registerIntegerData("Options.automaster-delay");
	    boolean actionbarProgress = registerBooleanData("Options.actionbar-progress");
	    boolean actionbarProgressOnlyPickaxe = registerBooleanData("Options.actionbar-progress-only-pickaxe");
	    String actionbarProgressFormat = registerStringData("Options.actionbar-progress-format");
	    int actionbarProgressUpdater = registerIntegerData("Options.actionbar-progress-updater");
	    boolean expbarProgress = registerBooleanData("Options.expbar-progress");
	    int expbarProgressUpdater = registerIntegerData("Options.expbar-progress-updater");
	    String expbarProgressFormat = registerStringData("Options.expbar-progress-format");
	    boolean autoSave = registerBooleanData("Options.autosave");
	    int autoSaveTime = registerIntegerData("Options.autosave-time");
	    boolean saveNotification = registerBooleanData("Options.save-notification");
	    boolean forceSave = registerBooleanData("Options.forcesave");
	    boolean saveOnLeave = registerBooleanData("Options.save-on-leave");
	    boolean rankupMaxWarpFilter = registerBooleanData("Options.rankupmax-warp-filter");
	    boolean allowEasterEggs = registerBooleanData("Options.allow-easter-eggs");
		//Under Ranklist-text
		String rankListText_rankCurrentFormat = registerStringData("Ranklist-text.rank-current-format");
		String rankListText_rankCompletedFormat = registerStringData("Ranklist-text.rank-completed-format");
		String rankListText_rankOtherFormat = registerStringData("Ranklist-text.rank-other-format");
		boolean rankListText_isEnablePages = registerBooleanData("Ranklist-text.enable-pages");
		int rankListText_rankPerPage = registerIntegerData("Ranklist-text.rank-per-page");
		List<String> rankListText_rankWithPagesListFormat = registerStringListData("Ranklist-text.rank-with-pages-list-format");
		List<String> rankListText_rankListFormat = registerStringListData("Ranklist-text.rank-list-format");
		//Under Prestigelist-text
		String prestigeListText_prestigeCurrentFormat = registerStringData("Prestigelist-text.prestige-current-format");
		String prestigeListText_prestigeCompletedFormat = registerStringData("Prestigelist-text.prestige-completed-format");
		String prestigeListText_prestigeOtherFormat = registerStringData("Prestigelist-text.prestige-other-format");
		boolean prestigeListText_isEnablePages = registerBooleanData("Prestigelist-text.enable-pages");
		int prestigeListText_prestigePerPage = registerIntegerData("Prestigelist-text.prestige-per-page");
		List<String> prestigeListText_prestigeWithPagesListFormat = registerStringListData("Prestigelist-text.prestige-with-pages-list-format");
		List<String> prestigeListText_prestigeListFormat = registerStringListData("Prestigelist-text.prestige-list-format");
		//Under Masterlist-text
		String masterListText_masterCurrentFormat = registerStringData("Masterlist-text.master-current-format");
		String masterListText_masterCompletedFormat = registerStringData("Masterlist-text.master-completed-format");
		String masterListText_masterOtherFormat = registerStringData("Masterlist-text.master-other-format");
		boolean masterListText_isEnablePages = registerBooleanData("Masterlist-text.enable-pages");
		int masterListText_masterPerPage = registerIntegerData("Masterlist-text.master-per-page");
		List<String> masterListText_masterWithPagesListFormat = registerStringListData("Masterlist-text.master-with-pages-list-format");
		List<String> masterListText_masterListFormat = registerStringListData("Masterlist-text.master-list-format");
		//Under Holograms.rankup
		boolean rankupHologramIsEnable = registerBooleanData("Holograms.rankup.enable");
		int rankupHologramRemoveTime = registerIntegerData("Holograms.rankup.remove-time");
		int rankupHologramHeight = registerIntegerData("Holograms.rankup.height");
		List<String> rankupHologramFormat = registerStringListData("Holograms.rankup.format");
		//Under Holograms.prestige
		boolean prestigeHologramIsEnable = registerBooleanData("Holograms.prestige.enable");
		int prestigeHologramRemoveTime = registerIntegerData("Holograms.prestige.remove-time");
		int prestigeHologramHeight = registerIntegerData("Holograms.prestige.height");
		List<String> prestigeHologramFormat = registerStringListData("Holograms.prestige.format");
		//Under Holograms.master
		boolean masterHologramIsEnable = registerBooleanData("Holograms.master.enable");
		int masterHologramRemoveTime = registerIntegerData("Holograms.master.remove-time");
		int masterHologramHeight = registerIntegerData("Holograms.master.height");
		List<String> masterHologramFormat = registerStringListData("Holograms.master.format");
		//Under MySQL
		boolean mySqlIsEnable = registerBooleanData("MySQL.enable");
		String mySqlHost = registerStringData("MySQL.host");
		int mySqlPort = registerIntegerData("MySQL.port");
		String mySqldatabase = registerStringData("MySQL.database");
		String mySqltable = registerStringData("MySQL.table");
		String mySqlUsername = registerStringData("MySQL.username");
		String mySqlPassword = registerStringData("MySQL.password");
		boolean mySqlUseSSL = registerBooleanData("MySQL.useSSL");
		boolean mySqlAutoReconnect = registerBooleanData("MySQL.autoReconnect");
		//Under Main-GUIOptions
		String previousPageItemName = registerStringData("Main-GUIOptions.previouspage-itemNAME");
		String previousPageItemDisplayName = registerStringData("Main-GUIOptions.previouspage-itemDISPLAYNAME");
		List<String> previousPageItemLore = registerStringListData("Main-GUIOptions.previouspage-itemLORE");
		List<String> previousPageItemEnchantments = registerStringListData("Main-GUIOptions.previouspage-itemENCHANTMENTS");
		int previousPageItemAmount = registerIntegerData("Main-GUIOptions.previouspage-itemAMOUNT");
		int previousPageItemData = registerIntegerData("Main-GUIOptions.previouspage-itemDATA");
		List<String> previousPageItemFlags = registerStringListData("Main-GUIOptions.previouspage-itemFLAGS");
		String nextPageItemName = registerStringData("Main-GUIOptions.nextpage-itemNAME");
		String nextPageItemDisplayName = registerStringData("Main-GUIOptions.nextpage-itemDISPLAYNAME");
		List<String> nextPageItemLore = registerStringListData("Main-GUIOptions.nextpage-itemLORE");
		List<String> nextPageItemEnchantments = registerStringListData("Main-GUIOptions.nextpage-itemENCHANTMENTS");
		int nextPageItemAmount = registerIntegerData("Main-GUIOptions.nextpage-itemAMOUNT");
		int nextPageItemData = registerIntegerData("Main-GUIOptions.nextpage-itemDATA");
		List<String> nextPageItemFlags = registerStringListData("Main-GUIOptions.nextpage-itemFLAGS");
		String noPreviousPages = registerStringData("Main-GUIOptions.no-previous-pages");
		String noAdditonalPages = registerStringData("Main-GUIOptions.no-additional-pages");
		String currentPageItemName = registerStringData("Main-GUIOptions.currentpage-itemNAME");
		String currentPageItemDisplayName = registerStringData("Main-GUIOptions.currentpage-itemDISPLAYNAME");
		List<String> currentPageItemLore = registerStringListData("Main-GUIOptions.currentpage-itemLORE");
		List<String> currentPageItemEnchantments = registerStringListData("Main-GUIOptions.currentpage-itemENCHANTMENTS");
		int currentPageItemAmount = registerIntegerData("Main-GUIOptions.currentpage-itemAMOUNT");
		int currentPageItemData = registerIntegerData("Main-GUIOptions.currentpage-itemDATA");
		List<String> currentPageItemFlags = registerStringListData("Main-GUIOptions.currentpage-itemFLAGS");
		//Under Ranklist-gui.current-format
		String rankListGuiTitle = registerStringData("Ranklist-gui.title");
		List<String> rankListGuiConstantItems = registerStringListData("Ranklist-gui.constant-items");
		String rankListGUIAllowedSlots = registerStringData("Ranklist-gui.allowed-slots");
		String rankListGuiCurrentItemName = registerStringData("Ranklist-gui.current-format.itemNAME");
		int rankListGuiCurrentItemAmount = registerIntegerData("Ranklist-gui.current-format.itemAMOUNT");
		String rankListGuiCurrentItemDisplayName = registerStringData("Ranklist-gui.current-format.itemDISPLAYNAME");
		List<String> rankListGuiCurrentItemLore = registerStringListData("Ranklist-gui.current-format.itemLORE");
		List<String> rankListGuiCurrentItemEnchantments = registerStringListData("Ranklist-gui.current-format.itemENCHANTMENTS");
		List<String> rankListGuiCurrentItemFlags = registerStringListData("Ranklist-gui.current-format.itemFLAGS");
		List<String> rankListGuiCurrentItemCommands = registerStringListData("Ranklist-gui.current-format.itemCOMMANDS");
		Map<String, Object> rankListCurrentCustomItems = registerMapData("Ranklist-gui.current-format.custom", true);
		//Under Ranklist-gui.completed-format
		String rankListGuiCompletedItemName = registerStringData("Ranklist-gui.completed-format.itemNAME");
		int rankListGuiCompletedItemAmount = registerIntegerData("Ranklist-gui.completed-format.itemAMOUNT");
		String rankListGuiCompletedItemDisplayName = registerStringData("Ranklist-gui.completed-format.itemDISPLAYNAME");
		List<String> rankListGuiCompletedItemLore = registerStringListData("Ranklist-gui.completed-format.itemLORE");
		List<String> rankListGuiCompletedItemEnchantments = registerStringListData("Ranklist-gui.completed-format.itemENCHANTMENTS");
		List<String> rankListGuiCompletedItemFlags = registerStringListData("Ranklist-gui.completed-format.itemFLAGS");
		List<String> rankListGuiCompletedItemCommands = registerStringListData("Ranklist-gui.completed-format.itemCOMMANDS");
		Map<String, Object> rankListCompletedCustomItems = registerMapData("Ranklist-gui.completed-format.custom", true);
		//Under Ranklist-gui.other-format
		String rankListGuiOtherItemName = registerStringData("Ranklist-gui.other-format.itemNAME");
		int rankListGuiOtherItemAmount = registerIntegerData("Ranklist-gui.other-format.itemAMOUNT");
		String rankListGuiOtherItemDisplayName = registerStringData("Ranklist-gui.other-format.itemDISPLAYNAME");
		List<String> rankListGuiOtherItemLore = registerStringListData("Ranklist-gui.other-format.itemLORE");
		List<String> rankListGuiOtherdItemEnchantments = registerStringListData("Ranklist-gui.other-format.itemENCHANTMENTS");
		List<String> rankListGuiOtherItemFlags = registerStringListData("Ranklist-gui.other-format.itemFLAGS");
		List<String> rankListGuiOtherItemCommands = registerStringListData("Ranklist-gui.other-format.itemCOMMANDS");
		Map<String, Object> rankListOtherCustomItems = registerMapData("Ranklist-gui.other-format.custom", true);
		//Under Prestigelist-gui.current-format
		String prestigeListGuiTitle = registerStringData("Prestigelist-gui.title");
		List<String> prestigeListGuiConstantItems = registerStringListData("Prestigelist-gui.constant-items");
		String prestigeListGUIAllowedSlots = registerStringData("Prestigelist-gui.allowed-slots");
		String prestigeListGuiCurrentItemName = registerStringData("Prestigelist-gui.current-format.itemNAME");
		int prestigeListGuiCurrentItemAmount = registerIntegerData("Prestigelist-gui.current-format.itemAMOUNT");
		String prestigeListGuiCurrentItemDisplayName = registerStringData("Prestigelist-gui.current-format.itemDISPLAYNAME");
		List<String> prestigeListGuiCurrentItemLore = registerStringListData("Prestigelist-gui.current-format.itemLORE");
		List<String> prestigeListGuiCurrentItemEnchantments = registerStringListData("Prestigelist-gui.current-format.itemENCHANTMENTS");
		List<String> prestigeListGuiCurrentItemFlags = registerStringListData("Prestigelist-gui.current-format.itemFLAGS");
		List<String> prestigeListGuiCurrentItemCommands = registerStringListData("Prestigelist-gui.current-format.itemCOMMANDS");
		Map<String, Object> prestigeListCurrentCustomItems = registerMapData("Prestigelist-gui.current-format.custom", true);
		//Under Prestigelist-gui.completed-format
		String prestigeListGuiCompletedItemName = registerStringData("Prestigelist-gui.completed-format.itemNAME");
		int prestigeListGuiCompletedItemAmount = registerIntegerData("Prestigelist-gui.completed-format.itemAMOUNT");
		String prestigeListGuiCompletedItemDisplayName = registerStringData("Prestigelist-gui.completed-format.itemDISPLAYNAME");
		List<String> prestigeListGuiCompletedItemLore = registerStringListData("Prestigelist-gui.completed-format.itemLORE");
		List<String> prestigeListGuiCompletedItemEnchantments = registerStringListData("Prestigelist-gui.completed-format.itemENCHANTMENTS");
		List<String> prestigeListGuiCompletedItemFlags = registerStringListData("Prestigelist-gui.completed-format.itemFLAGS");
		List<String> prestigeListGuiCompletedItemCommands = registerStringListData("Prestigelist-gui.completed-format.itemCOMMANDS");
		Map<String, Object> prestigeListCompletedCustomItems = registerMapData("Prestigelist-gui.completed-format.custom", true);
		//Under Prestigelist-gui.other-format
		String prestigeListGuiOtherItemName = registerStringData("Prestigelist-gui.other-format.itemNAME");
		int prestigeListGuiOtherItemAmount = registerIntegerData("Prestigelist-gui.other-format.itemAMOUNT");
		String prestigeListGuiOtherItemDisplayName = registerStringData("Prestigelist-gui.other-format.itemDISPLAYNAME");
		List<String> prestigeListGuiOtherItemLore = registerStringListData("Prestigelist-gui.other-format.itemLORE");
		List<String> prestigeListGuiOtherdItemEnchantments = registerStringListData("Prestigelist-gui.other-format.itemENCHANTMENTS");
		List<String> prestigeListGuiOtherItemFlags = registerStringListData("Prestigelist-gui.other-format.itemFLAGS");
		List<String> prestigeListGuiOtherItemCommands = registerStringListData("Prestigelist-gui.other-format.itemCOMMANDS");
		Map<String, Object> prestigeListOtherCustomItems = registerMapData("Prestigelist-gui.other-format.custom", true);
		//Under Masterlist-gui.current-format
		String masterListGuiTitle = registerStringData("Masterlist-gui.title");
		List<String> masterListGuiConstantItems = registerStringListData("Masterlist-gui.constant-items");
		String masterListGUIAllowedSlots = registerStringData("Masterlist-gui.allowed-slots");
		String masterListGuiCurrentItemName = registerStringData("Masterlist-gui.current-format.itemNAME");
		int masterListGuiCurrentItemAmount = registerIntegerData("Masterlist-gui.current-format.itemAMOUNT");
		String masterListGuiCurrentItemDisplayName = registerStringData("Masterlist-gui.current-format.itemDISPLAYNAME");
		List<String> masterListGuiCurrentItemLore = registerStringListData("Masterlist-gui.current-format.itemLORE");
		List<String> masterListGuiCurrentItemEnchantments = registerStringListData("Masterlist-gui.current-format.itemENCHANTMENTS");
		List<String> masterListGuiCurrentItemFlags = registerStringListData("Masterlist-gui.current-format.itemFLAGS");
		List<String> masterListGuiCurrentItemCommands = registerStringListData("Masterlist-gui.current-format.itemCOMMANDS");
		Map<String, Object> masterListCurrentCustomItems = registerMapData("Masterlist-gui.current-format.custom", true);
		//Under Masterlist-gui.completed-format
		String masterListGuiCompletedItemName = registerStringData("Masterlist-gui.completed-format.itemNAME");
		int masterListGuiCompletedItemAmount = registerIntegerData("Masterlist-gui.completed-format.itemAMOUNT");
		String masterListGuiCompletedItemDisplayName = registerStringData("Masterlist-gui.completed-format.itemDISPLAYNAME");
		List<String> masterListGuiCompletedItemLore = registerStringListData("Masterlist-gui.completed-format.itemLORE");
		List<String> masterListGuiCompletedItemEnchantments = registerStringListData("Masterlist-gui.completed-format.itemENCHANTMENTS");
		List<String> masterListGuiCompletedItemFlags = registerStringListData("Masterlist-gui.completed-format.itemFLAGS");
		List<String> masterListGuiCompletedItemCommands = registerStringListData("Masterlist-gui.completed-format.itemCOMMANDS");
		Map<String, Object> masterListCompletedCustomItems = registerMapData("Masterlist-gui.completed-format.custom", true);
		//Under Masterlist-gui.other-format
		String masterListGuiOtherItemName = registerStringData("Masterlist-gui.other-format.itemNAME");
		int masterListGuiOtherItemAmount = registerIntegerData("Masterlist-gui.other-format.itemAMOUNT");
		String masterListGuiOtherItemDisplayName = registerStringData("Masterlist-gui.other-format.itemDISPLAYNAME");
		List<String> masterListGuiOtherItemLore = registerStringListData("Masterlist-gui.other-format.itemLORE");
		List<String> masterListGuiOtherItemEnchantments = registerStringListData("Masterlist-gui.other-format.itemENCHANTMENTS");
		List<String> masterListGuiOtherItemFlags = registerStringListData("Masterlist-gui.other-format.itemFLAGS");
		List<String> masterListGuiOtherItemCommands = registerStringListData("Masterlist-gui.other-format.itemCOMMANDS");
		Map<String, Object> masterListOtherCustomItems = registerMapData("Masterlist-gui.other-format.custom", true);
		//Under PrestigeOptions
		boolean prestigeOptionResetMoney = registerBooleanData("PrestigeOptions.ResetMoney");
		boolean prestigeOptionResetRank = registerBooleanData("PrestigeOptions.ResetRank");
		double prestigeOptionRankupCostIncreasePercentage = registerDoubleData("PrestigeOptions.rankup_cost_increase_percentage");
		String prestigeOptionCostIncreaseType = registerStringData("PrestigeOptions.cost_increase_type");
		String prestigeOptionCostIncreaseExpression = registerStringData("PrestigeOptions.cost_increase_expression");
		List<String> prestigeOptionPrestigeCMDS = registerStringListData("PrestigeOptions.prestige-cmds");
		List<String> prestigeOptionPrestigeDeleteCMDS = registerStringListData("PrestigeOptions.prestige-delete-cmds");
		List<String> prestigeOptionPrestigeResetCMDS = registerStringListData("PrestigeOptions.prestige-reset-cmds");
		//Under MasterOptions
		boolean masterOptionResetMoney = registerBooleanData("MasterOptions.ResetMoney");
		boolean masterOptionResetRank = registerBooleanData("MasterOptions.ResetRank");
		boolean masterOptionResetPrestige = registerBooleanData("MasterOptions.ResetPrestige");
		double masterOptionPrestigeCostIncreasePercentage = registerDoubleData("MasterOptions.prestige_cost_increase_percentage");
		String masterOptionCostIncreaseType = registerStringData("MasterOptions.cost_increase_type");
		String masterOptionCostIncreaseExpression = registerStringData("MasterOptions.cost_increase_expression");
		List<String> masterOptionMasterCMDS = registerStringListData("MasterOptions.master-cmds");
		List<String> masterOptionMasterDeleteCMDS = registerStringListData("MasterOptions.master-delete-cmds");
		List<String> masterOptionMasterResetCMDS = registerStringListData("MasterOptions.master-reset-cmds");
		//Under RankOptions
		List<String> rankOptionRankDeleteCMDS = registerStringListData("RankOptions.rank-delete-cmds");
		List<String> rankOptionRankResetCMDS = registerStringListData("RankOptions.rank-reset-cmds");
		//Under PlaceholderAPI
		String rankupProgressStyle = registerStringData("PlaceholderAPI.rankup-progress-style");
		String rankupProgressFilled = registerStringData("PlaceholderAPI.rankup-progress-filled");
		String rankupProgressNeeded = registerStringData("PlaceholderAPI.rankup-progress-needed");
		boolean rankupProgressFullEnabled = registerBooleanData("PlaceholderAPI.rankup-progress-full-enabled");
		String rankupProgressFull = registerStringData("PlaceholderAPI.rankup-progress-full");
		String rankupProgressLastRank = registerStringData("PlaceholderAPI.rankup-progress-lastrank");
		String rankupPercentageLastRank = registerStringData("PlaceholderAPI.rankup-percentage-lastrank");
		String rankupCostLastRank = registerStringData("PlaceholderAPI.rankup-cost-lastrank");
		String rankupLastRank = registerStringData("PlaceholderAPI.rankup-lastrank");
		boolean currentRankLastRankEnabled = registerBooleanData("PlaceholderAPI.currentrank-lastrank-enabled");
		String currentRankLastRank = registerStringData("PlaceholderAPI.currentrank-lastrank");
		String prestigeLastPrestige = registerStringData("PlaceholderAPI.prestige-lastprestige");
		String prestigeNotPrestiged = registerStringData("PlaceholderAPI.prestige-notprestiged");
		String nextPrestigeNotPrestiged = registerStringData("PlaceholderAPI.nextprestige-notprestiged");
		String masterNotMastered = registerStringData("PlaceholderAPI.master-notmastered");
		String nextMasterNotMastered = registerStringData("PlaceholderAPI.nextmaster-notmastered");
		String masterLastMaster = registerStringData("PlaceholderAPI.master-lastmaster");
		String currencySymbol = registerStringData("PlaceholderAPI.currency-symbol");
		boolean isCurrencySymbolBehind = registerBooleanData("PlaceholderAPI.currency-symbol-behind");
		String percentSign = registerStringData("PlaceholderAPI.percent-sign");
		boolean isPercentSignBehind = registerBooleanData("PlaceholderAPI.percent-sign-behind");
		String nextProgressStyleRankup = registerStringData("PlaceholderAPI.next-progress-style.rankup");
		String nextProgressFilledRankup = registerStringData("PlaceholderAPI.next-progress-filled.rankup");
		String nextProgressNeededRankup = registerStringData("PlaceholderAPI.next-progress-needed.rankup");
		String nextProgressStylePrestige = registerStringData("PlaceholderAPI.next-progress-style.prestige");
		String nextProgressFilledPrestige = registerStringData("PlaceholderAPI.next-progress-filled.prestige");
		String nextProgressNeededPrestige = registerStringData("PlaceholderAPI.next-progress-needed.prestige");
		String nextProgressStyleMaster = registerStringData("PlaceholderAPI.next-progress-style.master");
		String nextProgressFilledMaster = registerStringData("PlaceholderAPI.next-progress-filled.master");
		String nextProgressNeededMaster = registerStringData("PlaceholderAPI.next-progress-needed.master");
		boolean nextProgressFullIsRankupEnabled = registerBooleanData("PlaceholderAPI.next-progress-full-isrankup-enabled");
		boolean nextProgressFullIsPrestigeEnabled = registerBooleanData("PlaceholderAPI.next-progress-full-isprestige-enabled");
		boolean nextProgressFullIsMasterEnabled = registerBooleanData("PlaceholderAPI.next-progress-full-ismaster-enabled");
		boolean nextProgressFullIsLastEnabled = registerBooleanData("PlaceholderAPI.next-progress-full-islast-enabled");
		String nextProgressFullIsRankup = registerStringData("PlaceholderAPI.next-progress-full-isrankup");
	    String nextProgressFullIsPrestige = registerStringData("PlaceholderAPI.next-progress-full-isprestige");
	    String nextProgressFullIsMaster = registerStringData("PlaceholderAPI.next-progress-full-ismaster");
	    String nextProgressFullIsLast = registerStringData("PlaceholderAPI.next-progress-full-islast");
	    String leaderboardNameRankNull = registerStringData("PlaceholderAPI.leaderboard-name-rank-null");
	    String leaderboardValueRankNull = registerStringData("PlaceholderAPI.leaderboard-value-rank-null");
	    String leaderboardNamePrestigeNull = registerStringData("PlaceholderAPI.leaderboard-name-prestige-null");
	    String leaderboardValuePrestigeNull = registerStringData("PlaceholderAPI.leaderboard-value-prestige-null");
	    String leaderboardNameMasterNull = registerStringData("PlaceholderAPI.leaderboard-name-master-null");
	    String leaderboardValueMasterNull = registerStringData("PlaceholderAPI.leaderboard-value-master-null");
	    //Under MoneyFormatter
	    String thousand = registerStringData("MoneyFormatter.thousand");
	    String million = registerStringData("MoneyFormatter.million");
	    String billion = registerStringData("MoneyFormatter.billion");
	    String trillion = registerStringData("MoneyFormatter.trillion");
	    String quadrillion = registerStringData("MoneyFormatter.quadrillion");
	    String quintillion = registerStringData("MoneyFormatter.quintillion");
	    String sextillion = registerStringData("MoneyFormatter.sextillion");
	    String septillion = registerStringData("MoneyFormatter.septillion");
	    String octillion = registerStringData("MoneyFormatter.octillion");
	    String nonillion = registerStringData("MoneyFormatter.nonillion");
	    String decillion = registerStringData("MoneyFormatter.decillion");
	    String undecillion = registerStringData("MoneyFormatter.undecillion");
	    String duoDecillion = registerStringData("MoneyFormatter.Duodecillion");
	    String zillion = registerStringData("MoneyFormatter.zillion");
	    //Under 'NOTHING'
	    String defaultRank = registerStringData("defaultrank");
	    String lastRank = registerStringData("lastrank");
	    String defaultPath = registerStringData("defaultpath");
	    String firstPrestige = registerStringData("firstprestige");
	    String lastPrestige = registerStringData("lastprestige");
	    String firstMaster = registerStringData("firstmaster");
	    String lastMaster = registerStringData("lastmaster");
	}
	
	public default String getStringData(String configNode) {
		return getStringMap().get(configNode);
	}
	
	public default int getIntegerData(String configNode) {
		return getIntegerMap().get(configNode);
	}
	
	public default double getDoubleData(String configNode) {
		return getDoubleMap().get(configNode);
	}
	
	public default boolean getBooleanData(String configNode) {
		return getBooleanMap().get(configNode);
	}
	
	public default List<String> getStringListData(String configNode) {
		return getStringListMap().get(configNode);
	}
	
	public default Set<String> getStringSetData(String configNode) {
		return getStringSetMap().get(configNode);
	}
	
	public default Object getData(String configNode) {
		return getGlobalMap().get(configNode);
	}
	
}

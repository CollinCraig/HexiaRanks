package me.hexiaranks.gui;

import java.util.HashMap;
import java.util.Map;

import me.hexiaranks.HexiaRanks;
import me.hexiaranks.data.GlobalDataStorage;
import me.hexiaranks.data.LevelType;

public class CustomMasterItems {

	private Map<String, MasterItem> customMasterItems;
	private HexiaRanks main;
	
	public CustomMasterItems(HexiaRanks main) {this.customMasterItems = new HashMap<>(); this.main = main;}
	
	public Map<String, MasterItem> getCustomMasterItems() {
		return this.customMasterItems;
	}
	
	public GlobalDataStorage gds() {
		return main.getGlobalStorage();
	}
	
	public void setup() {
		for(String master : main.masterStorage.getMasterData().keySet()) {
			for(int i = 0; i < 3; i++) {
				LevelState ls = LevelState.values()[i];
				if(main.getCustomItemsManager().hasCustomLevelItem(LevelType.REBIRTH, ls) && main.getCustomItemsManager().hasCustomFormat(LevelType.REBIRTH, ls, master)) {
					MasterItem ri = new MasterItem();
					MasterState rs = new MasterState();
					rs.setLevelState(ls);
					rs.setMaster(master);
					ri.setMaterial(main.getCustomItemsManager().readCustomLevelItemName(LevelType.REBIRTH, ls, master));
					ri.setAmount(main.getCustomItemsManager().readCustomLevelItemAmount(LevelType.REBIRTH, ls, master));
					ri.setDisplayName(gds().translateHexColorCodes(main.getCustomItemsManager().readCustomLevelItemDisplayName(LevelType.REBIRTH, ls, master)));
					ri.setLore(gds().translateHexColorCodes(main.getCustomItemsManager().readCustomLevelItemLore(LevelType.REBIRTH, ls, master)));
					ri.setEnchantments(main.getCustomItemsManager().readCustomLevelItemEnchantments(LevelType.REBIRTH, ls, master));
					ri.setFlags(main.getCustomItemsManager().readCustomLevelItemFlags(LevelType.REBIRTH, ls, master));
					ri.setCommands(gds().translateHexColorCodes(main.getCustomItemsManager().readCustomLevelItemCommands(LevelType.REBIRTH, ls, master)));
					customMasterItems.put(rs.toString(), ri);
				}
			}
		}
	}
	
}

package me.hexiaranks.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cloutteam.samjakob.gui.buttons.GUIButton;
import cloutteam.samjakob.gui.types.PaginatedGUI;
import me.hexiaranks.HexiaRanks;
import me.hexiaranks.data.RankPath;
import me.hexiaranks.utils.SkullCreator;
import me.hexiaranks.utils.XMaterial;

public class GuiListManager {

	public HexiaRanks main;
	public PaginatedGUI ranksGUI;
	public PaginatedGUI prestigesGUI;
	public PaginatedGUI masteryGUI;
	public RankItem emptyRankItem;
	public PrestigeItem emptyPrestigeItem;
	public MasterItem emptyMasterItem;
	public List<Integer> allowedRankSlots, allowedPrestigeSlots, allowedMasterSlots;
	public Map<String, List<String>> allowedRankSlotsMap;
	public Map<String, InventoryHolder> guiViewers;
	public GuiListManager(HexiaRanks main) {this.main = main;
	ranksGUI = new PaginatedGUI(this.main.prxAPI.c(this.main.globalStorage.getStringData("Ranklist-gui.title")));
	prestigesGUI = new PaginatedGUI(this.main.prxAPI.c(this.main.globalStorage.getStringData("Prestigelist-gui.title")));
	masteryGUI = new PaginatedGUI(this.main.prxAPI.c(this.main.globalStorage.getStringData("Masterlist-gui.title")));
	allowedRankSlots = new ArrayList<>();
	allowedPrestigeSlots = new ArrayList<>();
	allowedMasterSlots = new ArrayList<>();
	emptyRankItem = new RankItem();
	emptyRankItem.setMaterial(null);
	emptyRankItem.setAmount(0);
	emptyRankItem.setDisplayName(null);
	emptyRankItem.setLore(null);
	emptyRankItem.setEnchantments(null);
	emptyRankItem.setFlags(null);
	emptyRankItem.setCommands(null);
	emptyPrestigeItem = new PrestigeItem();
	emptyPrestigeItem.setMaterial(null);
	emptyPrestigeItem.setAmount(0);
	emptyPrestigeItem.setDisplayName(null);
	emptyPrestigeItem.setLore(null);
	emptyPrestigeItem.setEnchantments(null);
	emptyPrestigeItem.setFlags(null);
	emptyPrestigeItem.setCommands(null);
	emptyMasterItem = new MasterItem();
	emptyMasterItem.setMaterial(null);
	emptyMasterItem.setAmount(0);
	emptyMasterItem.setDisplayName(null);
	emptyMasterItem.setLore(null);
	emptyMasterItem.setEnchantments(null);
	emptyMasterItem.setFlags(null);
	emptyMasterItem.setCommands(null);
	guiViewers = new HashMap<>();
	}

	
	public void setupConstantItems() {
		if(!this.main.globalStorage.getStringListData("Ranklist-gui.constant-items").isEmpty()) {
			List<String> constantItems = main.globalStorage.getStringListData("Ranklist-gui.constant-items");
			for(String item : constantItems) {
				GUIButton button = new GUIButton(main.getCustomItemsManager().readCustomItem(item));
				button.setListener(event -> {
					event.setCancelled(true);
					main.executeCommands((Player)event.getWhoClicked(), main.getCustomItemsManager().readCustomItemCommands(item));
				});
				int slot = main.getCustomItemsManager().readCustomItemSlot(item);
				int page = 0; page = main.getCustomItemsManager().readCustomItemPage(item); if(page!=1) {page = page * 44;} else {page = 0;}
				ranksGUI.setButton(slot + page, button);
			}
			for(String str : main.globalStorage.getStringData("Ranklist-gui.allowed-slots").split(",")) {
				int x = Integer.valueOf(str);
				if(!allowedRankSlots.contains(x)) {
				allowedRankSlots.add(x);
				}
			}
		}

		if(!this.main.globalStorage.getStringListData("Prestigelist-gui.constant-items").isEmpty()) {
			List<String> constantItems = main.globalStorage.getStringListData("Prestigelist-gui.constant-items");
			for(String item : constantItems) {
				GUIButton button = new GUIButton(main.getCustomItemsManager().readCustomItem(item));
				button.setListener(event -> {event.setCancelled(true);
				main.executeCommands((Player)event.getWhoClicked(), main.getCustomItemsManager().readCustomItemCommands(item));
				});
				int slot = main.getCustomItemsManager().readCustomItemSlot(item);
				int page = 0; page = main.getCustomItemsManager().readCustomItemPage(item); if(page!=1) {page = page * 44;} else {page = 0;}
				prestigesGUI.setButton(slot + page, button);
			}
			for(String str : main.globalStorage.getStringData("Prestigelist-gui.allowed-slots").split(",")) {
				int x = Integer.valueOf(str);
				if(!allowedPrestigeSlots.contains(x)) {
				allowedPrestigeSlots.add(x);
				}
			}
		}

		if(!this.main.globalStorage.getStringListData("Masterlist-gui.constant-items").isEmpty()) {
			List<String> constantItems = main.globalStorage.getStringListData("Masterlist-gui.constant-items");
			for(String item : constantItems) {
				GUIButton button = new GUIButton(main.getCustomItemsManager().readCustomItem(item));
				button.setListener(event -> {event.setCancelled(true);
				main.executeCommands((Player)event.getWhoClicked(), main.getCustomItemsManager().readCustomItemCommands(item));
				});
				int slot = main.getCustomItemsManager().readCustomItemSlot(item);
				int page = 0; page = main.getCustomItemsManager().readCustomItemPage(item); if(page!=1) {page = page * 44;} else {page = 0;}
				masteryGUI.setButton(slot + page, button);
			}
			for(String str : main.globalStorage.getStringData("Masterlist-gui.allowed-slots").split(",")) {
				int x = Integer.valueOf(str);
				if(!allowedMasterSlots.contains(x)) {
				allowedMasterSlots.add(x);
				}
			}
		}
	}
	
	public RankItem getCustomItem(final RankState rankState) {
		return main.getCustomRankItems().getCustomRankItems().containsKey(rankState.toString()) ? main.getCustomRankItems().getCustomRankItems().get(rankState.toString()) : emptyRankItem;
	}
	
	public PrestigeItem getCustomItem(final PrestigeState prestigeState) {
		return main.getCustomPrestigeItems().getCustomPrestigeItems().containsKey(prestigeState.toString()) ? main.getCustomPrestigeItems().getCustomPrestigeItems().get(prestigeState.toString()) : emptyPrestigeItem;
	}
	
	public MasterItem getCustomItem(final MasterState masterState) {
		return main.getCustomMasterItems().getCustomMasterItems().containsKey(masterState.toString()) ? main.getCustomMasterItems().getCustomMasterItems().get(masterState.toString()) : emptyMasterItem;
	}
	
	    
		@SuppressWarnings("deprecation")
		public ItemStack parseStack(String itemValue) {
			ItemStack x = null;
			String originalValue = itemValue;
			itemValue = itemValue.toUpperCase();
			try {
				
			if(itemValue.contains(";")) {
				// 1.8 - 1.15
				String[] nameAndData = itemValue.split(";");
				String name = nameAndData[0];
				short data = Short.parseShort(nameAndData[1]);
				x = new ItemStack(XMaterial.matchXMaterial(name).get().parseMaterial());
				x.setDurability(data);
			} else if (itemValue.contains("#")) {
				// 1.8 - 1.15
				String[] nameAndData = itemValue.split("#");
				String name = nameAndData[0];
				byte data = Byte.parseByte(nameAndData[1]);
				x = XMaterial.matchXMaterial(Integer.valueOf(name), data).get().parseItem();
			} else if (itemValue.contains("->")) {
				// 1.8 - 1.12 || 1.8 - 1.15 (ViaVersion)
				String[] nameAndData = itemValue.split("->");
				String name = nameAndData[0];
				byte data = Byte.parseByte(nameAndData[1]);
				x = new ItemStack(Material.matchMaterial(name), 1, data);
			} else if (itemValue.contains("@HEAD@")) {
				String[] nameAndData = originalValue.split("@HEAD@");
				String data = nameAndData[1];
				x = XMaterial.PLAYER_HEAD.parseItem(true);
				if(data.length() > 16) {
					if(data.contains("net")) {
						main.debug("from url: " + data);
					x = SkullCreator.itemWithUrl(x, data);
					} else {
						main.debug("from base64: " + data);
						if(data.contains("=")) {
					x = SkullCreator.itemWithBase64(x, data);
						} else {
							main.debug("from url: " + data);
							x = SkullCreator.itemWithUrl(x, "http://textures.minecraft.net/texture/" + data);
						}
					}
				} else {
					   main.debug("from name: " + data);
					x = SkullCreator.itemFromName(data);
				}
			} else {
				// 1.8 - 1.15 Direct Parse => {itemname:itemdata}
				x = XMaterial.matchXMaterial(itemValue).get().parseItem();
			}
			return x;
			} catch (Exception err) {
			    main.getLogger().warning("Error while parsing an item name! unable to parse item: " + itemValue);
			    main.getLogger().warning("Please try another format from the formats mentioned below current-format section underneath Ranklist-gui in config.yml");
				err.printStackTrace();
			   return new ItemStack(Material.BEDROCK, 1);	
			}
			
		}
	
		public String tr(String string) {
			return main.getGlobalStorage().translateHexColorCodes(string);
		}
		
	public void openRanksGUI(Player player) {
		Player p = player;
		PaginatedGUI playerGUI = ranksGUI;
		playerGUI.previousPage();
		RankPath rp = main.prxAPI.getPlayerRankPath(p);
		String playerRank = rp.getRankName();
		String playerPath = rp.getPathName();
		List<String> ranksCollection = main.prxAPI.getRanksCollection(playerPath);
		int playerRankIndex = ranksCollection.indexOf(playerRank);
		String playerPrestige = main.prxAPI.getPlayerPrestige(p);
		String playerMaster = main.prxAPI.getPlayerMaster(p);
		RankState rs = new RankState();
		for(String rank : ranksCollection) {
			int rankIndex = ranksCollection.indexOf(rank);
			if(playerRankIndex > rankIndex) { // if completed
				// placeholders {
				String rankName = rank;
				RankPath xrp = RankPath.getRankPath(rankName, playerPath);
				String rankDisplayName = main.prxAPI.c(main.prxAPI.getRankDisplay(xrp));
				double rankCostNumber = (main.prxAPI.getIncreasedRankupCostX(playerMaster, playerPrestige, xrp));
				String rankCost = String.valueOf(rankCostNumber);
				String formattedRankCost = main.formatBalance(rankCostNumber);
				rs.setLevelState(LevelState.COMPLETED);
				rs.setRankPath(xrp);
				// }
				String itemName = getCustomItem(rs).getMaterial() != null ? getCustomItem(rs).getMaterial() : main.globalStorage.getStringData("Ranklist-gui.completed-format.itemNAME");
				int itemAmount = getCustomItem(rs).getAmount() != 0 ? getCustomItem(rs).getAmount() : main.globalStorage.getIntegerData("Ranklist-gui.completed-format.itemAMOUNT");
				String itemDisplayName = getCustomItem(rs).getDisplayName() != null ? getCustomItem(rs).getDisplayName()
						.replace("%completedrank_display%", rankDisplayName)
						.replace("%completedrank_cost%", rankCost)
						.replace("%completedrank_cost_formatted%", formattedRankCost) 
						: 
							main.globalStorage.getStringData("Ranklist-gui.completed-format.itemDISPLAYNAME").replace("%completedrank%", rankName)
						.replace("%completedrank_display%", rankDisplayName)
						.replace("%completedrank_cost%", rankCost)
						.replace("%completedrank_cost_formatted%", formattedRankCost);
				List<String> itemLore = getCustomItem(rs).getLore() != null ? getCustomItem(rs).getLore() : main.globalStorage.getStringListData("Ranklist-gui.completed-format.itemLORE");
				List<String> itemEnchantments = getCustomItem(rs).getEnchantments() != null ? getCustomItem(rs).getEnchantments() : main.globalStorage.getStringListData("Ranklist-gui.completed-format.itemENCHANTMENTS");
				List<String> itemFlags = getCustomItem(rs).getFlags() != null ? getCustomItem(rs).getFlags() : main.globalStorage.getStringListData("Ranklist-gui.completed-format.itemFLAGS");
				List<String> itemCommands = getCustomItem(rs).getCommands() != null ? getCustomItem(rs).getCommands() : main.globalStorage.getStringListData("Ranklist-gui.completed-format.itemCOMMANDS");
				List<String> realItemCommands = new ArrayList<String>();
				List<String> coloredItemLore = new ArrayList<String>();
				ItemStack completedItem = parseStack(itemName);
				completedItem.setAmount(itemAmount);
				ItemMeta completedMeta = completedItem.getItemMeta();
				completedMeta.setDisplayName(main.prxAPI.c(itemDisplayName));
				itemLore.forEach(line -> {coloredItemLore.add(main.getString(line.replace("%completedrank%", rankName)
						.replace("%completedrank_display%", rankDisplayName)	.replace("%completedrank_cost%", rankCost)
						.replace("%completedrank_cost_formatted%", formattedRankCost), p.getName()));});
				completedMeta.setLore(coloredItemLore);
				itemEnchantments.forEach(line -> {
					String[] splittedLine = line.split(" ");
					Enchantment enchant = EnchantmentReader.matchEnchantment(splittedLine[0]);
					int lvl = Integer.valueOf(splittedLine[1]);
					completedMeta.addEnchant(enchant, lvl, true);
				});
				if(!main.isBefore1_7) {
				itemFlags.forEach(line -> {completedMeta.addItemFlags(ItemFlagReader.matchItemFlag(line));});
				}
				completedItem.setItemMeta(completedMeta);
				GUIButton completedButton = new GUIButton(completedItem);
				completedButton.setListener(event -> {
					if(itemCommands.contains("(cancel-item_move)")) {
						event.setCancelled(true);
					}
					if(itemCommands.contains("(closeinv)")) {
						p.closeInventory();
					}
                    for(String command : itemCommands) {
                    	if(command.startsWith("[")) {
                    		realItemCommands.add(main.getString(command
                    				.replace("%completedrank%", rankName).replace("%completedrank_display%", rankName).replace("%completedrank_cost%", rankCost)
            						.replace("%completedrank_cost_formatted%", formattedRankCost), p.getName()));
                    	}
                    }
                    main.executeCommands(p, realItemCommands);
				});
				if(!allowedRankSlots.isEmpty()) {
			        playerGUI.setButton(allowedRankSlots.get(rankIndex), completedButton);
					} else {
						playerGUI.setButton(rankIndex, completedButton);
					}
		        
			}
			if(playerRankIndex == rankIndex) { // if current
				// placeholders {
				String rankName = rank;
				RankPath xrp = RankPath.getRankPath(rankName, playerPath);
				String rankDisplayName = main.prxAPI.c(main.prxAPI.getRankDisplay(xrp));
				double rankCostNumber = (main.prxAPI.getIncreasedRankupCostX(playerMaster, playerPrestige, xrp));
				String rankCost = String.valueOf(rankCostNumber);
				String formattedRankCost = main.formatBalance(rankCostNumber);
				rs.setLevelState(LevelState.CURRENT);
				rs.setRankPath(xrp);
				// }
				String itemName = getCustomItem(rs).getMaterial() != null ? getCustomItem(rs).getMaterial() : main.globalStorage.getStringData("Ranklist-gui.current-format.itemNAME");
				int itemAmount = getCustomItem(rs).getAmount() != 0 ? getCustomItem(rs).getAmount() : main.globalStorage.getIntegerData("Ranklist-gui.current-format.itemAMOUNT");
				String itemDisplayName = getCustomItem(rs).getDisplayName() != null ? getCustomItem(rs).getDisplayName()	
						.replace("%currentrank_display%", rankDisplayName)
						.replace("%currentrank_cost%", rankCost)
						.replace("%currentrank_cost_formatted%", formattedRankCost) 
						: 
							main.globalStorage.getStringData("Ranklist-gui.current-format.itemDISPLAYNAME").replace("%currentrank%", rankName)
						.replace("%currentrank_display%", rankDisplayName)
						.replace("%currentrank_cost%", rankCost)
						.replace("%currentrank_cost_formatted%", formattedRankCost);
				List<String> itemLore = getCustomItem(rs).getLore() != null ? getCustomItem(rs).getLore() : main.globalStorage.getStringListData("Ranklist-gui.current-format.itemLORE");
				List<String> itemEnchantments = getCustomItem(rs).getEnchantments() != null ? getCustomItem(rs).getEnchantments() : main.globalStorage.getStringListData("Ranklist-gui.current-format.itemENCHANTMENTS");
				List<String> itemFlags = getCustomItem(rs).getFlags() != null ? getCustomItem(rs).getFlags() : main.globalStorage.getStringListData("Ranklist-gui.current-format.itemFLAGS");
				List<String> itemCommands = getCustomItem(rs).getCommands() != null ? getCustomItem(rs).getCommands() : main.globalStorage.getStringListData("Ranklist-gui.current-format.itemCOMMANDS");
				List<String> realItemCommands = new ArrayList<String>();
				List<String> coloredItemLore = new ArrayList<String>();
				ItemStack currentItem = parseStack(itemName);
				currentItem.setAmount(itemAmount);
				ItemMeta currentMeta = currentItem.getItemMeta();
				currentMeta.setDisplayName(main.prxAPI.c(itemDisplayName));
				itemLore.forEach(line -> {coloredItemLore.add(main.getString(line.replace("%currentrank%", rankName)
						.replace("%currentrank_display%", rankDisplayName).replace("%currentrank_cost%", rankCost)
						.replace("%currentrank_cost_formatted%", formattedRankCost), p.getName()));});
				currentMeta.setLore(coloredItemLore);
				itemEnchantments.forEach(line -> {
					String[] splittedLine = line.split(" ");
					Enchantment enchant = EnchantmentReader.matchEnchantment(splittedLine[0]);
					int lvl = Integer.valueOf(splittedLine[1]);
					currentMeta.addEnchant(enchant, lvl, true);
				});
				if(!main.isBefore1_7) {
				itemFlags.forEach(line -> {currentMeta.addItemFlags(ItemFlagReader.matchItemFlag(line));});
				}
				currentItem.setItemMeta(currentMeta);
				GUIButton currentButton = new GUIButton(currentItem);
				currentButton.setListener(event -> {
					if(itemCommands.contains("(cancel-item_move)")) {
						event.setCancelled(true);
					}
					if(itemCommands.contains("(closeinv)")) {
						p.closeInventory();
					}
                    for(String command : itemCommands) {
                    	if(command.startsWith("[")) {
                    		realItemCommands.add(main.getString(command
                    				.replace("%currentrank%", rankName).replace("%currentrank_display%", rankName)	.replace("%currentrank_cost%", rankCost)
            						.replace("%currentrank_cost_formatted%", formattedRankCost), p.getName()));
                    	}
                    }
                    main.executeCommands(p, realItemCommands);
				});
				if(!allowedRankSlots.isEmpty()) {
			        playerGUI.setButton(allowedRankSlots.get(rankIndex), currentButton);
					} else {
						playerGUI.setButton(rankIndex, currentButton);
					}
			}
			if(playerRankIndex < rankIndex) { // if not completed
				// placeholders {
				String rankName = rank;
				RankPath xrp = RankPath.getRankPath(rankName, playerPath);
				String rankDisplayName = main.prxAPI.c(main.prxAPI.getRankDisplay(xrp));
				double rankCostNumber = (main.prxAPI.getIncreasedRankupCostX(playerMaster, playerPrestige, xrp));
				String rankCost = String.valueOf(rankCostNumber);
				String formattedRankCost = main.formatBalance(rankCostNumber);
				rs.setLevelState(LevelState.OTHER);
				rs.setRankPath(xrp);
				// }
				String itemName = getCustomItem(rs).getMaterial() != null ? getCustomItem(rs).getMaterial() : main.globalStorage.getStringData("Ranklist-gui.other-format.itemNAME");
				int itemAmount = getCustomItem(rs).getAmount() != 0 ? getCustomItem(rs).getAmount() : main.globalStorage.getIntegerData("Ranklist-gui.other-format.itemAMOUNT");
				String itemDisplayName = getCustomItem(rs).getDisplayName() != null ? getCustomItem(rs).getDisplayName()
						.replace("%otherrank_display%", rankDisplayName)
						.replace("%otherrank_cost%", rankCost)
						.replace("%otherrank_cost_formatted%", formattedRankCost) 
						: 
							main.globalStorage.getStringData("Ranklist-gui.other-format.itemDISPLAYNAME").replace("%otherrank%", rankName)
						.replace("%otherrank_display%", rankDisplayName)
						.replace("%otherrank_cost%", rankCost)
						.replace("%otherrank_cost_formatted%", formattedRankCost);
				List<String> itemLore = getCustomItem(rs).getLore() != null ? getCustomItem(rs).getLore() : main.globalStorage.getStringListData("Ranklist-gui.other-format.itemLORE");
				List<String> itemEnchantments = getCustomItem(rs).getEnchantments() != null ? getCustomItem(rs).getEnchantments() : main.globalStorage.getStringListData("Ranklist-gui.other-format.itemENCHANTMENTS");
				List<String> itemFlags = getCustomItem(rs).getFlags() != null ? getCustomItem(rs).getFlags() : main.globalStorage.getStringListData("Ranklist-gui.other-format.itemFLAGS");
				List<String> itemCommands = getCustomItem(rs).getCommands() != null ? getCustomItem(rs).getCommands() : main.globalStorage.getStringListData("Ranklist-gui.other-format.itemCOMMANDS");
				List<String> realItemCommands = new ArrayList<String>();
				List<String> coloredItemLore = new ArrayList<String>();
				ItemStack otherItem = parseStack(itemName);
				otherItem.setAmount(itemAmount);
				ItemMeta otherMeta = otherItem.getItemMeta();
				otherMeta.setDisplayName(main.prxAPI.c(itemDisplayName));
				itemLore.forEach(line -> {coloredItemLore.add(main.getString(line.replace("%otherrank%", rankName)
						.replace("%otherrank_display%", rankDisplayName).replace("%otherrank_cost%", rankCost)
						.replace("%otherrank_cost_formatted%", formattedRankCost), p.getName()));});
				otherMeta.setLore(coloredItemLore);
				itemEnchantments.forEach(line -> {
					String[] splittedLine = line.split(" ");
					Enchantment enchant = EnchantmentReader.matchEnchantment(splittedLine[0]);
					int lvl = Integer.valueOf(splittedLine[1]);
					otherMeta.addEnchant(enchant, lvl, true);
				});
				if(!main.isBefore1_7) {
				itemFlags.forEach(line -> {otherMeta.addItemFlags(ItemFlagReader.matchItemFlag(line));});
				}
				otherItem.setItemMeta(otherMeta);
				GUIButton otherButton = new GUIButton(otherItem);
				otherButton.setListener(event -> {
					if(itemCommands.contains("(cancel-item_move)")) {
						event.setCancelled(true);
					}
					if(itemCommands.contains("(closeinv)")) {
						p.closeInventory();
					}
                    for(String command : itemCommands) {
                    	if(command.startsWith("[")) {
                    		realItemCommands.add(main.getString(command
                    				.replace("%otherrank%", rankName).replace("%otherrank_display%", rankName).replace("%otherrank_cost%", rankCost)
            						.replace("%otherrank_cost_formatted%", formattedRankCost), p.getName()));
                    	}
                    }
                    main.executeCommands(p, realItemCommands);
				});
				if(!allowedRankSlots.isEmpty()) {
		        playerGUI.setButton(allowedRankSlots.get(rankIndex), otherButton);
				} else {
					playerGUI.setButton(rankIndex, otherButton);
				}
			}
		}
		p.openInventory(playerGUI.getInventory());
	}
	
	public void openPrestigesGUI(Player player) {
		Player p = player;
		PaginatedGUI playerGUI = prestigesGUI;
		playerGUI.previousPage();
		String playerPrestige = main.prxAPI.getPlayerPrestige(p);
		List<String> prestigesCollection = main.prxAPI.getPrestigesCollection();
		int playerPrestigeIndex = prestigesCollection.indexOf(playerPrestige);
		PrestigeState ps = new PrestigeState();
		for(String prestige : prestigesCollection) {
			int prestigeIndex = prestigesCollection.indexOf(prestige);
			if(playerPrestigeIndex > prestigeIndex) { // if completed
				// placeholders {
				String prestigeName = prestige;
				String prestigeDisplayName = main.prxAPI.c(main.prestigeStorage.getDisplayName(prestigeName));
				String master = main.prxAPI.getPlayerMaster(p);
				double prestigeCostNumber = (main.prxAPI.getIncreasedPrestigeCost(master, prestigeName));
				String prestigeCost = String.valueOf(prestigeCostNumber);
				String formattedPrestigeCost = main.formatBalance(prestigeCostNumber);
				ps.setLevelState(LevelState.COMPLETED);
				ps.setPrestige(prestigeName);
				// }
				String itemName = getCustomItem(ps).getMaterial() != null ? getCustomItem(ps).getMaterial() : main.globalStorage.getStringData("Prestigelist-gui.completed-format.itemNAME");
				int itemAmount = getCustomItem(ps).getAmount() != 0 ? getCustomItem(ps).getAmount() : main.globalStorage.getIntegerData("Prestigelist-gui.completed-format.itemAMOUNT");
				String itemDisplayName = getCustomItem(ps).getDisplayName() != null ? getCustomItem(ps).getDisplayName() : main.globalStorage.getStringData("Prestigelist-gui.completed-format.itemDISPLAYNAME").replace("%completedprestige%", prestigeName)
						.replace("%completedprestige_display%", prestigeDisplayName)
						.replace("%completedprestige_cost%", prestigeCost)
						.replace("%completedprestige_cost_formatted%", formattedPrestigeCost);
				List<String> itemLore = getCustomItem(ps).getLore() != null ? getCustomItem(ps).getLore() : main.globalStorage.getStringListData("Prestigelist-gui.completed-format.itemLORE");
				List<String> itemEnchantments = getCustomItem(ps).getEnchantments() != null ? getCustomItem(ps).getEnchantments() : main.globalStorage.getStringListData("Prestigelist-gui.completed-format.itemENCHANTMENTS");
				List<String> itemFlags = getCustomItem(ps).getFlags() != null ? getCustomItem(ps).getFlags() : main.globalStorage.getStringListData("Prestigelist-gui.completed-format.itemFLAGS");
				List<String> itemCommands = getCustomItem(ps).getCommands() != null ? getCustomItem(ps).getCommands() : main.globalStorage.getStringListData("Prestigelist-gui.completed-format.itemCOMMANDS");
				List<String> realItemCommands = new ArrayList<String>();
				List<String> coloredItemLore = new ArrayList<String>();
				ItemStack completedItem = parseStack(itemName);
				completedItem.setAmount(itemAmount);
				ItemMeta completedMeta = completedItem.getItemMeta();
				completedMeta.setDisplayName(main.prxAPI.c(itemDisplayName));
				itemLore.forEach(line -> {coloredItemLore.add(main.getString(line.replace("%completedprestige%", prestigeName)
						.replace("%completedprestige_display%", prestigeDisplayName)	.replace("%completedprestige_cost%", prestigeCost)
						.replace("%completedprestige_cost_formatted%", formattedPrestigeCost), p.getName()));});
				completedMeta.setLore(coloredItemLore);
				itemEnchantments.forEach(line -> {
					String[] splittedLine = line.split(" ");
					Enchantment enchant = EnchantmentReader.matchEnchantment(splittedLine[0]);
					int lvl = Integer.valueOf(splittedLine[1]);
					completedMeta.addEnchant(enchant, lvl, true);
				});
				if(!main.isBefore1_7) {
				itemFlags.forEach(line -> {completedMeta.addItemFlags(ItemFlagReader.matchItemFlag(line));});
				}
				completedItem.setItemMeta(completedMeta);
				GUIButton completedButton = new GUIButton(completedItem);
				completedButton.setListener(event -> {
					if(itemCommands.contains("(cancel-item_move)")) {
						event.setCancelled(true);
					}
					if(itemCommands.contains("(closeinv)")) {
						p.closeInventory();
					}
                    for(String command : itemCommands) {
                    	if(command.startsWith("[")) {
                    		realItemCommands.add(main.getString(command
                    				.replace("%completedprestige%", prestigeName).replace("%completedprestige_display%", prestigeName).replace("%completedprestige_cost%", prestigeCost)
            						.replace("%completedprestige_cost_formatted%", formattedPrestigeCost), p.getName()));
                    	}
                    }
                    main.executeCommands(p, realItemCommands);
				});
				if(!allowedPrestigeSlots.isEmpty()) {
		        playerGUI.setButton(allowedPrestigeSlots.get(prestigeIndex), completedButton);
			    } else {
			    	playerGUI.setButton(prestigeIndex, completedButton);
			    }
			}
			if(playerPrestigeIndex == prestigeIndex) { // if current
				// placeholders {
				String prestigeName = prestige;
				String prestigeDisplayName = main.prxAPI.c(main.prestigeStorage.getDisplayName(prestigeName));
				String master = main.prxAPI.getPlayerMaster(p);
				double prestigeCostNumber = (main.prxAPI.getIncreasedPrestigeCost(master, prestigeName));
				String prestigeCost = String.valueOf(prestigeCostNumber);
				String formattedPrestigeCost = main.formatBalance(prestigeCostNumber);
				ps.setLevelState(LevelState.CURRENT);
				ps.setPrestige(prestigeName);
				// }
				String itemName = getCustomItem(ps).getMaterial() != null ? getCustomItem(ps).getMaterial() : main.globalStorage.getStringData("Prestigelist-gui.current-format.itemNAME");
				int itemAmount = getCustomItem(ps).getAmount() != 0 ? getCustomItem(ps).getAmount() : main.globalStorage.getIntegerData("Prestigelist-gui.current-format.itemAMOUNT");
				String itemDisplayName = getCustomItem(ps).getDisplayName() != null ? getCustomItem(ps).getDisplayName() : main.globalStorage.getStringData("Prestigelist-gui.current-format.itemDISPLAYNAME").replace("%currentprestige%", prestigeName)
						.replace("%currentprestige_display%", prestigeDisplayName)
						.replace("%currentprestige_cost%", prestigeCost)
						.replace("%currentprestige_cost_formatted%", formattedPrestigeCost);
				List<String> itemLore = getCustomItem(ps).getLore() != null ? getCustomItem(ps).getLore() : main.globalStorage.getStringListData("Prestigelist-gui.current-format.itemLORE");
				List<String> itemEnchantments = getCustomItem(ps).getEnchantments() != null ? getCustomItem(ps).getEnchantments() : main.globalStorage.getStringListData("Prestigelist-gui.current-format.itemENCHANTMENTS");
				List<String> itemFlags = getCustomItem(ps).getFlags() != null ? getCustomItem(ps).getFlags() : main.globalStorage.getStringListData("Prestigelist-gui.current-format.itemFLAGS");
				List<String> itemCommands = getCustomItem(ps).getCommands() != null ? getCustomItem(ps).getCommands() : main.globalStorage.getStringListData("Prestigelist-gui.current-format.itemCOMMANDS");
				List<String> realItemCommands = new ArrayList<String>();
				List<String> coloredItemLore = new ArrayList<String>();
				ItemStack currentItem = parseStack(itemName);
				currentItem.setAmount(itemAmount);
				ItemMeta currentMeta = currentItem.getItemMeta();
				currentMeta.setDisplayName(main.prxAPI.c(itemDisplayName));
				itemLore.forEach(line -> {coloredItemLore.add(main.getString(line.replace("%currentprestige%", prestigeName)
						.replace("%currentprestige_display%", prestigeDisplayName).replace("%currentprestige_cost%", prestigeCost)
						.replace("%currentprestige_cost_formatted%", formattedPrestigeCost), p.getName()));});
				currentMeta.setLore(coloredItemLore);
				itemEnchantments.forEach(line -> {
					String[] splittedLine = line.split(" ");
					Enchantment enchant = EnchantmentReader.matchEnchantment(splittedLine[0]);
					int lvl = Integer.valueOf(splittedLine[1]);
					currentMeta.addEnchant(enchant, lvl, true);
				});
				if(!main.isBefore1_7) {
				itemFlags.forEach(line -> {currentMeta.addItemFlags(ItemFlagReader.matchItemFlag(line));});
				}
				currentItem.setItemMeta(currentMeta);
				GUIButton currentButton = new GUIButton(currentItem);
				currentButton.setListener(event -> {
					if(itemCommands.contains("(cancel-item_move)")) {
						event.setCancelled(true);
					}
					if(itemCommands.contains("(closeinv)")) {
						p.closeInventory();
					}
                    for(String command : itemCommands) {
                    	if(command.startsWith("[")) {
                    		realItemCommands.add(main.getString(command
                    				.replace("%currentprestige%", prestigeName).replace("%currentprestige_display%", prestigeName)	.replace("%currentprestige_cost%", prestigeCost)
            						.replace("%currentprestige_cost_formatted%", formattedPrestigeCost), p.getName()));
                    	}
                    }
                    main.executeCommands(p, realItemCommands);
				});
				if(!allowedPrestigeSlots.isEmpty()) {
			        playerGUI.setButton(allowedPrestigeSlots.get(prestigeIndex), currentButton);
				    } else {
				    	playerGUI.setButton(prestigeIndex, currentButton);
				    }
			}
			if(playerPrestigeIndex < prestigeIndex) { // if not completed
				// placeholders {
				String prestigeName = prestige;
				String prestigeDisplayName = main.prxAPI.c(main.prestigeStorage.getDisplayName(prestigeName));
				String master = main.prxAPI.getPlayerMaster(p);
				double prestigeCostNumber = (main.prxAPI.getIncreasedPrestigeCost(master, prestigeName));
				String prestigeCost = String.valueOf(prestigeCostNumber);
				String formattedPrestigeCost = main.formatBalance(prestigeCostNumber);
				ps.setLevelState(LevelState.OTHER);
				ps.setPrestige(prestigeName);
				// }
				String itemName = getCustomItem(ps).getMaterial() != null ? getCustomItem(ps).getMaterial() : main.globalStorage.getStringData("Prestigelist-gui.other-format.itemNAME");
				int itemAmount = getCustomItem(ps).getAmount() != 0 ? getCustomItem(ps).getAmount() : main.globalStorage.getIntegerData("Prestigelist-gui.other-format.itemAMOUNT");
				String itemDisplayName = getCustomItem(ps).getDisplayName() != null ? getCustomItem(ps).getDisplayName() : main.globalStorage.getStringData("Prestigelist-gui.other-format.itemDISPLAYNAME").replace("%otherprestige%", prestigeName)
						.replace("%otherprestige_display%", prestigeDisplayName)
						.replace("%otherprestige_cost%", prestigeCost)
						.replace("%otherprestige_cost_formatted%", formattedPrestigeCost);
				List<String> itemLore = getCustomItem(ps).getLore() != null ? getCustomItem(ps).getLore() : main.globalStorage.getStringListData("Prestigelist-gui.other-format.itemLORE");
				List<String> itemEnchantments = getCustomItem(ps).getEnchantments() != null ? getCustomItem(ps).getEnchantments() : main.globalStorage.getStringListData("Prestigelist-gui.other-format.itemENCHANTMENTS");
				List<String> itemFlags = getCustomItem(ps).getFlags() != null ? getCustomItem(ps).getFlags() : main.globalStorage.getStringListData("Prestigelist-gui.other-format.itemFLAGS");
				List<String> itemCommands = getCustomItem(ps).getCommands() != null ? getCustomItem(ps).getCommands() : main.globalStorage.getStringListData("Prestigelist-gui.other-format.itemCOMMANDS");
				List<String> realItemCommands = new ArrayList<String>();
				List<String> coloredItemLore = new ArrayList<String>();
				ItemStack otherItem = parseStack(itemName);
				otherItem.setAmount(itemAmount);
				ItemMeta otherMeta = otherItem.getItemMeta();
				otherMeta.setDisplayName(main.prxAPI.c(itemDisplayName));
				itemLore.forEach(line -> {coloredItemLore.add(main.getString(line.replace("%otherprestige%", prestigeName)
						.replace("%otherprestige_display%", prestigeDisplayName).replace("%otherprestige_cost%", prestigeCost)
						.replace("%otherprestige_cost_formatted%", formattedPrestigeCost), p.getName()));});
				otherMeta.setLore(coloredItemLore);
				itemEnchantments.forEach(line -> {
					String[] splittedLine = line.split(" ");
					Enchantment enchant = EnchantmentReader.matchEnchantment(splittedLine[0]);
					int lvl = Integer.valueOf(splittedLine[1]);
					otherMeta.addEnchant(enchant, lvl, true);
				});
				if(!main.isBefore1_7) {
				itemFlags.forEach(line -> {otherMeta.addItemFlags(ItemFlagReader.matchItemFlag(line));});
				}
				otherItem.setItemMeta(otherMeta);
				GUIButton otherButton = new GUIButton(otherItem);
				otherButton.setListener(event -> {
					if(itemCommands.contains("(cancel-item_move)")) {
						event.setCancelled(true);
					}
					if(itemCommands.contains("(closeinv)")) {
						p.closeInventory();
					}
                    for(String command : itemCommands) {
                    	if(command.startsWith("[")) {
                    		realItemCommands.add(main.getString(command
                    				.replace("%otherprestige%", prestigeName).replace("%otherprestige_display%", prestigeName).replace("%otherprestige_cost%", prestigeCost)
            						.replace("%otherprestige_cost_formatted%", formattedPrestigeCost), p.getName()));
                    	}
                    }
                    main.executeCommands(p, realItemCommands);
				});
				if(!allowedPrestigeSlots.isEmpty()) {
			        playerGUI.setButton(allowedPrestigeSlots.get(prestigeIndex), otherButton);
				    } else {
				    	playerGUI.setButton(prestigeIndex, otherButton);
				    }
			}
		}
		p.openInventory(playerGUI.getInventory());
	}
	
	public void openMasteryGUI(Player player) {
		Player p = player;
		PaginatedGUI playerGUI = masteryGUI;
		playerGUI.previousPage();
		String playerMaster = main.prxAPI.getPlayerMaster(p);
		List<String> masteryCollection = main.prxAPI.getMasteryCollection();
		int playerMasterIndex = masteryCollection.indexOf(playerMaster);
		MasterState rs = new MasterState();
		for(String master : masteryCollection) {
			int masterIndex = masteryCollection.indexOf(master);
			if(playerMasterIndex > masterIndex) { // if completed
				// placeholders {
				String masterName = master;
				String masterDisplayName = main.prxAPI.c(main.masterStorage.getDisplayName(masterName));
				double masterCostNumber = (main.masterStorage.getCost(masterName));
				String masterCost = String.valueOf(masterCostNumber);
				String formattedMasterCost = main.formatBalance(masterCostNumber);
				rs.setLevelState(LevelState.COMPLETED);
				rs.setMaster(master);
				// }
				String itemName = getCustomItem(rs).getMaterial() != null ? getCustomItem(rs).getMaterial() : main.globalStorage.getStringData("Masterlist-gui.completed-format.itemNAME");
				int itemAmount = getCustomItem(rs).getAmount() != 0 ? getCustomItem(rs).getAmount() : main.globalStorage.getIntegerData("Masterlist-gui.completed-format.itemAMOUNT");
				String itemDisplayName = getCustomItem(rs).getDisplayName() != null ? getCustomItem(rs).getDisplayName() : main.globalStorage.getStringData("Masterlist-gui.completed-format.itemDISPLAYNAME").replace("%completedmaster%", masterName)
						.replace("%completedmaster_display%", masterDisplayName)
						.replace("%completedmaster_cost%", masterCost)
						.replace("%completedmaster_cost_formatted%", formattedMasterCost);
				List<String> itemLore = getCustomItem(rs).getLore() != null ? getCustomItem(rs).getLore() : main.globalStorage.getStringListData("Masterlist-gui.completed-format.itemLORE");
				List<String> itemEnchantments = getCustomItem(rs).getEnchantments() != null ? getCustomItem(rs).getEnchantments() : main.globalStorage.getStringListData("Masterlist-gui.completed-format.itemENCHANTMENTS");
				List<String> itemFlags = getCustomItem(rs).getFlags() != null ? getCustomItem(rs).getFlags() : main.globalStorage.getStringListData("Masterlist-gui.completed-format.itemFLAGS");
				List<String> itemCommands = getCustomItem(rs).getCommands() != null ? getCustomItem(rs).getCommands() : main.globalStorage.getStringListData("Masterlist-gui.completed-format.itemCOMMANDS");
				List<String> realItemCommands = new ArrayList<String>();
				List<String> coloredItemLore = new ArrayList<String>();
				ItemStack completedItem = parseStack(itemName);
				completedItem.setAmount(itemAmount);
				ItemMeta completedMeta = completedItem.getItemMeta();
				completedMeta.setDisplayName(main.prxAPI.c(itemDisplayName));
				itemLore.forEach(line -> {coloredItemLore.add(main.getString(line.replace("%completedmaster%", masterName)
						.replace("%completedmaster_display%", masterDisplayName)	.replace("%completedmaster_cost%", masterCost)
						.replace("%completedmaster_cost_formatted%", formattedMasterCost), p.getName()));});
				completedMeta.setLore(coloredItemLore);
				itemEnchantments.forEach(line -> {
					String[] splittedLine = line.split(" ");
					Enchantment enchant = EnchantmentReader.matchEnchantment(splittedLine[0]);
					int lvl = Integer.valueOf(splittedLine[1]);
					completedMeta.addEnchant(enchant, lvl, true);
				});
				if(!main.isBefore1_7) {
				itemFlags.forEach(line -> {completedMeta.addItemFlags(ItemFlagReader.matchItemFlag(line));});
				}
				completedItem.setItemMeta(completedMeta);
				GUIButton completedButton = new GUIButton(completedItem);
				completedButton.setListener(event -> {
					if(itemCommands.contains("(cancel-item_move)")) {
						event.setCancelled(true);
					}
					if(itemCommands.contains("(closeinv)")) {
						p.closeInventory();
					}
                    for(String command : itemCommands) {
                    	if(command.startsWith("[")) {
                    		realItemCommands.add(main.getString(command
                    				.replace("%completedmaster%", masterName).replace("%completedmaster_display%", masterName).replace("%completedmaster_cost%", masterCost)
            						.replace("%completedmaster_cost_formatted%", formattedMasterCost), p.getName()));
                    	}
                    }
                    main.executeCommands(p, realItemCommands);
				});
				if(!allowedMasterSlots.isEmpty()) {
		        playerGUI.setButton(allowedMasterSlots.get(masterIndex), completedButton);
				} else {
					playerGUI.setButton(masterIndex, completedButton);
				}
			}
			if(playerMasterIndex == masterIndex) { // if current
				// placeholders {
				String masterName = master;
				String masterDisplayName = main.prxAPI.c(main.masterStorage.getDisplayName(masterName));
				double masterCostNumber = (main.masterStorage.getCost(masterName));
				String masterCost = String.valueOf(masterCostNumber);
				String formattedMasterCost = main.formatBalance(masterCostNumber);
				rs.setLevelState(LevelState.CURRENT);
				rs.setMaster(masterName);
				// }
				String itemName = getCustomItem(rs).getMaterial() != null ? getCustomItem(rs).getMaterial() : main.globalStorage.getStringData("Masterlist-gui.current-format.itemNAME");
				int itemAmount = getCustomItem(rs).getAmount() != 0 ? getCustomItem(rs).getAmount() : main.globalStorage.getIntegerData("Masterlist-gui.current-format.itemAMOUNT");
				String itemDisplayName = getCustomItem(rs).getDisplayName() != null ? getCustomItem(rs).getDisplayName() : main.globalStorage.getStringData("Masterlist-gui.current-format.itemDISPLAYNAME").replace("%currentmaster%", masterName)
						.replace("%currentmaster_display%", masterDisplayName)
						.replace("%currentmaster_cost%", masterCost)
						.replace("%currentmaster_cost_formatted%", formattedMasterCost);
				List<String> itemLore = getCustomItem(rs).getLore() != null ? getCustomItem(rs).getLore() : main.globalStorage.getStringListData("Masterlist-gui.current-format.itemLORE");
				List<String> itemEnchantments = getCustomItem(rs).getEnchantments() != null ? getCustomItem(rs).getEnchantments() : main.globalStorage.getStringListData("Masterlist-gui.current-format.itemENCHANTMENTS");
				List<String> itemFlags = getCustomItem(rs).getFlags() != null ? getCustomItem(rs).getFlags() : main.globalStorage.getStringListData("Masterlist-gui.current-format.itemFLAGS");
				List<String> itemCommands = getCustomItem(rs).getCommands() != null ? getCustomItem(rs).getCommands() : main.globalStorage.getStringListData("Masterlist-gui.current-format.itemCOMMANDS");
				List<String> realItemCommands = new ArrayList<String>();
				List<String> coloredItemLore = new ArrayList<String>();
				ItemStack currentItem = parseStack(itemName);
				currentItem.setAmount(itemAmount);
				ItemMeta currentMeta = currentItem.getItemMeta();
				currentMeta.setDisplayName(main.prxAPI.c(itemDisplayName));
				itemLore.forEach(line -> {coloredItemLore.add(main.getString(line.replace("%currentmaster%", masterName)
						.replace("%currentmaster_display%", masterDisplayName).replace("%currentmaster_cost%", masterCost)
						.replace("%currentmaster_cost_formatted%", formattedMasterCost), p.getName()));});
				currentMeta.setLore(coloredItemLore);
				itemEnchantments.forEach(line -> {
					String[] splittedLine = line.split(" ");
					Enchantment enchant = EnchantmentReader.matchEnchantment(splittedLine[0]);
					int lvl = Integer.valueOf(splittedLine[1]);
					currentMeta.addEnchant(enchant, lvl, true);
				});
				if(!main.isBefore1_7) {
				itemFlags.forEach(line -> {currentMeta.addItemFlags(ItemFlagReader.matchItemFlag(line));});
				}
				currentItem.setItemMeta(currentMeta);
				GUIButton currentButton = new GUIButton(currentItem);
				currentButton.setListener(event -> {
					if(itemCommands.contains("(cancel-item_move)")) {
						event.setCancelled(true);
					}
					if(itemCommands.contains("(closeinv)")) {
						p.closeInventory();
					}
                    for(String command : itemCommands) {
                    	if(command.startsWith("[")) {
                    		realItemCommands.add(main.getString(command
                    				.replace("%currentmaster%", masterName).replace("%currentmaster_display%", masterName)	.replace("%currentmaster_cost%", masterCost)
            						.replace("%currentmaster_cost_formatted%", formattedMasterCost), p.getName()));
                    	}
                    }
                    main.executeCommands(p, realItemCommands);
				});
				if(!allowedMasterSlots.isEmpty()) {
			        playerGUI.setButton(allowedMasterSlots.get(masterIndex), currentButton);
					} else {
						playerGUI.setButton(masterIndex, currentButton);
					}
			}
			if(playerMasterIndex < masterIndex) { // if not completed
				// placeholders {
				String masterName = master;
				String masterDisplayName = main.prxAPI.c(main.masterStorage.getDisplayName(masterName));
				double masterCostNumber = (main.masterStorage.getCost(masterName));
				String masterCost = String.valueOf(masterCostNumber);
				String formattedMasterCost = main.formatBalance(masterCostNumber);
				rs.setLevelState(LevelState.OTHER);
				rs.setMaster(masterName);
				// }
				String itemName = getCustomItem(rs).getMaterial() != null ? getCustomItem(rs).getMaterial() : main.globalStorage.getStringData("Masterlist-gui.other-format.itemNAME");
				int itemAmount = getCustomItem(rs).getAmount() != 0 ? getCustomItem(rs).getAmount() : main.globalStorage.getIntegerData("Masterlist-gui.other-format.itemAMOUNT");
				String itemDisplayName = getCustomItem(rs).getDisplayName() != null ? getCustomItem(rs).getDisplayName() : main.globalStorage.getStringData("Masterlist-gui.other-format.itemDISPLAYNAME").replace("%othermaster%", masterName)
						.replace("%othermaster_display%", masterDisplayName)
						.replace("%othermaster_cost%", masterCost)
						.replace("%othermaster_cost_formatted%", formattedMasterCost);
				List<String> itemLore = getCustomItem(rs).getLore() != null ? getCustomItem(rs).getLore() : main.globalStorage.getStringListData("Masterlist-gui.other-format.itemLORE");
				List<String> itemEnchantments = getCustomItem(rs).getEnchantments() != null ? getCustomItem(rs).getEnchantments() : main.globalStorage.getStringListData("Masterlist-gui.other-format.itemENCHANTMENTS");
				List<String> itemFlags = getCustomItem(rs).getFlags() != null ? getCustomItem(rs).getFlags() : main.globalStorage.getStringListData("Masterlist-gui.other-format.itemFLAGS");
				List<String> itemCommands = getCustomItem(rs).getCommands() != null ? getCustomItem(rs).getCommands() : main.globalStorage.getStringListData("Masterlist-gui.other-format.itemCOMMANDS");
				List<String> realItemCommands = new ArrayList<String>();
				List<String> coloredItemLore = new ArrayList<String>();
				ItemStack otherItem = parseStack(itemName);
				otherItem.setAmount(itemAmount);
				ItemMeta otherMeta = otherItem.getItemMeta();
				otherMeta.setDisplayName(main.prxAPI.c(itemDisplayName));
				itemLore.forEach(line -> {coloredItemLore.add(main.getString(line.replace("%othermaster%", masterName)
						.replace("%othermaster_display%", masterDisplayName).replace("%othermaster_cost%", masterCost)
						.replace("%othermaster_cost_formatted%", formattedMasterCost), p.getName()));});
				otherMeta.setLore(coloredItemLore);
				itemEnchantments.forEach(line -> {
					String[] splittedLine = line.split(" ");
					Enchantment enchant = EnchantmentReader.matchEnchantment(splittedLine[0]);
					int lvl = Integer.valueOf(splittedLine[1]);
					otherMeta.addEnchant(enchant, lvl, true);
				});
				if(!main.isBefore1_7) {
				itemFlags.forEach(line -> {otherMeta.addItemFlags(ItemFlagReader.matchItemFlag(line));});
				}
				otherItem.setItemMeta(otherMeta);
				GUIButton otherButton = new GUIButton(otherItem);
				otherButton.setListener(event -> {
					if(itemCommands.contains("(cancel-item_move)")) {
						event.setCancelled(true);
					}
					if(itemCommands.contains("(closeinv)")) {
						p.closeInventory();
					}
                    for(String command : itemCommands) {
                    	if(command.startsWith("[")) {
                    		realItemCommands.add(main.getString(command
                    				.replace("%othermaster%", masterName).replace("%othermaster_display%", masterName).replace("%othermaster_cost%", masterCost)
            						.replace("%othermaster_cost_formatted%", formattedMasterCost), p.getName()));
                    	}
                    }
                    main.executeCommands(p, realItemCommands);
				});
				if(!allowedMasterSlots.isEmpty()) {
			        playerGUI.setButton(allowedMasterSlots.get(masterIndex), otherButton);
					} else {
						playerGUI.setButton(masterIndex, otherButton);
					}
			}
		}
		p.openInventory(playerGUI.getInventory());
	}
}

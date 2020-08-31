package me.hexiaranks.api;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hexiaranks.HexiaRanks;
import me.hexiaranks.utils.CollectionUtils;
import me.hexiaranks.utils.CollectionUtils.PaginatedList;

public class Mastery {
	
	private HexiaRanks main = (HexiaRanks)Bukkit.getPluginManager().getPlugin("HexiaRanks");
	String masterCurrentFormat;
	String masterCompletedFormat;
	String masterOtherFormat;
	boolean enablePages;
	int masterPerPage;
	List<String> masterWithPagesListFormat;
	List<String> masterListFormat;
	boolean isCustomList;
	List<String> masterListFormatHeader;
	List<String> masterListFormatFooter;
	List<String> masteryCollection;
	List<String> currentMastery;
	List<String> completedMastery;
	List<String> otherMastery;
	List<String> nonPagedMastery;
	String lastPageReached;
	public String masterListConsole;
	List<String> header;
	List<String> footer;
	public String masterListInvalidPage;
	/**
	    *
	    * @param sender The sender to send the list to
	    * @param list The list to paginate
	    * @param page The page number to display.
	    * @param countAll The count of all available entries 
	    */
	  public void paginate(CommandSender sender, List<String> list, int page, List<String> header, List<String> footer)
	  {
		  masterPerPage = main.globalStorage.getIntegerData("Masterlist-text.master-per-page");
	      int totalPageCount = 1;
	 
	      if((list.size() % masterPerPage) == 0)
	      {
	        if(list.size() > 0)
	        {
	            totalPageCount = list.size() / masterPerPage;
	        }     
	      }
	      else
	      {
	        totalPageCount = (list.size() / masterPerPage) + 1;
	      }
	 
	      if(page <= totalPageCount)
	      {

	        //beginline
	           if(header != null) {
		         	  for(String line : header) {
		         		  sender.sendMessage(line.replace("%totalpages%", String.valueOf(totalPageCount)));
		         	  }
		            }

	        if(list.isEmpty())
	        {
	            sender.sendMessage(ChatColor.BLACK + "[?] mastery list is empty [?]");
	        }
	        else
	        {
	            int i = 0, k = 0;
	            page--;
	 
	            for (String entry : list)
	            {
	              k++;
	              if ((((page * masterPerPage) + i + 1) == k) && (k != ((page * masterPerPage) + masterPerPage + 1)))
	              {
	                  i++;
	                  sender.sendMessage(entry.replace("%totalpages%", String.valueOf(totalPageCount)).replace("%currentpage%", String.valueOf(page)));
	              }
	            }
	        }
	 //endline
	           if(footer != null) {
		         	  for(String line : footer) {
		         		  sender.sendMessage(line.replace("%totalpages%", String.valueOf(totalPageCount)));
		         	  }
		            }
	        
	      }
	      else
	      {
	        sender.sendMessage(main.prxAPI.c(lastPageReached.replace("%page%", String.valueOf(totalPageCount))));
	      }
	  }
	
		public void load() {
			masterCurrentFormat = main.globalStorage.getStringData("Masterlist-text.master-current-format");
			masterCompletedFormat = main.globalStorage.getStringData("Masterlist-text.master-completed-format");
			masterOtherFormat = main.globalStorage.getStringData("Masterlist-text.master-other-format");
			enablePages = main.globalStorage.getBooleanData("Masterlist-text.enable-pages");
			masterPerPage = main.globalStorage.getIntegerData("Masterlist-text.master-per-page");
			masterWithPagesListFormat = main.globalStorage.getStringListData("Masterlist-text.master-with-pages-list-format");
			masterListFormat = main.globalStorage.getStringListData("Masterlist-text.master-list-format");
			masterListFormatHeader = new ArrayList<>();
			masterListFormatFooter = new ArrayList<>();
			masteryCollection = new LinkedList<>();
			currentMastery = new ArrayList<>();
			completedMastery = new ArrayList<>();
			otherMastery = new ArrayList<>();
			nonPagedMastery = new ArrayList<>();
			lastPageReached = main.messagesStorage.getStringMessage("masterlist-last-page-reached");
			masterListConsole = main.messagesStorage.getStringMessage("masterlist-console");
			if(enablePages) {
				if(masterWithPagesListFormat.contains("[masterylist]")) {
					isCustomList = false;
				} else {
					isCustomList = true;
				}
			} else {
				if(masterListFormat.contains("[masterylist]")) {
					isCustomList = false;
				} else {
					isCustomList = true;
				}
			}
			masterListInvalidPage = main.messagesStorage.getStringMessage("masterlist-invalid-page");
		}
	  
	public Mastery() {}
	
	/**
	 * 
	 * @param pageNumber put null if you want to send a normal list
	 * @param sender
	 */
	public void send(final String pageNumber, final CommandSender sender) {
		if(!enablePages || pageNumber == null) {
			sendList(sender);
		} else {
			if(!main.prxAPI.numberAPI.isNumber(pageNumber) || Integer.valueOf(pageNumber) < 1) {
				sender.sendMessage(main.prxAPI.c(masterListInvalidPage).replace("%page%", pageNumber));
				return;
			}
			sendPagedList(pageNumber, sender);
		}
	}
	
	private void sendList(CommandSender sender) {
		if(!enablePages) {
			// no enable pages
			if(isCustomList) {
				masterListFormat.forEach(format -> sender.sendMessage(main.prxAPI.c(format)));
				return;
			}
			Player p = (Player)sender;
			String masterName = main.prxAPI.getPlayerMaster(p);
			List<String> newMasteryCollection = main.masterStorage.getMasteryCollection();
			if(masteryCollection.isEmpty()) {
                masteryCollection = newMasteryCollection;
			}
			
			if(masteryCollection.size() != newMasteryCollection.size()) {
				masteryCollection = newMasteryCollection;
			}
			Integer varIndex = masterListFormat.indexOf(String.valueOf("[masterylist]"));
			// header and footer setup {
			if(masterListFormatHeader.isEmpty() && masterListFormatFooter.isEmpty() && masterListFormat.size() > 1) {
			  for(int i = 0; i < masterListFormat.size(); i++) {
				  if(varIndex > i) {
					  masterListFormatHeader.add(masterListFormat.get(i));
				  } if (varIndex < i) {
					  masterListFormatFooter.add(masterListFormat.get(i));
			  	  }
			  }
			}
			// }
			// send header {
			for(String header : masterListFormatHeader) {
		       sender.sendMessage(main.prxAPI.c(header));
			}
			// }
			// send ranks list {
		    currentMastery.clear();
			completedMastery.clear();
			otherMastery.clear();
			
				String lastPrestige = main.prxAPI.getLastPrestige();
				String lastPrestigeDisplay = main.prxAPI.getPrestige(lastPrestige).getDisplayName();
				String firstMaster = main.prxAPI.getFirstMaster();
				String firstMasterDisplay = main.masterStorage.getDisplayName(firstMaster);
				double firstMasterCost = main.prxAPI.getMasterCost(firstMaster);
				if(masterName == null) {
					otherMastery.add(main.prxAPI.cp(masterOtherFormat.replace("%master_name%", lastPrestige)
							.replace("%master_displayname%", lastPrestigeDisplay)
							.replace("%nextmaster_name%", firstMaster)
							.replace("%nextmaster_displayname%", firstMasterDisplay)
							.replace("%nextmaster_cost%", String.valueOf(firstMasterCost))
							.replace("%nextmaster_cost_formatted%", main.prxAPI.formatBalance(firstMasterCost))
							, p));
				} else if (masterName.equals(firstMaster)) {
					completedMastery.add(main.prxAPI.cp(masterCompletedFormat.replace("%master_name%", lastPrestige)
							.replace("%master_displayname%", lastPrestigeDisplay)
							.replace("%nextmaster_name%", firstMaster)
							.replace("%nextmaster_displayname%", firstMasterDisplay)
							.replace("%nextmaster_cost%", String.valueOf(firstMasterCost))
							.replace("%nextmaster_cost_formatted%", main.prxAPI.formatBalance(firstMasterCost))
							, p));
				} else {
					completedMastery.add(main.prxAPI.cp(masterCompletedFormat.replace("%master_name%", lastPrestige)
							.replace("%master_displayname%", lastPrestigeDisplay)
							.replace("%nextmaster_name%", firstMaster)
							.replace("%nextmaster_displayname%", firstMasterDisplay)
							.replace("%nextmaster_cost%", String.valueOf(firstMasterCost))
							.replace("%nextmaster_cost_formatted%", main.prxAPI.formatBalance(firstMasterCost))
							, p));
				}
				
			int currentMasterIndex = masteryCollection.indexOf(masterName);
			for(String master : masteryCollection) {
				if(currentMasterIndex == masteryCollection.indexOf(master)) {
					// save rank current format {
					String master2 = master;
					if(!main.masterStorage.getNextMasterName(master2).equalsIgnoreCase("lastmaster")) {
					String format = main.prxAPI.cp(masterCurrentFormat.replace("%master_name%", master2)
							.replace("%master_displayname%", main.masterStorage.getDisplayName(master2))
							.replace("%nextmaster_name%", main.masterStorage.getDisplayName(master2))
							.replace("%nextmaster_displayname%", main.masterStorage.getNextMasterDisplayName(master2))
							.replace("%nextmaster_cost%", String.valueOf(main.masterStorage.getNextMasterCost(master2)))
							.replace("%nextmaster_cost_formatted%", main.prxAPI.formatBalance(main.masterStorage.getNextMasterCost(master2)))
                            , p);			
					    currentMastery.add(format);
					}
					// }
				} if (currentMasterIndex > masteryCollection.indexOf(master)) {
					// save rank completed format {
					String master2 = master;
					if(!main.masterStorage.getNextMasterName(master2).equalsIgnoreCase("lastmaster")) {
					String format = main.prxAPI.cp(masterCompletedFormat.replace("%master_name%", master2)
							.replace("%master_displayname%", main.masterStorage.getDisplayName(master2))
							.replace("%nextmaster_name%", main.masterStorage.getNextMasterName(master2))
							.replace("%nextmaster_displayname%", main.masterStorage.getNextMasterDisplayName(master2))
							.replace("%nextmaster_cost%", String.valueOf(main.masterStorage.getNextMasterCost(master2)))
							.replace("%nextmaster_cost_formatted%", main.prxAPI.formatBalance(main.masterStorage.getNextMasterCost(master2)))
                            , p);			
					completedMastery.add(format);
					}
					// }
				} if (currentMasterIndex < masteryCollection.indexOf(master)) {
					// save rank other format {
					String master2 = master;
					if(!main.masterStorage.getNextMasterName(master2).equalsIgnoreCase("lastmaster")) {
					String format = main.prxAPI.cp(masterOtherFormat.replace("%master_name%", master2)
							.replace("%master_displayname%", main.masterStorage.getDisplayName(master2))
							.replace("%nextmaster_name%", main.masterStorage.getNextMasterName(master2))
							.replace("%nextmaster_displayname%", main.masterStorage.getNextMasterDisplayName(master2))
							.replace("%nextmaster_cost%", String.valueOf(main.masterStorage.getNextMasterCost(master2)))
							.replace("%nextmaster_cost_formatted%", main.prxAPI.formatBalance(main.masterStorage.getNextMasterCost(master2)))
                            , p);			
					otherMastery.add(format);
					}
					// }
				}
			}
            completedMastery.forEach(line -> {sender.sendMessage(line);});
			currentMastery.forEach(line -> {sender.sendMessage(line);});
			otherMastery.forEach(line -> {sender.sendMessage(line);});
			for(String footer : masterListFormatFooter) {
				sender.sendMessage(main.prxAPI.c(footer));
			}
			// }
			return;
		}
	}
	
	private void sendPagedList(String pageNumber, CommandSender sender) {
		if(enablePages) {
			if(isCustomList) {
				//this.paginate(sender, masterWithPagesListFormat, Integer.parseInt(pageNumber), null, null);
				List<String> customList = CollectionUtils.paginateList(masterWithPagesListFormat, masterPerPage, Integer.parseInt(pageNumber));
				customList.forEach(line -> {
					sender.sendMessage(main.getString(line, sender.getName()));
				});
				return;
			}
			Player p = (Player)sender;
            String masterName = main.prxAPI.getPlayerMaster(p);
			List<String> newMasteryCollection = main.masterStorage.getMasteryCollection();
			if(masteryCollection.isEmpty()) {
                masteryCollection = newMasteryCollection;
			}
			
			if(masteryCollection.size() != newMasteryCollection.size()) {
				masteryCollection = newMasteryCollection;
			}
			PaginatedList paginatedList = CollectionUtils.paginateListCollectable(masteryCollection, masterPerPage, Integer.parseInt(pageNumber));
			int currentPage = paginatedList.getCurrentPage();
			int finalPage = paginatedList.getFinalPage();
			if(currentPage > finalPage) {
				  sender.sendMessage(main.prxAPI.c(lastPageReached.replace("%page%", String.valueOf(finalPage))));
				  return;
			}
			masteryCollection = paginatedList.collect();
			Integer varIndex = masterWithPagesListFormat.indexOf("[masterylist]");
			// header and footer setup {
			if(masterListFormatHeader.isEmpty() && masterListFormatFooter.isEmpty() && masterListFormat.size() > 1) {
			  for(int i = 0; i < masterWithPagesListFormat.size(); i++) {
				  if(varIndex > i) {
					  masterListFormatHeader.add(masterWithPagesListFormat.get(i));
				  } if (varIndex < i) {
					  masterListFormatFooter.add(masterWithPagesListFormat.get(i));
			  	  }
			  }
			}
			// }
			// send header {
			if(header == null) {
				header = new ArrayList<String>();
			}
			header.clear();
			for(String header : masterListFormatHeader) {
		       this.header.add(main.prxAPI.c(header.replace("%currentpage%", pageNumber).replace("%totalpages%", String.valueOf(finalPage))));
			}
			// }
			// send mastery list {
		    currentMastery.clear();
			completedMastery.clear();
			otherMastery.clear();
			if(currentPage <= 1) {
				String lastPrestige = main.prxAPI.getLastPrestige();
				String lastPrestigeDisplay = main.prxAPI.getPrestige(lastPrestige).getDisplayName();
				String firstMaster = main.prxAPI.getFirstMaster();
				String firstMasterDisplay = main.masterStorage.getDisplayName(firstMaster);
				double firstMasterCost = main.prxAPI.getMasterCost(firstMaster);
				if(masterName == null) {
					otherMastery.add(main.prxAPI.cp(masterOtherFormat.replace("%master_name%", lastPrestige)
							.replace("%master_displayname%", lastPrestigeDisplay)
							.replace("%nextmaster_name%", firstMaster)
							.replace("%nextmaster_displayname%", firstMasterDisplay)
							.replace("%nextmaster_cost%", String.valueOf(firstMasterCost))
							.replace("%nextmaster_cost_formatted%", main.prxAPI.formatBalance(firstMasterCost))
							, p));
				} else if (masterName.equals(firstMaster)) {
					completedMastery.add(main.prxAPI.cp(masterCompletedFormat.replace("%master_name%", lastPrestige)
							.replace("%master_displayname%", lastPrestigeDisplay)
							.replace("%nextmaster_name%", firstMaster)
							.replace("%nextmaster_displayname%", firstMasterDisplay)
							.replace("%nextmaster_cost%", String.valueOf(firstMasterCost))
							.replace("%nextmaster_cost_formatted%", main.prxAPI.formatBalance(firstMasterCost))
							, p));
				} else {
					completedMastery.add(main.prxAPI.cp(masterCompletedFormat.replace("%master_name%", lastPrestige)
							.replace("%master_displayname%", lastPrestigeDisplay)
							.replace("%nextmaster_name%", firstMaster)
							.replace("%nextmaster_displayname%", firstMasterDisplay)
							.replace("%nextmaster_cost%", String.valueOf(firstMasterCost))
							.replace("%nextmaster_cost_formatted%", main.prxAPI.formatBalance(firstMasterCost))
							, p));
				}
				}
			int currentMasterIndex = newMasteryCollection.indexOf(masterName);
			for(String master : masteryCollection) {
				if(currentMasterIndex == newMasteryCollection.indexOf(master)) {
					// save master current format {
					String master2 = master;
					if(!main.masterStorage.getNextMasterName(master2).equalsIgnoreCase("lastmaster")) {
					String format = main.prxAPI.cp(masterCurrentFormat.replace("%master_name%", master2)
							.replace("%master_displayname%", main.masterStorage.getDisplayName(master2))
							.replace("%nextmaster_name%", main.masterStorage.getNextMasterName(master2))
							.replace("%nextmaster_displayname%", main.masterStorage.getNextMasterDisplayName(master2))
							.replace("%nextmaster_cost%", String.valueOf(main.masterStorage.getNextMasterCost(master2)))
							.replace("%nextmaster_cost_formatted%", main.prxAPI.formatBalance(main.masterStorage.getNextMasterCost(master2)))
                            , p);			
					    currentMastery.add(format);
					}
					// }
				} if (currentMasterIndex > newMasteryCollection.indexOf(master)) {
					// save master completed format {
					String master2 = master;
					if(!main.masterStorage.getNextMasterName(master2).equalsIgnoreCase("lastmaster")) {
					String format = main.prxAPI.cp(masterCompletedFormat.replace("%master_name%", master2)
							.replace("%master_displayname%", main.masterStorage.getDisplayName(master2))
							.replace("%nextmaster_name%", main.masterStorage.getNextMasterName(master2))
							.replace("%nextmaster_displayname%", main.masterStorage.getNextMasterDisplayName(master2))
							.replace("%nextmaster_cost%", String.valueOf(main.masterStorage.getNextMasterCost(master2)))
							.replace("%nextmaster_cost_formatted%", main.prxAPI.formatBalance(main.masterStorage.getNextMasterCost(master2)))
                            , p);			
					completedMastery.add(format);
					}
					// }
				} if (currentMasterIndex < newMasteryCollection.indexOf(master)) {
					// save rank other format {
					String master2 = master;
					if(!main.masterStorage.getNextMasterName(master2).equalsIgnoreCase("lastmaster")) {
					String format = main.prxAPI.cp(masterOtherFormat.replace("%master_name%", master2)
							.replace("%master_displayname%", main.masterStorage.getDisplayName(master2))
							.replace("%nextmaster_name%", main.masterStorage.getNextMasterName(master2))
							.replace("%nextmaster_displayname%", main.masterStorage.getNextMasterDisplayName(master2))
							.replace("%nextmaster_cost%", String.valueOf(main.masterStorage.getNextMasterCost(master2)))
							.replace("%nextmaster_cost_formatted%", main.prxAPI.formatBalance(main.masterStorage.getNextMasterCost(master2)))
                            , p);			
					otherMastery.add(format);
					}
					// }
				}
			}
			nonPagedMastery.clear();
            completedMastery.forEach(line -> {nonPagedMastery.add(line);});
			currentMastery.forEach(line -> {nonPagedMastery.add(line);});
			otherMastery.forEach(line -> {nonPagedMastery.add(line);});
			if(footer == null) {
				footer = new ArrayList<String>();
			}
			footer.clear();
			for(String footer : masterListFormatFooter) {
				this.footer.add(main.prxAPI.c(footer.replace("%currentpage%", pageNumber).replace("%totalpages%", String.valueOf(finalPage))));
			}
			this.header.forEach(sender::sendMessage);
			this.nonPagedMastery.forEach(sender::sendMessage);
			this.footer.forEach(sender::sendMessage);
            //paginate(sender, nonPagedMastery, Integer.valueOf(pageNumber), header, footer);

			
		}
	}
}

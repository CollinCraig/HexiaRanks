package me.hexiaranks.hooks;

import java.text.DecimalFormat;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.hexiaranks.HexiaRanks;
import me.hexiaranks.api.PRXAPI;
import me.hexiaranks.data.RankPath;
import me.hexiaranks.leaderboard.LeaderboardManager;
import me.hexiaranks.utils.CollectionUtils;

public class PapiHook extends PlaceholderExpansion {
	
    private HexiaRanks main;
    private PRXAPI prxAPI;
    private LeaderboardManager lbm;
    private String nullNameRank;
    private String nullValueRank;
    private String nullNamePrestige;
    private String nullValuePrestige;
    private String nullNameMaster;
    private String nullValueMaster;

	public PapiHook(HexiaRanks main) {
		super();
		this.main = main;
		prxAPI = this.main.prxAPI;
		lbm = this.main.lbm;
		nullNameRank = this.main.globalStorage.getStringData("PlaceholderAPI.leaderboard-name-rank-null");
		nullValueRank = this.main.globalStorage.getStringData("PlaceholderAPI.leaderboard-value-rank-null");
		nullNamePrestige = this.main.globalStorage.getStringData("PlaceholderAPI.leaderboard-name-prestige-null");
		nullValuePrestige = this.main.globalStorage.getStringData("PlaceholderAPI.leaderboard-value-prestige-null");
		nullNameMaster = this.main.globalStorage.getStringData("PlaceholderAPI.leaderboard-name-master-null");
		nullValueMaster = this.main.globalStorage.getStringData("PlaceholderAPI.leaderboard-value-master-null");
	}

	public String getNullNameRank() {
		return this.nullNameRank;
	}

	public String getNullValueRank() {
		return this.nullValueRank;
	}
	
	public String getNullNamePrestige() {
		return this.nullNamePrestige;
	}
	
	public String getNullValuePrestige() {
		return this.nullValuePrestige;
	}
	
	public String getNullNameMaster() {
		return this.nullNameMaster;
	}
	
	public String getNullValueMaster() {
		return this.nullValueMaster;
	}
	
    @Override
    public boolean persist(){
        return true;
    }
    
    @Override
    public boolean canRegister(){
        return true;
    }

    @Nonnull
    public String getChatColorsInString(final String string) {
    	String chatColors = "";
    	if(string == null || !ChatColor.stripColor(string).contains("&")) {
    		return chatColors;
    	} else {
    		int i = -1;
    		for(char character : string.toCharArray()) {
    			i++;
    			if(character == '&') {
    				chatColors = chatColors + String.valueOf(string.charAt(i + 1));
    			}
    		}
    	}
    	return chatColors;
    }
    
    @Override
	public String onRequest(OfflinePlayer arg0, String arg1) {
        OfflinePlayer p = arg0;
        if(arg1.equalsIgnoreCase("currentrank_number")) {
        	return String.valueOf(prxAPI.getPlayerRankNumber(p));
        }
        if(arg1.equalsIgnoreCase("currentprestige_number")) {
        	return String.valueOf(prxAPI.getPlayerPrestigeNumber(p));
        }
        if(arg1.equalsIgnoreCase("current_displayname")) {
        	return prxAPI.getStageDisplay(p.getPlayer(), " ", true);
        }
        if(arg1.startsWith("current_displayname_customspace_")) {
        	String spaceChar = arg1.replace("current_displayname_customspace_", "");
        	return prxAPI.getStageDisplay(p.getPlayer(), spaceChar, true);
        }
		if(arg1.equalsIgnoreCase("currentrank_name")) {
			if(prxAPI.isLastRank(p) && prxAPI.main.globalStorage.getBooleanData("PlaceholderAPI.currentrank-lastrank-enabled")) {
				return prxAPI.getPluginMainClass().getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.currentrank-lastrank"), arg0.getName());
			}
			return String.valueOf(prxAPI.getPlayerRank(p));
		}
		if(arg1.startsWith("currentrank_name_")) {
			Player player = Bukkit.getPlayer(arg1.replace("currentrank_name_", ""));
			if(player == null) {
				return "null";
			}
			return String.valueOf(prxAPI.getPlayerRank(p));
		}
		if(arg1.equalsIgnoreCase("currentrank_displayname")) {
			if(prxAPI.isLastRank(p) && prxAPI.main.globalStorage.getBooleanData("PlaceholderAPI.currentrank-lastrank-enabled")) {
				return prxAPI.getPluginMainClass().getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.currentrank-lastrank"), arg0.getName());
			}
			return String.valueOf(prxAPI.getPlayerRankDisplay(p));
		}
		if(arg1.equalsIgnoreCase("currentrank_firstcolor")) {
			String stripped = ChatColor.stripColor(prxAPI.getPlayerRankDisplay(p));
			return String.valueOf(stripped.charAt(1)) == null ? "&r" : String.valueOf(stripped.charAt(1));
		}
		if(arg1.equalsIgnoreCase("currentrank_lastchar")) {
			String rankDisplay = prxAPI.getPlayerRankDisplay(p);
			return String.valueOf(rankDisplay.charAt(rankDisplay.length() - 1));
		}
		if(arg1.equalsIgnoreCase("currentrank_lastcolors")) {
			String rankDisplay = prxAPI.getPlayerRankDisplay(p);
			return String.valueOf(ChatColor.getLastColors(rankDisplay));
		}
		if(arg1.equalsIgnoreCase("currentrank_secondcolor")) {
			String stripped = ChatColor.stripColor(prxAPI.getPlayerRankDisplay(p));
			return String.valueOf(stripped.charAt(3)) == null ? "&r" : String.valueOf(stripped.charAt(3));
		}
		if(arg1.equalsIgnoreCase("currentrank_thirdcolor")) {
			String stripped = ChatColor.stripColor(prxAPI.getPlayerRankDisplay(p));
			return String.valueOf(stripped.charAt(5)) == null ? "&r" : String.valueOf(stripped.charAt(5));
		}
		if(arg1.equalsIgnoreCase("currentrank_afterbracketcolor")) {
			String rankDisplay = prxAPI.getPlayerRankDisplay(p);
			return String.valueOf(rankDisplay.charAt(rankDisplay.indexOf("[") + 2));
		}
		if(arg1.equalsIgnoreCase("currentrank_afterspacecolor")) {
			String rankDisplay = prxAPI.getPlayerRankDisplay(p);
			return String.valueOf(rankDisplay.charAt(rankDisplay.indexOf(" ") + 2));
		}
		if(arg1.equalsIgnoreCase("currentrank_colors")) {
			String knownColors = getChatColorsInString(prxAPI.getPlayerRankDisplay(p));
			return knownColors;
		}
		if(arg1.equalsIgnoreCase("rankup_percentage")) {
			if(prxAPI.getPlayerNextRank(p) == null) {
				  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.rankup-percentage-lastrank"), arg0.getName());
			}
			if(prxAPI.isPercentSignBehind()) {
			return prxAPI.getPercentSign() + String.valueOf(prxAPI.getPlayerRankupPercentageDirect(p));
			} else {
				return String.valueOf(prxAPI.getPlayerRankupPercentageDirect(p)) + prxAPI.getPercentSign();
			}
		}
		if(arg1.equalsIgnoreCase("rankup_percentage_plain")) {
			if(prxAPI.getPlayerNextRank(p) == null) {
				  return "100";
			}
			return String.valueOf(prxAPI.getPlayerRankupPercentageDirect(p));
		}
		if(arg1.startsWith("plaindecimal_")) {
			String bsed = PlaceholderAPI.setBracketPlaceholders(p, arg1.replace("plaindecimal_", ""));
			if(!prxAPI.numberAPI.isNumber(bsed)) {
				String.valueOf(prxAPI.numberAPI.keepNumbersWithDots(bsed));
			}
			return String.valueOf(prxAPI.numberAPI.keepNumbersWithDots(prxAPI.numberAPI.deleteScientificNotationA(Double.valueOf(bsed))));
		}
		if(arg1.startsWith("plain_")) {
			String bsed = PlaceholderAPI.setBracketPlaceholders(p, arg1.replace("plain_", ""));
			return String.valueOf(prxAPI.numberAPI.keepNumbers(bsed));
		}
		if(arg1.startsWith("integerize_")) {
			String integerize = PlaceholderAPI.setBracketPlaceholders(p, prxAPI.numberAPI.keepNumbersWithDots(arg1.replace("integerize_", "")));
		    if(prxAPI.numberAPI.isNumber(integerize)) {
		    double val = Double.valueOf(integerize);
			return String.valueOf(prxAPI.numberAPI.toFakeInteger(val));
		    } else {
		    	return integerize;
		    }
		}
		if(arg1.equalsIgnoreCase("next_percentage")) {
			if(prxAPI.isPercentSignBehind()) {
				return prxAPI.getPercentSign() + String.valueOf(prxAPI.getPlayerNextPercentage(p).getPercentage());
			} else {
				return String.valueOf(prxAPI.getPlayerNextPercentage(p).getPercentage()) + prxAPI.getPercentSign();
			}
		}
		if(arg1.equalsIgnoreCase("next_percentage_plain")) {
               return String.valueOf(prxAPI.getPlayerNextPercentage(p).getPercentage());
		}
		if(arg1.equalsIgnoreCase("next_percentage_decimal")) {
			if(prxAPI.isPercentSignBehind()) {
				return prxAPI.getPercentSign() + String.valueOf(prxAPI.getPlayerNextPercentageDecimal(p));
			} else {
				return String.valueOf(prxAPI.getPlayerNextPercentageDecimal(p)) + prxAPI.getPercentSign();
			}
		}
		if(arg1.equalsIgnoreCase("rankup_percentage_decimal")) {
			if(prxAPI.getPlayerNextRank(p) == null) {
				  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.rankup-percentage-lastrank"), arg0.getName());
			}
			if(prxAPI.isPercentSignBehind()) {
			return prxAPI.getPercentSign() + String.valueOf(prxAPI.getPlayerRankupPercentageDecimalDirect(p));
			} else {
				return String.valueOf(prxAPI.getPlayerRankupPercentageDecimalDirect(p)) + prxAPI.getPercentSign();
			}
		}
		if(arg1.equalsIgnoreCase("rankup_percentage_nolimit")) {
			if(prxAPI.getPlayerNextRank(p) == null) {
				  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.rankup-percentage-lastrank"), arg0.getName());
			}
			if(prxAPI.isPercentSignBehind()) {
			return prxAPI.getPercentSign() + String.valueOf(prxAPI.getPlayerRankupPercentageNoLimitDirect(p));
			} else {
				return String.valueOf(prxAPI.getPlayerRankupPercentageNoLimitDirect(p)) + prxAPI.getPercentSign();
			}
		}
		if(arg1.equalsIgnoreCase("rankup_percentage_decimal_nolimit")) {
			if(prxAPI.getPlayerNextRank(p) == null) {
				  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.rankup-percentage-lastrank"), arg0.getName());
			}
			if(prxAPI.isPercentSignBehind()) {
			return prxAPI.getPercentSign() + String.valueOf(prxAPI.getPlayerRankupPercentageDecimalNoLimitDirect(p));
			} else {
				return String.valueOf(prxAPI.getPlayerRankupPercentageDecimalNoLimitDirect(p)) + prxAPI.getPercentSign();
			}
		}
		if(arg1.equalsIgnoreCase("rankup_progress")) {
			if(prxAPI.getPlayerNextRank(p) == null) {
				  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.rankup-progress-lastrank"), arg0.getName());
			}
			return String.valueOf(prxAPI.getPlayerRankupProgressBar(p));
		}
		if(arg1.equalsIgnoreCase("next_progress")) {
			return String.valueOf((prxAPI.getPlayerNextProgress(p)));
		}
		if(arg1.equalsIgnoreCase("next_progress_double")) {
			return String.valueOf((prxAPI.getPlayerNextProgressExtended(p)));
		}
		if(arg1.equalsIgnoreCase("rankup_progress_double")) {
			if(prxAPI.getPlayerNextRank(p) == null) {
				  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.rankup-progress-lastrank"), arg0.getName());
			}
			return String.valueOf(prxAPI.getPlayerRankupProgressBarExtended(p));
		}
		if(arg1.equalsIgnoreCase("rankup_name")) {
			if(prxAPI.getPlayerNextRank(p) == null) {
				return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.rankup-lastrank"), arg0.getName());
			}
			return String.valueOf(prxAPI.getPlayerNextRank(p));
		}
		if(arg1.startsWith("rankup_name_")) {
			Player player = Bukkit.getPlayer(arg1.replace("rankup_name_", ""));
			if(player == null) {
				return "null";
			}
			return String.valueOf(prxAPI.getPlayerNextRank(p));
		}
		if(arg1.equalsIgnoreCase("rankup_displayname")) {
			if(prxAPI.getPlayerNextRank(p) == null) {
				return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.rankup-lastrank"), arg0.getName());
			}
			return String.valueOf(prxAPI.getPlayerRankupDisplay(p));
		}
		if((arg1.equalsIgnoreCase("rankup_cost"))) {
			if(prxAPI.getPlayerNextRank(p) == null) {
				return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.rankup-cost-lastrank"), arg0.getName());
			}
			if(prxAPI.isCurrencySymbolBehind()) {
			return  String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol()) + String.valueOf(prxAPI.getPlayerRankupCostWithIncreaseDirect(p));
			} else {
				return String.valueOf(prxAPI.getPlayerRankupCostWithIncreaseDirect(p)) + String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol());
			}
		}
		if((arg1.equalsIgnoreCase("rankup_cost_integer"))) {
			if(prxAPI.getPlayerNextRank(p) == null) {
				return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.rankup-cost-lastrank"), arg0.getName());
			}
			if(prxAPI.isCurrencySymbolBehind()) {
			return  String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol()) + String.valueOf(prxAPI.numberAPI.toFakeInteger(prxAPI.getPlayerRankupCostWithIncreaseDirect(p)));
			} else {
				return String.valueOf(prxAPI.numberAPI.toFakeInteger(prxAPI.getPlayerRankupCostWithIncreaseDirect(p))) + String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol());
			}
		}
		if((arg1.equalsIgnoreCase("rankup_cost_plain"))) {
			if(prxAPI.getPlayerNextRank(p) == null) {
				return "0.0";
			}
           return String.valueOf(prxAPI.numberAPI.deleteScientificNotationA(prxAPI.getPlayerRankupCostWithIncreaseDirect(p)));
		}
		if((arg1.equalsIgnoreCase("rankup_cost_integer_plain"))) {
           return String.valueOf(prxAPI.numberAPI.toFakeInteger(prxAPI.getPlayerRankupCostWithIncreaseDirect(p)));
		}

		if((arg1.equalsIgnoreCase("rankup_cost_formatted"))) {
			if(prxAPI.getPlayerNextRank(p) == null) {
				return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.rankup-cost-lastrank"), arg0.getName());
			}
			if(prxAPI.isCurrencySymbolBehind()) {
			return String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol()) + String.valueOf(prxAPI.getPluginMainClass().formatBalance(prxAPI.getPlayerRankupCostWithIncreaseDirect(p)));
			} else {
				return String.valueOf(prxAPI.getPluginMainClass().formatBalance(prxAPI.getPlayerRankupCostWithIncreaseDirect(p)))  + String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol());
			}
		}
		if(arg1.equalsIgnoreCase("money_nonformatted")) {
			return String.valueOf(prxAPI.getPluginMainClass().econ.getBalance(p));
		}
		if(arg1.equalsIgnoreCase("money_decimalformatted")) {
			DecimalFormat df = new DecimalFormat("###,###.##");
			String finalv = df.format(prxAPI.getPluginMainClass().econ.getBalance(p));
			return finalv;
		}
		if(arg1.startsWith("decimalformat_")) {
			DecimalFormat df = new DecimalFormat("###,###.##");
			try {
			double placeholderToFormat = Double.valueOf(PlaceholderAPI.setBracketPlaceholders(p, prxAPI.numberAPI.keepNumbersWithDots(arg1.replace("decimalformat_", ""))));
			String formatted = df.format(placeholderToFormat);
			return formatted;
			} catch (NumberFormatException nfe) {
				return String.valueOf(PlaceholderAPI.setBracketPlaceholders(p, arg1.replace("decimalformat_", "")));
			}
		}
		if((arg1.equalsIgnoreCase("money"))) {
			return String.valueOf(prxAPI.getPluginMainClass().formatBalance(prxAPI.getPluginMainClass().econ.getBalance(p)));
		}
		if(arg1.equalsIgnoreCase("master_name")) {
			if(!prxAPI.hasMastered(p)) {
			  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.master-notmastered"), arg0.getName());
			}
			return String.valueOf(prxAPI.getPlayerMaster(p));
		}
		if(arg1.startsWith("master_name_")) {
			Player player = Bukkit.getPlayer(arg1.replace("master_name_", ""));
			if(player == null) {
				return "null";
			}
			return String.valueOf(prxAPI.getPlayerMaster(p));
		}
		if(arg1.equalsIgnoreCase("master_displayname")) {
			if(!prxAPI.hasMastered(p)) {
				  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.master-notmastered"), arg0.getName());
				}
			return String.valueOf(prxAPI.getPlayerMasterDisplay(p));
		}
		if(arg1.equalsIgnoreCase("prestige_name")) {
			if(!prxAPI.hasPrestiged(p)) {
			  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.prestige-notprestiged"), arg0.getName());
			}
			return String.valueOf(prxAPI.getPlayerPrestige(p));
		}
		if(arg1.startsWith("prestige_name_")) {
			Player player = Bukkit.getPlayer(arg1.replace("prestige_name_", ""));
			if(player == null) {
				return "null";
			}
			return String.valueOf(prxAPI.getPlayerPrestige(p));
		}
		if(arg1.equalsIgnoreCase("prestige_displayname")) {
			if(!prxAPI.hasPrestiged(p)) {
				  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.prestige-notprestiged"), arg0.getName());
				}
			return String.valueOf(prxAPI.getPlayerPrestigeDisplay(p));
		}
		if(arg1.equalsIgnoreCase("prestige_cost")) {
			if(!prxAPI.hasPrestiged(p)) {
				  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.prestige-notprestiged"), arg0.getName());
				}
			if(prxAPI.isCurrencySymbolBehind()) {
			return String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol()) + String.valueOf(prxAPI.getPlayerPrestigeCost(p));
			} else {
				return String.valueOf(prxAPI.getPlayerPrestigeCost(p)) + String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol());
			}
		}
		if(arg1.equalsIgnoreCase("prestige_cost_plain")) {
			if(!prxAPI.hasPrestiged(p)) {
				return "0.0";
			}
			return String.valueOf(prxAPI.getPlayerPrestigeCost(p));
		}
		if(arg1.equalsIgnoreCase("prestige_cost_integer_plain")) {
			if(!prxAPI.hasPrestiged(p)) {
				return "0";
			}
			return String.valueOf(prxAPI.numberAPI.toFakeInteger(prxAPI.getPlayerPrestigeCost(p)));
		}
		if(arg1.equalsIgnoreCase("prestige_cost_formatted")) {
			if(!prxAPI.hasPrestiged(p)) {
				  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.prestige-notprestiged"), arg0.getName());
				}
			if(prxAPI.isCurrencySymbolBehind()) {
			return String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol()) + String.valueOf(prxAPI.getPlayerPrestigeCostFormatted(p));
			} else {
				return String.valueOf(prxAPI.getPlayerPrestigeCostFormatted(p)) + String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol());
			}
		}
		if(arg1.equalsIgnoreCase("nextmaster_name")) {
			if(!prxAPI.hasMastered(p)) {
				return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.nextmaster-notmastered"), arg0.getName());
			}
			if(!prxAPI.hasNextMaster(p)) {
				  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.master-lastmaster"), arg0.getName());
			}
			return String.valueOf(prxAPI.getPlayerNextMaster(p));
		}
		if(arg1.startsWith("nextmaster_name_")) {
			Player player = Bukkit.getPlayer(arg1.replace("nextmaster_name_", ""));
			if(player == null) {
				return "null";
			}
			return String.valueOf(prxAPI.getPlayerNextMaster(p));
		}
		if(arg1.equalsIgnoreCase("nextmaster_displayname")) {
			if(!prxAPI.hasMastered(p)) {
				return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.nextmaster-notmastered"), arg0.getName());
			}
			if(!prxAPI.hasNextMaster(p)) {
				  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.master-lastmaster"), arg0.getName());
			}
			return String.valueOf(prxAPI.getPlayerNextMasterDisplay(p));
		}
		if(arg1.equalsIgnoreCase("nextprestige_name")) {
			if(!prxAPI.hasPrestiged(p)) {
				return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.nextprestige-notprestiged"), arg0.getName());
			}
			if(!prxAPI.hasNextPrestige(p)) {
				  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.prestige-lastprestige"), arg0.getName());
			}
			return String.valueOf(prxAPI.getPlayerNextPrestige(p));
		}
		if(arg1.startsWith("nextprestige_name_")) {
			Player player = Bukkit.getPlayer(arg1.replace("nextprestige_name_", ""));
			if(player == null) {
				return "null";
			}
			return String.valueOf(prxAPI.getPlayerNextPrestige(p));
		}
		if(arg1.equalsIgnoreCase("nextprestige_displayname")) {
			if(!prxAPI.hasPrestiged(p)) {
				return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.nextprestige-notprestiged"), arg0.getName());
			}
			if(!prxAPI.hasNextPrestige(p)) {
				  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.prestige-lastprestige"), arg0.getName());
			}
			return String.valueOf(prxAPI.getPlayerNextPrestigeDisplay(p));
		}
		if(arg1.equalsIgnoreCase("nextprestige_cost")) {
			if(!prxAPI.hasNextPrestige(p)) {
				  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.prestige-lastprestige"), arg0.getName());
			}
			if(prxAPI.isCurrencySymbolBehind()) {
			return String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol()) + String.valueOf(prxAPI.getPlayerNextPrestigeCostWithIncreaseDirect(p)); 
			} else {
				return String.valueOf(prxAPI.getPlayerNextPrestigeCostWithIncreaseDirect(p) + String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol())); 
			}
		}
		if(arg1.equalsIgnoreCase("nextprestige_cost_integer")) {
			if(!prxAPI.hasNextPrestige(p)) {
				  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.prestige-lastprestige"), arg0.getName());
			}
			if(prxAPI.isCurrencySymbolBehind()) {
			return String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol()) + String.valueOf(prxAPI.numberAPI.toFakeInteger(prxAPI.getPlayerNextPrestigeCostWithIncreaseDirect(p))); 
			} else {
				return String.valueOf(prxAPI.numberAPI.toFakeInteger(prxAPI.getPlayerNextPrestigeCostWithIncreaseDirect(p)) + String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol())); 
			}
		}
		if(arg1.equalsIgnoreCase("nextprestige_cost_plain")) {
			return String.valueOf(prxAPI.numberAPI.deleteScientificNotationA(prxAPI.getPlayerNextPrestigeCostWithIncreaseDirect(p)));
		}
		if(arg1.equalsIgnoreCase("nextprestige_cost_integer_plain")) {
			return String.valueOf(prxAPI.numberAPI.toFakeInteger(prxAPI.getPlayerNextPrestigeCostWithIncreaseDirect(p)));
		}
		if(arg1.equalsIgnoreCase("nextmaster_cost")) {
			if(!prxAPI.hasNextMaster(p)) {
				return main.getString(main.globalStorage.getStringData("PlaceholderAPI.master-lastmaster"), arg0.getName());
			}
			if(prxAPI.isCurrencySymbolBehind()) {
				return String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol()) + String.valueOf(prxAPI.getPlayerNextMasterCost(p));
			} else {
				return String.valueOf(prxAPI.getPlayerNextMasterCost(p)) + String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol());
			}
		}
		if(arg1.equalsIgnoreCase("nextmaster_cost_integer")) {
			if(!prxAPI.hasNextMaster(p)) {
				return main.getString(main.globalStorage.getStringData("PlaceholderAPI.master-lastmaster"), arg0.getName());
			}
			if(prxAPI.isCurrencySymbolBehind()) {
				return String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol()) + String.valueOf(prxAPI.numberAPI.toFakeInteger(prxAPI.getPlayerNextMasterCost(p)));
			} else {
				return String.valueOf(prxAPI.numberAPI.toFakeInteger(prxAPI.getPlayerNextMasterCost(p))) + String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol());
			}
		}
		if(arg1.equalsIgnoreCase("nextmaster_cost_plain")) {
			if(!prxAPI.hasNextMaster(p)) {
				return "0.0";
			}
			return String.valueOf(prxAPI.numberAPI.deleteScientificNotationA(prxAPI.getPlayerNextMasterCost(p)));
		}
		if(arg1.equalsIgnoreCase("nextmaster_cost_integer_plain")) {
			if(!prxAPI.hasNextMaster(p)) {
				return "0";
			}
			return String.valueOf(prxAPI.numberAPI.toFakeInteger(prxAPI.getPlayerNextMasterCost(p)));
		}
		if(arg1.equalsIgnoreCase("nextprestige_cost_formatted")) {
			if(!prxAPI.hasNextPrestige(p)) {
				  return main.getString(prxAPI.main.globalStorage.getStringData("PlaceholderAPI.prestige-lastprestige"), arg0.getName());
			}
			if(prxAPI.isCurrencySymbolBehind()) {
			return String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol()) + String.valueOf(main.formatBalance(prxAPI.getPlayerNextPrestigeCostWithIncreaseDirect(p)));
			} else {
				return String.valueOf(main.formatBalance(prxAPI.getPlayerNextPrestigeCostWithIncreaseDirect(p))) + String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol());
			}
		}
		if(arg1.equalsIgnoreCase("nextmaster_cost_formatted")) {
			if(!prxAPI.hasNextMaster(p)) {
				return main.getString(main.globalStorage.getStringData("PlaceholderAPI.master-lastmaster"), arg0.getName());
			}
			if(prxAPI.isCurrencySymbolBehind()) {
				return String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol()) + String.valueOf(main.formatBalance(prxAPI.getPlayerNextMasterCost(p)));
			} else {
				return String.valueOf(main.formatBalance(prxAPI.getPlayerNextMasterCost(p))) + String.valueOf(prxAPI.getPlaceholderAPICurrencySymbol());
			}
		}
		if(arg1.equalsIgnoreCase("can_prestige")) {
			return String.valueOf(prxAPI.canPrestige(p.getPlayer()));
		}
		if(arg1.equalsIgnoreCase("can_master")) {
			return String.valueOf(prxAPI.canMaster(p.getPlayer()));
		}
		if(arg1.startsWith("rank_displayname_")) {
			String rank = arg1.split("_")[2];
			RankPath rp = new RankPath(rank, prxAPI.getDefaultPath());
			if(!prxAPI.rankPathExists(rp)) {
				return rank + " is not available";
			}
			return prxAPI.getRankDisplay(rp);
		}
		if(arg1.startsWith("rank_cost_integer_")) {
			String rank = arg1.split("_")[3];
			RankPath rp = new RankPath(rank, prxAPI.getDefaultPath());
			if(!prxAPI.rankPathExists(rp)) {
				return rank + " is not available";
			}
			String prestige = prxAPI.getPlayerPrestige(p);
			String master = prxAPI.getPlayerMaster(p);
			double rankCost = prxAPI.getRankCost(rp);
			double increasedRankCost = prxAPI.getIncreasedRankupCostX(master, prestige, rankCost);
			return String.valueOf(prxAPI.numberAPI.toFakeInteger(increasedRankCost));
		}
		if(arg1.startsWith("rank_cost_")) {
			String rank = arg1.split("_")[2];
			RankPath rp = new RankPath(rank, prxAPI.getDefaultPath());
			if(!prxAPI.rankPathExists(rp)) {
				return rank + " is not available";
			}
			String prestige = prxAPI.getPlayerPrestige(p);
			String master = prxAPI.getPlayerMaster(p);
			double rankCost = prxAPI.getRankCost(rp);
			String increasedRankCost = String.valueOf(prxAPI.getIncreasedRankupCostX(master, prestige, rankCost));
			return String.valueOf(increasedRankCost);
		}
		if(arg1.startsWith("rank_costformatted_")) {
			String rank = arg1.split("_")[2];
			RankPath rp = new RankPath(rank, prxAPI.getDefaultPath());
			if(!prxAPI.rankPathExists(rp)) {
				return rank + " is not available";
			}
			String prestige = prxAPI.getPlayerPrestige(p);
			String master = prxAPI.getPlayerMaster(p);
			double rankCost = prxAPI.getRankCost(rp);
			double increasedRankCost = prxAPI.getIncreasedRankupCostX(master, prestige, rankCost);
			return String.valueOf(prxAPI.formatBalance(increasedRankCost));
		}
		if(arg1.startsWith("prestige_displayname_")) {
			String prestige = arg1.split("_")[2];
			return String.valueOf(prxAPI.getPrestigeDisplay(prestige));
		}
		if(arg1.startsWith("prestige_cost_integer_")) {
			String prestige = arg1.split("_")[3];
			String master = prxAPI.getPlayerMaster(p);
			double prestigeCost = prxAPI.getPrestigeCost(prestige);
			double increasedPrestigeCost = prxAPI.getIncreasedPrestigeCost(master, prestigeCost);
			return String.valueOf(prxAPI.numberAPI.toFakeInteger(increasedPrestigeCost));
		}
		if(arg1.startsWith("prestige_cost_")) {
			String prestige = arg1.split("_")[2];
			String master = prxAPI.getPlayerMaster(p);
			double prestigeCost = prxAPI.getPrestigeCost(prestige);
			double increasedPrestigeCost = prxAPI.getIncreasedPrestigeCost(master, prestigeCost);
			return String.valueOf(increasedPrestigeCost);
		}
		if(arg1.startsWith("prestige_costformatted_")) {
			String prestige = arg1.split("_")[2];
			String master = prxAPI.getPlayerMaster(p);
			double prestigeCost = prxAPI.getPrestigeCost(prestige);
			double increasedPrestigeCost = prxAPI.getIncreasedPrestigeCost(master, prestigeCost);
			return String.valueOf(prxAPI.formatBalance(increasedPrestigeCost));
		}
		if(arg1.startsWith("master_displayname_")) {
			String master = arg1.split("_")[2];
			return prxAPI.getMasterDisplay(master);
		}
		if(arg1.startsWith("master_cost_integer_")) {
			String master = arg1.split("_")[3];
			return String.valueOf(prxAPI.numberAPI.toFakeInteger(prxAPI.getMasterCost(master)));
		}
		if(arg1.startsWith("master_cost_")) {
			String master = arg1.split("_")[2];
			return String.valueOf(prxAPI.getMasterCost(master));
		}
		if(arg1.startsWith("master_costformatted_")) {
			String master = arg1.split("_")[2];
			return String.valueOf(prxAPI.getMasterCostFormatted(master));
		}
		if(arg1.startsWith("name_prestige_")) {
			int position = Integer.valueOf(arg1.split("_")[2]);
			return String.valueOf(lbm.getPlayerNameFromPositionPrestige(position, getNullNamePrestige()));
		}
		if(arg1.startsWith("value_prestige_")) {
			int position = Integer.valueOf(arg1.split("_")[2]);
			return String.valueOf(lbm.getPlayerPrestigeFromPosition(position, getNullValuePrestige()));
		}
		if(arg1.startsWith("valuedisplay_prestige_")) {
			int position = Integer.valueOf(arg1.split("_")[2]);
			return String.valueOf(lbm.getPlayerPrestigeDisplayNameFromPosition(position, getNullValuePrestige()));
		}
		if(arg1.startsWith("name_master_")) {
			int position = Integer.valueOf(arg1.split("_")[2]);
			return String.valueOf(lbm.getPlayerNameFromPositionMaster(position, getNullNameMaster()));
		}
		if(arg1.startsWith("value_master_")) {
			int position = Integer.valueOf(arg1.split("_")[2]);
			return String.valueOf(lbm.getPlayerMasterFromPosition(position, getNullValueMaster()));
		}
		if(arg1.startsWith("valuedisplay_master_")) {
			int position = Integer.valueOf(arg1.split("_")[2]);
			return String.valueOf(lbm.getPlayerMasterDisplayNameFromPosition(position, getNullValueMaster()));
		}
		if(arg1.startsWith("name_rank_")) {
			int position = Integer.valueOf(arg1.split("_")[2]);
			return String.valueOf(lbm.getPlayerNameFromPositionRank(position, getNullNameRank()));
		}
		if(arg1.startsWith("value_rank_")) {
			int position = Integer.valueOf(arg1.split("_")[2]);
			return String.valueOf(lbm.getPlayerRankFromPosition(position, getNullValueRank()));
		}
		if(arg1.startsWith("valuedisplay_rank_")) {
			int position = Integer.valueOf(arg1.split("_")[2]);
			return String.valueOf(lbm.getPlayerRankDisplayNameFromPosition(position, getNullValueRank()));
		}
		if(arg1.startsWith("name_stage_")) {
			int position = Integer.valueOf(arg1.split("_")[2]);
            return String.valueOf(lbm.getPlayerNameFromPositionGlobal(position, getNullNameRank()));
		}
		if(arg1.startsWith("value_stage_")) {
			int position = Integer.valueOf(arg1.split("_")[2]);
			return String.valueOf(lbm.getPlayerStageFromPosition(position, getNullValueRank()));
		}
		if(arg1.startsWith("valuedisplay_stage_")) {
			int position = Integer.valueOf(arg1.split("_")[2]);
			return String.valueOf(lbm.getPlayerStageDisplayNameFromPosition(position, getNullValueRank()));
		}
		if(arg1.startsWith("prestigemaster_stage_")) {
			int position = Integer.valueOf(arg1.replace("prestigemaster_stage_", ""));
			Entry<UUID, Integer> playerPosition = lbm.getPlayerFromPositionGlobal(position);
			String display = "";
			if(playerPosition == null) {
				display = "";
			} else {
				display = prxAPI.getPrestigeAndMasterDisplay(playerPosition.getKey(), " ");
			}
			return String.valueOf(display);
		}
		if(arg1.startsWith("has_prestiged")) {
			return String.valueOf(prxAPI.hasPrestiged(p));
		}
		if(arg1.startsWith("has_mastered")) {
			return String.valueOf(prxAPI.hasMastered(p));
		}
		if((arg0 == null)) {
			return null;
		}
		return null;
		
	}

	@Override
	public String getAuthor() {
		return CollectionUtils.collectionToString(main.getDescription().getAuthors(), ", ");
	}

	@Override
	public String getIdentifier() {
		return "HexiaRanks";
	}

	@Override
	public String getVersion() {
		return main.getDescription().getVersion();
	}
 
}

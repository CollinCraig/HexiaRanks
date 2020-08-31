package me.hexiaranks.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.hexiaranks.HexiaRanks;
import net.md_5.bungee.api.ChatColor;

public class ChatColorReplacer1_16 implements ChatColorReplacer {

	private HexiaRanks main;
	public ChatColorReplacer1_16(HexiaRanks main) {
		this.main = main;
	}
	
	private String c(String textToTranslate) {
		return ChatColor.translateAlternateColorCodes('&', textToTranslate);
	}
	
	@Override
	public String parsePlaceholders(String message) {
		return main.getPlaceholderReplacer().parseCached(c(message));
	}
	@Override
	public String parsePlaceholders(String message, Player player) {
		return main.getPlaceholderReplacer().parse(c(message), player);
	}
	@Override
	public String parsePlaceholders(String message, OfflinePlayer offlinePlayer) {
		return main.getPlaceholderReplacer().parse(c(message), offlinePlayer);
	}
	@Override
	public String parsePlaceholders(String message, String playerName) {
		return main.getPlaceholderReplacer().parse(c(message), Bukkit.getPlayer(playerName));
	}
	
	
}

package me.hexiaranks.hooks;
import java.util.Arrays;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import me.hexiaranks.HexiaRanks;
 
public class GMHook {
	private HexiaRanks plugin;

	public GMHook(final HexiaRanks plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(final PluginEnableEvent event) {
		final PluginManager pluginManager = plugin.getServer().getPluginManager();

	}
}
package me.hexiaranks.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.hexiaranks.data.PrestigeDataHandler;

public class PrePrestigeMaxEvent extends Event implements Cancellable {

	private boolean isCancelled;
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private PrestigeDataHandler prestige;
	
	public PrePrestigeMaxEvent(Player player, PrestigeDataHandler prestige) {
		this.player = player;
		this.prestige = prestige;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
	}

	public Player getPlayer() {
		return this.player;
	}
	
	public PrestigeDataHandler getPrestige() {
		return this.prestige;
	}
	
	public String getPrestigeName() {
		return this.prestige.getName();
	}
	
}

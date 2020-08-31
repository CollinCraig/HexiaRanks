package me.hexiaranks.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class XMasterEvent extends Event implements Cancellable {
    private Player player;
    private String masterReason;
    private boolean isCancelled;
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
     
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public XMasterEvent(Player player, String masterReason) {
    	this.player = player;
    	this.masterReason = masterReason;
    	this.isCancelled = false;
    	
    }
	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return this.isCancelled;
	}
	@Override
	public void setCancelled(boolean cancel) {
		// TODO Auto-generated method stub
		this.isCancelled = cancel;
	};
	public Player getPlayer() {
		return this.player;
		
	}
	/**
	 * master reasons strings:
	 * - REBIRTHUP
	 * - REBIRTHUP_BY_OTHER
	 * - SETREBIRTH
	 * - RESETREBIRTH
	 * - DELREBIRTH
	 */
	public String getMasterReason() {
		return this.masterReason;
	}
}

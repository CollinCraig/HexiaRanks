package me.hexiaranks.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class XMasterUpdateEvent extends Event implements Cancellable {
    private Player player;
    private MasterUpdateCause cause;
    private boolean isCancelled;
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
     
    public static HandlerList getHandlerList() {
        return handlers;
    }
    public XMasterUpdateEvent(Player player, MasterUpdateCause cause) {
    	this.player = player;
    	this.cause = cause;
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
	
	public MasterUpdateCause getCause() {
		return this.cause;
	}
}

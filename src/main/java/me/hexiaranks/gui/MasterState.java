package me.hexiaranks.gui;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class MasterState {
	
	private String master;
	private LevelState levelState;
	
	public MasterState() {}
	
	public MasterState(String master) {
		this.master = master;
	}
	
	public void setMaster(String master) {
		this.master = master;
	}
	
	public String getMaster() {
		return this.master;
	}
	
	public void setLevelState(LevelState levelState) {
		this.levelState = levelState;
	}
	
	public LevelState getLevelState() {
		return this.levelState;
	}
	
    @Override
    public int hashCode() {
        return new HashCodeBuilder(18, 31).
            append(master).
            append(levelState).
            toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof MasterState))
            return false;
        if (obj == this)
            return true;

        MasterState rhs = (MasterState) obj;
        return new EqualsBuilder().
            append(master, rhs.master).
            append(levelState, rhs.levelState).
            isEquals();
    }
    
	public String toString() {
		return "[path:" + master + "]||[state:" + levelState.name() + "]";
	}
}

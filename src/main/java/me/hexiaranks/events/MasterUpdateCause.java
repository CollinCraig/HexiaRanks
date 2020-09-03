package me.hexiaranks.events;

public enum MasterUpdateCause {MASTERUP, SETMASTER, DELMASTER, OTHER;

	private static MasterUpdateCause cause;
	
	public static void setMasterUpdateCause(MasterUpdateCause cause) {
		MasterUpdateCause.cause = cause;
	}
	
	public MasterUpdateCause getMasterUpdateCause() {
		return cause;
	}
}

package me.hexiaranks.data;

public enum LevelType {
   RANK,PRESTIGE,MASTER,OTHER,UNKNOWN;
   
   private static LevelType levelType = UNKNOWN;
	
   public void setLevelType(LevelType levelType) {
	   LevelType.levelType = levelType;
   }
   
   public LevelType getLevelType() {
	   return levelType;
   }
   
}


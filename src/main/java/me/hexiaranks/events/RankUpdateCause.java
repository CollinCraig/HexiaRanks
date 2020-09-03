package me.hexiaranks.events;


/**
 * 
 * @enum RANKUPMAX is before the rankupmax process
 *
 */
	public enum RankUpdateCause{NORMAL_RANKUP, FORCE_RANKUP, NORMAL_RANKUPMAX, NORMAL_RANKUPBY, RANKSET, RANKSET_BYPRESTIGE, RANKSET_BYMASTER, RANKSET_BYCONVERT
		, RANKUPMAX, OTHER;
		
	private static RankUpdateCause rankUpdateCause;
	

	public static void setRankUpdateCause(RankUpdateCause rankUpdateCause)
	{
		RankUpdateCause.rankUpdateCause = rankUpdateCause;
	}

	public static RankUpdateCause getRankUpdateCause()
	{
			return rankUpdateCause;
	}

	
}

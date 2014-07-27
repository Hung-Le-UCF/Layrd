package com.Voltronics.game;

public interface LayrdGoogleGameInterface
{
	public boolean getSignedInGPGS();
	public void loginGPGS();
	public void submiteScoreGPGS(int score);
	public void getLeaderboardGPGS();
	public void getAchievementsGPGS();
	public void logOutGPGS();
	public void unlockAchievementGPGS(String achievementId);

}

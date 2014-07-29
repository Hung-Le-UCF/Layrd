package com.Voltronics.game.android;

import android.content.Intent;
import android.os.Bundle;

import com.Voltronics.game.LayrdGame;
import com.Voltronics.game.LayrdGoogleGameInterface;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;


public class AndroidLauncher extends AndroidApplication implements LayrdGoogleGameInterface{
	
	GameHelper mHelper;
	GoogleApiClient mClient;
	
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
	    mHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		initialize(new LayrdGame(this), config);
	
	    // enable debug logs (if applicable)
	    GameHelper.GameHelperListener listener = new GameHelper.GameHelperListener() {
	        @Override
	        public void onSignInSucceeded() 
	        {
	            // handle sign-in succeess
	        	//unlockAchievementGPGS("CgkI78movo8SEAIQAQ");

	        }
	        @Override
	        public void onSignInFailed() {
	            // handle sign-in failure (e.g. show Sign In button)
	        }

	    };
	    mHelper.setup(listener);
	}
	
	@Override
	protected void onStart() {
	    super.onStart();
	   mHelper.onStart(this);
	}

	@Override
	protected void onStop() {
	    super.onStop();
	    mHelper.onStop();
	}

	@Override
	protected void onActivityResult(int request, int response, Intent data) {
	    super.onActivityResult(request, response, data);
	    mHelper.onActivityResult(request, response, data);
	}

	@Override
	public boolean getSignedInGPGS() {
		// TODO Auto-generated method stub
		return mHelper.isSignedIn();
	}

	@Override
	public void loginGPGS() {
		// TODO Auto-generated method stub
		try{
			runOnUiThread(new Runnable(){
				public void run(){
					mHelper.beginUserInitiatedSignIn();
				}
			});
			
		}catch (final Exception ex)
		{
			
		}
		
	}

	@Override
	public void submiteScoreGPGS(int score) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getLeaderboardGPGS() {
		// TODO Auto-generated method stub
		startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mHelper.getApiClient(), "CgkI5qzL-98HEAIQBQ"), 10);
		
	}

	@Override
	public void getAchievementsGPGS() {
		// TODO Auto-generated method stub
		
		startActivityForResult(Games.Achievements.getAchievementsIntent(mHelper.getApiClient()), 1);
		
	}

	@Override
	public void logOutGPGS() {
		// TODO Auto-generated method stub
		mHelper.signOut();
		
	}
	
	@Override
	public void unlockAchievementGPGS(String achievementId) {
		Games.Achievements.unlock(mHelper.getApiClient(), achievementId);
	}
}

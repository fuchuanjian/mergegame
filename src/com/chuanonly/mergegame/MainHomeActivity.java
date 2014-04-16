package com.chuanonly.mergegame;


import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.InterstitialAd;
import com.google.ads.AdRequest.ErrorCode;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainHomeActivity extends Activity {

	public MainHomeActivity() {
		mainActivity = this;
	}
	 private InterstitialAd interstitial;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		interstitial = new InterstitialAd(this, "a1534d6f6acb6ed");
		 AdRequest adRequest = new AdRequest();
		 interstitial.loadAd(adRequest);
		 interstitial.setAdListener(new AdListener() {
			@Override
			public void onReceiveAd(Ad arg0)
			{
				if (arg0 == interstitial) {
				      interstitial.show();
				    }
			}
			
			@Override
			public void onPresentScreen(Ad arg0)
			{
			}
			
			@Override
			public void onLeaveApplication(Ad arg0)
			{
			}
			
			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1)
			{
			}
			
			@Override
			public void onDismissScreen(Ad arg0)
			{
			}
		});
//		root = (RelativeLayout) findViewById(R.id.container);
//		root.setBackgroundColor(0xfffaf8ef);

		tvScore = (TextView) findViewById(R.id.tvScore);
		tvBestScore = (TextView) findViewById(R.id.tvBestScore);

		gameView = (GameView) findViewById(R.id.gameView);

		btnNewGame = (Button) findViewById(R.id.btnNewGame);
		btnNewGame.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
			gameView.startGame();
		}});
		
		animLayer = (AnimLayer) findViewById(R.id.animLayer);
	}

	public void clearScore(){
		score = 0;
		showScore();
	}

	public void showScore(){
		tvScore.setText(score+"");
	}

	public void addScore(int s){
		score+=s;
		showScore();

		int maxScore = Math.max(score, getBestScore());
		saveBestScore(maxScore);
		showBestScore(maxScore);
	}

	public void saveBestScore(int s){
		Editor e = getPreferences(MODE_PRIVATE).edit();
		e.putInt(SP_KEY_BEST_SCORE, s);
		e.commit();
	}

	public int getBestScore(){
		return getPreferences(MODE_PRIVATE).getInt(SP_KEY_BEST_SCORE, 0);
	}

	public void showBestScore(int s){
		tvBestScore.setText(s+"");
	}
	
	public AnimLayer getAnimLayer() {
		return animLayer;
	}

	private int score = 0;
	private TextView tvScore,tvBestScore;
	private Button btnNewGame;
	private GameView gameView;
	private AnimLayer animLayer = null;

	private static MainHomeActivity mainActivity = null;

	public static MainHomeActivity getMainActivity() {
		return mainActivity;
	}

	public static final String SP_KEY_BEST_SCORE = "bestScore";

}

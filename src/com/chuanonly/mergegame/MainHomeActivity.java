package com.chuanonly.mergegame;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainHomeActivity extends Activity {
	public static int GAME_MODE = 2;
	public static final int[] SCORE= {1024, 1024, 2048, 4096, Integer.MAX_VALUE};
	private SoundPlayHelper mSoundPlayHelper;
	private ImageView mSoundImg;
	private ImageView mResultImg;
	private TextView mUndoTV;
	private LinearLayout mADLayout;
	private AdView mAdView;
	private InterstitialAd mInterstitialAd;
	private ImageView mArrow;
	public MainHomeActivity() {
		mainActivity = this;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
//		root = (RelativeLayout) findViewById(R.id.container);
//		root.setBackgroundColor(0xfffaf8ef);
		tvScore = (TextView) findViewById(R.id.tvScore);
		tvBestScore = (TextView) findViewById(R.id.tvBestScore);

		gameView = (GameView) findViewById(R.id.gameView);

		btnNewGame = (TextView) findViewById(R.id.ok);
		btnNewGame.setOnClickListener(mClick);
		findViewById(R.id.icon_help).setOnClickListener(mClick);
		findViewById(R.id.icon_setting).setOnClickListener(mClick);
		mSoundImg = (ImageView) findViewById(R.id.icon_sound);
		mSoundImg.setOnClickListener(mClick);
		mUndoTV = (TextView) findViewById(R.id.undo);
		mUndoTV.setOnClickListener(mClick);
		mArrow = (ImageView) findViewById(R.id.arrow);
		mArrow.setOnClickListener(mClick);
		
		mResultImg = (ImageView) findViewById(R.id.result_img);
		animLayer = (AnimLayer) findViewById(R.id.animLayer);
		int mode = Util.getIntFromSharedPref("mode", 2);
		setCheckbox(mode);
		mSoundPlayHelper = new SoundPlayHelper();
		mSoundPlayHelper.initSounds(this);
		mSoundPlayHelper.loadSfx(this, R.raw.button, SOUND_BOTTON);
		
		//ad
		mADLayout = (LinearLayout) findViewById(R.id.layout_ad);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		gameView.getSaveRecord();
		int sound = Util.getIntFromSharedPref("Sound", 1);
		if (sound == 1)
		{
			playBgMusic();
			mSoundImg.setImageResource(R.drawable.sound_on);
		}else
		{
			mSoundImg.setImageResource(R.drawable.sound_off);
		}
		checkShowAd();
	}
	private void checkShowAd() {
		try {
			if (!Util.isNetworkAvailable(getApplicationContext()))
			{
				mArrow.setVisibility(View.GONE);
				mAdView.setVisibility(View.GONE);
				return;
			}
			
			int loginCnt = Util.getIntFromSharedPref(Util.LOG_INT_CNT, 0);
			if (loginCnt >= 2 && Util.isNetworkAvailable(getApplicationContext()))
			{			
				if (mAdView == null)
				{
					mAdView = new AdView(this);
					mAdView.setAdUnitId("a1534d6f6acb6ed");
					mAdView.setAdSize(AdSize.BANNER);
					mADLayout.addView(mAdView);
					AdRequest adRequest = new AdRequest.Builder().build();
					mAdView.loadAd(adRequest);
					mArrow.setVisibility(View.GONE);
					mAdView.setVisibility(View.GONE);
					mAdView.setAdListener(new AdListener() {
						@Override
						public void onAdLoaded() {
							super.onAdLoaded();
							isLoadad = true;
							mArrow.setVisibility(View.VISIBLE);
							mAdView.setVisibility(View.VISIBLE);
						}
						@Override
						public void onAdClosed() {
							mAdView.setVisibility(View.GONE);
							mArrow.setVisibility(View.GONE);
							super.onAdClosed();
						}
					});
				}else
				{
					if (isLoadad)
					{						
						mArrow.setVisibility(View.VISIBLE);
						mAdView.setVisibility(View.VISIBLE);
					}
				}
			}else
			{
				mArrow.setVisibility(View.GONE);
				if (mAdView != null)
				{				
					mAdView.setVisibility(View.GONE);
				}
			}
			Util.setIntToSharedPref(Util.LOG_INT_CNT, loginCnt+1);
			
			if ( loginCnt % 5 == 4)
			{
				if (mInterstitialAd == null)
				{
					mInterstitialAd = new InterstitialAd(this);
					mInterstitialAd.setAdUnitId("a1534d6f6acb6ed");
				}
				AdRequest adRequest = new AdRequest.Builder().build();
				mInterstitialAd.loadAd(adRequest);
				mInterstitialAd.setAdListener(new AdListener() {
					@Override
					public void onAdLoaded() {
						super.onAdLoaded();
						if (mInterstitialAd.isLoaded())
						{
							mInterstitialAd.show();
						}
					}
				});
				
			}
		} catch (Exception e) {
		}
		
	}
	@Override
	protected void onPause() {
    	gameView.saveRecord();
		super.onPause();
		mSoundPlayHelper.stopBGMusic();
	}
	private boolean isLoadad = false;
	private OnClickListener mClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.ok)
			{
				playSound(SOUND_BOTTON);
				gameView.startGame();
				mResultImg.setVisibility(View.INVISIBLE);
				checkShowAd();
			}else if (v.getId() == R.id.undo)
			{
				playSound(SOUND_BOTTON);
				gameView.undo();
			}else if(v.getId()== R.id.arrow)
			{
				mArrow.setVisibility(View.GONE);
				mAdView.setVisibility(View.GONE);
			}
			else if (v.getId() == R.id.icon_setting)
			{
				showPopupWindow();
			}else if (v.getId() == R.id.icon_sound)
			{
				int sound = Util.getIntFromSharedPref("Sound", 1);
				if (sound == 1)
				{
					Util.setIntToSharedPref("Sound", 0);
					mSoundImg.setImageResource(R.drawable.sound_off);
					stopBgMusic();
				}else
				{
					Util.setIntToSharedPref("Sound", 1);
					mSoundImg.setImageResource(R.drawable.sound_on);
					playBgMusic();
				}
			}
			
		}
	};
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
		hignScore = maxScore;
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

	public int score = 0;
	public int hignScore = 0;
	private TextView tvScore,tvBestScore;
	private TextView btnNewGame;
	private GameView gameView;
	private AnimLayer animLayer = null;

	private static MainHomeActivity mainActivity = null;

	public static MainHomeActivity getMainActivity() {
		return mainActivity;
	}

	public static final String SP_KEY_BEST_SCORE = "bestScore";
	private PopupWindow mPopupWindowMenu;
	// 初始化popupwindow
    private void showPopupWindow() {
    	if (mPopupWindowMenu == null)
    	{    		
    		View view = getLayoutInflater().inflate(R.layout.popup_window_layout, null, false);
    		mPopupWindowMenu = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
    		mPopupWindowMenu.setBackgroundDrawable(new ColorDrawable(0));// 点击窗口外消失
    		mPopupWindowMenu.setFocusable(true);
    		mPopupWindowMenu.setTouchable(true);
    		mPopupWindowMenu.setOutsideTouchable(true);// 点击窗口外消失,需要设置背景、焦点、touchable、update\
    		mPopupWindowMenu.update();
    		// 设置进入退出动画
    		mPopupWindowMenu.setAnimationStyle(R.style.popwindow_anim_style);
    		view.findViewById(R.id.layout1).setOnClickListener(menuClick);
    		view.findViewById(R.id.layout2).setOnClickListener(menuClick);
    		view.findViewById(R.id.layout3).setOnClickListener(menuClick);
    		view.findViewById(R.id.layout4).setOnClickListener(menuClick);
    	}
    	mPopupWindowMenu.showAsDropDown(findViewById(R.id.icon_setting), 0, 0);
    	int mode = Util.getIntFromSharedPref("mode", 2);
    	setCheckbox(mode);
    }
    private void setCheckbox(int index)
    {
    	GAME_MODE = index;
    	if (mPopupWindowMenu != null)
    	{    		
    		for (int i = 1; i <= 4; i++)
    		{
    			int id = getResources().getIdentifier("checkbox_" + i, "id",
    					getPackageName());
    			CheckBox checkBox =	(CheckBox) mPopupWindowMenu.getContentView().findViewById(id);
    			if (index == i)
    			{
    				checkBox.setChecked(true);
    				
    			}else
    			{
    				checkBox.setChecked(false);
    			}
    		}
    	}
    	int imgid = getResources().getIdentifier("mode" + index, "drawable",
				getPackageName());
    	((ImageView)findViewById(R.id.mode_img)).setImageResource(imgid);
    	
    }
    private OnClickListener menuClick = new OnClickListener() {
    	
    	@Override
    	public void onClick(View v) {
    		if (v.getId() == R.id.layout1)
    		{
    			Util.setIntToSharedPref("mode", 1);
    			setCheckbox(1);
    		}else if (v.getId() == R.id.layout2)
    		{
    			Util.setIntToSharedPref("mode", 2);
    			setCheckbox(2);
    		}else if (v.getId() == R.id.layout3)
    		{
    			Util.setIntToSharedPref("mode", 3);
    			setCheckbox(3);
    		}else if (v.getId() == R.id.layout4)
    		{
    			Util.setIntToSharedPref("mode", 4);
    			setCheckbox(4);
    		}
    	}
    };
    protected void onDestroy() 
    {
    	mSoundPlayHelper.release();
    	super.onDestroy();
    };
    public static final int  SOUND_BOTTON = 1;
    public void playBgMusic()
    {
    	int sound = Util.getIntFromSharedPref("Sound", 1);
    	if (sound == 1)
    	{    		
    		mSoundPlayHelper.playBGMusic();
    	}
    }
    public void stopBgMusic()
    {
    	mSoundPlayHelper.stopBGMusic();
    }
    public void playSound(int id)
    {
    	int sound = Util.getIntFromSharedPref("Sound", 1);
    	if (sound == 1)
    	{    		
    		mSoundPlayHelper.play(id, 0);
    	}
    }
	public  void winGame() {
		mResultImg.setImageResource(R.drawable.win);
		mResultImg.setVisibility(View.VISIBLE);
		setUndoBtnEnable(false);
		checkShowAd();
	}
	public void loseGame() {
		mResultImg.setImageResource(R.drawable.lose);
		mResultImg.setVisibility(View.VISIBLE);
		setUndoBtnEnable(false);
		checkShowAd();
	}
	public void undo(int undoScore) {
		score = undoScore;
		showScore();
	}
	private long lastPressback = 0;

	@Override
	public void onBackPressed()
	{
		if (lastPressback + 3000 < System.currentTimeMillis())
		{
			lastPressback = System.currentTimeMillis();
			Util.showToast(APP.getContext().getString(
					R.string.toast_exit));
		} else
		{
			super.onBackPressed();
		}
	}
	public void setUndoBtnEnable(boolean canClick) {
		mUndoTV.setClickable(canClick);
		if (canClick)
		{
			mUndoTV.setTextColor(0Xffffffff);
		}else
		{
			mUndoTV.setTextColor(0X30ffffff);
		}
	}
}

package com.chuanonly.mergegame;
import java.io.IOException;
import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;

public class SoundPlayHelper {

	// 音效的音量
	int streamVolume;

	// 定义SoundPool 对象
	private SoundPool soundPool;
	private MediaPlayer mMediaPlayer;
	// 定义HASH表
	private HashMap<Integer, Integer> soundPoolMap;

	/***************************************************************
	 * Function: initSounds(); Parameters: null Returns: None. Description:
	 * 初始化声音系统 Notes: none.
	 ***************************************************************/
	public void initSounds(Context context) {
		// 初始化soundPool 对象,第一个参数是允许有多少个声音流同时播放,第2个参数是声音类型,第三个参数是声音的品质
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);

		// 初始化HASH表
		soundPoolMap = new HashMap<Integer, Integer>();

		// 获得声音设备和设备音量
		AudioManager mgr = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		mMediaPlayer = MediaPlayer.create(context, R.raw.bg);
		mMediaPlayer.setLooping(true);
	}

	/**
	 * 把资源中的音效加载到指定的ID(播放的时候就对应到这个ID播放就行了)
	 * Function: loadSfx(); Parameters: null Returns: None. Description: 加载音效资源
	 * Notes: none.
	 */
	public void loadSfx(Context context, int raw, int ID) {
		soundPoolMap.put(ID, soundPool.load(context, raw, 1));
	}

	/***************************************************************
	 * Function: play(); Parameters: sound:要播放的音效的ID, loop:循环次数 Returns: None.
	 * Description: 播放声音 Notes: none.
	 ***************************************************************/
	public void play(int sound, int uLoop) {
		soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1,
				uLoop, 1f);
	}
	public  void release()
	{
		soundPool.release();
		soundPool = null;
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer = null;
	}

	public void playBGMusic() {
		if (!mMediaPlayer.isPlaying())
		{			
			mMediaPlayer.start();
		}
	}

	public void stopBGMusic() {
		if (mMediaPlayer.isPlaying())
		{			
			mMediaPlayer.pause();
		}
	}
}


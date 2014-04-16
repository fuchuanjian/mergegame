package com.chuanonly.mergegame;

import android.app.Application;
import android.content.Context;

public class APP extends Application
{

	private static Context mContext;
	private static APP mApplication;
	public static int width;
	public static int height;
	public static float density;
	public static int language = 0;

	@Override
	public void onCreate()
	{
		super.onCreate();
		mApplication = this;
		mContext = getApplicationContext();

		width = mContext.getResources().getDisplayMetrics().widthPixels;
		height = mContext.getResources().getDisplayMetrics().heightPixels;
		density = mContext.getResources().getDisplayMetrics().heightPixels;

		String country = getResources().getConfiguration().locale.getCountry();
		if (country.equalsIgnoreCase("CN"))
		{
			language = 0;
		}else if (country.equalsIgnoreCase("TW"))
		{
			language = 1;
		}else 
		{
			language = 2;
		}
	}

	public static Context getContext()
	{
		return mContext;
	}

	public static APP getInstance()
	{
		return mApplication;
	}
	
}

package dev.mxw.chattingui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import dev.mxw.base.BaseApplication;
import dev.mxw.chattingui.BuildConfig;

public class AppConfig {
	public static final boolean DEBUG = BuildConfig.DEBUG;
	
	public static final String ROOT_FOLDER_NAME = BaseApplication.getInstance().getPackageName();
	public static final String IM_FOLDER_NAME = "im";
	public static final String IM_DOWNLOADED_VOICE_FOLDER_NAME = "downloadedvoice";
	
	public static boolean GUIDE_MODE = false;
	
	public static boolean isGuideMode(){
		return GUIDE_MODE;
	}
	
	public static String getRootPath(){
		return Environment.getExternalStorageDirectory()+"/"+ROOT_FOLDER_NAME;
	}
	
	public static boolean isFirstLaunch(){
		SharedPreferences launchInfo = BaseApplication.getInstance().getBaseContext().getSharedPreferences("LAUNCH_INFO", Context.MODE_PRIVATE);
		return launchInfo.getBoolean("first_launch", true);
	}
	
	public static void setFirstLaunch(boolean value){
		SharedPreferences launchInfo = BaseApplication.getInstance().getBaseContext().getSharedPreferences("LAUNCH_INFO", Context.MODE_PRIVATE);
		Editor editor = launchInfo.edit();
		editor.putBoolean("first_launch", value);
		editor.commit();
	}
	
}

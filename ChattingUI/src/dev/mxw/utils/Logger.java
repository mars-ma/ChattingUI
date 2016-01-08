package dev.mxw.utils;

import android.text.TextUtils;
import android.util.Log;
import dev.mxw.chattingui.BuildConfig;

public class Logger {
	public static boolean DEBUG = BuildConfig.DEBUG;
	private static final String TAG = Logger.class.getSimpleName();

	public static void e(String str) {
		e(TAG,str);
	}

	public static void e(String tag, String str) {
		if (DEBUG) {
			str = str.trim();
			int index = 0;
			int maxLength = 4000;
			String sub;
			while (index < str.length()) {
				// java的字符不允许指定超过总的长度end
				if (str.length() <= index + maxLength) {
					sub = str.substring(index);
				} else {
					sub = str.substring(index, index + maxLength);
				}
				index += maxLength;
				Log.e(TextUtils.isEmpty(tag) ? TAG : tag, sub.trim());
			}
		}
	}

	public static void test(String str) {
		e("test",str);
	}

}

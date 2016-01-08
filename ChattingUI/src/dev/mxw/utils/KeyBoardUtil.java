package dev.mxw.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyBoardUtil {
	
	public static void hideKeyBoard(EditText editText){
		InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(),
				0);
	}
	
	public static boolean showKeyBoard(EditText editText){
		InputMethodManager imm = (InputMethodManager) editText.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
	}
}

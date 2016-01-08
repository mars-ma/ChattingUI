package dev.mxw.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import dev.mxw.chattingui.R;

public abstract class BaseFragmentActivity extends FragmentActivity{

	View lay_fragment;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_common_with_fragment);
		setFragment(getBindFragment());
	}
	
	public void setFragment(Fragment fragment){
		FragmentManager manager = getSupportFragmentManager();
		manager.beginTransaction().replace(R.id.lay_fragment,fragment).commit();
	}
	
	public abstract Fragment getBindFragment();
}

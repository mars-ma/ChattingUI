package dev.mxw.im.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import dev.mxw.base.BaseFragmentActivity;
import dev.mxw.im.ui.fragment.ChattingFragment;
import dev.mxw.im.user.MyInfo;
import dev.mxw.im.user.OtherInfo;

public class ChattingWithSingleActivity extends BaseFragmentActivity{
	ChattingFragment mChattingFragment;
	@Override
	public Fragment getBindFragment() {
		mChattingFragment = new ChattingFragment() {
			
			@Override
			public OtherInfo bindOtherInfo() {
				return (OtherInfo) getIntent().getSerializableExtra("otherInfo");
			}
			
			@Override
			public MyInfo bindMyInfo() {
				return (MyInfo) getIntent().getSerializableExtra("myInfo");
			}
		};
		return mChattingFragment;
	}
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setTitle("聊天界面");
	}

}

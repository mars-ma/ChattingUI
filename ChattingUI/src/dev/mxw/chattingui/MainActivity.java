package dev.mxw.chattingui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import dev.mxw.factory.ToastFactory;
import dev.mxw.im.config.CommonConfig;
import dev.mxw.im.data.db.IMMessageManager;
import dev.mxw.im.ui.activity.ChattingWithSingleActivity;
import dev.mxw.im.user.MyInfo;
import dev.mxw.im.user.OtherInfo;
import dev.mxw.utils.Logger;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

	@ViewById
	Button btnStartChattingMe;

	@ViewById
	Button btnStartChattingOther;

	@ViewById
	Button btnClearDB;

	@ViewById
	TextView tvMyUnRead;

	@ViewById
	TextView tvOtherUnRead;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	void init() {
		btnStartChattingMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ChattingWithSingleActivity.class);
				OtherInfo otherInfo = new OtherInfo();
				otherInfo.setOtherChatIdNo(11);
				otherInfo.setOtherName("ROBO");
				otherInfo.setOtherHeadURL("");

				MyInfo myInfo = new MyInfo();
				myInfo.setMyChatIdNo(01);
				myInfo.setMyName("MXW");
				myInfo.setMyHeadURL("");
				intent.putExtra("myInfo", myInfo);
				intent.putExtra("otherInfo", otherInfo);
				startActivity(intent);
			}
		});

		btnStartChattingOther.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ChattingWithSingleActivity.class);
				OtherInfo otherInfo = new OtherInfo();
				otherInfo.setOtherChatIdNo(01);
				otherInfo.setOtherName("MXW");
				otherInfo.setOtherHeadURL("");

				MyInfo myInfo = new MyInfo();
				myInfo.setMyChatIdNo(11);
				myInfo.setMyName("ROBO");
				myInfo.setMyHeadURL("");
				intent.putExtra("myInfo", myInfo);
				intent.putExtra("otherInfo", otherInfo);
				startActivity(intent);
			}
		});

		btnClearDB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				IMMessageManager.getInstance(MainActivity.this).clear();
				ToastFactory.showToast(MainActivity.this, "数据库已清空");
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (CommonConfig.LOCAL_TEST_ENABLE) {
			int myUnRead = IMMessageManager.getInstance(MainActivity.this).queryUnReadCount(01, 11);
			Logger.e("我的未读 "+myUnRead);
			int otherUnRead = IMMessageManager.getInstance(MainActivity.this).queryUnReadCount(11, 01);
			tvMyUnRead.setText("我的未读消息:" + myUnRead);
			tvOtherUnRead.setText("对方的未读消息:" + otherUnRead);
		}
	}
}

package dev.mxw.im.ui.fragment;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import de.greenrobot.event.EventBus;
import dev.mxw.base.BaseFragment;
import dev.mxw.chattingui.R;
import dev.mxw.factory.ToastFactory;
import dev.mxw.im.communication.CommunicaionReceiver;
import dev.mxw.im.communication.CommunicationSender;
import dev.mxw.im.communication.callback.ISendFileCallBack;
import dev.mxw.im.communication.callback.ISendTextCallBack;
import dev.mxw.im.config.CommonConfig;
import dev.mxw.im.data.bean.IMMessage;
import dev.mxw.im.data.db.IMMessageManager;
import dev.mxw.im.event.NewMSG;
import dev.mxw.im.event.RefreshChatingListView;
import dev.mxw.im.event.SendWebURL;
import dev.mxw.im.event.UNREAD;
import dev.mxw.im.event.UpdateProgressImageView;
import dev.mxw.im.ui.adapter.IMMessagesAdapter;
import dev.mxw.im.user.MyInfo;
import dev.mxw.im.user.OtherInfo;
import dev.mxw.utils.ErrorCode;
import dev.mxw.utils.FileUtil;
import dev.mxw.utils.KeyBoardUtil;
import dev.mxw.utils.Logger;
import dev.mxw.utils.MediaPlayTools;
import dev.mxw.utils.MediaRecordFunc;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public abstract class ChattingFragment extends BaseFragment implements OnClickListener {

	// View层
	// 更多，语音面板
	private LinearLayout lay_more_panel, ll_voice_area;

	// 文字输入框
	private EditText et_content;

	private ImageView iv_more, iv_voice;
	private Button btn_send_msg;
	// 聊天列表
	private PullToRefreshListView listview;
	private ImageButton mVoiceRecord;
	// 语音计时器
	private Chronometer mChronometer;
	private TextView tv_voice_hint;

	/**
	 * 对方的userId
	 */
	private IMMessageManager mIMMessageManager;

	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
	private ArrayList<IMMessage> mMessages = new ArrayList<>();

	private IMMessagesAdapter mMessageAdapter;
	private int pageNo = 1;
	private int pageCount = 100;

	private OtherInfo otherInfo;
	private MyInfo myInfo;

	public abstract MyInfo bindMyInfo();

	public abstract OtherInfo bindOtherInfo();

	CommunicationSender mCommunicationProxy;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		mIMMessageManager = IMMessageManager.getInstance(getActivity());
		setMyInfo(bindMyInfo());
		setOtherInfo(bindOtherInfo());
		mCommunicationProxy = new CommunicationSender(myInfo.getMyChatIdNo(), otherInfo.getOtherChatIdNo());
	}

	public void onEventMainThread(RefreshChatingListView obj) {
		mMessageAdapter.notifyDataSetChanged();
	}

	public void onEventMainThread(NewMSG msg) {
		refreshData(1);
	}

	public void onEventMainThread(SendWebURL obj) {
		Logger.e("发送网页");
		try {
			sendWebURL(obj.title, obj.content, obj.imgURL, obj.targetURL);
		} catch (JSONException e) {
			ToastFactory.showToast(getActivity(), "发送失败，未知错误");
			e.printStackTrace();
		}
	}

	private void sendWebURL(String title, String content, String imgURL, String targetURL) throws JSONException {
		final IMMessage msg = new IMMessage();
		JSONObject contentObj = new JSONObject();
		contentObj.put("type", 1); // 1 网页

		JSONObject data = new JSONObject();
		data.put("title", title);
		data.put("content", content);
		data.put("imgURL", imgURL);
		data.put("targetURL", targetURL);

		contentObj.put("data", data);

		msg.setContent(contentObj.toString());
		msg.setType(0);
		msg.setFrom(getMyChatIdNo());
		msg.setTo(getOtherChatIdNo());
		msg.setOwner(getMyChatIdNo());
		msg.setTime(System.currentTimeMillis());
		msg.setOther(getOtherChatIdNo());

		mCommunicationProxy.sendText(msg.getContent(), new ISendTextCallBack() {

			@Override
			public void onSuccess() {
				msg.setStatus(1);
				mIMMessageManager.insert(msg);
				Logger.e("插入消息 content " + msg.getContent() + " from " + msg.getFrom() + " to " + msg.getTo() + " time "
						+ dateFormatter.format(new Date(msg.getTime())) + " type " + msg.getType() + " status "
						+ msg.getStatus());
				refreshData(1);
			}

			@Override
			public void onFailed() {
				ToastFactory.showToast(getActivity(), "消息发送失败");
				msg.setStatus(0);
				mIMMessageManager.insert(msg);
				Logger.e("插入消息 content " + msg.getContent() + " from " + msg.getFrom() + " to " + msg.getTo() + " time "
						+ dateFormatter.format(new Date(msg.getTime())) + " type " + msg.getType() + " status "
						+ msg.getStatus());
				refreshData(1);
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub

			}
		});
	}

	private void sendLocation(String address, double latitude, double longitude) throws JSONException {
		final IMMessage msg = new IMMessage();
		JSONObject contentObj = new JSONObject();
		contentObj.put("type", 2); // 2 位置

		JSONObject data = new JSONObject();
		data.put("address", address);
		data.put("latitude", latitude);
		data.put("longitude", longitude);

		contentObj.put("data", data);

		msg.setContent(contentObj.toString());
		msg.setType(0);
		msg.setFrom(getMyChatIdNo());
		msg.setTo(getOtherChatIdNo());
		msg.setOwner(getMyChatIdNo());
		msg.setTime(System.currentTimeMillis());
		msg.setOther(getOtherChatIdNo());

		mCommunicationProxy.sendText(msg.getContent(), new ISendTextCallBack() {

			@Override
			public void onSuccess() {
				msg.setStatus(1);
				mIMMessageManager.insert(msg);
				Logger.e("插入消息 content " + msg.getContent() + " from " + msg.getFrom() + " to " + msg.getTo() + " time "
						+ dateFormatter.format(new Date(msg.getTime())) + " type " + msg.getType() + " status "
						+ msg.getStatus());
				refreshData(1);
			}

			@Override
			public void onFailed() {
				ToastFactory.showToast(getActivity(), "消息发送失败");
				msg.setStatus(0);
				mIMMessageManager.insert(msg);
				Logger.e("插入消息 content " + msg.getContent() + " from " + msg.getFrom() + " to " + msg.getTo() + " time "
						+ dateFormatter.format(new Date(msg.getTime())) + " type " + msg.getType() + " status "
						+ msg.getStatus());
				refreshData(1);
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		IMMessagesAdapter.playing_id = -10000;
		MediaPlayTools.getInstance().stop();
		mMessageAdapter.notifyDataSetChanged();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (getMyChatIdNo() != -10000)
			mIMMessageManager.updateUnRead(getMyChatIdNo(), getOtherChatIdNo());
		EventBus.getDefault().post(new UNREAD());
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void initViews() {
		setRootView(R.layout.fragment_chatting_with_single);
		initChattingListView();
		initEditText();
		initMorePanel();
		initVoicePannel();
		refreshData(1);
	}

	private void initVoicePannel() {
		ll_voice_area = (LinearLayout) findViewById(R.id.ll_voice_area);
		iv_voice = (ImageView) findViewById(R.id.iv_voice);
		iv_voice.setOnClickListener(this);
		mChronometer = ((Chronometer) findViewById(R.id.chronometer));
		mVoiceRecord = ((ImageButton) findViewById(R.id.voice_record_imgbtn));
		tv_voice_hint = (TextView) findViewById(R.id.tv_voice_hint);
		mVoiceRecord.setOnTouchListener(mOnVoiceRecTouchListener);
	}

	private void initEditText() {
		btn_send_msg = (Button) findViewById(R.id.btn_send_msg);
		btn_send_msg.setOnClickListener(this);
		et_content = (EditText) findViewById(R.id.et_content);
		et_content.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideMorePanel();
				hideVoicePannel();
				et_content.requestFocus();
				KeyBoardUtil.showKeyBoard(et_content);
				return false;
			}
		});
		et_content.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s != null && s.length() > 0) {
					showMorePanelButton(false);
				} else {
					showMorePanelButton(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 有文字时隐藏更多面板的进入按钮，显示发送按钮
	 * 
	 * @param show
	 */
	private void showMorePanelButton(boolean show) {
		if (show) {
			iv_more.setVisibility(View.VISIBLE);
			btn_send_msg.setVisibility(View.GONE);
		} else {
			iv_more.setVisibility(View.GONE);
			btn_send_msg.setVisibility(View.VISIBLE);
		}
	}

	private void hideVoicePannel() {
		ll_voice_area.setVisibility(View.GONE);
	}

	private void hideMorePanel() {
		lay_more_panel.setVisibility(View.GONE);
	}

	private void initMorePanel() {
		iv_more = (ImageView) findViewById(R.id.iv_more);
		iv_more.setOnClickListener(this);
		lay_more_panel = (LinearLayout) findViewById(R.id.lay_more_panel);
		findViewById(R.id.lay_video).setOnClickListener(this);
		findViewById(R.id.lay_pic).setOnClickListener(this);
		findViewById(R.id.lay_voice).setOnClickListener(this);
		findViewById(R.id.lay_location).setOnClickListener(this);
	}

	private void initChattingListView() {
		listview = (PullToRefreshListView) findViewById(R.id.listview);
		mMessageAdapter = new IMMessagesAdapter(getActivity(), mMessages, myInfo, otherInfo);
		listview.setAdapter(mMessageAdapter);
		listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						listview.onRefreshComplete();
						pageNo++;
						refreshData(2);
					}
				}, 200);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				listview.onRefreshComplete();
			}
		});
	}

	private void refreshData(int type) {
		mMessages.clear();
		mMessages.addAll(mIMMessageManager.query(getMyChatIdNo(), getOtherChatIdNo(), pageNo, pageCount));
		java.util.Collections.reverse(mMessages);
		Logger.e("refreshData 查询到记录 " + mMessages.size());

		int totalCount = mIMMessageManager.getTotalCount(getMyChatIdNo(), getOtherChatIdNo());

		int totalPage = (int) Math.ceil((double) totalCount / pageCount);
		if (pageNo < totalPage) {
			listview.setMode(Mode.PULL_FROM_START);
		} else {
			listview.setMode(Mode.DISABLED);
		}
		mMessageAdapter.notifyDataSetChanged();

		if (type == 1) {
			listview.getRefreshableView().setSelection(mMessages.size() - 1);
			Logger.e("selection " + (mMessages.size() - 1));
		}
	}

	@Override
	public void initRQs() {

	}

	public String getOtherName() {
		return otherInfo.getOtherName();
	}

	public String getOtherHeadURL() {
		return otherInfo.getOtherHeadURL();
	}

	public int getOtherChatIdNo() {
		return otherInfo.getOtherChatIdNo();
	}

	public void setOtherInfo(OtherInfo info) {
		otherInfo = info;
	}

	public void setMyInfo(MyInfo info) {
		myInfo = info;
	}

	public String getMyName() {
		return myInfo.getMyName();
	}

	public String getMyHeadURL() {
		return myInfo.getMyHeadURL();
	}

	public int getMyChatIdNo() {
		return myInfo.getMyChatIdNo();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_more:
			deployMorePanel();
			break;
		case R.id.btn_send_msg:
			try {
				sendTextMSG();
			} catch (Exception e) {
				ToastFactory.showToast(getActivity(), "未知异常");
				e.printStackTrace();
			}
			break;
		case R.id.lay_pic:
			choosePic();
			hideMorePanel();
			break;
		case R.id.lay_video:
			rqVideoSession();
			hideMorePanel();
			break;
		case R.id.iv_voice:
			deployVoicePannel();
			break;
		case R.id.lay_voice:
			rqVoiceSession();
			hideMorePanel();
			break;
		case R.id.lay_location:
			shareMyLocation();
			hideMorePanel();
			break;
		}
	}

	private void shareMyLocation() {
		// startActivityForResult(new Intent(getActivity(),
		// ChooseLocationActivity.class), 3);
	}

	protected void rqVoiceSession() {

	}

	private void deployVoicePannel() {
		if (ll_voice_area.getVisibility() == View.GONE) {
			// 展开语音面板
			if (lay_more_panel.getVisibility() == View.VISIBLE) {
				lay_more_panel.setVisibility(View.GONE);
			}
			KeyBoardUtil.hideKeyBoard(et_content);
			ll_voice_area.setVisibility(View.VISIBLE);
		} else {
			ll_voice_area.setVisibility(View.GONE);
		}

	}

	private void rqVideoSession() {
	}

	private void choosePic() {
		int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;

		boolean showCamera = true;
		int maxNum = 1;

		Intent intent = new Intent(getActivity(), MultiImageSelectorActivity.class);
		// 是否显示拍摄图片
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);
		// 最大可选择图片数量
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
		// 选择模式
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
		// 默认选择
		startActivityForResult(intent, 1);

	}

	private void deployMorePanel() {
		if (lay_more_panel.getVisibility() == View.GONE) {
			if (ll_voice_area.getVisibility() == View.VISIBLE) {
				ll_voice_area.setVisibility(View.GONE);
			}
			// 展示面板
			KeyBoardUtil.hideKeyBoard(et_content);
			lay_more_panel.setVisibility(View.VISIBLE);
		} else {
			if (et_content.getText().toString() != null && et_content.getText().toString().length() > 0) {
				KeyBoardUtil.showKeyBoard(et_content);
				et_content.requestFocus();
			}
			lay_more_panel.setVisibility(View.GONE);
		}
	}

	private OnTouchListener mOnVoiceRecTouchListener = new OnTouchListener() {

		// 记录时间差
		long currentTimeMillis, recordTime = 0;
		private boolean mVoiceButtonTouched;
		private static final int CANCLE_DANSTANCE = 60;

		boolean sendVoice;
		int recorder_state;
		boolean isStoppedByTime = false;

		private long getAvailaleSize() {
			File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return (availableBlocks * blockSize) / 1024 / 1024;// MIB单位
		}

		private void animate(ImageView imgView, boolean start) {
			if (start) {
				// imgView.setBackgroundResource(R.drawable.voicebtn_animation_list);
				imgView.setImageResource(R.drawable.voicebtn_animation_list);
			}
			Drawable drawable = imgView.getDrawable();
			if (!(drawable instanceof AnimationDrawable)) {
				return;
			}
			AnimationDrawable frameAnimation = (AnimationDrawable) drawable;

			if (frameAnimation.isRunning()) {
				if (!start)
					frameAnimation.stop();
			} else {
				if (start) {
					frameAnimation.stop();
					frameAnimation.start();
				}
			}
			if (!start) {
				imgView.setImageResource(R.drawable.voice_push_button);
			}
		}

		private boolean isExistExternalStore() {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				return true;
			} else {
				return false;
			}
		}

		private Runnable startRecord = new Runnable() {

			@Override
			public void run() {
				mChronometer.setBase(SystemClock.elapsedRealtime());
				mChronometer.setOnChronometerTickListener(new OnChronometerTickListener() {
					@Override
					public void onChronometerTick(Chronometer chronometer) {
						String time = chronometer.getText().toString();
						if ("01:00".equals(time)) {// 判断五秒之后，让手机震动
							isStoppedByTime = true;
							animate(mVoiceRecord, false);
							mChronometer.setOnChronometerTickListener(null);
							mChronometer.stop();
							mChronometer.setBase(SystemClock.elapsedRealtime());
							Logger.e("录音按钮抬起");
							resetVoiceRecordingButton();
						}
					}
				});
				mChronometer.start();
			}
		};

		private void resetVoiceRecordingButton() {
			tv_voice_hint.setText("按住说话");
			mVoiceButtonTouched = false;

			if (sendVoice) {
				if (recorder_state == ErrorCode.SUCCESS) {
					MediaRecordFunc.getInstance().stopRecordAndFile();
					if (System.currentTimeMillis() - recordTime >= 2000) {
						String amrFile = MediaRecordFunc.getInstance().getFilePath();
						File armFileObj = new File(amrFile);
						if (armFileObj.exists()) {
							sendVoiceMSG(armFileObj.getAbsolutePath());
						} else {
							ToastFactory.showToast(getActivity(), "录制失败");
						}
					} else {
						ToastFactory.showToast(getActivity(), "语音时间过短");
					}
				}
			} else {
				MediaRecordFunc.getInstance().stopRecordAndFile();
				String amrFile = MediaRecordFunc.getInstance().getFilePath();
				File armFileObj = new File(amrFile);
				if (armFileObj.exists()) {
					armFileObj.delete();
				}
			}
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (getAvailaleSize() < 10) {
				ToastFactory.showToast(getActivity(), getString(R.string.media_no_memory));
				return false;
			}

			long time = System.currentTimeMillis() - currentTimeMillis;
			if (time <= 300) {
				currentTimeMillis = System.currentTimeMillis();
				return false;
			}

			if (!isExistExternalStore()) {
				ToastFactory.showToast(getActivity(), getString(R.string.media_ejected));
				return false;

			}

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				isStoppedByTime = false;
				animate(mVoiceRecord, true);
				mVoiceButtonTouched = true;
				// mVoiceRecord.setEnabled(false);
				onPause();
				Logger.e("录音按钮按下");
				tv_voice_hint.setText("松开手指，发送录音");
				sendVoice = true;
				recorder_state = MediaRecordFunc.getInstance().startRecordAndFile();
				if (recorder_state == ErrorCode.SUCCESS) {
					recordTime = System.currentTimeMillis();
					if (mHandler != null) {
						mHandler.postDelayed(startRecord, 220);
					}
				} else {
					MediaRecordFunc.getInstance().stopRecordAndFile();
					ToastFactory.showToast(getActivity(), ErrorCode.getErrorInfo(getActivity(), recorder_state));
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (isStoppedByTime) {
					return false;
				}
				if (event.getX() <= 0.0F || event.getY() <= -CANCLE_DANSTANCE
						|| event.getX() >= mVoiceRecord.getWidth()) {
					tv_voice_hint.setText(R.string.chatfooter_cancel_rcd_release);
					sendVoice = false;
				} else {
					tv_voice_hint.setText("松开手指，发送录音");
					sendVoice = true;
				}
				break;
			case MotionEvent.ACTION_UP:
				Logger.e("ACTION_UP isStoppedByTime " + isStoppedByTime);
				if (!isStoppedByTime) {
					animate(mVoiceRecord, false);
					mHandler.removeCallbacks(startRecord);
					mChronometer.setOnChronometerTickListener(null);
					mChronometer.stop();
					mChronometer.setBase(SystemClock.elapsedRealtime());
					Logger.e("录音按钮抬起");
					resetVoiceRecordingButton();
				} else {
					tv_voice_hint.setText("按住说话");
					mVoiceButtonTouched = false;
				}
				break;
			}
			return false;
		}
	};

	/**
	 * 发送语音文件
	 * 
	 * @param absolutePath
	 */
	private void sendVoiceMSG(String absolutePath) {
		if (!FileUtil.isFileExist(absolutePath)) {
			ToastFactory.showToast(getActivity(), "找不到语音文件" + absolutePath + "!");
			return;
		}
		//
		final IMMessage msg = new IMMessage();
		msg.setFilePath(absolutePath);
		msg.setType(2);
		msg.setFrom(getMyChatIdNo());
		msg.setTo(getOtherChatIdNo());
		msg.setOwner(msg.getFrom());
		msg.setOther(msg.getTo());
		msg.setTime(System.currentTimeMillis());

		mCommunicationProxy.sendFile(absolutePath, new ISendFileCallBack() {

			@Override
			public void onSuccess() {
				try {
					JSONObject custom = new JSONObject();
					custom.put("progress", 100);
					msg.setCustom(custom.toString());
					msg.setStatus(1); // 成功
					mIMMessageManager.update(msg);
					Logger.e("更新语音 path " + msg.getFilePath() + " from " + msg.getFrom() + " to " + msg.getTo()
							+ " time " + dateFormatter.format(new Date(msg.getTime())) + " type " + msg.getType()
							+ " status " + msg.getStatus() + " custom " + msg.getCustom() + " msgId " + msg.getId());
					if (CommonConfig.LOCAL_TEST_ENABLE)
						CommunicaionReceiver.getDefault().onReceiveFile(msg.getFrom(), msg.getTo(), "",
								msg.getFilePath(), 2);
					refreshData(1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailed() {
				ToastFactory.showToast(getActivity(), "发送失败,请检查网络");
				try {
					JSONObject custom = new JSONObject();
					custom.put("progress", 0);
					msg.setCustom(custom.toString());
					msg.setStatus(0); // 失败
					mIMMessageManager.update(msg);
					Logger.e("更新语音 path " + msg.getFilePath() + " from " + msg.getFrom() + " to " + msg.getTo()
							+ " time " + dateFormatter.format(new Date(msg.getTime())) + " type " + msg.getType()
							+ " status " + msg.getStatus() + " custom " + msg.getCustom() + " msgId " + msg.getId());
					refreshData(1);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onUpdateProgress(int progress) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStart() {
				try {
					JSONObject custom = new JSONObject();
					// custom.put("taskID", taskId);
					custom.put("progress", 0);
					msg.setCustom(custom.toString());
					msg.setStatus(2); // 进行中
					int msgId = mIMMessageManager.insert(msg);
					msg.setId(msgId);
					Logger.e("插入语音 path " + msg.getFilePath() + " from " + msg.getFrom() + " to " + msg.getTo()
							+ " time " + dateFormatter.format(new Date(msg.getTime())) + " type " + msg.getType()
							+ " status " + msg.getStatus() + " custom " + msg.getCustom() + " msgId " + msgId);
					refreshData(1);
				} catch (JSONException e) {
					e.printStackTrace();
					ToastFactory.showToast(getActivity(), "参数错误");
				}
			}

		});
	}

	/**
	 * 向对方发送图片
	 * 
	 * @param path
	 */
	private void sendPicMSG(String path) {
		if (!FileUtil.isFileExist(path)) {
			ToastFactory.showToast(getActivity(), "找不到图片" + path + "!");
			return;
		}
		//
		final IMMessage msg = new IMMessage();
		msg.setFilePath(path);
		msg.setType(1);
		msg.setFrom(getMyChatIdNo());
		msg.setTo(getOtherChatIdNo());
		msg.setOwner(msg.getFrom());
		msg.setOther(msg.getTo());
		msg.setTime(System.currentTimeMillis());

		String format = path.substring(path.lastIndexOf("."));
		String tempPath = getActivity().getBaseContext().getCacheDir().getAbsolutePath() + "/temp"
				+ System.currentTimeMillis() + format;
		Logger.e("图片暂存地址 " + tempPath);
		FileUtil.copy(path, tempPath);

		mCommunicationProxy.sendFile(path, new ISendFileCallBack() {

			@Override
			public void onSuccess() {
				try {
					JSONObject custom = new JSONObject();
					custom.put("progress", 100);
					msg.setCustom(custom.toString());
					msg.setStatus(1);

					mIMMessageManager.update(msg);
					Logger.e("更新图片 path " + msg.getFilePath() + " from " + msg.getFrom() + " to " + msg.getTo()
							+ " time " + dateFormatter.format(new Date(msg.getTime())) + " type " + msg.getType()
							+ " status " + msg.getStatus() + " custom " + msg.getCustom() + " msgId " + msg.getId());
					EventBus.getDefault().post(new UpdateProgressImageView(msg.getId(), 100, 1));

					if (CommonConfig.LOCAL_TEST_ENABLE)
						CommunicaionReceiver.getDefault().onReceiveFile(msg.getFrom(), msg.getTo(), "",
								msg.getFilePath(), 1);

					refreshData(1);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailed() {
				ToastFactory.showToast(getActivity(), "发送失败,请检查网络");
				try {
					JSONObject custom = new JSONObject();
					custom.put("progress", 0);
					msg.setCustom(custom.toString());
					msg.setStatus(0);
					mIMMessageManager.update(msg);
					Logger.e("更新图片 path " + msg.getFilePath() + " from " + msg.getFrom() + " to " + msg.getTo()
							+ " time " + dateFormatter.format(new Date(msg.getTime())) + " type " + msg.getType()
							+ " status " + msg.getStatus() + " custom " + msg.getCustom() + " msgId " + msg.getId());
					EventBus.getDefault().post(new UpdateProgressImageView(msg.getId(), 0, 0));
					refreshData(1);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onUpdateProgress(int progress) {
				try {
					JSONObject custom = new JSONObject();
					custom.put("progress", progress);
					msg.setCustom(custom.toString());
					mIMMessageManager.update(msg);
					EventBus.getDefault().post(new UpdateProgressImageView(msg.getId(), progress, 1));
					refreshData(1);
				} catch (JSONException e) {
					e.printStackTrace();
					ToastFactory.showToast(getActivity(), "参数错误");
				}
			}

			@Override
			public void onStart() {
				try {
					JSONObject custom = new JSONObject();
					custom.put("progress", 0);
					msg.setCustom(custom.toString());
					msg.setStatus(2);
					int msgId = mIMMessageManager.insert(msg);
					msg.setId(msgId);
					Logger.e("插入图片 path " + msg.getFilePath() + " from " + msg.getFrom() + " to " + msg.getTo()
							+ " time " + dateFormatter.format(new Date(msg.getTime())) + " type " + msg.getType()
							+ " status " + msg.getStatus() + " custom " + msg.getCustom() + " msgId " + msgId);
					refreshData(1);
				} catch (JSONException e) {
					e.printStackTrace();
					ToastFactory.showToast(getActivity(), "参数错误");
				}
			}
		});
	}

	private void sendTextMSG() throws JSONException {
		if (TextUtils.isEmpty(et_content.getText().toString())) {
			KeyBoardUtil.hideKeyBoard(et_content);
			return;
		}
		String text = et_content.getText().toString();

		final IMMessage msg = new IMMessage();
		JSONObject contentObj = new JSONObject();
		contentObj.put("type", 0); // 0文字
		contentObj.put("data", text);

		msg.setContent(contentObj.toString());
		msg.setType(0);
		msg.setFrom(getMyChatIdNo());
		msg.setTo(getOtherChatIdNo());
		msg.setOwner(getMyChatIdNo());
		msg.setTime(System.currentTimeMillis());
		msg.setOther(getOtherChatIdNo());

		mCommunicationProxy.sendText(contentObj.toString(), new ISendTextCallBack() {

			@Override
			public void onSuccess() {
				et_content.setText("");
				msg.setStatus(1);
				mIMMessageManager.insert(msg);
				Logger.e("插入消息 content " + msg.getContent() + " from " + msg.getFrom() + " to " + msg.getTo() + " time "
						+ dateFormatter.format(new Date(msg.getTime())) + " type " + msg.getType() + " status "
						+ msg.getStatus());
				if (CommonConfig.LOCAL_TEST_ENABLE)
					CommunicaionReceiver.getDefault().onReceiveText(msg.getFrom(), msg.getTo(), msg.getContent());
				refreshData(1);
			}

			@Override
			public void onFailed() {
				et_content.setText("");
				ToastFactory.showToast(getActivity(), "消息发送失败");
				msg.setStatus(0);
				mIMMessageManager.insert(msg);
				Logger.e("插入消息 content " + msg.getContent() + " from " + msg.getFrom() + " to " + msg.getTo() + " time "
						+ dateFormatter.format(new Date(msg.getTime())) + " type " + msg.getType() + " status "
						+ msg.getStatus());
				refreshData(1);
			}

			@Override
			public void onStart() {

			}
		});
	}

	private void sendTextMSG(String text) throws JSONException {
		if (TextUtils.isEmpty(text)) {
			Logger.e("要发送的文本为空");
			return;
		}

		final IMMessage msg = new IMMessage();
		JSONObject contentObj = new JSONObject();
		contentObj.put("type", 0); // 0文字
		contentObj.put("data", text);

		msg.setContent(contentObj.toString());
		msg.setType(0);
		msg.setFrom(getMyChatIdNo());
		msg.setTo(getOtherChatIdNo());
		msg.setOwner(getMyChatIdNo());
		msg.setTime(System.currentTimeMillis());
		msg.setOther(getOtherChatIdNo());

		mCommunicationProxy.sendText(contentObj.toString(), new ISendTextCallBack() {

			@Override
			public void onSuccess() {
				msg.setStatus(1);
				mIMMessageManager.insert(msg);
				Logger.e("插入消息 content " + msg.getContent() + " from " + msg.getFrom() + " to " + msg.getTo() + " time "
						+ dateFormatter.format(new Date(msg.getTime())) + " type " + msg.getType() + " status "
						+ msg.getStatus());
				// 本地测试
				if (CommonConfig.LOCAL_TEST_ENABLE)
					CommunicaionReceiver.getDefault().onReceiveText(msg.getFrom(), msg.getTo(), msg.getContent());
				refreshData(1);
			}

			@Override
			public void onFailed() {
				ToastFactory.showToast(getActivity(), "消息发送失败");
				msg.setStatus(0);
				mIMMessageManager.insert(msg);
				Logger.e("插入消息 content " + msg.getContent() + " from " + msg.getFrom() + " to " + msg.getTo() + " time "
						+ dateFormatter.format(new Date(msg.getTime())) + " type " + msg.getType() + " status "
						+ msg.getStatus());
				refreshData(1);
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if (resultCode == Activity.RESULT_OK) {
				ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				StringBuilder sb = new StringBuilder();
				if (mSelectPath == null || mSelectPath.isEmpty()) {
					return;
				}
				String path = mSelectPath.get(0);
				sendPicMSG(path);
			}
		} else if (requestCode == 2) {
			if (resultCode == Activity.RESULT_OK) {
				String content = data.getStringExtra("content");
				et_content.setText(content);
				hideMorePanel();
				hideVoicePannel();
				et_content.setSelection(et_content.getText().toString().length());
				et_content.requestFocus();
				if (!KeyBoardUtil.showKeyBoard(et_content)) {
					KeyBoardUtil.showKeyBoard(et_content);
				}
			}
		} else if (resultCode == Activity.RESULT_OK && requestCode == 3) {
			double lat = data.getDoubleExtra("lat", -1);
			double lon = data.getDoubleExtra("lon", -1);
			String address = data.getStringExtra("loc");

			if (lat != -1 && lon != -1) {
				try {
					sendLocation(address, lat, lon);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

package dev.mxw.base;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;
import de.greenrobot.event.EventBus;
import dev.mxw.im.communication.CommunicaionReceiver;
import dev.mxw.im.communication.listener.OnReceiveFileListener;
import dev.mxw.im.communication.listener.OnReceiveTextListener;
import dev.mxw.im.config.CommonConfig;
import dev.mxw.im.data.bean.IMMessage;
import dev.mxw.im.data.db.IMMessageManager;
import dev.mxw.im.event.NewMSG;
import dev.mxw.im.event.UNREAD;

public class BaseApplication extends Application {
	private static BaseApplication mApplication;
	public static final String VOLLEY_TAG = "VolleyPatterns";

	public BaseApplication() {
		mApplication = this;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(this);
		initIMCommunication();
	}

	private void initIMCommunication() {
		CommunicaionReceiver.getDefault().setOnReceiveTextListener(new OnReceiveTextListener() {
			@Override
			public void onReceiveText(int dwFromUserid, int dwToUserid, String message) {
				IMMessage msg = new IMMessage();
				msg.setContent(message);
				msg.setFrom(dwFromUserid);
				msg.setOwner(dwToUserid);
				msg.setTo(dwToUserid);
				msg.setStatus(1);
				msg.setType(0);
				msg.setUnread(1); 
				msg.setOther(dwFromUserid);
				msg.setTime(System.currentTimeMillis());
				IMMessageManager mIMMessageManager = IMMessageManager.getInstance(getApplicationContext());
				mIMMessageManager.insert(msg);
				EventBus.getDefault().post(new NewMSG());
				EventBus.getDefault().post(new UNREAD());
			}
		});

		CommunicaionReceiver.getDefault().setOnReceiveFileListener(new OnReceiveFileListener() {
			@Override
			public void onReceiveFile(int dwFromUserid, int dwToUserid, String fileName, String filePath,
					int fileType) {
				IMMessageManager mIMMessageManager = IMMessageManager.getInstance(getApplicationContext());
				IMMessage msg = new IMMessage();
				msg.setUnread(1);
				msg.setFrom(dwFromUserid);
				msg.setTo(dwToUserid);
				msg.setStatus(1);
				msg.setTime(System.currentTimeMillis());
				msg.setOther(msg.getFrom());
				msg.setOwner(msg.getTo());
				if (fileType == 1) {
					msg.setFilePath(filePath);
					msg.setType(1);
				} else if (fileType == 2) {
					msg.setFilePath(filePath);
					msg.setType(2);
					//本地测试使用本地的语音，无需下载
					if (!CommonConfig.LOCAL_TEST_ENABLE)
						msg.setStatus(0);
				}
				mIMMessageManager.insert(msg);
				EventBus.getDefault().post(new NewMSG());
				EventBus.getDefault().post(new UNREAD());
			}
		});
	}

	public static BaseApplication getInstance() {
		return mApplication;
	}

	private void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(3)
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()).memoryCache(new LruMemoryCache(5 * 1024 * 1024))
				.memoryCacheSize(10 * 1024 * 1024).diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(200)
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);
		// com.nostra13.universalimageloader.utils.L.writeDebugLogs(false);
	}

	public ImageLoader getImageLoader() {
		if (!ImageLoader.getInstance().isInited()) {
			initImageLoader(this);
		}
		return ImageLoader.getInstance();
	}

}

package dev.mxw.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;
import dev.mxw.base.BaseApplication;
import dev.mxw.chattingui.AppConfig;
import dev.mxw.im.data.bean.IMMessage;
import dev.mxw.im.data.db.IMMessageManager;
import dev.mxw.im.event.RefreshChatingListView;

public class VoiceDownloader {
	private static ExecutorService thread_pool = Executors.newCachedThreadPool();

	public static String getDownloadedVoiceFilePath(String url) {
		return getDownloadedVoiceFolderPath() + "/" + MD5.encodeByMD5(url);
	}

	public static String getDownloadedVoiceFolderPath() {
		return AppConfig.getRootPath() + "/" + AppConfig.IM_FOLDER_NAME + "/"
				+ AppConfig.IM_DOWNLOADED_VOICE_FOLDER_NAME;
	}

	private static ArrayList<String> downloading_url = new ArrayList<>();

	public static void download(final IMMessage message, final boolean cover, final DownloadCallBack callback) {

		final String urlAddress = message.getFilePath();
		final String path = getDownloadedVoiceFilePath(urlAddress);
		if (downloading_url.contains(urlAddress)) {
			Logger.e("VoiceDownloader","重复下载 强制返回 url "+urlAddress);
			return;
		} else {
			Logger.e("VoiceDownloader","开始下载 url "+urlAddress);
			downloading_url.add(message.getFilePath());
		}
		Runnable download = new Runnable() {
			@Override
			public void run() {
				try {
					message.setStatus(2);
					IMMessageManager.getInstance(BaseApplication.getInstance().getBaseContext()).update(message);
					if (callback != null) {
						callback.onStart();
					}
					Logger.e("VoiceDownloader", "url " + urlAddress + ";path " + path + ";cover " + cover);

					File file = new File(path);
					// 如果目标文件已经存在，则删除。产生覆盖旧文件的效果
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					if (file.exists()) {
						if (!cover) {
							if (callback != null) {
								callback.onFinished();
							}
							// 已经存在语音，刷新列表
							downloading_url.remove(urlAddress);
							EventBus.getDefault().post(new RefreshChatingListView());
							return;
						} else
							file.delete();
					}

					// 构造URL
					URL url = new URL(urlAddress);
					// 打开连接
					URLConnection con = url.openConnection();
					// 获得文件的长度
					int contentLength = con.getContentLength();
					Logger.e("长度 :" + contentLength / 1024 + "KB");
					// 输入流
					InputStream is = con.getInputStream();
					// 1K的数据缓冲
					byte[] bs = new byte[1024];
					// 读取到的数据长度
					int len;
					// 输出的文件流
					OutputStream os = new FileOutputStream(path);
					// 开始读取
					while ((len = is.read(bs)) != -1) {
						os.write(bs, 0, len);
					}
					// 完毕，关闭所有链接
					os.close();
					is.close();

					if (callback != null) {
						callback.onFinished();
					}
					message.setFilePath(path);
					message.setStatus(1);
					IMMessageManager.getInstance(BaseApplication.getInstance().getBaseContext()).update(message);
					downloading_url.remove(urlAddress);
					EventBus.getDefault().post(new RefreshChatingListView());
				} catch (Exception e) {
					e.printStackTrace();
					if (callback != null) {
						callback.onError(e);
					}
					message.setFilePath(urlAddress);
					message.setStatus(0);
					IMMessageManager.getInstance(BaseApplication.getInstance().getBaseContext()).update(message);
					downloading_url.remove(urlAddress);
					EventBus.getDefault().post(new RefreshChatingListView());
				}
				Logger.e("VoiceDownloader","已移除 url "+urlAddress);
			}
		};
		thread_pool.execute(download);
	}
	
	public interface DownloadCallBack {
		public abstract void onUpdateProgress(int progress);

		public abstract void onFinished();

		public abstract void onStart();
		
		public abstract void onError(Exception e);
	}
}

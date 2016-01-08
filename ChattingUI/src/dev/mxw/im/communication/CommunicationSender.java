package dev.mxw.im.communication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import dev.mxw.im.communication.callback.ISendFileCallBack;
import dev.mxw.im.communication.callback.ISendTextCallBack;

public class CommunicationSender {

	// 创建一个固定大小的线程池
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

	private int mChatIdNo, otherChatIdNo;
	Handler mHandler = new Handler();

	public CommunicationSender(int mId, int otherId) {
		mChatIdNo = mId;
		otherChatIdNo = otherId;
	}

	public void sendText(String text, ISendTextCallBack cb) {
		if (cb != null) {
			cb.onStart();
			cb.onSuccess();
		}
	}

	public void sendFile(String filePath, final ISendFileCallBack cb) {
		// test
		Runnable sendFile = new Runnable() {

			@Override
			public void run() {
				int progress = 0;
				if (cb != null) {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							cb.onStart();
						}
					});
				}
				while (progress != 100) {

					final int prgress_copy = progress;
					if (cb != null) {
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								cb.onUpdateProgress(prgress_copy);
							}
						});
					}

					try {
						Thread.sleep(200);
						progress += 10;
					} catch (InterruptedException e) {
						e.printStackTrace();
						if (cb != null) {
							mHandler.post(new Runnable() {

								@Override
								public void run() {
									cb.onFailed();
								}
							});
						}
					}
				}
				if (progress == 100) {
					if (cb != null) {
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								cb.onSuccess();
							}
						});
					}
				}
			}
		};
		fixedThreadPool.execute(sendFile);
	}
}

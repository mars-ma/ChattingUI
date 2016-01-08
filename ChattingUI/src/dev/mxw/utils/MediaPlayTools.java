/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */package dev.mxw.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.text.TextUtils;

/**
 *
 * <p>
 * Title: MediaPlayTools.java
 * </p>
 * <p>
 * Description: case R.id.start: File file = new
 * File(Environment.getExternalStorageDirectory(),
 * "voiceDemo/fn_2013100916_99e9ba8bf0924f269e4e25ab1df6c726_1142.amr");
 * MediaPlayTools.getInstance().playVoice(file.getAbsolutePath(), false); break;
 * case R.id.puse:
 * 
 * MediaPlayTools.getInstance().pause(); break; case R.id.resume:
 * 
 * MediaPlayTools.getInstance().resume(); break; case R.id.stop:
 * 
 * MediaPlayTools.getInstance().stop(); break;
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: http://www.cloopen.com
 * </p>
 * 
 * @author Jorstin Chan
 * @date 2013-10-16
 * @version 3.5
 */
public class MediaPlayTools {

	public static int playingId = -10000;
	private static final String TAG = "MediaPlayTools";

	private static MediaPlayTools mInstance = null;

	/**
	 * The definition of the state of play Play error
	 */
	private static final int STATUS_ERROR = -1;

	/**
	 * Stop playing
	 */
	private static final int STATUS_STOP = 0;

	/**
	 * Voice playing
	 */
	private static final int STATUS_PLAYING = 1;

	/**
	 * Pause playback
	 */
	private static final int STATUS_PAUSE = 2;

	private MediaPlayer mediaPlayer = new MediaPlayer();
	private OnVoicePlayCompletionListener mListener;

	/**
	 * The local path voice file
	 */
	private String urlPath = "";

	private int status = 0;

	public boolean isPause() {
		return STATUS_PAUSE == status;
	}

	public boolean isStop() {
		return STATUS_STOP == status;
	}

	public MediaPlayTools() {

		setOnCompletionListener();
		setOnErrorListener();
	}

	synchronized public static MediaPlayTools getInstance() {
		if (null == mInstance) {
			mInstance = new MediaPlayTools();
		}
		return mInstance;
	}

	/**
	 * <p>
	 * Title: play
	 * </p>
	 * <p>
	 * Description: Speech interface, you can set the start position to play,
	 * and to select the output stream (Earpiece or Speaker)
	 * </p>
	 * 
	 * @param isEarpiece
	 * @param seek
	 */
	private void play(boolean isEarpiece, int seek) {

		Logger.e("6");
		int streamType = AudioManager.STREAM_MUSIC;
		if (TextUtils.isEmpty(urlPath)) {
			return;
		}

		if (isEarpiece) {
			streamType = AudioManager.STREAM_VOICE_CALL;
		}

		if (mediaPlayer == null) {
			Logger.e("8");
			mediaPlayer = new MediaPlayer();
			setOnCompletionListener();
			setOnErrorListener();
		}
		try {
			Logger.e("7");
			mediaPlayer.reset();
			mediaPlayer.setAudioStreamType(streamType);
			Logger.e("MediaPlayTools 播放音频 " + urlPath);
			mediaPlayer.setDataSource(urlPath);
			mediaPlayer.prepare();
			if (seek > 0) {
				mediaPlayer.seekTo(seek);
			}
			mediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * <p>
	 * Title: play
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param urlPath
	 * @param isEarpiece
	 * @param seek
	 * @return
	 *
	 * @see #play(boolean, int)
	 */
	private boolean play(String urlPath, boolean isEarpiece, int seek) {

		if (status != STATUS_STOP) {
			Logger.e("2");
			return false;
		}

		this.urlPath = urlPath;

		boolean result = false;
		try {
			Logger.e("3");
			play(isEarpiece, seek);
			this.status = STATUS_PLAYING;
			result = true;
		} catch (Exception e) {
			e.printStackTrace();

			try {
				Logger.e("4");
				play(true, seek);
				result = true;
			} catch (Exception e1) {
				e1.printStackTrace();
				Logger.e("5");
				result = false;
			}

		}

		return result;

	}

	/**
	 * Using the speaker model play audio files
	 * <p>
	 * Title: playVoice
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param urlPath
	 * @param isEarpiece
	 * @return
	 */
	public boolean playVoice(String urlPath, boolean isEarpiece) {
		Logger.e("1");
		Logger.e("playVoice " + urlPath);
		return play(urlPath, isEarpiece, 0);
	}

	/**
	 *
	 * <p>
	 * Title: resume
	 * </p>
	 * <p>
	 * Description: Recovery pause language file, from the last to suspend the
	 * position to start playing
	 * </p>
	 * 
	 * @return
	 */
	public boolean resume() {

		if (this.status != STATUS_PAUSE) {

			return false;
		}

		boolean result = false;

		try {
			mediaPlayer.start();
			this.status = STATUS_PLAYING;
			result = true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			this.status = STATUS_ERROR;
			result = false;
		}

		return result;

	}

	public boolean stop() {

		if (status != STATUS_PLAYING && status != STATUS_PAUSE) {

			return false;
		}

		boolean result = false;
		try {
			if (mediaPlayer != null) {
				this.mediaPlayer.stop();
				this.mediaPlayer.release();
				this.mediaPlayer = null;
			}
			this.status = STATUS_STOP;
			result = true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			this.status = STATUS_ERROR;
			result = false;

		}

		return result;
	}

	private boolean calling = false;

	/**
	 *
	 * <p>
	 * Title: setSpeakerOn
	 * </p>
	 * <p>
	 * Description: Set the output device mode (the Earpiece or Speaker) to play
	 * voice
	 * </p>
	 * 
	 * @param speakerOn
	 */
	public void setSpeakerOn(boolean speakerOn) {

		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
		}

		if (calling) {
			// 这里需要判断当前的状态是否是正在系统电话振铃或者接听中

		} else {
			int currentPosition = mediaPlayer.getCurrentPosition();

			stop();

			setOnCompletionListener();
			setOnErrorListener();

			play(urlPath, !speakerOn, currentPosition);
		}
	}

	public boolean pause() {
		if (this.status != STATUS_PLAYING) {
			return false;
		}

		boolean result = false;

		try {

			mediaPlayer.pause();
			this.status = STATUS_PAUSE;
			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			status = STATUS_ERROR;
		}

		return result;
	}

	public int getStatus() {
		return status;
	}

	public boolean isPlaying() {

		if (this.status == STATUS_PLAYING) {
			return true;
		}

		return false;
	}

	private void setOnCompletionListener() {

		//
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				status = STATUS_STOP;
				Logger.e("MediaPlayTools 播放音频完毕 " + urlPath);
				if (mListener != null) {
					mListener.OnVoicePlayCompletion();
				}
			}
		});
	}

	/**
	 *
	 * <p>
	 * Title: setOnErrorListener
	 * </p>
	 * <p>
	 * Description: Set the language player initialization error correction
	 * </p>
	 */
	private void setOnErrorListener() {

		//
		mediaPlayer.setOnErrorListener(null);
	}

	public void setOnVoicePlayCompletionListener(OnVoicePlayCompletionListener l) {
		mListener = l;
	}

	public interface OnVoicePlayCompletionListener {
		void OnVoicePlayCompletion();
	}
}

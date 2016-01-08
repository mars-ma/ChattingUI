package dev.mxw.im.ui.widget;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.greenrobot.event.EventBus;
import dev.mxw.chattingui.R;
import dev.mxw.factory.ToastFactory;
import dev.mxw.im.communication.CommunicationSender;
import dev.mxw.im.communication.callback.ISendFileCallBack;
import dev.mxw.im.communication.callback.ISendTextCallBack;
import dev.mxw.im.config.CommonConfig;
import dev.mxw.im.data.bean.IMMessage;
import dev.mxw.im.data.db.IMMessageManager;
import dev.mxw.im.event.NewMSG;
import dev.mxw.im.event.RefreshChatingListView;
import dev.mxw.im.ui.adapter.IMMessagesAdapter;
import dev.mxw.im.user.MyInfo;
import dev.mxw.im.user.OtherInfo;
import dev.mxw.utils.FileUtil;
import dev.mxw.utils.ImageLoader;
import dev.mxw.utils.Logger;
import dev.mxw.utils.MediaPlayTools;
import dev.mxw.utils.VoiceDownloader;
import dev.mxw.widget.ProgressImageView;
import io.github.barbosa.messagesview.library.utils.PrettyDate;
import io.github.barbosa.messagesview.library.views.AvatarView;

public abstract class IMMessageListItem extends RelativeLayout {

	protected ProgressBar progressbar;
	protected AvatarView avatarView;
	protected TextView dateTextView;
	protected TextView senderNameTextView;
	protected ImageButton resend;
	protected ImageView iv_pic;
	protected LinearLayout chat_message_image_right_list_item_bubble, chat_message_image_left_list_item_bubble;

	private MessageSenderType senderType = MessageSenderType.OTHERS;
	private Context mContext;
	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private MyInfo mInfo;
	private OtherInfo oInfo;
	CommunicationSender mCommunicationProxy;

	public IMMessageListItem(Context context, MyInfo myinfo, OtherInfo otherInfo) {
		super(context);
		mContext = context;
		mInfo = myinfo;
		oInfo = otherInfo;
		mCommunicationProxy = new CommunicationSender(mInfo.getMyChatIdNo(), oInfo.getOtherChatIdNo());
	}

	private void setMessageFromMe(final IMMessage message) {
		if (chat_message_image_right_list_item_bubble != null)
			chat_message_image_right_list_item_bubble.setOnClickListener(null);

		// 我的名称
		if (avatarView != null) {
			avatarView.setName("我", null);
		}
		// 我的头像
		if (avatarView != null && avatarView.getImageView() != null) {
			ImageLoader.displayImage(mInfo.getMyHeadURL(), avatarView.getImageView(), ImageLoader
					.getCommon(R.drawable.default_touxiang, R.drawable.default_touxiang, R.drawable.default_touxiang));
		}
		// 如果发送状态为0，显示重新发送
		if (message.getStatus() == 0) {
			resend.setVisibility(View.VISIBLE);
			resend.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					final IMMessage msg = message;
					if (msg.getType() == 0) {
						// 重新发送消息
						mCommunicationProxy.sendText(msg.getContent(), new ISendTextCallBack() {

							@Override
							public void onSuccess() {
								msg.setStatus(1);
								msg.setTime(System.currentTimeMillis());
								IMMessageManager.getInstance(mContext).update(msg);
								Logger.e("更新消息 content " + msg.getContent() + " from " + msg.getFrom() + " to "
										+ msg.getTo() + " time " + dateFormatter.format(new Date(msg.getTime()))
										+ " type " + msg.getType() + " status " + msg.getStatus());
								EventBus.getDefault().post(new NewMSG());
							}

							@Override
							public void onFailed() {
								ToastFactory.showToast(mContext, "消息发送失败");
								EventBus.getDefault().post(new NewMSG());
							}

							@Override
							public void onStart() {
								// TODO Auto-generated method stub

							}
						});
					} else if (msg.getType() == 1) {

						// 重新发送图片
						String format = message.getFilePath().substring(message.getFilePath().lastIndexOf("."));
						String tempPath = getContext().getCacheDir().getAbsolutePath() + "/temp"
								+ System.currentTimeMillis() + format;
						Logger.e("图片暂存地址 " + tempPath);
						FileUtil.copy(message.getFilePath(), tempPath);

						mCommunicationProxy.sendFile(message.getFilePath(), new ISendFileCallBack() {

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub

							}

							@Override
							public void onFailed() {
								// TODO Auto-generated method stub
								ToastFactory.showToast(mContext, "发送失败,请检查网络");
							}

							@Override
							public void onUpdateProgress(int progress) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onStart() {
								// TODO Auto-generated method stub
								try {
									JSONObject custom = new JSONObject();
									custom.put("progress", 0);
									msg.setCustom(custom.toString());
									msg.setStatus(2); // 更新为进行中
									msg.setTime(System.currentTimeMillis());
									IMMessageManager.getInstance(mContext).update(msg);
									Logger.e("更新图片 path " + msg.getFilePath() + " from " + msg.getFrom() + " to "
											+ msg.getTo() + " time " + dateFormatter.format(new Date(msg.getTime()))
											+ " type " + msg.getType() + " status " + msg.getStatus() + " custom "
											+ msg.getCustom() + " msgId " + msg.getId());

									// SendPicture.startCallBackThread(tempPath,
									// msg.getTo(), msg, mAnyChatOutParam,
									// taskId);
									EventBus.getDefault().post(new NewMSG());
								} catch (JSONException e) {
									e.printStackTrace();
									ToastFactory.showToast(mContext, "参数错误");
								}
							}
						});
					} else if (msg.getType() == 2) {
						// 重新发送语音
						mCommunicationProxy.sendFile(message.getFilePath(), new ISendFileCallBack() {

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub

							}

							@Override
							public void onFailed() {
								// TODO Auto-generated method stub
								ToastFactory.showToast(mContext, "发送失败,请检查网络");
							}

							@Override
							public void onUpdateProgress(int progress) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onStart() {
								// TODO Auto-generated method stub
								JSONObject custom = new JSONObject();
								// custom.put("taskID", taskId);
								try {
									custom.put("progress", 0);

									msg.setCustom(custom.toString());
									msg.setStatus(2); // 更新为进行中
									msg.setTime(System.currentTimeMillis());
									IMMessageManager.getInstance(mContext).update(msg);
									Logger.e("更新语音 path " + msg.getFilePath() + " from " + msg.getFrom() + " to "
											+ msg.getTo() + " time " + dateFormatter.format(new Date(msg.getTime()))
											+ " type " + msg.getType() + " status " + msg.getStatus() + " custom "
											+ msg.getCustom() + " msgId " + msg.getId());

									// SendVoice.startCallBackThread(msg.getFilePath(),
									// msg.getTo(), msg, mAnyChatOutParam,
									// taskId);
									EventBus.getDefault().post(new NewMSG());
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
					}
				}
			});
		} else {
			resend.setVisibility(View.GONE);
		}

		// 显示发送的图片
		if (iv_pic != null && message.getType() == 1 && !TextUtils.isEmpty(message.getFilePath())) {
			if (iv_pic instanceof ProgressImageView) {

				if (message.getStatus() == 1) {
					((ProgressImageView) iv_pic).setProgress(100);
					((ProgressImageView) iv_pic).setMSGID(-10000);
				} else if (message.getStatus() == 0) {
					((ProgressImageView) iv_pic).setProgress(-1);
					((ProgressImageView) iv_pic).setMSGID(-10000);
					((ProgressImageView) iv_pic).setProgress(0);
				} else if (message.getStatus() == 2) {
					String custom = message.getCustom();
					try {
						if (!TextUtils.isEmpty(custom)) {
							JSONObject customOBJ = new JSONObject(custom);
							int progress = customOBJ.getInt("progress");
							((ProgressImageView) iv_pic).setMSGID(message.getId());
							((ProgressImageView) iv_pic).setProgress(progress);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			ImageLoader.displayImage("file://" + message.getFilePath(), iv_pic,
					ImageLoader.getCommon(R.drawable.pic_thumb_bg, R.drawable.pic_thumb_bg, R.drawable.pic_thumb_bg));

			chat_message_image_right_list_item_bubble.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						String cachePath = message.getFilePath();
						Intent intent = new Intent();
						intent.setAction(android.content.Intent.ACTION_VIEW);
						File picFile = new File(cachePath);
						if (picFile.exists()) {
							intent.setDataAndType(Uri.fromFile(picFile), "image/*");
							getContext().startActivity(intent);
						} else {
							ToastFactory.showToast(getContext(), "图片已删除...");
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						ToastFactory.showToast(getContext(), "图片已删除...");
					}
				}
			});

		}

		// 显示语音消息
		if (chat_message_image_right_list_item_bubble != null && iv_pic != null && message.getType() == 2)

		{
			if (IMMessagesAdapter.playing_id == message.getId()) {
				animateRight(iv_pic, true);
			} else {
				animateRight(iv_pic, false);
			}

			chat_message_image_right_list_item_bubble.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (TextUtils.isEmpty(message.getFilePath()) || !FileUtil.isFileExist(message.getFilePath())) {
						ToastFactory.showToast(mContext, "语音文件不存在!");
						return;
					}
					MediaPlayTools instance = MediaPlayTools.getInstance();
					if (instance.isPlaying()) {
						if (IMMessagesAdapter.playing_id == message.getId()) {
							instance.pause();
							IMMessagesAdapter.playing_id = -10000;
							MediaPlayTools.playingId = message.getId();
							EventBus.getDefault().post(new RefreshChatingListView());
						} else {
							instance.stop();
							instance.setOnVoicePlayCompletionListener(
									new MediaPlayTools.OnVoicePlayCompletionListener() {
								@Override
								public void OnVoicePlayCompletion() {
									IMMessagesAdapter.playing_id = -10000;
									EventBus.getDefault().post(new RefreshChatingListView());
								}
							});
							String fileLocalPath = message.getFilePath();
							instance.playVoice(fileLocalPath, false);
							MediaPlayTools.playingId = message.getId();
							IMMessagesAdapter.playing_id = message.getId();
							EventBus.getDefault().post(new RefreshChatingListView());
						}
					} else {
						if (MediaPlayTools.playingId == message.getId() && instance.isPause()) {
							instance.resume();
							IMMessagesAdapter.playing_id = message.getId();
							MediaPlayTools.playingId = message.getId();
							EventBus.getDefault().post(new RefreshChatingListView());
						} else {
							instance.stop();
							instance.setOnVoicePlayCompletionListener(
									new MediaPlayTools.OnVoicePlayCompletionListener() {
								@Override
								public void OnVoicePlayCompletion() {
									IMMessagesAdapter.playing_id = -10000;
									EventBus.getDefault().post(new RefreshChatingListView());
								}
							});
							String fileLocalPath = message.getFilePath();
							instance.playVoice(fileLocalPath, false);
							MediaPlayTools.playingId = message.getId();
							IMMessagesAdapter.playing_id = message.getId();
							EventBus.getDefault().post(new RefreshChatingListView());
						}

					}
				}

			});
		}

		// 显示发送语音的进度条
		if (progressbar != null) {
			if (message.getType() == 2 && message.getStatus() == 2) {
				progressbar.setVisibility(View.VISIBLE);
			} else {
				progressbar.setVisibility(View.GONE);
			}
		}
	}

	public void setMessage(final IMMessage message) {
		if (message == null)
			return;

		if (message.getOwner() == message.getFrom()) {
			setMessageFromMe(message);

		} else {
			setMessageFromOther(message);
		}

		if (dateTextView != null) {
			String prettyDate = PrettyDate.with(getContext()).prettify(new Date(message.getTime()));
			dateTextView.setText(prettyDate);
		}
	}

	private void setMessageFromOther(final IMMessage message) {

		if (chat_message_image_left_list_item_bubble != null) {
			chat_message_image_left_list_item_bubble.setOnClickListener(null);
		}
		// for (DoctorBean info : MyContacts.persons) {
		// if (info.getDoctorAnyChatId() == message.getOther()) {
		if (avatarView != null) {
			avatarView.setName(TextUtils.isEmpty(oInfo.getOtherName()) ? "" + message.getOther() : oInfo.getOtherName(),
					null);
		}
		if (senderNameTextView != null) {
			senderNameTextView
					.setText(TextUtils.isEmpty(oInfo.getOtherName()) ? "" + message.getOther() : oInfo.getOtherName());
		}
		if (avatarView != null && avatarView.getImageView() != null) {
			ImageLoader.displayImage(oInfo.getOtherHeadURL(), avatarView.getImageView(), ImageLoader
					.getCommon(R.drawable.default_touxiang, R.drawable.default_touxiang, R.drawable.default_touxiang));
		}
		if (iv_pic != null && message.getType() == 1 && !TextUtils.isEmpty(message.getFilePath())) {
			ImageLoader.displayImage((CommonConfig.LOCAL_TEST_ENABLE ? "file://" : "") + message.getFilePath(), iv_pic,
					ImageLoader.getCommon(R.drawable.pic_thumb_bg, R.drawable.pic_thumb_bg, R.drawable.pic_thumb_bg));
			chat_message_image_left_list_item_bubble.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						Logger.e("点击的图片缓存路径 " + ImageLoader.getImagePath(message.getFilePath()));

						String cachePath = ImageLoader.getImagePath(message.getFilePath());
						Intent intent = new Intent();
						intent.setAction(android.content.Intent.ACTION_VIEW);
						File picFile = new File(cachePath);
						if (picFile.exists()) {
							intent.setDataAndType(Uri.fromFile(picFile), "image/*");
							getContext().startActivity(intent);
						} else {
							ToastFactory.showToast(getContext(), "图片已删除...");
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						ToastFactory.showToast(getContext(), "图片正在加载中...");
					}
				}
			});

		}

		if (chat_message_image_left_list_item_bubble != null && iv_pic != null && message.getType() == 2) {
			if (IMMessagesAdapter.playing_id == message.getId()) {
				animateLeft(iv_pic, true);
			} else {
				animateLeft(iv_pic, false);
			}
			if (message.getStatus() == 0 || message.getStatus() == 2) {
				// 未完成下载，显示进度条
				progressbar.setVisibility(View.VISIBLE);
				chat_message_image_left_list_item_bubble.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ToastFactory.showToast(mContext, "语音下载中!");
					}
				});
				if (message.getStatus() == 0) {
					//本地测试忽略下载步骤
					VoiceDownloader.download(message, false, null);
				}
			} else if (message.getStatus() == 1) {
				progressbar.setVisibility(View.GONE);
				chat_message_image_left_list_item_bubble.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (TextUtils.isEmpty(message.getFilePath()) || !FileUtil.isFileExist(message.getFilePath())) {
							ToastFactory.showToast(mContext, "语音文件不存在!");
							return;
						}
						MediaPlayTools instance = MediaPlayTools.getInstance();
						if (instance.isPlaying()) {
							if (IMMessagesAdapter.playing_id == message.getId()) {
								instance.pause();
								IMMessagesAdapter.playing_id = -10000;
								MediaPlayTools.playingId = message.getId();
								EventBus.getDefault().post(new RefreshChatingListView());
							} else {
								instance.stop();
								instance.setOnVoicePlayCompletionListener(
										new MediaPlayTools.OnVoicePlayCompletionListener() {
									@Override
									public void OnVoicePlayCompletion() {
										IMMessagesAdapter.playing_id = -10000;
										EventBus.getDefault().post(new RefreshChatingListView());
									}
								});
								String fileLocalPath = message.getFilePath();
								instance.playVoice(fileLocalPath, false);
								MediaPlayTools.playingId = message.getId();
								IMMessagesAdapter.playing_id = message.getId();
								EventBus.getDefault().post(new RefreshChatingListView());
							}
						} else {
							if (MediaPlayTools.playingId == message.getId() && instance.isPause()) {
								instance.resume();
								IMMessagesAdapter.playing_id = message.getId();
								MediaPlayTools.playingId = message.getId();
								EventBus.getDefault().post(new RefreshChatingListView());
							} else {
								instance.stop();
								instance.setOnVoicePlayCompletionListener(
										new MediaPlayTools.OnVoicePlayCompletionListener() {
									@Override
									public void OnVoicePlayCompletion() {
										IMMessagesAdapter.playing_id = -10000;
										EventBus.getDefault().post(new RefreshChatingListView());
									}
								});
								String fileLocalPath = message.getFilePath();
								instance.playVoice(fileLocalPath, false);
								MediaPlayTools.playingId = message.getId();
								IMMessagesAdapter.playing_id = message.getId();
								EventBus.getDefault().post(new RefreshChatingListView());
							}

						}
					}
				});
			}
		}
		// break;
		// }
		// }
	}

	private void animateRight(ImageView imgView, boolean start) {
		if (start) {
			// imgView.setBackgroundResource(R.drawable.voicebtn_animation_list);
			imgView.setImageResource(R.drawable.voice_listitem_animation_list_right);
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
			imgView.setImageResource(R.drawable.chatto_voice_playing);
		}
	}

	private void animateLeft(ImageView imgView, boolean start) {
		if (start) {
			// imgView.setBackgroundResource(R.drawable.voicebtn_animation_list);
			imgView.setImageResource(R.drawable.voice_listitem_animation_list_left);
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
			imgView.setImageResource(R.drawable.chatfrom_voice_playing);
		}
	}

	public MessageSenderType getSenderType() {
		return senderType;
	}

	public void setSenderType(MessageSenderType senderType) {
		this.senderType = senderType;
	}

	public enum MessageSenderType {
		ME, OTHERS
	}
}

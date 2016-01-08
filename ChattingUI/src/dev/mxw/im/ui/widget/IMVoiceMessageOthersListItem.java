package dev.mxw.im.ui.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import dev.mxw.chattingui.R;
import dev.mxw.im.data.bean.IMMessage;
import dev.mxw.im.user.MyInfo;
import dev.mxw.im.user.OtherInfo;
import dev.mxw.utils.FileUtil;
import dev.mxw.utils.Logger;
import io.github.barbosa.messagesview.library.views.AvatarView;

public class IMVoiceMessageOthersListItem extends IMImageMessageListItem {

	public IMVoiceMessageOthersListItem(Context context,MyInfo myinfo,OtherInfo otherinfo) {
        super(context, myinfo, otherinfo);

		inflate(context, R.layout.chat_message_voice_left_list_item, this);
		avatarView = (AvatarView) findViewById(R.id.chat_message_image_left_list_item_avatar);
		dateTextView = (TextView) findViewById(R.id.chat_message_image_left_list_item_date);
		imageNameTextView = (TextView) findViewById(R.id.chat_message_image_left_list_item_file_name);
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		chat_message_image_left_list_item_bubble = (LinearLayout) findViewById(
				R.id.chat_message_image_left_list_item_bubble);
	}

	@Override
	public void setMessage(IMMessage message) {
		super.setMessage(message);
		if (FileUtil.isFileExist(message.getFilePath())) {
			MediaPlayer mp = MediaPlayer.create(getContext(), Uri.parse(message.getFilePath()));
			if (mp != null) {
				int duration = mp.getDuration();
				Logger.e("音频 " + message.getFilePath() + " 时长 " + duration);
				int durationSeccond = duration / 1000;
				imageNameTextView.setText(durationSeccond + "''");
			}
		}
	}
}

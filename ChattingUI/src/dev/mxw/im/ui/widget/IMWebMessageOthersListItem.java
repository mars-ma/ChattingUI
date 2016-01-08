package dev.mxw.im.ui.widget;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import dev.mxw.chattingui.R;
import dev.mxw.im.data.bean.IMMessage;
import dev.mxw.im.user.MyInfo;
import dev.mxw.im.user.OtherInfo;
import dev.mxw.utils.ImageLoader;
import dev.mxw.widget.MTextView;
import io.github.barbosa.messagesview.library.views.AvatarView;

public class IMWebMessageOthersListItem extends IMImageMessageListItem {

	private TextView tv_title;
	private MTextView tv_content;

	public IMWebMessageOthersListItem(Context context,MyInfo myinfo,OtherInfo otherinfo) {
        super(context, myinfo, otherinfo);

		inflate(context, R.layout.chat_message_web_left_list_item, this);

		avatarView = (AvatarView) findViewById(R.id.chat_message_image_left_list_item_avatar);
		dateTextView = (TextView) findViewById(R.id.chat_message_image_left_list_item_date);
		imageNameTextView = (TextView) findViewById(R.id.chat_message_image_left_list_item_file_name);
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		chat_message_image_left_list_item_bubble = (LinearLayout) findViewById(
				R.id.chat_message_image_left_list_item_bubble);

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_content = (MTextView) findViewById(R.id.tv_content);

	}

	@Override
	public void setMessage(IMMessage message) {
		super.setMessage(message);

		String text = message.getContent() != null ? message.getContent() : "";
		try {
			JSONObject contentObj = new JSONObject(text);
			int type = contentObj.getInt("type");
			if (type == 1) {
				JSONObject data = contentObj.getJSONObject("data");
				final String title = data.getString("title");
				String content = data.getString("content");
				String imgURL = data.getString("imgURL");
				final String targetURL = data.getString("targetURL");

				tv_title.setText(title);
				tv_content.setMText(content.subSequence(0, 20) + "...");
				tv_title.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
				tv_content.setTextColor(getContext().getResources().getColor(android.R.color.primary_text_light));
				ImageLoader.displayImage(imgURL, iv_pic, ImageLoader.getCommon(R.drawable.ic_launcher,
						R.drawable.ic_launcher, R.drawable.ic_launcher));
				if (!TextUtils.isEmpty(targetURL))
					chat_message_image_left_list_item_bubble.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
//							Intent intent = new Intent(getContext(), WebViewer.class);
//							intent.putExtra("title", title);
//							intent.putExtra("URL", targetURL);
//							getContext().startActivity(intent);
						}
					});
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}

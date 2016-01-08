package dev.mxw.im.ui.widget;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import dev.mxw.chattingui.R;
import dev.mxw.im.data.bean.IMMessage;
import dev.mxw.im.user.MyInfo;
import dev.mxw.im.user.OtherInfo;
import dev.mxw.widget.MTextView;
import io.github.barbosa.messagesview.library.views.AvatarView;

public class IMLocationMessageMeListItem extends IMMessageListItem {

	private TextView tv_title;
	private MTextView tv_content;

	public IMLocationMessageMeListItem(Context context,MyInfo myinfo,OtherInfo otherinfo) {
        super(context, myinfo, otherinfo);

		inflate(context, R.layout.chat_message_web_right_list_item, this);

		avatarView = (AvatarView) findViewById(R.id.chat_message_image_right_list_item_avatar);
		dateTextView = (TextView) findViewById(R.id.chat_message_image_right_list_item_date);
		resend = (ImageButton) findViewById(R.id.resend);
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		iv_pic.setImageResource(R.drawable.icon_weizhi02);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		chat_message_image_right_list_item_bubble = (LinearLayout) findViewById(
				R.id.chat_message_image_right_list_item_bubble);

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
			if (type == 2) {
				JSONObject data = contentObj.getJSONObject("data");
				final String address = data.getString("address");
				final double lat = data.getDouble("latitude");
				final double lon = data.getDouble("longitude");

				tv_title.setText("我的位置");
				tv_content.setMText(address);
				tv_title.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
				tv_content.setTextColor(getContext().getResources().getColor(android.R.color.primary_text_light));
				chat_message_image_right_list_item_bubble.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//						Intent intent = new Intent(getContext(), ShowLocationActivity.class);
//						intent.putExtra("lat", lat);
//						intent.putExtra("lon", lon);
//						intent.putExtra("loc",address);
//						getContext().startActivity(intent);
					}
				});
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}

package dev.mxw.im.ui.widget;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.TextView;
import dev.mxw.im.data.bean.IMMessage;
import dev.mxw.im.user.MyInfo;
import dev.mxw.im.user.OtherInfo;

public abstract class IMTextMessageListItem extends IMMessageListItem {

	protected TextView messageTextView;

	public IMTextMessageListItem(Context context,MyInfo myinfo,OtherInfo otherinfo) {
        super(context, myinfo, otherinfo);
	}

	public void setMessage(IMMessage message) {
		super.setMessage(message);

		if (messageTextView != null && message.getType() == 0) {
			String text = message.getContent() != null ? message.getContent() : "";
			try {
				JSONObject contentObj = new JSONObject(text);
				int type = contentObj.getInt("type");
				if (type == 0) {
					text = contentObj.getString("data");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch 
				text ="";
				e.printStackTrace();
			}

			messageTextView.setText(text);
		}
	}

}

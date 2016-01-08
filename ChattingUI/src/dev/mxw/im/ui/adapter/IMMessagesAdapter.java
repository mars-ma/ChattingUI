package dev.mxw.im.ui.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import dev.mxw.im.data.bean.IMMessage;
import dev.mxw.im.ui.widget.IMImageMessageMeListItem;
import dev.mxw.im.ui.widget.IMImageMessageOthersListItem;
import dev.mxw.im.ui.widget.IMLocationMessageMeListItem;
import dev.mxw.im.ui.widget.IMLocationMessageOthersListItem;
import dev.mxw.im.ui.widget.IMMessageListItem;
import dev.mxw.im.ui.widget.IMTextMessageMeListItem;
import dev.mxw.im.ui.widget.IMTextMessageOthersListItem;
import dev.mxw.im.ui.widget.IMVoiceMessageMeListItem;
import dev.mxw.im.ui.widget.IMVoiceMessageOthersListItem;
import dev.mxw.im.ui.widget.IMWebMessageMeListItem;
import dev.mxw.im.ui.widget.IMWebMessageOthersListItem;
import dev.mxw.im.user.MyInfo;
import dev.mxw.im.user.OtherInfo;

public class IMMessagesAdapter extends ArrayAdapter<IMMessage> {

	private static final int VIEW_TYPE_TEXT_OTHERS = 0;
	private static final int VIEW_TYPE_TEXT_ME = 1;
	private static final int VIEW_TYPE_IMAGE_OTHERS = 2;
	private static final int VIEW_TYPE_IMAGE_ME = 3;
	private static final int VIEW_TYPE_VOICE_OTHERS = 4;
	private static final int VIEW_TYPE_VOICE_ME = 5;
	private static final int VIEW_TYPE_WEB_OTHERS = 6;
	private static final int VIEW_TYPE_WEB_ME = 7;
	private static final int VIEW_TYPE_LOCATION_OTHERS = 8;
	private static final int VIEW_TYPE_LOCATION_ME = 9;

	public static int playing_id;
	private MyInfo mInfo;
	private OtherInfo oInfo;

	public IMMessagesAdapter(Context context, ArrayList<IMMessage> mMessages, MyInfo myInfo, OtherInfo otherInfo) {
		super(context, 0, mMessages);
		mInfo = myInfo;
		oInfo = otherInfo;
	}

	@Override
	public int getViewTypeCount() {
		return 10;
	}

	@Override
	public int getItemViewType(int position) {
		IMMessage message = getItem(position);
		if (message.getOwner() == message.getFrom()) {
			switch (message.getType()) {
			case 0:
				String text = message.getContent() != null ? message.getContent() : "";
				try {
					JSONObject contentObj = new JSONObject(text);
					int type = contentObj.getInt("type");
					if (type == 0) {
						return VIEW_TYPE_TEXT_ME;
					} else if (type == 1) {
						return VIEW_TYPE_WEB_ME;
					} else if (type == 2) {
						return VIEW_TYPE_LOCATION_ME;
					}
				} catch (JSONException e) {
				}
				return VIEW_TYPE_TEXT_ME;
			case 1:
				return VIEW_TYPE_IMAGE_ME;
			case 2:
				return VIEW_TYPE_VOICE_ME;
			}
		} else {
			switch (message.getType()) {
			case 0:
				String text = message.getContent() != null ? message.getContent() : "";
				try {
					JSONObject contentObj = new JSONObject(text);
					int type = contentObj.getInt("type");
					if (type == 0) {
						return VIEW_TYPE_TEXT_OTHERS;
					} else if (type == 1) {
						return VIEW_TYPE_WEB_OTHERS;
					} else if (type == 2) {
						return VIEW_TYPE_LOCATION_OTHERS;
					}
				} catch (JSONException e) {
				}
				return VIEW_TYPE_TEXT_OTHERS;
			case 1:
				return VIEW_TYPE_IMAGE_OTHERS;
			case 2:
				return VIEW_TYPE_VOICE_OTHERS;
			}
		}
		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		IMMessageListItem view;

		if (convertView == null) {
			switch (getItemViewType(position)) {
			case VIEW_TYPE_IMAGE_ME:
				view = new IMImageMessageMeListItem(getContext(), mInfo, oInfo);
				break;
			case VIEW_TYPE_IMAGE_OTHERS:
				view = new IMImageMessageOthersListItem(getContext(), mInfo, oInfo);
				break;
			case VIEW_TYPE_TEXT_ME:
				view = new IMTextMessageMeListItem(getContext(), mInfo, oInfo);
				break;
			case VIEW_TYPE_TEXT_OTHERS:
				view = new IMTextMessageOthersListItem(getContext(), mInfo, oInfo);
				break;
			case VIEW_TYPE_VOICE_OTHERS:
				view = new IMVoiceMessageOthersListItem(getContext(), mInfo, oInfo);
				break;
			case VIEW_TYPE_VOICE_ME:
				view = new IMVoiceMessageMeListItem(getContext(), mInfo, oInfo);
				break;
			case VIEW_TYPE_WEB_ME:
				view = new IMWebMessageMeListItem(getContext(), mInfo, oInfo);
				break;
			case VIEW_TYPE_WEB_OTHERS:
				view = new IMWebMessageOthersListItem(getContext(), mInfo, oInfo);
				break;
			case VIEW_TYPE_LOCATION_ME:
				view = new IMLocationMessageMeListItem(getContext(), mInfo, oInfo);
				break;
			case VIEW_TYPE_LOCATION_OTHERS:
				view = new IMLocationMessageOthersListItem(getContext(), mInfo, oInfo);
				break;
			default:
				view = new IMTextMessageOthersListItem(getContext(), mInfo, oInfo);
				break;
			}
		} else {
			switch (getItemViewType(position)) {
			case VIEW_TYPE_IMAGE_ME:
				view = (IMImageMessageMeListItem) convertView;
				break;
			case VIEW_TYPE_IMAGE_OTHERS:
				view = (IMImageMessageOthersListItem) convertView;
				break;
			case VIEW_TYPE_TEXT_ME:
				view = (IMTextMessageMeListItem) convertView;
				break;
			case VIEW_TYPE_TEXT_OTHERS:
				view = (IMTextMessageOthersListItem) convertView;
				break;
			case VIEW_TYPE_VOICE_OTHERS:
				view = (IMVoiceMessageOthersListItem) convertView;
				break;
			case VIEW_TYPE_VOICE_ME:
				view = (IMVoiceMessageMeListItem) convertView;
				break;
			case VIEW_TYPE_WEB_ME:
				view = (IMWebMessageMeListItem) convertView;
				break;
			case VIEW_TYPE_WEB_OTHERS:
				view = (IMWebMessageOthersListItem) convertView;
				break;
			case VIEW_TYPE_LOCATION_ME:
				view = (IMLocationMessageMeListItem) convertView;
				break;
			case VIEW_TYPE_LOCATION_OTHERS:
				view = (IMLocationMessageOthersListItem) convertView;
				break;
			default:
				view = (IMTextMessageOthersListItem) convertView;
				break;
			}
		}
		view.setMessage(getItem(position));
		return view;
	}
}

package dev.mxw.im.ui.widget;


import android.content.Context;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import dev.mxw.chattingui.R;
import dev.mxw.im.user.MyInfo;
import dev.mxw.im.user.OtherInfo;
import dev.mxw.widget.ProgressImageView;
import io.github.barbosa.messagesview.library.views.AvatarView;

public class IMImageMessageMeListItem extends IMImageMessageListItem {

    public IMImageMessageMeListItem(Context context,MyInfo myinfo,OtherInfo otherinfo) {
        super(context, myinfo, otherinfo);

        inflate(context, R.layout.chat_message_image_right_list_item, this);

        avatarView = (AvatarView) findViewById(R.id.chat_message_image_right_list_item_avatar);
        dateTextView = (TextView) findViewById(R.id.chat_message_image_right_list_item_date);
        imageNameTextView = (TextView) findViewById(R.id.chat_message_image_right_list_item_file_name);
        resend = (ImageButton) findViewById(R.id.resend);
        iv_pic = (ProgressImageView) findViewById(R.id.iv_pic);
        chat_message_image_right_list_item_bubble = (LinearLayout)findViewById(R.id.chat_message_image_right_list_item_bubble);

    }
}
	
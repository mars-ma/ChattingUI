package dev.mxw.im.ui.widget;


import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import dev.mxw.chattingui.R;
import dev.mxw.im.user.MyInfo;
import dev.mxw.im.user.OtherInfo;
import io.github.barbosa.messagesview.library.views.AvatarView;

public class IMImageMessageOthersListItem extends IMImageMessageListItem {

    public IMImageMessageOthersListItem(Context context,MyInfo myinfo,OtherInfo otherinfo) {
        super(context, myinfo, otherinfo);

        inflate(context, R.layout.chat_message_image_left_list_item, this);

        avatarView = (AvatarView) findViewById(R.id.chat_message_image_left_list_item_avatar);
        dateTextView = (TextView) findViewById(R.id.chat_message_image_left_list_item_date);
        senderNameTextView = (TextView) findViewById(R.id.chat_message_image_left_list_item_sender_name);
        imageNameTextView = (TextView) findViewById(R.id.chat_message_image_left_list_item_file_name);
        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        chat_message_image_left_list_item_bubble= (LinearLayout) findViewById(R.id.chat_message_image_left_list_item_bubble);

    }
}

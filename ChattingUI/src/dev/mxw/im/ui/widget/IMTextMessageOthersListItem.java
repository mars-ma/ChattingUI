package dev.mxw.im.ui.widget;


import android.content.Context;
import android.widget.TextView;
import dev.mxw.chattingui.R;
import dev.mxw.im.user.MyInfo;
import dev.mxw.im.user.OtherInfo;
import io.github.barbosa.messagesview.library.views.AvatarView;

public class IMTextMessageOthersListItem extends IMTextMessageListItem {

    public IMTextMessageOthersListItem(Context context,MyInfo myinfo,OtherInfo otherinfo) {
        super(context, myinfo, otherinfo);

        inflate(context, R.layout.chat_message_text_left_list_item, this);

        avatarView = (AvatarView) findViewById(R.id.chat_message_text_left_list_item_avatar);
        messageTextView = (TextView) findViewById(R.id.chat_message_text_left_list_item_message);
        dateTextView = (TextView) findViewById(R.id.chat_message_text_left_list_item_date);
        senderNameTextView = (TextView) findViewById(R.id.chat_message_text_left_list_item_sender_name);
    }
}

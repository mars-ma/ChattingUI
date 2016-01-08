package dev.mxw.im.ui.widget;


import android.content.Context;
import android.widget.ImageButton;
import android.widget.TextView;
import dev.mxw.chattingui.R;
import dev.mxw.im.config.UIConfig;
import dev.mxw.im.user.MyInfo;
import dev.mxw.im.user.OtherInfo;
import io.github.barbosa.messagesview.library.views.AvatarView;

public class IMTextMessageMeListItem extends IMTextMessageListItem {

    public IMTextMessageMeListItem(Context context,MyInfo myinfo,OtherInfo otherinfo) {
        super(context, myinfo, otherinfo);

        inflate(context, R.layout.chat_message_text_right_list_item, this);

        avatarView = (AvatarView) findViewById(R.id.chat_message_text_right_list_item_avatar);
        messageTextView = (TextView) findViewById(R.id.chat_message_text_right_list_item_message);
        dateTextView = (TextView) findViewById(R.id.chat_message_text_right_list_item_date);
        resend = (ImageButton) findViewById(R.id.resend);
        
        messageTextView.setTextColor(UIConfig.MyTextViewColor);
    }
}

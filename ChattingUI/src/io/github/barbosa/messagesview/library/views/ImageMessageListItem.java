package io.github.barbosa.messagesview.library.views;

import android.content.Context;
import android.widget.TextView;

import io.github.barbosa.messagesview.library.models.Message;

public class ImageMessageListItem extends MessageListItem {

    protected TextView imageNameTextView;

    public ImageMessageListItem(Context context) {
        super(context);
    }

    @Override
    public void setMessage(Message message) {
        super.setMessage(message);

        if (imageNameTextView != null) {
//            imageNameTextView.setText(message.getFileOriginalName());
        }
    }
}

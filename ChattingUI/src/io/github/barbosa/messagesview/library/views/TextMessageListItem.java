package io.github.barbosa.messagesview.library.views;

import android.content.Context;
import android.widget.TextView;

import io.github.barbosa.messagesview.library.models.Message;

public abstract class TextMessageListItem extends MessageListItem {

    protected TextView messageTextView;

    public TextMessageListItem(Context context) {
        super(context);
    }

    public void setMessage(Message message) {
        super.setMessage(message);

        if (messageTextView != null) {
            String text = message.getText() != null ? message.getText() : "";
            messageTextView.setText(text);
        }
    }

}

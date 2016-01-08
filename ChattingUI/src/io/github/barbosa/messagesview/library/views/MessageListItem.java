package io.github.barbosa.messagesview.library.views;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;
import io.github.barbosa.messagesview.library.models.Message;
import io.github.barbosa.messagesview.library.models.Sender;
import io.github.barbosa.messagesview.library.utils.PrettyDate;

public abstract class MessageListItem extends RelativeLayout {

    protected AvatarView avatarView;
    protected TextView dateTextView;
    protected TextView senderNameTextView;

    private MessageSenderType senderType = MessageSenderType.OTHERS;

    public MessageListItem(Context context) {
        super(context);
    }

    public void setMessage(Message message) {
        if (message == null)
            return;

        if (dateTextView != null) {
            String prettyDate = PrettyDate.with(getContext()).prettify(message.getCreatedAt());
            dateTextView.setText(prettyDate);
        }

        Sender sender = message.getSender();
        if (sender == null)
            return;

        if (avatarView != null) {
            avatarView.setName(sender.getFirstName(), sender.getLastName());

            if (sender.getImageURL() != null) {
                Picasso.with(getContext())
                        .load(sender.getImageURL())
                        .resize(80, 80)
                        .into(avatarView.getImageView());
            }
        }

        if (senderNameTextView != null) {
            senderNameTextView.setText(message.getSender().getFirstAndLastName());
        }
    }

    public MessageSenderType getSenderType() {
        return senderType;
    }

    public void setSenderType(MessageSenderType senderType) {
        this.senderType = senderType;
    }

    public enum MessageSenderType {
        ME, OTHERS
    }
}

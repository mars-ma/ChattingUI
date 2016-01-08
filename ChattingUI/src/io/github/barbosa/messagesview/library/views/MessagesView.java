package io.github.barbosa.messagesview.library.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class MessagesView extends ListView {

    private static final int PADDING_BOTTOM = 30;

    public MessagesView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundColor(getResources().getColor(android.R.color.white));
        setSelector(android.R.color.transparent);
        setDivider(null);
        setPadding(0, 0, 0, PADDING_BOTTOM);
        setClipToPadding(false);
    }
}

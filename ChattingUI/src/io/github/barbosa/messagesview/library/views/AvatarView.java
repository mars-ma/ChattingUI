package io.github.barbosa.messagesview.library.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AvatarView extends RelativeLayout {

    private RoundedImageView imageView;
    private TextView textView;

    public AvatarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        textView = new TextView(context);
        textView.setTextColor(context.getResources().getColor(android.R.color.white));
        textView.setTextSize(18);
        textView.setGravity(Gravity.CENTER);
        addView(textView, new ViewGroup.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        imageView = new RoundedImageView(context);
        addView(imageView, new ViewGroup.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    public void setName(String firstName, String lastName) {
        if (textView == null)
            return;

        textView.setText(String.format("%s%s", firstLetterUpper(firstName), firstLetterUpper(lastName)));
    }

    private String firstLetterUpper(String word) {
        if (word == null || word.length() == 0)
            return "";

        return word.substring(0, 1).toUpperCase();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTextView() {
        return textView;
    }
}


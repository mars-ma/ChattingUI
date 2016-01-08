package io.github.barbosa.messagesview.library.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import dev.mxw.chattingui.R;

public class PrettyDate {

    private Context context;

    public PrettyDate(Context context) {
        this.context = context;
    }

    public static PrettyDate with(Context context) {
        return new PrettyDate(context);
    }

    public String prettify(Date date) {
        if (date == null)
            return "";

        long diff = (new Date()).getTime() - date.getTime();

        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        if (seconds < 60)
            return context.getString(R.string.chat_time_now);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        if (minutes < 60)
            return String.format(context.getString(R.string.chat_time_x_minutes_ago), (int) minutes);

        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        if (hours < 12)
            return String.format(context.getString(R.string.chat_time_x_hours_ago), (int) hours);

        DateFormat dateFormat = new SimpleDateFormat("MM'/'dd", Locale.US);
        return dateFormat.format(date);
    }
}

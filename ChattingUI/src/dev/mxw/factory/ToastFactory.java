package dev.mxw.factory;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import dev.mxw.chattingui.R;
import dev.mxw.utils.DensityUtil;

public class ToastFactory {

	public static void showToast(final Context context, String text) {
		showToast(context, text, -1, 1000);
	}

	public static void showToast(final Context context, String text, String default_text) {
		showToast(context, TextUtils.isEmpty(text) ? default_text : text, -1, 1000);
	}

	public static void showWarnToast(final Context context, String text) {
		showToast(context, text);
		// showToast(context, text, R.drawable.toast_gantanhao, 1000);
	}

	public static void showToast(final Context context, String text, int id, long time) {
		if (TextUtils.isEmpty(text.trim())) {
			return;
		}
		Toast toast = new Toast(context.getApplicationContext());
		toast.setGravity(Gravity.CENTER, 0, 0);
		View v = LayoutInflater.from(context).inflate(R.layout.toast_content, null);
		v.setBackgroundDrawable(
				DrawableFactory.makeNoStrokeGradientDrawable(Color.parseColor("#88000000"), DensityUtil.fromDpToPx(5)));
		ImageView imageView = (ImageView) v.findViewById(R.id.toast_img);
		if (id != -1) {
			imageView.setVisibility(View.VISIBLE);
			imageView.setImageResource(id);
		}
		TextView toast_text = (TextView) v.findViewById(R.id.toast_text);
		toast_text.setText(text);
		toast.setView(v);

		toast.setDuration((int) time);
		toast.show();
	}

}

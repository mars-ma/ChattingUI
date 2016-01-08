package dev.mxw.factory;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import dev.mxw.utils.DensityUtil;

public class DrawableFactory {
	public static GradientDrawable makeNoStrokeGradientDrawable(int color, float t_l, float t_r, float b_r, float b_l) {
		GradientDrawable gd = new GradientDrawable();
		gd.setColor(color);
		gd.setCornerRadii(new float[] { t_l, t_l, t_r, t_r, b_r, b_r, b_l, b_l });
		return gd;
	}

	public static GradientDrawable makeNoStrokeGradientDrawable(int color, float radius) {
		GradientDrawable gd = new GradientDrawable();
		gd.setColor(color);
		gd.setCornerRadii(new float[] { radius, radius, radius, radius, radius, radius, radius, radius });
		return gd;
	}

	public static GradientDrawable makeListViewItemTop(int color) {
		return makeNoStrokeGradientDrawable(color, DensityUtil.fromDpToPx(5), DensityUtil.fromDpToPx(5), 0, 0);
	}

	public static GradientDrawable makeListViewItemBottom(int color) {
		return makeNoStrokeGradientDrawable(color, 0, 0, DensityUtil.fromDpToPx(5), DensityUtil.fromDpToPx(5));
	}

	public static GradientDrawable makeListViewItemSingle(int color) {
		return makeNoStrokeGradientDrawable(color, DensityUtil.fromDpToPx(5));
	}

	public static GradientDrawable makeListViewItemNormal(int color) {
		return makeNoStrokeGradientDrawable(color, 0);
	}

	public static GradientDrawable makeNoStrokeGradientDrawable(int color) {

		GradientDrawable gd = new GradientDrawable();
		gd.setColor(color);
		int radius = (int) DensityUtil.fromDpToPx(5);
		gd.setCornerRadii(new float[] { radius, radius, radius, radius, radius, radius, radius, radius });
		return gd;
	}

	public static GradientDrawable makeStrokeGradientDrawable(int color, float t_l, float t_r, float b_r, float b_l,
			int stroke_width, int stroke_color) {
		GradientDrawable gd = new GradientDrawable();
		gd.setColor(color);
		gd.setCornerRadii(new float[] { t_l, t_l, t_r, t_r, b_r, b_r, b_l, b_l });
		gd.setStroke(stroke_width, stroke_color);
		return gd;
	}

	public static GradientDrawable makeStrokeGradientDrawable(int color, float radius, int stroke_width,
			int stroke_color) {
		GradientDrawable gd = new GradientDrawable();
		gd.setColor(color);
		gd.setCornerRadii(new float[] { radius, radius, radius, radius, radius, radius, radius, radius });
		gd.setStroke(stroke_width, stroke_color);
		return gd;
	}

	public static GradientDrawable makeStrokeGradientDrawable(int color, int stroke_color) {
		GradientDrawable gd = new GradientDrawable();
		gd.setColor(color);
		int radius = (int) DensityUtil.fromDpToPx(5);
		gd.setCornerRadii(new float[] { radius, radius, radius, radius, radius, radius, radius, radius });
		gd.setStroke((int) DensityUtil.fromDpToPx(1), stroke_color);
		return gd;
	}

	public static StateListDrawable makeStateListDrawable(Drawable normal, Drawable pressed) {
		StateListDrawable drawable = new StateListDrawable();
		// Non focused states
		drawable.addState(new int[] { -android.R.attr.state_focused, -android.R.attr.state_selected,
				-android.R.attr.state_pressed }, normal);
		drawable.addState(new int[] { -android.R.attr.state_focused, android.R.attr.state_selected,
				-android.R.attr.state_pressed }, normal);
		drawable.addState(new int[] { android.R.attr.state_selected, -android.R.attr.state_pressed }, pressed);
		drawable.addState(new int[] { android.R.attr.state_selected, android.R.attr.state_pressed }, pressed);
		drawable.addState(new int[] { android.R.attr.state_pressed }, pressed);
		return drawable;
	}

	public static GradientDrawable makeOvalDrawable(int inner_color, int stroke_color) {
		GradientDrawable gd = new GradientDrawable();
		gd.setShape(GradientDrawable.OVAL);
		gd.setColor(inner_color);
		gd.setStroke((int) DensityUtil.fromDpToPx(1), stroke_color);
		return gd;
	}

	public static ColorStateList makeColorStateList(int normal_color, int pressed_color) {
		int[] colors = new int[] { pressed_color, pressed_color, normal_color, pressed_color, normal_color,
				normal_color };
		int[][] states = new int[6][];
		states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
		states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
		states[2] = new int[] { android.R.attr.state_enabled };
		states[3] = new int[] { android.R.attr.state_focused };
		states[4] = new int[] { android.R.attr.state_window_focused };
		states[5] = new int[] {};
		ColorStateList color = new ColorStateList(states, colors);
		return color;
	}

	public static StateListDrawable makeReserveBackground(int color, int stroke_color) {
		return makeStateListDrawable(makeStrokeGradientDrawable(color, 0, (int)DensityUtil.fromDpToPx(1), stroke_color),
				makeStrokeGradientDrawable(stroke_color, 0,(int)DensityUtil.fromDpToPx(1), color));
	}
	
	public static StateListDrawable makeReserveBackground(int color, int stroke_color,float radius) {
		return makeStateListDrawable(makeStrokeGradientDrawable(color, radius, (int)DensityUtil.fromDpToPx(1), stroke_color),
				makeStrokeGradientDrawable(stroke_color, radius,(int)DensityUtil.fromDpToPx(1), color));
	}
	
}

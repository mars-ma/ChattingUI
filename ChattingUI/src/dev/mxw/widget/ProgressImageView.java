package dev.mxw.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;
import de.greenrobot.event.EventBus;
import dev.mxw.im.event.UpdateProgressImageView;
import dev.mxw.utils.DensityUtil;
import dev.mxw.utils.Logger;

public class ProgressImageView extends ImageView {

	private Paint mPaint;// 画笔
	int width = 0;
	int height = 0;
	Context context = null;
	int progress = -1;
	int msgId;

	public void setMSGID(int id) {
		this.msgId = id;
	}

	public int getMSGID() {
		return msgId;
	}

	public ProgressImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(UpdateProgressImageView event) {
		if (event.msgId == getMSGID()) {
			if (event.status == 1) {
				setProgress(event.progress);
				Logger.e("ProgressImageView taskId " + event.msgId + " 当前进度 " + event.progress);
			} else {
				Logger.e("ProgressImageView taskId " + event.msgId + " 任务失败");
			}
		}
	}

	public ProgressImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		mPaint = new Paint();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (progress == 100 || progress == -1)
			return;
		mPaint.setAntiAlias(true); // 消除锯齿
		mPaint.setStyle(Paint.Style.FILL);

		mPaint.setColor(Color.parseColor("#70000000"));// 半透明
		canvas.drawRect(0, 0, getWidth(), getHeight() - getHeight() * progress / 100, mPaint);

		mPaint.setColor(Color.parseColor("#00000000"));// 全透明
		canvas.drawRect(0, getHeight() - getHeight() * progress / 100, getWidth(), getHeight(), mPaint);

		mPaint.setTextSize(DensityUtil.fromDpToPx(22));
		mPaint.setColor(Color.parseColor("#FFFFFF"));
		mPaint.setStrokeWidth(2);
		Rect rect = new Rect();
		mPaint.getTextBounds("100%", 0, "100%".length(), rect);// 确定文字的宽度
		canvas.drawText(progress + "%", getWidth() / 2 - rect.width() / 2, getHeight() / 2, mPaint);

	}

	public void setProgress(int progress) {
		this.progress = progress;
		postInvalidate();
	};

}
package dev.mxw.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.graphics.Bitmap;
import android.widget.ImageView;
import dev.mxw.base.BaseApplication;

public class ImageLoader {
	private DisplayImageOptions commonOptions = null;

	public static DisplayImageOptions getCommon(int loadingimg, int emptyImg,
			int errorImg) {
		return new DisplayImageOptions.Builder().cacheOnDisc(true)
				.cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
				.showImageForEmptyUri(emptyImg).showImageOnFail(errorImg)
				.showStubImage(loadingimg)
				.imageScaleType(ImageScaleType.EXACTLY).build();
	}
	
	public static DisplayImageOptions getCommon(int errorImg) {
		return new DisplayImageOptions.Builder().cacheOnDisc(true)
				.cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
				.showImageForEmptyUri(errorImg).showImageOnFail(errorImg)
				.showStubImage(errorImg)
				.imageScaleType(ImageScaleType.EXACTLY).build();
	}
	
	public static DisplayImageOptions getBigCommon(int errorImg) {
		return new DisplayImageOptions.Builder().cacheOnDisc(true)
				.cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
				.showImageForEmptyUri(errorImg).showImageOnFail(errorImg)
				.showStubImage(errorImg)
				.imageScaleType(ImageScaleType.NONE).build();
	}

	/** Բ�� **/
	public static DisplayImageOptions getCommon(int loadingimg, int emptyImg,
			int errorImg, int radius) {
		return new DisplayImageOptions.Builder().cacheOnDisc(true)
				.cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
				.showImageForEmptyUri(emptyImg).showImageOnFail(errorImg)
				.showStubImage(loadingimg)
				.imageScaleType(ImageScaleType.EXACTLY)
				.displayer(new RoundedBitmapDisplayer(radius)).build();
	}

	public static DisplayImageOptions getCommon() {
		return new DisplayImageOptions.Builder().cacheOnDisc(true)
				.cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).build();
	}
	
	public static DisplayImageOptions getCommonUserPic() {
		return new DisplayImageOptions.Builder().cacheOnDisc(false)
				.cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).build();
	}

	private DisplayImageOptions cornerOptions = null;

	public DisplayImageOptions getCorner(int loadingimg, int emptyImg,
			int errorImg, int roundPixels) {
		if (cornerOptions == null) {
			cornerOptions = new DisplayImageOptions.Builder().cacheOnDisc(true)
					.cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
					.showImageForEmptyUri(emptyImg).showImageOnFail(errorImg)
					.showStubImage(loadingimg)
					.displayer(new RoundedBitmapDisplayer(roundPixels))
					.imageScaleType(ImageScaleType.EXACTLY).build();
		}
		return cornerOptions;
	}

	public static void displayImage(String url, ImageView imageView,
			DisplayImageOptions options) {
		BaseApplication.getInstance().getImageLoader()
				.displayImage(url, imageView, options);
	}
	
	public static void displayImage(String url, ImageView imageView,
			int resId) {
		BaseApplication.getInstance().getImageLoader()
				.displayImage(url, imageView, getCommon(resId));
	}

	public static void displayImage(String url, ImageView imageView,
			DisplayImageOptions options, ImageLoadingListener listener) {
		BaseApplication.getInstance().getImageLoader()
				.displayImage(url, imageView, options, listener);
	}

	public static void loadImage(String url, DisplayImageOptions options,
			ImageLoadingListener listener) {
		BaseApplication.getInstance().getImageLoader()
				.loadImage(url, options, listener);
	}

	public static ImageLoader getInstance() {
		// TODO Auto-generated method stub
		return null;
	}
	
    public static String getImagePath(String url){
        return BaseApplication.getInstance().getImageLoader().getDiscCache().get(url).getPath();
    }

}

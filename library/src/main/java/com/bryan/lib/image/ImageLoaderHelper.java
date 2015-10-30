package com.bryan.lib.image;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.HashMap;

/**
 * Get Config of UniversalImageLoader
 */
public class ImageLoaderHelper {

	private static HashMap<String, DisplayImageOptions> displayImageOptions = new HashMap<String, DisplayImageOptions>();

	/**
	 * 初始化ImageLoader，在Application初始化调用
	 * 
	 * @param context
	 */
	public static void initImageLoaderConfiguration(Context context) {
		ImageLoader.getInstance().init(getDefaultImageLoaderConfiguration(context));
	}

	/**
	 * The method is to get the Default UniversalImageLoader config
	 *
	 * @param context
	 * @return ImageLoaderConfiguration
	 */
	public static ImageLoaderConfiguration getDefaultImageLoaderConfiguration(Context context) {
		return getDefaultImageLoaderConfiguration(context, true);
	}

	public static ImageLoaderConfiguration getDefaultImageLoaderConfiguration(Context context, boolean isWriteLog) {
		ImageLoaderConfiguration.Builder builder = getDefaultImageLoaderConfigurationBuilder(context);
		if (isWriteLog) {
			builder.writeDebugLogs();
		}
		ImageLoaderConfiguration config = builder.build();
		return config;
	}

	public static ImageLoaderConfiguration.Builder getDefaultImageLoaderConfigurationBuilder(Context context) {
//        File cacheDir = StorageUtils.getCacheDirectory(context);
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context)
//                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
//                .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75, null)
//                .threadPoolSize(3) // default
				.threadPriority(Thread.NORM_PRIORITY - 2) // default
				.tasksProcessingOrder(QueueProcessingType.LIFO) // default
				.denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
//                .memoryCacheSize(2 * 1024 * 1024)
//                .memoryCacheSizePercentage(13) // default
//                .diskCache(new UnlimitedDiscCache(cacheDir)) // default
				.diskCacheSize(50 * 1024 * 1024) // 50Mb
//                .diskCacheFileCount(1000)
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()) // default
//                .imageDownloader(new BaseImageDownloader(context)) // default
//                .imageDecoder(new BaseImageDecoder(false)) // default
//                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
//                .defaultDisplayImageOptions(getDefaultImageOptions())
		;
		return builder;
	}

	public static Builder getDefaultImageOptionsBuilder() {
		Builder builder = new Builder().resetViewBeforeLoading(false) // default
				//.delayBeforeLoading(1000)
				//.showImageOnLoading(R.drawable.titanic_wave_black)
				.considerExifParams(false) // default
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
				.bitmapConfig(Bitmap.Config.RGB_565) // default
				//.displayer(new SimpleBitmapDisplayer()) // default
				//.handler(new Handler()) // default
				.cacheInMemory(true).cacheOnDisk(true);
		return builder;
	}

	/**
	 * 无预置图片加载配置
	 * 
	 * @return
	 */
	public static DisplayImageOptions getDefaultImageOptions() {
		DisplayImageOptions options = getDefaultImageOptionsBuilder().build();
		return options;
	}

	/**
	 * 预置DrawableId图片加载配置,同时将配置存入HashMap以便多次使用,在Adapter初始化时尽量使用getDisplayImageOptions(DrawableId, false)
	 * 
	 * @param drawableId
	 * @return
	 */
	public static DisplayImageOptions getDisplayImageOptions(int drawableId) {
		return getDisplayImageOptions(drawableId, true);
	}
	
	public static DisplayImageOptions getDisplayImageOptions(int drawableId, int cornerRadius) {
		return getDisplayImageOptions(drawableId, cornerRadius, true);
	}

	/**
	 * 预置drawableId图片加载配置
	 * 
	 * @param drawableId
	 * @param isReusable
	 *            是否多次使用，true:存入Hashmap；false:不存
	 * @return
	 */
	public static DisplayImageOptions getDisplayImageOptions(int drawableId, boolean isReusable) {
		return getDisplayImageOptions(drawableId, drawableId, drawableId, 0, isReusable);
	}

	public static DisplayImageOptions getDisplayImageOptions(int drawableId, int cornerRadius, boolean isReusable) {
		return getDisplayImageOptions(drawableId, drawableId, drawableId, cornerRadius, isReusable);
	}
	/**
	 * 自定义图片加载配置,同时将配置存入HashMap以便多次使用,getDefaultImageOptions() = getDisplayImageOptions(0,0,0)
	 * 
	 * @param loadingDrawableId
	 *            0为不加载对应状态图片，不为0加载对应状态图片
	 * @param emptyDrawableId
	 *            0为不加载对应状态图片，不为0加载对应状态图片
	 * @param failDrawableId
	 *            0为不加载对应状态图片，不为0加载对应状态图片
	 * @return
	 */
	public static DisplayImageOptions getDisplayImageOptions(int loadingDrawableId, int emptyDrawableId, int failDrawableId) {
		return getDisplayImageOptions(loadingDrawableId, emptyDrawableId, failDrawableId, 0, true);
	}
	
	public static DisplayImageOptions getDisplayImageOptions(int loadingDrawableId, int emptyDrawableId, int failDrawableId, int cornerRadius) {
		return getDisplayImageOptions(loadingDrawableId, emptyDrawableId, failDrawableId, cornerRadius, true);
	}

	/**
	 * 自定义图片加载,全为0使用: getDefaultImageOptions() = getDisplayImageOptions(0,0,0)
	 * 
	 * @param loadingDrawableId
	 *            0为不加载对应状态图片，不为0加载对应状态图片
	 * @param emptyDrawableId
	 *            0为不加载对应状态图片，不为0加载对应状态图片
	 * @param failDrawableId
	 *            0为不加载对应状态图片，不为0加载对应状态图片
	 * @param isReusable
	 *            是否多次使用，true:存入hashmap；false:不存
	 * @return
	 */
	public static DisplayImageOptions getDisplayImageOptions(int loadingDrawableId, int emptyDrawableId, int failDrawableId, int cornerRadius, boolean isReusable) {
		DisplayImageOptions options = null;
		Builder builder = null;
		if (!displayImageOptions.containsKey(loadingDrawableId + "_" + emptyDrawableId + "_" + failDrawableId + "_" + cornerRadius)) {
			builder = new Builder().showImageOnLoading(loadingDrawableId).showImageForEmptyUri(emptyDrawableId).showImageOnFail(failDrawableId)
					.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).resetViewBeforeLoading(true);
			if (cornerRadius != 0)
				builder.displayer(new RoundedBitmapDisplayer(cornerRadius));
			options = builder.build();
			if (isReusable)
				displayImageOptions.put(loadingDrawableId + "_" + emptyDrawableId + "_" + failDrawableId + "_" + cornerRadius, options);
		} else {
			options = displayImageOptions.get(loadingDrawableId + "_" + emptyDrawableId + "_" + failDrawableId + "_" + cornerRadius);
		}
		return options;
	}
}

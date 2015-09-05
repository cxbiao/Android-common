package com.bryan.lib.image;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.HashMap;

/**
 * Get Config of UniversalImageLoader
 * "http://site.com/image.png" // from Web
 "file:///mnt/sdcard/image.png" // from SD card
 "file:///mnt/sdcard/video.mp4" // from SD card (video thumbnail)
 "content://media/external/images/media/13" // from content provider
 "content://media/external/video/media/13" // from content provider (video thumbnail)
 "assets://image.png" // from assets
 "drawable://" + R.drawable.img // from drawables (non-9patch images)
 */
public class ImageLoaderHelper {
	
	private static HashMap<String, DisplayImageOptions> displayImageOptions = new HashMap<String, DisplayImageOptions>();
	
	/**
	 * 初始化ImageLoader，在Application初始化调用
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
        return getDefaultImageLoaderConfiguration(context, false);
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
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
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

    public static DisplayImageOptions.Builder getDefaultImageOptionsBuilder() {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)  // default
                //.delayBeforeLoading(1000)
                //.showImageOnLoading(R.drawable.titanic_wave_black)
                .considerExifParams(false) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                //.displayer(new SimpleBitmapDisplayer()) // default
                //.handler(new Handler()) // default
                .cacheInMemory(true)
                .cacheOnDisk(true);
        return builder;
    }
    

    /**
	 * 获取圆角头像builder
	 * 
	 * @return
	 */
	public static DisplayImageOptions.Builder getDefaultHeadIconOptionsBuilder(int resId, boolean isRounded) {
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder().resetViewBeforeLoading(false) // default
				// .delayBeforeLoading(1000)
				// .showImageOnLoading(R.drawable.titanic_wave_black)
				.considerExifParams(false) // default
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
				.bitmapConfig(Bitmap.Config.ARGB_8888) // default
				// default
				// .handler(new Handler()) // default
				.cacheInMemory(true).cacheOnDisk(true).showImageOnLoading(resId).showImageForEmptyUri(resId).showImageOnFail(resId);
		if (isRounded) {
			builder.displayer(new RoundedBitmapDisplayer(10));
		}
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
	 * 获取圆通头像Options
	 * 
	 * @return
	 */
	public static DisplayImageOptions getDisplayHeadIconOptions(int resId, boolean isRounded) {
		DisplayImageOptions options = getDefaultHeadIconOptionsBuilder(resId, isRounded).build();
		return options;
	}
    
	/**
	 * 预置layoutId图片加载配置,同时将配置存入HashMap以便多次使用,在Adapter初始化时尽量使用getDisplayImageOptions(layoutId, false)
	 * 
	 * @param layoutId
	 * @return
	 */
	public static DisplayImageOptions getDisplayImageOptions(int layoutId) {
		return getDisplayImageOptions(layoutId, true);
	}
	
	/**
	 * 预置layoutId图片加载配置
	 * 
	 * @param layoutId
	 * @param isReusable
	 *            是否多次使用，true:存入Hashmap；false:不存
	 * @return
	 */
	public static DisplayImageOptions getDisplayImageOptions(int layoutId, boolean isReusable) {
		return getDisplayImageOptions(layoutId, layoutId, layoutId, isReusable);
	}
	
	/**
	 * 自定义图片加载配置,同时将配置存入HashMap以便多次使用,getDefaultImageOptions() = getDisplayImageOptions(0,0,0)
	 * 
	 * @param loadingLayoutId
	 *            0为不加载对应状态图片，不为0加载对应状态图片
	 * @param emptyLayoutId
	 *            0为不加载对应状态图片，不为0加载对应状态图片
	 * @param failLayoutId
	 *            0为不加载对应状态图片，不为0加载对应状态图片
	 * @return
	 */
	public static DisplayImageOptions getDisplayImageOptions(int loadingLayoutId, int emptyLayoutId, int failLayoutId) {
		return getDisplayImageOptions(loadingLayoutId, emptyLayoutId, failLayoutId, true);
	}
	
	/**
	 * 自定义图片加载,全为0使用: getDefaultImageOptions() = getDisplayImageOptions(0,0,0)
	 * 
	 * @param loadingLayoutId
	 *            0为不加载对应状态图片，不为0加载对应状态图片
	 * @param emptyLayoutId
	 *            0为不加载对应状态图片，不为0加载对应状态图片
	 * @param failLayoutId
	 *            0为不加载对应状态图片，不为0加载对应状态图片
	 * @param isReusable
	 *            是否多次使用，true:存入hashmap；false:不存
	 * @return
	 */
	public static DisplayImageOptions getDisplayImageOptions(int loadingLayoutId, int emptyLayoutId, int failLayoutId, boolean isReusable) {
		DisplayImageOptions options = null;
		if (!displayImageOptions.containsKey(loadingLayoutId + "_" + emptyLayoutId + "_" + failLayoutId)) {
			options = new DisplayImageOptions.Builder().showImageOnLoading(loadingLayoutId).showImageForEmptyUri(emptyLayoutId).showImageOnFail(failLayoutId).cacheInMemory(true)
					.cacheOnDisk(true).considerExifParams(true)/*.displayer(new RoundedBitmapDisplayer(20))*/.build();
			if(isReusable)
				displayImageOptions.put(loadingLayoutId + "_" + emptyLayoutId + "_" + failLayoutId, options);
		} else {
			options = displayImageOptions.get(loadingLayoutId + "_" + emptyLayoutId + "_" + failLayoutId);
		}
		return options;
	}


}

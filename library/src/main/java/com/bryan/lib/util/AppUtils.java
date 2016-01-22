package com.bryan.lib.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.bryan.lib.log.KLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 应用信息工具类
 */
public class AppUtils {

	public static final String DEFAULT_INSTALL = "com.android.AutoInstallFlash.INSTALL"; // 发送静默安装Action字符串
	public static final String DEFAULT_UNINSTALL = "com.android.AutoInstallFlash.UNINSTALL";// 发送静默删除Action字符串
	public static final String KEY_INSTALL = "key_install";// 发送静默安装APK路径数组关键字
	public static final String KEY_UNINSTALL = "key_uninstall";// 发送静默删除APK包名数组关键字

	/**
	 * 通过应用包名获取应用信息
	 * 
	 * @param context
	 *            上下文
	 * @param packageName
	 *            应用包名
	 * @return 应用信息对象 null:应用未安装
	 */
	public static Map<String, Object> getAppInfo(Context context, String packageName) {
		Map<String, Object> appInfoMap = null;
		try {
			// 打开应用程序
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
			if (packageInfo != null && packageInfo.applicationInfo != null) {
				appInfoMap = new HashMap<String, Object>();
				appInfoMap.put("versionCode", packageInfo.versionCode);// 应用版本号
				appInfoMap.put("versionName", packageInfo.versionName);// 应用版本名称
				appInfoMap.put("appName", packageInfo.applicationInfo.loadLabel(context.getPackageManager()));// 应用名称
				appInfoMap.put("className", packageInfo.applicationInfo.className);// 应用启动类
				appInfoMap.put("launcherClassName", context.getPackageManager().getLaunchIntentForPackage(packageName));
				appInfoMap.put("appIcon", packageInfo.applicationInfo.loadIcon(context.getPackageManager()));
			}
		} catch (NameNotFoundException e) {
			KLog.e("没找到已安装的apk包", e);
		}
		return appInfoMap;
	}

	/**
	 * 判断应用是否已安装
	 * 
	 * @param context
	 *            上下文对象
	 * @param packageName
	 *            应用包名
	 * @return 是否已安装 true:已安装 false:未安装
	 */
	public static boolean checkAppInstall(Context context, String packageName) {
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo packageInfo = manager.getPackageInfo(packageName, 0);
			if (packageInfo != null && packageInfo.applicationInfo != null && !TextUtils.isEmpty(packageInfo.applicationInfo.packageName)) {
				// 应用已安装
				return true;
			}
		} catch (NameNotFoundException e) {
			// 应用未安装
			KLog.i("NameNotFoundException ::->" + packageName);
		}
		return false;
	}

	/**
	 * 通过APK文件目录获取APK文件信息
	 * 
	 * @param context
	 *            上下文对象
	 * @param apkFilePath
	 *            APK文件目录
	 * @return null:如果返回为空,则说明文件不可用.
	 */
	public static Map<String, Object> getApkFileInfo(Context context, String apkFilePath) {
		// APk信息Map
		Map<String, Object> appInfoMap = null;
		try {
			// 如果文件不存在,则直接返回空.
			if (!(new File(apkFilePath).exists())) {
				return null;
			}
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
			if (packageInfo != null) {
				appInfoMap = new HashMap<String, Object>();
				// ApplicationInfo appInfo = packageInfo.applicationInfo;
				// String appName =
				// packageManager.getApplicationLabel(appInfo).toString();//应用名称
				// String packageName = appInfo.packageName;//得到安装包名称
				// Drawable icon =
				// packageManager.getApplicationIcon(appInfo);//得到图标信息
				appInfoMap.put("versionName", packageInfo.versionName);// 应用版本名称
				appInfoMap.put("versionCode", packageInfo.versionCode);// 应用版本号
			}
		} catch (Exception e) {
		}
		return appInfoMap;
	}

	/**
	 * 调用系统默认方式安装应用
	 * 
	 * @param context
	 *            上下文对象
	 * @param apkPath
	 *            APK文件目录
	 */
	public static void defaultInstallApp(Context context, String apkPath) {
		// 安装应用
		try {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
			context.startActivity(i);
		} catch (Exception e) {
			// 如果安装时异常,则删除该应用文件.
			File file = new File(apkPath);
			if (file != null && file.exists()) {
				file.delete();
			}
			KLog.e("安装应用[" + apkPath + "]异常...", e);
		}
	}

	/**
	 * 调用系统默认方式卸载应用
	 * 
	 * @param context
	 *            上下文对象
	 * @param appPackageName
	 *            要卸载的应用包名
	 */
	public static void defaultUninstallApp(Context context, String appPackageName) {
		try {
			// 卸载应用程序
			// 通过程序的报名创建URI
			Uri packageURI = Uri.parse("package:" + appPackageName);
			// 创建Intent意图
			Intent intent = new Intent(Intent.ACTION_DELETE, packageURI);
			// 执行卸载程序
			context.startActivity(intent);
		} catch (Exception e) {
			KLog.e("卸载应用[" + appPackageName + "]异常...", e);
		}
	}

	/**
	 * 启动应用
	 * 
	 * @param context
	 *            上下文
	 * @param appPackageName
	 *            应用包名
	 */
	public static void startApplication(Context context, String appPackageName) {
		Intent intent = null;
		try {
			// 通过应用包名获取应用Intent
			PackageManager packageManager = context.getPackageManager();
			intent = packageManager.getLaunchIntentForPackage(appPackageName);
			if (intent != null) {
				context.startActivity(intent);
			} else {
				KLog.e("启动应用[" + appPackageName + "]时获取应用Intent失败...");
			}
		} catch (Exception e) {
			KLog.e("启动应用[" + appPackageName + "," + intent + "]", e);
		}
	}




	/**
	 * 检验Apk文件是否完整可用
	 * 
	 * @param apkPath
	 *            APK文件路径
	 * @return true:可用 false:不可用
	 */
	public static boolean checkingApkAvailable(Context context, String apkPath) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
		if (packageInfo != null) {
			ApplicationInfo appInfo = packageInfo.applicationInfo;
			if (packageManager != null && appInfo != null) {
				/*
				 * try { String appName = StringUtil.toString(packageManager.getApplicationLabel (appInfo));//应用名称 } catch(Exception e){}
				 */
				String packageName = appInfo.packageName;// 得到安装包名称
				String version = packageInfo.versionName;// 得到版本信息
				// Drawable icon =
				// packageManager.getApplicationIcon(appInfo);//得到图标信息
				if (!TextUtils.isEmpty(packageName) && !TextUtils.isEmpty(version)) {
					return true;
				}
			}
		}
		return false;
	}

	// 软件类型判断软件
	// 未知软件类型
	public static final int UNKNOW_APP = 0;
	// 用户软件类型
	public static final int USER_APP = 1;
	// 系统软件
	public static final int SYSTEM_APP = 2;
	// 系统升级软件
	public static final int SYSTEM_UPDATE_APP = 4;
	// 系统+升级软件
	public static final int SYSTEM_REF_APP = SYSTEM_APP | SYSTEM_UPDATE_APP;

	/**
	 * 检查app是否是系统rom集成的
	 * 
	 * @param pname
	 * @return
	 */
	public static int checkAppType(Context context, String pname) {
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(pname, 0);
			// 是系统软件或者是系统软件更新
			if (isSystemApp(pInfo) || isSystemUpdateApp(pInfo)) {
				return SYSTEM_REF_APP;
			} else {
				return USER_APP;
			}

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return UNKNOW_APP;
	}

	public static boolean isSystemApp(PackageInfo pInfo) {
		return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
	}

	public static boolean isSystemUpdateApp(PackageInfo pInfo) {
		return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
	}

	/**
	 * 获取应用版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			// 当前应用的版本名称
			String versionName = info.versionName;
			// 当前版本的版本号
			// int versionCode = info.versionCode;
			// 当前版本的包名
			// String packageNames = info.packageName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";

	}

	/**
	 * 获取应用版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			// 当前应用的版本名称
			// String versionName = info.versionName;
			// 当前版本的版本号
			int versionCode = info.versionCode;
			// 当前版本的包名
			// String packageNames = info.packageName;
			return versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;

	}

	/**
	 * 获取应用名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getApplicationName(Context context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	/**
	 * 获取application中指定的meta-data
	 *
	 * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
	 */
	public static String getAppMetaData(Context ctx, String key) {
		if (ctx == null || TextUtils.isEmpty(key)) {
			return null;
		}
		String resultData = null;
		try {
			PackageManager packageManager = ctx.getPackageManager();
			if (packageManager != null) {
				ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
				if (applicationInfo != null) {
					if (applicationInfo.metaData != null) {
						resultData = applicationInfo.metaData.getString(key);
					}
				}

			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return resultData;
	}

	/**
	 * 判断应用是否前台启动 需要权限:android.permission.GET_TASKS
	 *
	 * @param context
	 * @return
	 */
	public static boolean isAppRunningForeground(Context context)
	{
		ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfos= activityManager.getRunningTasks(1);
        ComponentName componentName=taskInfos.get(0).topActivity;
        return context.getPackageName().equalsIgnoreCase(componentName.getPackageName());
	}

	public static String getTopActivityName(Context context)
	{
		ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List <RunningTaskInfo> taskInfos = activityManager.getRunningTasks(1);
		return taskInfos.get(0).topActivity.getClassName();
	}

	public static List<String> getRunningApps(Context context)
	{
		ArrayList localArrayList = new ArrayList();
		ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
		Iterator<ActivityManager.RunningAppProcessInfo> localIterator = appProcessInfos.iterator();
		while (localIterator.hasNext())
		{
			ActivityManager.RunningAppProcessInfo appProcessInfo = localIterator.next();
			String str = appProcessInfo.processName;
			if (str.contains(":"))
				str = str.substring(0, str.indexOf(":"));
			if (localArrayList.contains(str))
				continue;
			localArrayList.add(str);
		}
		return localArrayList;
	}


	/**
	 * Activity位于堆栈的顶层
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isTopActivity(Context context, String className) {
		String top=getTopActivityName(context);
		return  top.compareToIgnoreCase(className)==0;
	}

	/**
	 * 判断应用是否已经启动
	 * 
	 * @param context
	 *            一个context
	 * @param packageName
	 *            要判断应用的包名
	 * @return boolean
	 */
	public static boolean isAppAlive(Context context, String packageName) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
		for (int i = 0; i < processInfos.size(); i++) {
			if (processInfos.get(i).processName.equals(packageName)) {
				Log.i("NotificationLaunch", String.format("the %s is running, isAppAlive return true", packageName));
				return true;
			}
		}
		Log.i("NotificationLaunch", String.format("the %s is not running, isAppAlive return false", packageName));
		return false;
	}



	/**
	 * 判断Activity是否存在启动
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isActivityRunning(Context context,String className) {
		ComponentName cmpName = new ComponentName(context.getPackageName(),className);
		boolean bIsExist = false;
		if (cmpName != null) { // 说明系统中存在这个activity
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
			KLog.i("---isActivityRunning---taskInfoList.size:" + taskInfoList.size());
			for (RunningTaskInfo taskInfo : taskInfoList) {
				KLog.i("---isActivityRunning---taskInfo:" + taskInfo.baseActivity);
				if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
					bIsExist = true;
					break;
				}
			}
		}
		return bIsExist;
	}

	public boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(100);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

}

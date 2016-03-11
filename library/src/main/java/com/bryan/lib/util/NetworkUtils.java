package com.bryan.lib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.bryan.lib.log.KLog;

public class NetworkUtils {
	
	private static final String TAG = "NetworkUtils";
	public enum NetType {
		MOBILE(1),
		MOBILE_2G(2),
		MOBILE_3G(3),
		MOBILE_4G(4),
		WIFI(5),
		UNKNOWN(6);
		NetType(int value) {
			this.value = value;
		}
		public int value;
	}

	/**
	 * 获取ConnectivityManager
	 */
	public static ConnectivityManager getConnManager(Context context) {
		return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	
	/**
	 * 检测网络是否为可用状态
	 */
	public static boolean isNetworkConnected(Context context) {
		NetworkInfo net = getConnManager(context).getActiveNetworkInfo();
		return net != null && net.isAvailable();
	}
	
	/**
	 * 判断网络连接是否有效（此时可传输数据）。
	 * @param context
	 * @return boolean 不管wifi，还是mobile net，只有当前在连接状态（可有效传输数据）才返回true,反之false。
	 */
	public static boolean isConnected(Context context) {
		NetworkInfo net = getConnManager(context).getActiveNetworkInfo();
		return net != null && net.isConnected();
	}
	
	public static NetType getConnectedType(Context context) {
		NetworkInfo net = getConnManager(context).getActiveNetworkInfo();
		if (net != null) {
			switch (net.getType()) {
				case ConnectivityManager.TYPE_WIFI :
					return NetType.WIFI;
				case ConnectivityManager.TYPE_MOBILE :
					return NetType.MOBILE;
				default :
					return NetType.UNKNOWN;
			}
		}
		return NetType.UNKNOWN;
	}


	public static NetType getExactNetworkType(Context context) {
		NetType type =NetType.UNKNOWN;

		NetworkInfo networkInfo = getConnManager(context).getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				type = NetType.WIFI;
			} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				String typeName = networkInfo.getSubtypeName();

				KLog.e("Network getSubtypeName : " + typeName);

				int networkType = networkInfo.getSubtype();
				switch (networkType) {
					case TelephonyManager.NETWORK_TYPE_GPRS:
					case 16:                                   //TelephonyManager.NETWORK_TYPE_GSM:
					case TelephonyManager.NETWORK_TYPE_EDGE:
					case TelephonyManager.NETWORK_TYPE_CDMA:
					case TelephonyManager.NETWORK_TYPE_1xRTT:
					case TelephonyManager.NETWORK_TYPE_IDEN:
						type = NetType.MOBILE_2G;
						break;
					case TelephonyManager.NETWORK_TYPE_UMTS:
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
					case TelephonyManager.NETWORK_TYPE_EVDO_A:
					case TelephonyManager.NETWORK_TYPE_HSDPA:
					case TelephonyManager.NETWORK_TYPE_HSUPA:
					case TelephonyManager.NETWORK_TYPE_HSPA:
					case TelephonyManager.NETWORK_TYPE_EVDO_B:
					case TelephonyManager.NETWORK_TYPE_EHRPD:
					case TelephonyManager.NETWORK_TYPE_HSPAP:
					case 17:                                     //TelephonyManager.NETWORK_TYPE_TD_SCDMA
						type = NetType.MOBILE_3G;
						break;
					case TelephonyManager.NETWORK_TYPE_LTE:
					case 18:            //NETWORK_TYPE_IWLAN
						type = NetType.MOBILE_4G;
						break;
					default:
						// TD-SCDMA 中国移动 联通 电信 三种3G制式
						if (typeName.equalsIgnoreCase("TD-SCDMA") || typeName.equalsIgnoreCase("WCDMA")
								|| typeName.equalsIgnoreCase("CDMA2000")) {
							type = NetType.MOBILE_3G;
						}
						break;
				}

				KLog.e("Network getSubtype : " + networkType);
			}
		}
		KLog.e("Network Type : " + type);
		return type;
	}
	

	
	/**
	 * 打印当前各种网络状态
	 * @param context
	 * @return boolean
	 */
	public static boolean printNetworkInfo(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo in = connectivity.getActiveNetworkInfo();
			Log.i(TAG, "-------------$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$-------------");
			Log.i(TAG, "getActiveNetworkInfo: " + in);
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					// if (info[i].getType() == ConnectivityManager.TYPE_WIFI) {
					Log.i(TAG, "NetworkInfo[" + i + "]isAvailable : " + info[i].isAvailable());
					Log.i(TAG, "NetworkInfo[" + i + "]isConnected : " + info[i].isConnected());
					Log.i(TAG, "NetworkInfo[" + i + "]isConnectedOrConnecting : " + info[i].isConnectedOrConnecting());
					Log.i(TAG, "NetworkInfo[" + i + "]: " + info[i]);
					// }
				}
				Log.i(TAG, "\n");
			} else {
				Log.i(TAG, "getAllNetworkInfo is null");
			}
		}
		return false;
	}
}

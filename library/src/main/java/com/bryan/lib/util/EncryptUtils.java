package com.bryan.lib.util;
import android.content.Context;
import android.util.Base64;

import com.facebook.crypto.Crypto;
import com.facebook.crypto.Entity;
import com.facebook.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.util.SystemNativeCryptoLibrary;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 加密工具类
 * 
 * @author bryan
 * 需要libconceal.so和libcryptox.so从及crypto.jar
 * 
 */
public class EncryptUtils {
	private static final int BUFFER_SIZE = 1024;
    private static Crypto mCrypto;
    
    
    private static Crypto getCrypto(Context context){
    	if(mCrypto==null){
    		synchronized(EncryptUtils.class){
    			if(mCrypto==null){
    				mCrypto=new Crypto(new SharedPrefsBackedKeyChain(context), new SystemNativeCryptoLibrary());
    			}
    		}
    	}
    	return mCrypto;
    }
	
	
	/**
	 * 移除加密文件
	 * 
	 * @param context
	 * @param key
	 */
	public static void removeCrypto(Context context, String key) {
		File file = new File(context.getFilesDir() + "/" + encryptBASE64(key + context.getPackageName()));
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 文件加密
	 */
	public static void encyptCrypto(Context context, String key, String value) {
		try {
			// Creates a new Crypto object with default implementations of
			// a key chain as well as native library.
			Crypto crypto =getCrypto(context);
			// Check for whether the crypto functionality is available
			// This might fail if Android does not load libaries correctly.
			if (!crypto.isAvailable()) {
				return;
			}

			OutputStream fileStream = new BufferedOutputStream(new FileOutputStream(context.getFilesDir() + "/" + encryptBASE64(key + context.getPackageName())));
			// Creates an output stream which encrypts the data as
			// it is written to it and writes it out to the file.
			OutputStream outputStream = crypto.getCipherOutputStream(fileStream, new Entity(context.getPackageName()));

			// Write plaintext to it.
			outputStream.write(value.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 文件解密
	 * 
	 * @return
	 */
	public static String decryptCrypto(Context context, String key) {
		try {
			// Creates a new Crypto object with default implementations of
			// a key chain as well as native library.
			Crypto crypto = getCrypto(context);
			// Check for whether the crypto functionality is available
			// This might fail if Android does not load libaries correctly.
			if (!crypto.isAvailable()) {
				return "";
			}

			// Get the file to which ciphertext has been written.
			FileInputStream fileStream = new FileInputStream(new File(context.getFilesDir() + "/" + encryptBASE64(key + context.getPackageName())));

			// Creates an input stream which decrypts the data as
			// it is read from it.
			InputStream inputStream = crypto.getCipherInputStream(fileStream, new Entity(context.getPackageName()));

			// Read into a byte array.
			int read;
			byte[] buffer = new byte[1024];

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			// You must read the entire stream to completion.
			// The verification is done at the end of the stream.
			// Thus not reading till the end of the stream will cause
			// a security bug. For safety, you should not
			// use any of the data until it's been fully read or throw
			// away the data if an exception occurs.
			while ((read = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, read);
			}
			String out = new String(outputStream.toByteArray());
			inputStream.close();
			outputStream.close();
			return out;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	/**
	 * BASE64 加密
	 * 
	 * @param str
	 * @return
	 */
	public static String encryptBASE64(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		try {
			byte[] encode = str.getBytes("UTF-8");
			// base64 加密
			return new String(Base64.encode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * BASE64 解密
	 * 
	 * @param str
	 * @return
	 */
	public static String decryptBASE64(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		try {
			byte[] encode = str.getBytes("UTF-8");
			// base64 解密
			return new String(Base64.decode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * GZIP 加密
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] encryptGZIP(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}

		try {
			// gzip压缩
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(baos);
			gzip.write(str.getBytes("UTF-8"));

			gzip.close();

			byte[] encode = baos.toByteArray();

			baos.flush();
			baos.close();

			// base64 加密
			return encode;
			// return new String(encode, "UTF-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * GZIP 解密
	 * 
	 * @param str
	 * @return
	 */
	public static String decryptGZIP(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}

		try {

			byte[] decode = str.getBytes("UTF-8");

			// gzip 解压缩
			ByteArrayInputStream bais = new ByteArrayInputStream(decode);
			GZIPInputStream gzip = new GZIPInputStream(bais);

			byte[] buf = new byte[BUFFER_SIZE];
			int len = 0;

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			while ((len = gzip.read(buf, 0, BUFFER_SIZE)) != -1) {
				baos.write(buf, 0, len);
			}
			gzip.close();
			baos.flush();

			decode = baos.toByteArray();

			baos.close();

			return new String(decode, "UTF-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 十六进制字符串 转换为 byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789abcdef".indexOf(c);
		// return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * byte[] 转换为 十六进制字符串
	 * 
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");

		if (src == null || src.length <= 0) {
			return null;
		}

		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
}

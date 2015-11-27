package com.bryan.lib.util;

import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {
    Cipher cipher = null;
    Cipher decipher = null;
    public static final int ALGORIGHM_DES = 0;
    public static final int ALGORIGHM_AES = 1;
    //AES key
    byte[] keyBytes = {74, 101, 104, 110, 118, 111, 110, 77, 97, 74, 105, 70, 97, 110, 103, 74, 101, 114, 118, 105, 115, 76, 105, 117, 76, 105, 117, 83, 104, 97, 111, 90};
    //DES key
    String key = "bryanhello";
    static final String HEXES = "0123456789ABCDEF";


    public void init(int param) {
        if (param == 0)
            initDES();
        else
            initAES();
    }

    public void initDES() {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("md5");
            byte[] bytes = localMessageDigest.digest(this.key.getBytes("utf-8"));
            this.keyBytes = Arrays.copyOf(bytes, 24);
            int i = 0;
            int j = 16;
            while (i < 8)
                this.keyBytes[(j++)] = this.keyBytes[(i++)];
            SecretKeySpec localSecretKeySpec = new SecretKeySpec(this.keyBytes, "DESede");
            IvParameterSpec localIvParameterSpec = new IvParameterSpec(new byte[8]);
            this.cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            this.cipher.init(1, localSecretKeySpec, localIvParameterSpec);
            this.decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            this.decipher.init(2, localSecretKeySpec, localIvParameterSpec);
            Log.d("encrypt", "initital for DES");
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public void initAES() {
        try {
            this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec localSecretKeySpec = new SecretKeySpec(this.keyBytes, "AES");
            this.cipher.init(1, localSecretKeySpec);
            this.decipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            this.decipher.init(2, localSecretKeySpec);
            Log.d("encrypt", "initital for AES");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static String getMD5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            byte[] bytes = digest.digest();
            return getHex(bytes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String encryptBase64String(String param) throws Exception {
        byte[] bytes = encrypt(param);
        return new String(Base64.encode(bytes, Base64.DEFAULT));
    }

    public String encryptString(String param) throws Exception {
        byte[] bytes = encrypt(param);
        return new String(getHex(bytes));
    }

    public String decryptBase64String(String param) throws Exception {
        byte[] base64Bytes = Base64.decode(param, Base64.DEFAULT);
        byte[] bytes = decrypt(base64Bytes);
        return new String(bytes, "UTF-8");
    }

    public String decryptString(String param) throws Exception {
        byte[] hexBytes = hex2Bytes(param);
        byte[] bytes = decrypt(hexBytes);
        return new String(bytes, "UTF-8");
    }


    public byte[] encrypt(String param) throws Exception {
        byte[] base64Bytes = param.getBytes("UTF-8");
        byte[] bytes = this.cipher.doFinal(base64Bytes);
        return bytes;
    }

    public byte[] encrypt(byte[] param) throws Exception {
        return this.cipher.doFinal(param);
    }

    public byte[] decrypt(byte[] param) throws Exception {
        return this.decipher.doFinal(param);
    }



    public static String getEncodeBase64(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            byte[] encode = str.getBytes("UTF-8");
            return new String(Base64.encode(encode, Base64.DEFAULT), "UTF-8");

        }  catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getDecodeBase64(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            byte[] encode = str.getBytes("UTF-8");
            return new String(Base64.decode(encode, Base64.DEFAULT), "UTF-8");

        }  catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static String getHex(byte[] param) {
        if (param == null)
            return null;
        StringBuilder stringBuilder = new StringBuilder(2 * param.length);
        byte[] bytes = param;
        int k = param.length;
        for (int j = 0; j < k; j++) {
            int i = bytes[j];
            stringBuilder.append("0123456789ABCDEF".charAt((i & 0xF0) >> 4)).append("0123456789ABCDEF".charAt(i & 0xF));
        }
        return stringBuilder.toString();
    }


    public static byte[] hex2Bytes(String param) throws NumberFormatException {
        byte[] arr = param.getBytes();
        int iLen = arr.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arr, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }
}


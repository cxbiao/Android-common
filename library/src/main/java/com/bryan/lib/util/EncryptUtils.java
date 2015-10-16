package com.bryan.lib.util;

import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 安全加密类
 */
public class EncryptUtils {


    private static final int BUFFER_SIZE = 1024;
    public static String getMD5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            return getHashString(digest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }


    private static byte[] coderByDES(byte[] plainText, String key, int mode)
            throws InvalidKeyException, InvalidKeySpecException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            BadPaddingException, IllegalBlockSizeException,
            UnsupportedEncodingException {
        SecureRandom sr = new SecureRandom();
        byte[] resultKey = makeKey(key);
        DESKeySpec desSpec = new DESKeySpec(resultKey);
        SecretKey secretKey = SecretKeyFactory.getInstance("DES")
                .generateSecret(desSpec);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(mode, secretKey, sr);
        return cipher.doFinal(plainText);
    }

    /** */
    /**
     * 生产8位的key
     *
     * @param key 字符串形式
     * @return
     * @throws UnsupportedEncodingException
     * @author yayagepei
     * @date 2008-10-8
     */
    private static byte[] makeKey(String key)
            throws UnsupportedEncodingException {
        byte[] keyByte = new byte[8];
        byte[] keyResult = key.getBytes("UTF-8");
        for (int i = 0; i < keyResult.length && i < keyByte.length; i++) {
            keyByte[i] = keyResult[i];
        }
        return keyByte;
    }

    /** */
    /**
     * DES加密
     *
     * @param plainText 明文
     * @param key       密钥
     * @return
     * @author yayagepei
     * @date 2008-10-8
     */
    public static String encoderByDES(String plainText, String key) {
        try {
            byte[] result = coderByDES(plainText.getBytes("UTF-8"), key,
                    Cipher.ENCRYPT_MODE);
            return byteArr2HexStr(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /** */
    /**
     * DES解密
     *
     * @param secretText 密文
     * @param key        密钥
     * @return
     * @author yayagepei
     * @date 2008-10-8
     */
    public static String decoderByDES(String secretText, String key) {
        try {
            byte[] result = coderByDES(hexStr2ByteArr(secretText), key,
                    Cipher.DECRYPT_MODE);
            return new String(result, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /** */
    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     */
    private static String byteArr2HexStr(byte[] arrB) {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /** */
    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     *
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     * @throws NumberFormatException
     */
    private static byte[] hexStr2ByteArr(String strIn)
            throws NumberFormatException {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}


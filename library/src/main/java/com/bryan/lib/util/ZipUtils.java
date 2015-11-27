/*
 *
 * COPYRIGHT NOTICE
 * Copyright (C) 2015, bryan <690158801@qq.com>
 * https://github.com/cxbiao/Android-common
 *
 * @license under the Apache License, Version 2.0
 *
 * @version 1.0
 * @author  bryan
 * @date    2015/11/27
 *
 */

package com.bryan.lib.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Authro：Cxb on 2015/11/27 10:44
 */
public class ZipUtils {

    private static final int BUFFER_SIZE = 1024;

    public static byte[] encodeGZIP(String str) {
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
            return encode;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String decodeGZIP(String str) {
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

package com.smtlibrary.utils;

import android.util.Log;

/**
 * Description : 日子工具类
 * Created by gbh on 16/6/25.
 */
public class LogUtils {
    public static boolean LOG_DEBUG = false;

    public static void v(String tag, String message) {
        if (LOG_DEBUG) {
            Log.v(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (LOG_DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (LOG_DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (LOG_DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (LOG_DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Exception e) {
        if (LOG_DEBUG) {
            Log.e(tag, message, e);
        }
    }

    public static void sysout(String tag, Object message) {
        if (LOG_DEBUG) {
            System.out.println(tag + ":" + String.valueOf(message));
        }
    }


}

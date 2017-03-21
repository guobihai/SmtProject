package com.smtlibrary.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * @time 2016-06-16
 */
public class PreferenceUtils {

	/**
	 *
	 * @param context
	 */
	public static void reset(final Context context) {
		PreferenceManager.getDefaultSharedPreferences(context).edit().clear()
				.commit();
	}

	/**
	 *
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getString(final Context context, String key,
			String defValue) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(key, defValue);
	}

	/**
	 *
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putString(final Context context, String key, String value) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString(key, value).commit();
	}

	/**
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getInt(final Context context, String key, int defValue) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(
				key, defValue);
	}

	/**
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putInt(final Context context, String key, int value) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putInt(key, value).commit();
	}

	public static void putBoolean(final Context context, String key,
			Boolean value) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putBoolean(key, value).commit();
	}

	public static boolean getBoolean(final Context context, String key,
			boolean defValue) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(key, defValue);
	}

	public static void putObject(final Context context,String key, Object object) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(baos);
			out.writeObject(object);
			String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
			PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, objectVal).commit();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static <T> T getObject(final Context context,String key, Class<T> clazz) {
		if (PreferenceManager.getDefaultSharedPreferences(context).contains(key)) {
			String objectVal = PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
			byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
			ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(bais);
				T t = (T) ois.readObject();
				return t;
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					if (bais != null) {
						bais.close();
					}
					if (ois != null) {
						ois.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}

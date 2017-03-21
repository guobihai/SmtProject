package com.smtlibrary.https;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.internal.$Gson$Types;
import com.smtlibrary.SmtApplication;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * guobihai
 * OkHttp 网络请求封装类
 * Created by gbh on 16/6/25.
 * application/xhtml+xml ：XHTML格式
 * application/xml ： XML数据格式
 * application/atom+xml ：Atom XML聚合格式
 * application/json ： JSON数据格式
 * application/pdf ：pdf格式
 * application/msword ： Word文档格式
 * application/octet-stream ： 二进制流数据（如常见的文件下载）
 * application/x-www-form-urlencoded ： <form encType=””>中默认的encType
 * <p>
 * 文／小龙人的绝望（简书作者）
 * 原文链接：http://www.jianshu.com/p/94fe3cc1c5d8
 * 著作权归作者所有，转载请联系作者获得授权，并标注“简书作者”。
 */
public class OkHttpUtils {
    private static String TAG = "OkHttpUtils";

    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private final int SECONDS = 30;

    public OkHttpUtils() {
        try {
            mOkHttpClient = new OkHttpClient();
            mOkHttpClient.setConnectTimeout(SECONDS, TimeUnit.SECONDS);
            mOkHttpClient.setWriteTimeout(SECONDS, TimeUnit.SECONDS);
            mOkHttpClient.setReadTimeout(SECONDS, TimeUnit.SECONDS);
            //cookie enabled
            mOkHttpClient.setCookieHandler(new CookieManager());
            mDelivery = new Handler(Looper.getMainLooper());

            if (null != SmtApplication.getSmtApplication()) {
                int cacheSize = 10 * 1024 * 1024; // 10 MiB
                File cacheDirectory = new File(SmtApplication.getSmtApplication().getCacheDir(), "OkHttpCache");
                Cache cache = new Cache(cacheDirectory, cacheSize);
                mOkHttpClient.setCache(cache);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建一个单例
     *
     * @return
     */
    private static OkHttpUtils getmInstance() {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                mInstance = new OkHttpUtils();
            }
        }
        return mInstance;
    }


    /**
     * get请求
     *
     * @param url      请求url
     * @param callback 请求回调
     */
    public static void get(String url, ResultCallBack callback) {
        getmInstance().getRequest(url, callback);
    }

    /**
     * post请求
     *
     * @param url      请求url
     * @param params   请求参数
     * @param callback 请求回调
     */
    public static void post(String url, List<Param> params, final ResultCallBack callback) {
        getmInstance().postRequest(url, params, callback);
    }

    /**
     * Post 请求
     *
     * @param url
     * @param body     数据体
     * @param callback
     */
    public static void post(String url, String body, final ResultCallBack callback) {
        getmInstance().postRequest(url, body, callback);
    }

    /**
     * Post 请求,返回数据流
     *
     * @param url
     * @param body     数据体
     * @param callback
     */
    public static void postResultStream(String url, String body, final ResultCallBack callback) {
        getmInstance().postRequestStream(url, body, callback);
    }

    /**
     * get 请求
     *
     * @param url
     * @param callBack
     */
    private void getRequest(String url, final ResultCallBack callBack) {
        final Request request = new Request.Builder().url(url).build();
        deliverResult(callBack, request);
    }

    /**
     * post 请求
     *
     * @param url
     * @param params
     * @param callBack
     */
    private void postRequest(String url, List<Param> params, final ResultCallBack callBack) {
        Request request = buildPostRequest(url, params);
        deliverResult(callBack, request);
    }

    /**
     * post 请求
     *
     * @param url
     * @param body
     * @param callBack
     */
    private void postRequest(String url, String body, final ResultCallBack callBack) {
        deliverResult(callBack, buildPostRequest(url, body));
    }

    /**
     * post 请求
     *
     * @param url
     * @param body
     * @param callBack
     */
    private void postRequestStream(String url, String body, final ResultCallBack callBack) {
        deliverResultStream(callBack, buildPostWebRequest(url, body));
    }

    /**
     * 任务请求
     *
     * @param callBack
     * @param request
     */
    private void deliverResult(final ResultCallBack callBack, Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailCallback(callBack, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    sendSuccessCallBack(callBack, response.body().string());
                } catch (final Exception e) {
                    e.printStackTrace();
                    sendFailCallback(callBack, e);
                }

            }
        });
    }


    /**
     * webservice 服务请求
     *
     * @param callBack inputStream
     * @param request
     */
    private void deliverResultStream(final ResultCallBack callBack, Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailCallback(callBack, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    sendSuccessCallBack(callBack, response.body().byteStream());
                } catch (final Exception e) {
                    e.printStackTrace();
                    sendFailCallback(callBack, e);
                }

            }
        });
    }


    /**
     * 创建Post request 参数对象
     *
     * @param url
     * @param params
     * @return
     */
    private Request buildPostRequest(String url, List<Param> params) {
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        for (Param p : params) {
            formEncodingBuilder.add(p.key, p.value);
        }
        RequestBody body = formEncodingBuilder.build();
        return new Request.Builder().post(body).url(url).build();

    }


    /**
     * @param url
     * @param body
     * @return
     */
    private Request buildPostRequest(String url, String body) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, body);
        return new Request.Builder()
                .post(requestBody).url(url).build();

    }

    /**
     * 提交webserver 数据体
     *
     * @param url
     * @param body
     * @return
     */
    private Request buildPostWebRequest(String url, String body) {
        MediaType xml = MediaType.parse("application/soap+xml; charset=utf-8");
        RequestBody requestBody = RequestBody.create(xml, body);
        return new Request.Builder()
                .post(requestBody).url(url).build();
    }


    /**
     * @param url
     * @param filePath
     * @return
     */
    private Request buildPostFileRequest(String url, String filePath) {
        File file = new File(filePath);

        RequestBody requestBody = new MultipartBuilder().type(MultipartBuilder.FORM)
                .addPart(RequestBody.create(null, "android"))
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"another\";filename=\"another.dex\""), RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();
        return new Request.Builder()
                .post(requestBody).url(url).build();
    }


    /**
     * 失败回调
     *
     * @param callback
     * @param e
     */
    private void sendFailCallback(final ResultCallBack callback, final Exception e) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onFailure(e);
                }
            }
        });
    }

    /**
     * 成功回调
     *
     * @param callback
     * @param obj
     */
    private void sendSuccessCallBack(final ResultCallBack callback, final Object obj) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onSuccess(obj);
                }
            }
        });
    }

    /**
     * http请求回调类,回调方法在UI线程中执行
     *
     * @param <T>
     */
    public static abstract class ResultCallBack<T> {
        Type mType;

        public ResultCallBack() {
        }

        static Type getSupperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getSuperclass();
            if (subclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        /**
         * 请求成功回调
         *
         * @param response
         */
        public abstract void onSuccess(T response);

        /**
         * 请求失败回调
         *
         * @param e
         */
        public abstract void onFailure(Exception e);

    }


    /**
     * post请求参数类
     */
    public static class Param {

        String key;
        String value;

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }


}

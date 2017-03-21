
package com.smtlibrary.https.helper;


import com.smtlibrary.https.listener.impl.ProgressRequestBody;
import com.smtlibrary.https.listener.ProgressListener;
import com.squareup.okhttp.RequestBody;

/**
 * 进度回调辅助类
 * User:lizhangqu(513163535@qq.com)
 * Date:2015-09-02
 * Time: 17:33
 */
public class ProgressHelper {

    /**
     * 包装请求体用于上传文件的回调
     * @param requestBody 请求体RequestBody
     * @param progressRequestListener 进度回调接口
     * @return 包装后的进度回调请求体
     */
    public static ProgressRequestBody addProgressRequestListener(RequestBody requestBody, ProgressListener progressRequestListener){
        //包装请求体
        return new ProgressRequestBody(requestBody,progressRequestListener);
    }
}

package com.wilddog.utils;

import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wilddog.client.SyncError;
import com.wilddog.wilddogim.core.wildcallback.WildValueCallBack;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DownLoadData {

    public DownLoadData() {
        // TODO Auto-generated constructor stub
    }
   // OkHttp的execute的方法是同步方法，
    //OkHttp的enqueue的方法是异步方法，
    public static void getData(final String path, final WildValueCallBack<byte[]> callback) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(path)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("获取音频失败", e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                int code = response.code();
                if (code == 200) {
                    InputStream is = response.body().byteStream();
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    byte[] data = outStream.toByteArray();//网页的二进制数据
                    outStream.flush();
                    outStream.close();
                    is.close();

                    if (callback != null) {
                        callback.onSuccess(data);
                    }
                } else {
                    if (callback != null) {
                        callback.onFailed(code, " getting data occurred error");
                    }
                }
            }
        });
    }

    public static void getImageData(final String path, final WildValueCallBack<byte[]> callback) {

        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(path)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //setErrorResId(view, errorResId);
            }

            @Override
            public void onResponse(Response response) {
                InputStream is = null;
                try {
                    is = response.body().byteStream();
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    byte[] data = outStream.toByteArray();//网页的二进制数据
                    outStream.flush();
                    outStream.close();

                    if (callback != null) {
                        callback.onSuccess(data);
                    }

                } catch (Exception e) {
                    //setErrorResId(view, errorResId);
                    callback.onFailed(new SyncError(111,e.toString(),"exception"));

                } finally {
                    if (is != null) try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}

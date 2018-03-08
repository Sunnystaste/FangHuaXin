package com.example.youyou.myheart.Tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by youyou on 2017/12/14.
 */

public class Util {
    public static String TAG="UTIL";
    public static Bitmap getbitmap(String imageUri) {
        Log.v(TAG, "getbitmap:" + imageUri);
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

            Log.v(TAG, "image download finished." + imageUri);
        } catch (IOException e) {
            e.printStackTrace();

            Log.v(TAG, "getbitmap bmp fail---");
            return null;
        }
        return bitmap;
    }
    //根据map的value获取map的key
    public static int getKey(Map<Integer,String> map,String value){
        int key=-1;
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if(value.equals(entry.getValue())){
                key=entry.getKey();
            }
        }
        return key;
    }
}
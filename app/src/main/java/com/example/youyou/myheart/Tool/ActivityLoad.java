package com.example.youyou.myheart.Tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;

import com.example.youyou.myheart.Data.ActivityClientVO;
import com.example.youyou.myheart.Data.Data;
import com.example.youyou.myheart.Data.Value;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.youyou.myheart.Tool.OkHttp3Util.doGet;

/**
 * Created by youyou on 2018/2/10.
 */

public class ActivityLoad {
    public static void onLoad(String url, ArrayList<ActivityClientVO> l, Map<Integer, Bitmap> IconMap) {
        Gson gson = new Gson();
        doGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int k=l.size();
                String jsonFromServer = response.body().string();
                Data<Value<ActivityClientVO>> data;
                Type jsonType = new TypeToken<Data<Value<ActivityClientVO>>>() {
                }.getType();
                data = gson.fromJson(jsonFromServer, jsonType);
                l.addAll(data.value.list);
                for (int i = k; i < l.size(); i++) {
                    final int j = i;
                    String anUserpic = l.get(i).titleGraph;
                    if (anUserpic != null)
                        doGet(anUserpic, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("GetIcon", "failure");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                InputStream in = response.body().byteStream();
                                Bitmap bitmap = BitmapFactory.decodeStream(in);
                                in.close();
                                IconMap.put(j, bitmap);
                            }
                        });
                }
            }
        });
    }
}

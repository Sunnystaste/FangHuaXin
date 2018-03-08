package com.example.lib;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class myClass {
    public static class GetExample {
        static OkHttpClient client = new OkHttpClient();
        static String run(String url) throws IOException {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
    }
    public class Data{
        public String error;
        public JsonObject param;
        public JsonObject values;
        public boolean success;
    }

    public static void main(String[] args) {
        String JsonString=null;
        String url="https://www.huaxinapp.xyz/test/activity/getAllActivityClientVOs.do?pageSize=9&pageNum=1";
        try {
            JsonString=GetExample.run(url);
            Gson gson = new Gson();
            Data data = gson.fromJson(JsonString, Data.class);
            System.out.println(data.values.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

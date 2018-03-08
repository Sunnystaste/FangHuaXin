package com.example.youyou.myheart.DiscoverActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.youyou.myheart.Adapter.ListViewAdapter_activity;
import com.example.youyou.myheart.Data.ActivityClientVO;
import com.example.youyou.myheart.Data.Data;
import com.example.youyou.myheart.Data.Value;
import com.example.youyou.myheart.R;
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

import static com.example.youyou.myheart.Tool.AppConstant.ServerUrl;
import static com.example.youyou.myheart.Tool.AppConstant.activityMap;
import static com.example.youyou.myheart.Tool.AppConstant.getActivityDetail;
import static com.example.youyou.myheart.Tool.AppConstant.getAllActivity;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doGet;
import static com.example.youyou.myheart.Tool.Util.getKey;
import static com.mob.MobSDK.getContext;

/**
 * Created by youyou on 2018/2/14.
 */

public class SortxqActivity extends Activity implements AdapterView.OnItemClickListener{
    String s;
    Context context;
    private Map<Integer,Bitmap> replyIconMap=new HashMap<>();
    ListViewAdapter_activity adapter;
    private ArrayList<ActivityClientVO> l=new ArrayList<ActivityClientVO>();
    Gson gson=new Gson();
    TextView title;
    ListView listView;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sortxq);
        Intent intent = getIntent();
        s=intent.getStringExtra("sort");
        int i=getKey(activityMap,s);
        url=ServerUrl+getAllActivity+"?pageSize=20&pageNum=1&acTypeFilterFlag=true&acType="+i;
        context=getContext();
        init();
    }

    private void init() {
        title=findViewById(R.id.title_sortxq);
        listView=findViewById(R.id.listview_sort);
        listView.setOnItemClickListener(this);
        listView.setEmptyView(findViewById(android.R.id.empty));
        title.setText(s);
        doGet(url,new Callback(){

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonFromServer = response.body().string();
                Data<Value<ActivityClientVO>> data;
                Type jsonType = new TypeToken<Data<Value<ActivityClientVO>>>() {
                }.getType();
                data = gson.fromJson(jsonFromServer, jsonType);
                l=data.value.list;
                for (int i = 0; i < l.size(); i++) {
                    final int j=i;
                    String anUserpic = l.get(i).titleGraph;
                    if (anUserpic != null)
                        doGet(anUserpic, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("GetIcon","failure");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                InputStream in = response.body().byteStream();
                                Bitmap bitmap = BitmapFactory.decodeStream(in);
                                in.close();
                                replyIconMap.put(j, bitmap);
                                if (replyIconMap.size() == l.size()) {
                                    Message msg = new Message();
                                    msg.what = 1;
                                    mHandler.sendMessage(msg);

                                }
                            }
                        });
                }
            }
        });
    }


    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                adapter = new ListViewAdapter_activity(l, replyIconMap, context);
                listView.setAdapter(adapter);
            }
        }
    };


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String qa=l.get(position).uuid;
        Intent intent=new Intent(getContext(), ActivityDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("acId",qa);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

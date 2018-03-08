package com.example.youyou.myheart.DiscoverActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.youyou.myheart.Data.ActivityDetailClientVO;
import com.example.youyou.myheart.Data.Data;
import com.example.youyou.myheart.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.youyou.myheart.UserActivity.UserFragment.token;
import static com.example.youyou.myheart.UserActivity.UserFragment.userData;
import static com.example.youyou.myheart.Tool.AppConstant.ServerUrl;
import static com.example.youyou.myheart.Tool.AppConstant.SighUp;
import static com.example.youyou.myheart.Tool.AppConstant.activityMap;
import static com.example.youyou.myheart.Tool.AppConstant.getActivityDetail;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doGet;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doPost;

/**
 * Created by youyou on 2017/12/9.
 */

public class ActivityDetail extends Activity{
    SimpleDateFormat sdf = new SimpleDateFormat("yy年MM月dd日HH:mm");
    private  ActivityDetailClientVO value;
    private ImageView title_im,menu;
    private TextView sort;
    private TextView score;
    private TextView title_tx;
    private TextView start;
    private TextView end;
    private TextView adress;
    private TextView content;
    private LinearLayout needAppointment;
    private Button status;
    private Gson gson=new Gson();
    private String acId;
    private String url;
    private final static String SignUpUrl=ServerUrl+SighUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitydetail);
        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        acId= bundle.getString("acId");
        title_im=findViewById(R.id.activity_detailtitle);
        sort=findViewById(R.id.activity_sort);
        score=findViewById(R.id.activity_people);
        title_tx=findViewById(R.id.activity_title);
        start=findViewById(R.id.activity_start);
        end=findViewById(R.id.activity_end);
        adress=findViewById(R.id.activity_adress);
        content=findViewById(R.id.activity_content);
        menu=findViewById(R.id.activity_menu);
        needAppointment=findViewById(R.id.activity_yuyuelinealayout);
        status=findViewById(R.id.indate_bt);
        if(userData.getUserId()==null)
            url=ServerUrl+getActivityDetail+"?acId=";
         else
             url=ServerUrl+getActivityDetail+"?userId="+userData.getUserId()+"&acId=";
        init();
    }

    private void init() {
        doGet(url + acId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonFromServer = response.body().string();
                Data<ActivityDetailClientVO> data;
                Type jsonType = new TypeToken<Data<ActivityDetailClientVO>>() {
                }.getType();
                data = gson.fromJson(jsonFromServer, jsonType);
                value=data.value;
                doGet(value.titleGraph, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream in = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(in);
                        in.close();
                        Message msg=new Message();
                        msg.what=0;
                        msg.obj=bitmap;
                        mHandler.sendMessage(msg);
                    }
                });
            }
        });
    }
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //HeadViewPager对应
            if (msg.what == 0) {
                title_im.setImageBitmap((Bitmap) msg.obj);
                title_tx.setText(value.name);
                sort.setText(activityMap.get(value.type));
                score.setText("积分：" + value.integral + "|规模(人):" + value.limitationCount);
                start.setText(sdf.format(new Date(value.startTime)).toString());
                end.setText(sdf.format(new Date(value.endTime)).toString());
                adress.setText(value.address);
                content.setText(value.acContent);
                if (value.needAppointment == false)
                    needAppointment.setVisibility(View.GONE);
                if (value.status == -1) {
                    status.setEnabled(true);
                    status.setText("未报名");
                    if(userData.getUserId()==null)
                        status.setEnabled(false);
                    status.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String,String> map=new HashMap<>();
                            map.put("userId",userData.getUserId());
                            map.put("acId",acId);
                            doPost(SignUpUrl, map, new okhttp3.Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Log.e("POST",response.body().string());
                                    init();
                                }
                            },token);
                        }
                    });
                }
                else {
                    status.setEnabled(false);
                    status.setText("已经报名");
                }
                menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu=new PopupMenu(ActivityDetail.this,v);//1.实例化PopupMenu
                        getMenuInflater().inflate(R.menu.menu,popupMenu.getMenu());//2.加载Menu资源

                        //3.为弹出菜单设置点击监听
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()){
                                    case R.id.option_normal_1:
                                        Platform.ShareParams sp = new Platform.ShareParams();
                                        String str = value.acContent;
                                        //str.substring(0,139);
                                        sp.setText(str);
                                        sp.setImageUrl(value.titleGraph);
                                        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                                        weibo.setPlatformActionListener(new PlatformActionListener() {
                                            @Override
                                            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                                            }

                                            @Override
                                            public void onError(Platform platform, int i, Throwable throwable) {

                                            }

                                            @Override
                                            public void onCancel(Platform platform, int i) {

                                            }
                                        });
                                        weibo.share(sp);
                                        return true;
                                    case R.id.option_normal_2:
                                        Platform.ShareParams sp0 = new Platform.ShareParams();
                                        sp0.setTitle(value.acContent);
                                        sp0.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
                                        sp0.setText(value.acContent);
                                        sp0.setImageUrl(value.titleGraph);
                                        Platform qzone = ShareSDK.getPlatform (QQ.NAME);
                                        // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                                        qzone.setPlatformActionListener (new PlatformActionListener() {
                                            public void onError(Platform arg0, int arg1, Throwable arg2) {
                                                //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
                                            }
                                            public void onComplete(Platform arg0, int arg1, HashMap arg2) {
                                                //分享成功的回调
                                            }
                                            public void onCancel(Platform arg0, int arg1) {
                                                //取消分享的回调
                                            }
                                        });
                                        // 执行图文分享
                                        qzone.share(sp0);
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        popupMenu.show();//4.显示弹出菜单
                    }
                });
            }
        }
    };
}

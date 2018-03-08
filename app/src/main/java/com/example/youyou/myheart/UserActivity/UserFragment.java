package com.example.youyou.myheart.UserActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youyou.myheart.Data.UserData;
import com.example.youyou.myheart.LoginCallBack;
import com.example.youyou.myheart.Paint.CircleImagView;
import com.example.youyou.myheart.R;
import com.example.youyou.myheart.Tool.Util;
import com.example.youyou.myheart.Paint.SelfDialog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import okhttp3.Call;
import okhttp3.Response;

import static com.example.youyou.myheart.MainActivity.homeFragment;
import static com.example.youyou.myheart.Tool.AppConstant.ServerUrl;
import static com.example.youyou.myheart.Tool.AppConstant.createUser;
import static com.example.youyou.myheart.Tool.AppConstant.getUserIntegral;
import static com.example.youyou.myheart.Tool.AppConstant.userLogin;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doGet;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doPost;

/**
 * Created by youyou on 2017/12/10.
 */

public class UserFragment extends Fragment implements View.OnClickListener {
    private LoginCallBack loginCallBack=homeFragment;
    private String url;
    Gson gson=new Gson();
    public static Platform qq = ShareSDK.getPlatform(QQ.NAME);
    public static Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
    private RelativeLayout activity, QandA, Points, Accrediatation, feedback;
    private ImageView mymessage;
    private SelfDialog selfDialog;
    public static String last = "defalut";
    private Button user_button;
    public TextView nicknameTextView,point;
    public CircleImagView userlogo;
    private Bitmap bitmap;
    private String loginUrl=ServerUrl+userLogin;
    private String createUrl=ServerUrl+createUser;
    public static UserData userData;
    public static String token;
    @Override
    public void onActivityCreated(Bundle bundle) {
        userData=new UserData();
        init();
        if (qq.isAuthValid()) {
            last = "上次登陆:QQ登陆";
            LoginQQ();
        }
        if (weibo.isAuthValid()) {
            last = "上次登陆:微博登陆";
            LoginWeiBo();
        }
        if(!weibo.isAuthValid()&&!qq.isAuthValid())
            loginCallBack.LogincallBack("null");
        super.onActivityCreated(bundle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.userfragment, container, false);
    }

    private void init() {
        point=getActivity().findViewById(R.id.point);
        mymessage=getActivity().findViewById(R.id.user_message);
        user_button = getActivity().findViewById(R.id.change_user);
        activity = getActivity().findViewById(R.id.user_activity);
        Accrediatation = getActivity().findViewById(R.id.user_accrediation);
        Points = getActivity().findViewById(R.id.user_Points);
        QandA = getActivity().findViewById(R.id.user_QandA);
        feedback = getActivity().findViewById(R.id.user_feedback);
        selfDialog = new SelfDialog(getActivity());
        mymessage.setOnClickListener(this);
        user_button.setOnClickListener(this);
        activity.setOnClickListener(this);
        Accrediatation.setOnClickListener(this);
        QandA.setOnClickListener(this);
        Points.setOnClickListener(this);
        feedback.setOnClickListener(this);
        //用来显示昵称的textview
        nicknameTextView = (TextView) getActivity().findViewById(R.id.user_nickname);
        //用来显示头像的Imageview
        userlogo = (CircleImagView) getActivity().findViewById(R.id.user_icon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_message:
                Intent intent = new Intent(getContext(), MyMessage.class);
                startActivity(intent);
                break;
            case R.id.user_activity:
                Intent intent1 = new Intent(getContext(), MyActivity.class);
                startActivity(intent1);
                break;
            case R.id.user_accrediation:
                Intent intent2 = new Intent(getContext(), MyAccreditation.class);
                startActivity(intent2);
                break;
            case R.id.user_QandA:
                Intent intent3 = new Intent(getContext(), MyQandA.class);
                startActivity(intent3);
                break;
            case R.id.user_Points:
                Intent intent4 = new Intent(getContext(), MyPoints.class);
                startActivity(intent4);
                break;
            case R.id.user_feedback:
                Intent intent5 = new Intent(getContext(), MyFeedBack.class);
                startActivity(intent5);
                break;

            case R.id.change_user:
                selfDialog.setCancelOnClickListener(new SelfDialog.CancelOnClickListener() {
                    @Override
                    public void CancelClick() {
                        selfDialog.dismiss();
                    }
                });
                selfDialog.setQqOnclickListener(new SelfDialog.QqOnClickListener() {
                    @Override
                    public void QqClick() {
                        if (selfDialog.xieyi.isChecked()) {
                            if (qq.isAuthValid()) {
                                qq.removeAccount(true);
                            }
                            if (weibo.isAuthValid()) {
                                weibo.removeAccount(true);
                            }
                            LoginQQ();
                            selfDialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "请同意话心协议", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                selfDialog.setSinaOnClickListener(new SelfDialog.SinaOnClickListener() {
                    @Override
                    public void SinaClick() {
                        if (selfDialog.xieyi.isChecked()) {
                            if (weibo.isAuthValid()) {
                                weibo.removeAccount(true);
                            }
                            if (qq.isAuthValid()) {
                                qq.removeAccount(true);
                            }
                            LoginWeiBo();
                            selfDialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "请同意话心协议", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                selfDialog.setZhuxiaoOnclickListener(new SelfDialog.ZhuxiaoOnClickListener() {
                    @Override
                    public void ZhuxiaoClick() {
                        if (weibo.isAuthValid()) {
                            userlogo.setImageResource(R.drawable.user_logo);
                            nicknameTextView.setText("尚未授权");
                            userData=new UserData();
                            loginCallBack.LogincallBack("null");
                            weibo.removeAccount(true);
                            last = "defalut";
                            selfDialog.dismiss();
                        }
                        else if (qq.isAuthValid()) {
                            userlogo.setImageResource(R.drawable.user_logo);
                            userData=new UserData();
                            loginCallBack.LogincallBack("null");
                            nicknameTextView.setText("尚未授权");
                            qq.removeAccount(true);
                            last = "defalut";
                            selfDialog.dismiss();
                        }
                        else
                            Toast.makeText(getContext(),"无授权信息无需注销",Toast.LENGTH_LONG).show();
                    }
                });
                selfDialog.show();
        }
    }



    //这里是调用WeiBo登录的关键代码
    public void LoginWeiBo() {
        //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                // TODO Auto-generated method stub
                arg2.printStackTrace();
            }
            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                // TODO Auto-generated method stub
                //输出所有授权信息
                last = "上次登陆:微博登陆";
                userData.setUserPic(arg0.getDb().getUserIcon());
                userData.setUserId(arg0.getDb().getUserId());
                userData.setNickname(arg0.getDb().getUserName());
                loginCallBack.LogincallBack(userData.getUserId());
                Iterator ite =arg2.entrySet().iterator();;
                while (ite.hasNext()) {
                    Map.Entry entry = (Map.Entry)ite.next();
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    Log.e(key.toString(),value.toString());
                    if(key.toString().equals("avatar_large")) {
                        final String value1 = value.toString();
                        new Thread() {
                            @Override
                            public void run() {
                                bitmap = Util.getbitmap(value1);
                                Message msg = new Message();
                                msg.obj = bitmap;
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                                Log.e("WeiBo","SendMessge1");
                                Log.e("WeiBo",bitmap.toString());
                            }
                        }.start();
                    }

                    if(key.toString().equals("name"))
                    {
                        Message msg=new Message();
                        msg.obj = value;
                        msg.what = 0;
                        mHandler.sendMessage(msg);
                        Log.e("WeiBo","SendMessge0");
                    }
                }
            }
            @Override
            public void onCancel(Platform arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });
        //authorize与showUser单独调用一个即可
        //weibo.authorize();//单独授权,OnComplete返回的hashmap是空的'
        weibo.showUser(null);//授权并获取用户信息
    }



    //这里是调用QQ登录的关键代码
    public void LoginQQ() {
        //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                // TODO Auto-generated method stub
                arg2.printStackTrace();
            }
            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                // TODO Auto-generated method stub
                //输出所有授权信息
                last = "上次登陆:QQ登陆";
                Iterator ite =arg2.entrySet().iterator();
                userData.setUserPic(arg0.getDb().getUserIcon());
                userData.setUserId(arg0.getDb().getUserId());
                userData.setNickname(arg0.getDb().getUserName());
                loginCallBack.LogincallBack(userData.getUserId());
                while (ite.hasNext()) {
                    Map.Entry entry = (Map.Entry)ite.next();
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    Log.e(key.toString(),value.toString());
                    if(key.toString().equals("figureurl_qq_2")) {
                        final String value1 = value.toString();
                        new Thread() {
                            @Override
                            public void run() {
                                bitmap = Util.getbitmap(value1);
                                Message msg = new Message();
                                msg.obj = bitmap;
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                                Log.e("QQ","SendMessge1");
                                Log.e("QQ",bitmap.toString());
                            }
                        }.start();
                    }

                    if(key.toString().equals("nickname"))
                    {
                        Message msg=new Message();
                        msg.obj = value;
                        msg.what = 0;
                        mHandler.sendMessage(msg);
                        Log.e("QQ","SendMessge0");
                    }
                }
            }
            @Override
            public void onCancel(Platform arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });
        //authorize与showUser单独调用一个即可
        //qq.authorize();//单独授权,OnComplete返回的hashmap是空的'
        qq.showUser(null);//授权并获取用户信息
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String nickname = msg.obj.toString();
                nicknameTextView.setText(nickname);
            }else if(msg.what == 1){
                Bitmap bitmap = (Bitmap)msg.obj;
                userlogo.setImageBitmap(bitmap);
                Log.e("userData",gson.toJson(userData));
                doPost(createUrl,gson.toJson(userData), new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.e("POST",response.body().string());
                        Map<String,String> map=new HashMap<>();
                        map.put("userId",userData.getUserId());
                        url=ServerUrl+getUserIntegral+"?userId="+userData.getUserId();
                        doPost(loginUrl,map, new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    String s=response.body().string();
                                    Log.e("POST",s);
                                    JSONObject jsonObject = new JSONObject(s);
                                    JSONObject value = jsonObject.getJSONObject("value");
                                    token = value.getString("token");
                                    Log.e("token",token);
                                    doGet(url, new okhttp3.Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {

                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String s=response.body().string();
                                            Log.e("POST",s);
                                            JSONObject jsonObject = null;
                                            try {
                                                jsonObject = new JSONObject(s);
                                                Integer value = jsonObject.getInt("value");
                                                Message msg = new Message();
                                                msg.obj =value;
                                                msg.what = 2;
                                                mHandler.sendMessage(msg);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },token);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }else if(msg.what==2){
                point.setText(msg.obj.toString());
            }
        }

    };

}
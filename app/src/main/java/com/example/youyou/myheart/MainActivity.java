package com.example.youyou.myheart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youyou.myheart.DiscoverActivity.LiveActivity;
import com.example.youyou.myheart.DiscoverActivity.DiscoverFragment;
import com.example.youyou.myheart.HomeActivity.HomeFragment;
import com.example.youyou.myheart.UserActivity.UserFragment;
import com.example.youyou.myheart.SearchActivity.SearchActivity;
import com.example.youyou.myheart.Tool.ActivityManagers;
import com.example.youyou.myheart.Tool.Constant;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.youyou.myheart.UserActivity.UserFragment.token;
import static com.example.youyou.myheart.UserActivity.UserFragment.userData;
import static com.example.youyou.myheart.Tool.AppConstant.ActivityCheckin;
import static com.example.youyou.myheart.Tool.AppConstant.ServerUrl;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doPost;

/**
 * 主Activity
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private long mExitTime;
    private LinearLayout home;
    private LinearLayout discover;
    private LinearLayout user;
    private ImageView home_img;
    private ImageView discover_img;
    private ImageView user_img;
    private TextView home_txv;
    private TextView discover_txv;
    private TextView user_txv;
    private Toolbar toolbar;
    private TextView mid_txv;
    private TextView left_txv;
    private ImageView search_img;
    public static HomeFragment homeFragment=new HomeFragment();
    public static DiscoverFragment discoverFragment=new DiscoverFragment();
    public static UserFragment userFragment=new UserFragment();
    private static final String url=ServerUrl+ActivityCheckin;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            Log.e("二维码", scanResult);
            Map<String, String> map = new HashMap<>();
            map.put("userId", userData.getUserId());
            map.put("acId", scanResult);
            doPost(url, map, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = scanResult;
                    mHandler.sendMessage(msg);
                }
            }, token);
        } catch(Exception e){
            Log.e("异常信息:","bundle");
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //showNormalDialog1();
        context=this;
        if (!ActivityManagers.getActivityList().contains(MainActivity.this)) {
             ActivityManagers.addActivity(MainActivity.this);
             }
        init();
    }

    private void init() {
        toolbar=this.findViewById(R.id.home_toolbar);
        left_txv=this.findViewById(R.id.left_txv);
        mid_txv=this.findViewById(R.id.mid_txv);
        search_img=this.findViewById(R.id.home_icon_search);
        search_img.setOnClickListener(this);
        left_txv.setOnClickListener(this);
        home=this.findViewById(R.id.home);
        discover=this.findViewById(R.id.discover);
        user=this.findViewById(R.id.user);
        home_img= this.findViewById(R.id.home_img);
        discover_img =this.findViewById(R.id.discover_img);
        user_img=this.findViewById(R.id.user_img);
        home_txv= this.findViewById(R.id.home_txv);
        discover_txv=this.findViewById(R.id.discover_txv);
        user_txv=this.findViewById(R.id.user_txv);
        home.setOnClickListener(this);
        discover.setOnClickListener(this);
        user.setOnClickListener(this);
        SelectedChange(true,false,false);
        FragmentTransaction fragmentTransaction= this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .add(R.id.fragment_content,homeFragment)
                .add(R.id.fragment_content,discoverFragment)
                .add(R.id.fragment_content,userFragment).commit();
        FragmentChange(true,false,false);
    }


    public void showNormalDialog1() {
        //AlertDialog
        //AlertDialog的构造方法是被修饰为protected
        //因此包外是无法使用的，所以我们利用构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框的标题
        builder.setTitle("更新提醒");
        //设置内容
        builder.setMessage("沙河预约点更改为通信楼318");
        //设置按钮
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNegativeButton("取消",null);
        builder.create().show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                ActivityManagers.finishSingleActivityByClass(MainActivity.class);
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.home:
                toolbar.setVisibility(View.VISIBLE);
                left_txv.setVisibility(View.GONE);
                mid_txv.setText(R.string.home);
                SelectedChange(true,false,false);
                FragmentChange(true,false,false);
                break;
            case R.id.discover:
                toolbar.setVisibility(View.VISIBLE);
                left_txv.setVisibility(View.VISIBLE);
                mid_txv.setText(R.string.discover);
                SelectedChange(false,true,false);
                FragmentChange(false,true,false);
                break;
            case R.id.user:
                toolbar.setVisibility(View.GONE);
                SelectedChange(false,false,true);
                FragmentChange(false,false,true);
                break;
            case R.id.home_icon_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                Toast.makeText(this,"点击了右侧寻找img",Toast.LENGTH_SHORT).show();
                break;
            case R.id.left_txv:
                Intent intent2 = new Intent(this, LiveActivity.class);
                startActivity(intent2);
                Toast.makeText(this,"点击了左侧分类txv",Toast.LENGTH_SHORT).show();
                break;
        }

    }
    /**
     * 在处理Fragment事务时调用FragmentChange
     * @param home 是否显示homeFragemtn
     * @param discover 是否显示discoverFragment
     * @param user 是否显示userFragemtn
     */
    public void FragmentChange(boolean home,boolean discover,boolean user)
    {
        FragmentTransaction fragmentTransaction= this.getSupportFragmentManager().beginTransaction();
        if(home==true)
            fragmentTransaction.show(homeFragment);
        else
            fragmentTransaction.hide(homeFragment);
        if(discover==true)
            fragmentTransaction.show(discoverFragment);
        else
            fragmentTransaction.hide(discoverFragment);
        if(user==true)
            fragmentTransaction.show(userFragment);
        else
            fragmentTransaction.hide(userFragment);
        fragmentTransaction.commit();
    }
    /**
     * 在点击标签栏调用的方法
     * @param home homeLinearlayout是否被点击
     * @param discover discoverLinearLayout是否被点击
     * @param user userLinearLayout是否被点击
     */
    public void SelectedChange(boolean home,boolean discover,boolean user)
    {
        home_img.setSelected(home);
        discover_img.setSelected(discover);
        user_img.setSelected(user);
        home_txv.setSelected(home);
        discover_txv.setSelected(discover);
        user_txv.setSelected(user);
    }
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Toast.makeText(context,"成功签到活动\n活动Id="+msg.obj.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    };
}
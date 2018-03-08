package com.example.youyou.myheart.SearchActivity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youyou.myheart.Adapter.MyFragmentPagerAdapter;
import com.example.youyou.myheart.FragmentCallBack;
import com.example.youyou.myheart.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youyou on 2017/12/8.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener {
    private FragmentCallBack fragmentCallBack;
    private FragmentCallBack fragmentCallBack1;
    private HuodongFragment huodongFragment;
    private WendaFragment wendaFragment;
    private List<Fragment> list=new ArrayList<>();
    private TextView back;
    private ViewPager vpager_four;
    private ImageView img_cursor;
    private TextView tv_one;
    private TextView tv_two;
    private EditText ed;
    private int offset = 0;//移动条图片的偏移量
    private int currIndex = 0;//当前页面的编号
    private int bmpWidth;// 移动条图片的长度
    private int one = 0; //移动条滑动一页的距离

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        initViews();
    }


    private void initViews() {
        ed=findViewById(R.id.search_ed);
        huodongFragment=new HuodongFragment();
        wendaFragment=new WendaFragment();
        fragmentCallBack=huodongFragment;
        fragmentCallBack1=wendaFragment;
            ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        fragmentCallBack.callBack(ed.getText().toString());
                        fragmentCallBack1.callBack(ed.getText().toString());
                        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        if(imm != null) {
                            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                                    0);
                        }
                    return false;
                }
                return false;
            }
        });
        back=findViewById(R.id.search_cancel);
        back.setOnClickListener(this);
        vpager_four = (ViewPager) findViewById(R.id.vpager_0);
        tv_one = (TextView) findViewById(R.id.tv_0);
        tv_two = (TextView) findViewById(R.id.tv_1);
        img_cursor = (ImageView) findViewById(R.id.img_cursor0);

        //下划线动画的相关设置：
        bmpWidth = BitmapFactory.decodeResource(getResources(), R.mipmap.line_2).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 2 - bmpWidth) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        img_cursor.setImageMatrix(matrix);// 设置动画初始位置
        //移动的距离
        one = offset * 2 + bmpWidth;// 移动一页的偏移量,比如1->2,或者2->3


        //往ViewPager填充View，同时设置点击事件与页面切换事件
        list.add(huodongFragment);
        list.add(wendaFragment);
        vpager_four.setAdapter(new MyFragmentPagerAdapter(this.getSupportFragmentManager(),list));
        vpager_four.setCurrentItem(0);          //设置ViewPager当前页，从0开始算

        tv_one.setOnClickListener(this);
        tv_two.setOnClickListener(this);
        vpager_four.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_0:
                vpager_four.setCurrentItem(0);
                break;
            case R.id.tv_1:
                vpager_four.setCurrentItem(1);
                break;
            case R.id.search_cancel:
                this.finish();
        }
    }

    @Override
    public void onPageSelected(int index) {
        Animation animation = null;
        switch (index) {
            case 0:
                if (currIndex == 1)
                    animation = new TranslateAnimation(one, 0, 0, 0);
                break;
            case 1:
                if (currIndex == 0)
                    animation = new TranslateAnimation(offset, one, 0, 0);
                break;
        }
        currIndex = index;
        animation.setFillAfter(true);// true表示图片停在动画结束位置
        animation.setDuration(300); //设置动画时间为300毫秒
        img_cursor.startAnimation(animation);//开始动画
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }
}
package com.example.youyou.myheart.DiscoverActivity;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youyou.myheart.Adapter.MyFragmentPagerAdapter;
import com.example.youyou.myheart.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youyou on 2017/12/10.
 */

public class DiscoverFragment extends Fragment implements View.OnClickListener,
        ViewPager.OnPageChangeListener{
    private ViewPager viewpager;
    private ImageView img_cursor;
    private TextView tv_one;
    private TextView tv_two;
    private TextView tv_three;
    private TextView tv_four;
    private TextView tv_five;
    private MyFragmentPagerAdapter adapter;
    private List<Fragment> list;
    private int offset = 0;//移动条图片的偏移量
    private int currIndex = 0;//当前页面的编号
    private int bmpWidth;// 移动条图片的长度
    private int one = 0; //移动条滑动一页的距离
    private int two = 0; //滑动条移动两页的距离
    private int three = 0; //滑动条移动三页的距离
    private int four = 0; //滑动条移动四页的距离

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        initViews();
        super.onActivityCreated(bundle);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.discoverfragment,container,false);
    }


    private void initViews() {
        viewpager = (ViewPager) getActivity().findViewById(R.id.viewPager);
        tv_one = (TextView) getActivity().findViewById(R.id.tv_1);
        tv_two = (TextView) getActivity().findViewById(R.id.tv_2);
        tv_three = (TextView)getActivity(). findViewById(R.id.tv_3);
        tv_four = (TextView) getActivity().findViewById(R.id.tv_4);
        tv_five = (TextView)getActivity(). findViewById(R.id.tv_5);
        img_cursor = (ImageView) getActivity().findViewById(R.id.img_cursor);

        //下划线动画的相关设置：
        bmpWidth = BitmapFactory.decodeResource(getResources(), R.mipmap.line_3).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 5 - bmpWidth) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        img_cursor.setImageMatrix(matrix);// 设置动画初始位置
        //移动的距离
        one = offset * 2 + bmpWidth;// 移动一页的偏移量,比如1->2,或者2->3
        two = one * 2;// 移动两页的偏移量,比如1直接跳3
        three = one * 3;// 移动三页的偏移量,比如1直接跳4
        four = one * 4;// 移动四页的偏移量,比如1直接跳5

        //往ViewPager填充View，同时设置点击事件与页面切换事件
        list = new ArrayList<>();
        list.add(new ZuixinFragment());
        list.add(new TiyanFragment());
        list.add(new KechengFragment());
        list.add(new HuodongFragment());
        list.add(new ShalongFragment());
        if(adapter==null) {
            adapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(), list);
            viewpager.setAdapter(adapter);
            viewpager.setOffscreenPageLimit(4);
        }
        tv_one.setOnClickListener(this);
        tv_two.setOnClickListener(this);
        tv_three.setOnClickListener(this);
        tv_four.setOnClickListener(this);
        tv_five.setOnClickListener(this);
        viewpager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int index) {
        {
            Animation animation = null;
            switch (index) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, 0, 0, 0);
                    } else if (currIndex == 4) {
                        animation = new TranslateAnimation(four, 0, 0, 0);
                    }
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, one, 0, 0);
                    } else if (currIndex == 4) {
                        animation = new TranslateAnimation(four, one, 0, 0);
                    }
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    }else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, two, 0, 0);
                    } else if (currIndex == 4) {
                        animation = new TranslateAnimation(four, two, 0, 0);
                    }
                    break;
                case 3:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset,three, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, three, 0, 0);
                    }else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, three, 0, 0);
                    } else if (currIndex == 4) {
                        animation = new TranslateAnimation(four, three, 0, 0);
                    }
                    break;
                case 4:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, four, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, four, 0, 0);
                    }else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, four, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, four, 0, 0);
                    }
                    break;
            }
            currIndex = index;
            if(animation!=null)
            {
            animation.setFillAfter(true);// true表示图片停在动画结束位置
            animation.setDuration(300); //设置动画时间为300毫秒
            img_cursor.startAnimation(animation);//开始动画
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_1:
                viewpager.setCurrentItem(0);
                break;
            case R.id.tv_2:
                viewpager.setCurrentItem(1);
                break;
            case R.id.tv_3:
                viewpager.setCurrentItem(2);
                break;
            case R.id.tv_4:
                viewpager.setCurrentItem(3);
                break;
            case R.id.tv_5:
                viewpager.setCurrentItem(4);
                break;
        }
    }


}

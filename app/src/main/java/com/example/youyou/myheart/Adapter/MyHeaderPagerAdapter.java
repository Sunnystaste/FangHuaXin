package com.example.youyou.myheart.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.example.youyou.myheart.CallBack;
import java.util.ArrayList;

/**
 * Created by youyou on 2018/1/31.
 */

public class MyHeaderPagerAdapter extends PagerAdapter {
    private ArrayList<View> viewLists;
    private CallBack callBack;

    public  MyHeaderPagerAdapter() {
    }

    public  MyHeaderPagerAdapter(ArrayList<View> viewLists, CallBack callBack) {
        super();
        this.viewLists = viewLists;
        this.callBack=callBack;
    }

    @Override
    public int getCount() {
        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = viewLists.get(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.callBack(position);
            }
        });
        container.addView(viewLists.get(position));
        return viewLists.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewLists.get(position));
    }

}
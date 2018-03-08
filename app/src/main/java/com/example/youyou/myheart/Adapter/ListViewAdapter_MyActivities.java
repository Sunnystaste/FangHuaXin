package com.example.youyou.myheart.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.youyou.myheart.Data.ActivityClientVO;
import com.example.youyou.myheart.Data.MyActivityClientVO;
import com.example.youyou.myheart.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.example.youyou.myheart.Tool.AppConstant.activityMap;
import static com.example.youyou.myheart.Tool.AppConstant.sortMap;

/**
 * Created by youyou on 2018/2/18.
 */

public class ListViewAdapter_MyActivities extends BaseAdapter {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
    public ArrayList<MyActivityClientVO> ActivityClientList;
    public Context context;

    public ListViewAdapter_MyActivities(ArrayList<MyActivityClientVO> ActivityClientList, Context context) {
        this.ActivityClientList = ActivityClientList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return  ActivityClientList.size();
    }

    @Override
    public Object getItem(int position) {
        return ActivityClientList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.myactivity_item, parent, false);
            holder = new ViewHolder();
            holder.content=view.findViewById(R.id.myactivity_content);
            holder.time=view.findViewById(R.id.myactivity_time);
            holder.type=view.findViewById(R.id.myactivity_type);
            view.setTag(holder);//为了复用holder
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        MyActivityClientVO ClientVO = ActivityClientList.get(position);
        holder.content.setText(ClientVO.name);
        holder.time.setText(sdf.format(new Date(ClientVO.startTime)).toString());
        holder.type.setText(activityMap.get(ClientVO.acType));
        return view;
    }

    private class ViewHolder {
        TextView content;
        TextView time;
        TextView type;
    }
}

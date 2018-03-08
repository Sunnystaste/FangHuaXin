package com.example.youyou.myheart.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youyou.myheart.Data.SortData;
import com.example.youyou.myheart.R;

import java.util.List;

import static com.example.youyou.myheart.Tool.AppConstant.SortList;

/**
 * Created by youyou on 2018/2/14.
 */

public class SortAdapter extends ArrayAdapter<SortData> {
    private int resourceId;
    public SortAdapter(@NonNull Context context, int resource, @NonNull List<SortData> objects) {
        super(context, resource, objects);
        this.resourceId=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        SortData s=getItem(position);
        View view ;
        ViewHolder holder;
        if(convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(R.layout.sort_item,parent,false);
            holder = new ViewHolder();
            holder.itemim=view.findViewById(R.id.sort_itemimg);
            holder.itemtx=view.findViewById(R.id.sort_itemtx);
            view.setTag(holder);//为了复用holder
        }else
        {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.itemim.setBackgroundResource(SortList.get(position).imgId);
        holder.itemtx.setText(SortList.get(position).name);
        return view;
    }
    class ViewHolder {
        ImageView itemim;
        TextView itemtx;
    }
}

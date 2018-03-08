package com.example.youyou.myheart.DiscoverActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.youyou.myheart.Adapter.SortAdapter;
import com.example.youyou.myheart.R;

import static com.example.youyou.myheart.Tool.AppConstant.HuodongList;
import static com.example.youyou.myheart.Tool.AppConstant.KechengList;
import static com.example.youyou.myheart.Tool.AppConstant.QitaList;
import static com.example.youyou.myheart.Tool.AppConstant.ShalongList;
import static com.example.youyou.myheart.Tool.AppConstant.SortList;
import static com.example.youyou.myheart.Tool.AppConstant.TiyanList;

/**
 * Created by youyou on 2017/12/9.
 */

public class LiveActivity extends Activity{
    ListView sort,xq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liveactivity);
        init();
        ArrayAdapter<String> tiyanadapter=new ArrayAdapter<String>(LiveActivity.this,android.R.layout.simple_list_item_1,TiyanList);
        ArrayAdapter<String> kechengadapter=new ArrayAdapter<String>(LiveActivity.this,android.R.layout.simple_list_item_1,KechengList);
        ArrayAdapter<String> huodongadapter=new ArrayAdapter<String>(LiveActivity.this,android.R.layout.simple_list_item_1,HuodongList);
        ArrayAdapter<String> shalongadapter=new ArrayAdapter<String>(LiveActivity.this,android.R.layout.simple_list_item_1,ShalongList);
        ArrayAdapter<String> qitaadapter=new ArrayAdapter<String>(LiveActivity.this,android.R.layout.simple_list_item_1,QitaList);
        SortAdapter sortAdapter=new SortAdapter(LiveActivity.this,R.layout.sort_item,SortList);
        sort.setAdapter(sortAdapter);
        sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        xq.setAdapter(tiyanadapter);
                        xq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(LiveActivity.this,SortxqActivity.class);
                                intent.putExtra("sort",TiyanList.get(position));
                                startActivity(intent);;
                            }
                        });
                        break;
                    case 1:
                        xq.setAdapter(kechengadapter);
                        xq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(LiveActivity.this, SortxqActivity.class);
                                intent.putExtra("sort", KechengList.get(position));
                                startActivity(intent);
                            }
                        });
                        break;
                    case 2:
                        xq.setAdapter(huodongadapter);
                        xq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(LiveActivity.this, SortxqActivity.class);
                                intent.putExtra("sort", HuodongList.get(position));
                                startActivity(intent);
                            }
                        });
                        break;
                    case 3:
                        xq.setAdapter(shalongadapter);
                        xq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(LiveActivity.this, SortxqActivity.class);
                                intent.putExtra("sort", ShalongList.get(position));
                                startActivity(intent);
                            }
                        });
                        break;
                    case 4:
                        xq.setAdapter(qitaadapter);
                        xq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(LiveActivity.this, SortxqActivity.class);
                                intent.putExtra("sort",QitaList.get(position));
                                startActivity(intent);
                            }
                        });
                        break;

                }
            }
        });
    }

    private void init() {
        sort=findViewById(R.id.sort_listview);
        xq=findViewById(R.id.sortXQ_listview);
    }
}

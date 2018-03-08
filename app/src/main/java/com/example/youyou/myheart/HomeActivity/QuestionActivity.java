package com.example.youyou.myheart.HomeActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youyou.myheart.Data.PostQuestion;
import com.example.youyou.myheart.R;
import com.example.youyou.myheart.Tool.AppConstant;
import com.google.gson.Gson;
import com.wx.wheelview.widget.WheelViewDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.youyou.myheart.UserActivity.UserFragment.token;
import static com.example.youyou.myheart.UserActivity.UserFragment.userData;
import static com.example.youyou.myheart.Tool.AppConstant.ServerUrl;
import static com.example.youyou.myheart.Tool.AppConstant.sortMap;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doPost;
import static com.example.youyou.myheart.Tool.Util.getKey;
import static com.example.youyou.myheart.UserActivity.MyFeedBack.showSoftInputFromWindow;

/**
 * Created by youyou on 2017/12/8.
 */

public class QuestionActivity extends Activity implements View.OnClickListener{
    Gson gson=new Gson();
    private PostQuestion data=new PostQuestion();
    private TextView sort,fabu,zishu;
    private ImageView back;
    private EditText editText;
    private RelativeLayout relativeLayout;
    private String addQuestion=ServerUrl+ AppConstant.addQuestion;
    private SwitchCompat s;
    final int maxNum = 300;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_layout);
        init();
    }

    private void init() {
        s=findViewById(R.id.switch1);
        relativeLayout=findViewById(R.id.relativeLayout_sort);
        sort=findViewById(R.id.textView_sort);
        fabu=findViewById(R.id.question_fabu);
        back=findViewById(R.id.back_question);
        editText=findViewById(R.id.edit_question);
        zishu=findViewById(R.id.zishu_question);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                zishu.setText(s.length()+"/"+maxNum);
            }
        });
        relativeLayout.setOnClickListener(this);
        fabu.setOnClickListener(this);
        back.setOnClickListener(this);
        showSoftInputFromWindow(this,editText);
    }

    private List createArrays() {
        ArrayList<String> list = new ArrayList<String>();
        Iterator iter = sortMap.entrySet().iterator();  //获得map的Iterator
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            list.add(entry.getValue().toString());
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.relativeLayout_sort:
                showDialog();
                break;
            case R.id.question_fabu:
                if(userData.getUserId()!=null) {
                    data.setUserId(userData.getUserId());
                    data.setAnonymous(s.isChecked());
                    data.setContent(editText.getText().toString());
                    Log.e("QuestionData", gson.toJson(data));
                    doPost(addQuestion, gson.toJson(data), new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.e("POST", response.body().string());
                        }
                    }, token);
                    Toast.makeText(this, "点击发布", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.back_question:
                this.finish();
                break;
        }
    }


    public void showDialog() {
        WheelViewDialog dialog = new WheelViewDialog(this);
        List<String> sort0=createArrays();
        dialog.setTitle("问题类型").setItems(sort0).setButtonText("确定").setDialogStyle(Color
                .parseColor("#6699ff")).setCount(7).show();
        dialog.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
            @Override
            public void onItemClick(int i, String s) {
                sort.setText(sort0.get(i));
                data.setQuType(getKey(sortMap,sort0.get(i)));
            }
        });
    }
}

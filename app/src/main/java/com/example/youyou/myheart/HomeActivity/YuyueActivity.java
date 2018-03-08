package com.example.youyou.myheart.HomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.youyou.myheart.Paint.CallDialog;
import com.example.youyou.myheart.R;

/**
 * Created by youyou on 2018/2/8.
 */

public class YuyueActivity extends Activity{
    private CallDialog d;
    private ImageView Call;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yueyue_layout);
        init();
    }

    private void init() {
        d=new CallDialog(this);
        Window dialogWindow = d.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        lp.y=50;
        dialogWindow.setAttributes(lp);
        Call=findViewById(R.id.call);
        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            d.setL1OnclickListener(new CallDialog.LlOnClickListener() {
                @Override
                public void L1Click() {
                    call("83208198");
                }
            });

            d.setL2OnclickListener(new CallDialog.L2OnClickListener() {
                @Override
                public void L2Click() {
                    call("61830031");
                }
            });


            d.setL3OnclickListener(new CallDialog.L3OnClickListener() {
                @Override
                public void L3Click() {
                    call("4001619995");
                }
            });
            d.setL4OnclickListener(new CallDialog.L4OnClickListener() {
                @Override
                public void L4Click() {
                    d.dismiss();
                }
            });
            d.show();
        }
    });
    }

    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

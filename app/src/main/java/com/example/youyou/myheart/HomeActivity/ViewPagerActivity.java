package com.example.youyou.myheart.HomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youyou.myheart.R;

/**
 * Created by youyou on 2017/12/8.
 */

public class ViewPagerActivity extends Activity implements View.OnClickListener{
    TextView title;
    WebView wv;
    ImageView back,progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.headlink);
        init();
    }

    private void init() {
        progress=findViewById(R.id.progress);
        back=findViewById(R.id.back_link);
        back.setOnClickListener(this);
        title=findViewById(R.id.title);
        wv=findViewById(R.id.webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setSupportMultipleWindows(true);
        wv.setWebViewClient(new WebViewClient());
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String t) {
                super.onReceivedTitle(view, t);
                Log.d("ANDROID_LAB", "TITLE=" + t);
                title.setText(t);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    ViewGroup.LayoutParams para;
                    para = progress.getLayoutParams();
                    Display display = getWindowManager().getDefaultDisplay();
                    int width= display.getWidth();
                    para.height = 3;
                    para.width = width*newProgress/100;
                    progress.setLayoutParams(para);
                }
                else
                    progress.setVisibility(View.GONE);
            }
        };
        wv.setWebChromeClient(wvcc);
        Intent intent =getIntent();
        String url=intent.getStringExtra("url");
        wv.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_link:
                this.finish();
                break;
        }
    }
}

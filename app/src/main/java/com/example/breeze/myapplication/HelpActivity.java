package com.example.breeze.myapplication;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Created by sunhq on 2015/9/14.
 */
public class HelpActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_task);
        setContentView(R.layout.help);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://w.mengzhuanapp.com/hz/help");

        TextView back = (TextView) findViewById(R.id.task_back);
        back.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.task_back:
                finish();
                break;
        }
    }
}

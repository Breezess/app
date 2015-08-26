package com.example.breeze.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import net.youmi.android.offers.OffersManager;

import cn.waps.AppConnect;
import im.fir.sdk.FIR;
import im.fir.sdk.callback.VersionCheckCallback;
import im.fir.sdk.version.AppVersion;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_main);
        setContentView(R.layout.activity_main);

        FIR.init(this);
        FIR.setDebug(true);
        FIRUtils.checkForUpdate(this, true);

        ImageButton button = (ImageButton) findViewById(R.id.button2);
        button.setOnClickListener(this);

        TableRow task1 = (TableRow) findViewById(R.id.man_task1);
        task1.setOnClickListener(this);
        ImageButton man_task1_image = (ImageButton) findViewById(R.id.man_task1_image);
        man_task1_image.setOnClickListener(this);

        TableRow task2 = (TableRow) findViewById(R.id.man_task2);
        task2.setOnClickListener(this);
        ImageButton man_task2_image = (ImageButton) findViewById(R.id.man_task2_image);
        man_task2_image.setOnClickListener(this);

        TextView exchange = (TextView) findViewById(R.id.exchange);
        exchange.setOnClickListener(this);


        ImageButton button1 = (ImageButton) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        ImageButton button3 = (ImageButton) findViewById(R.id.button3);
        button3.setOnClickListener(this);
    }

    /**
     * 跳转到...
     */
    private void redirectTo(){
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button2:
                redirectTo();
                break;
            case R.id.man_task1:
            case R.id.man_task1_image:
                OffersManager.getInstance(this).onAppLaunch();
                OffersManager.getInstance(this).showOffersWall();
                break;
            case R.id.man_task2:
            case R.id.man_task2_image:
                AppConnect.getInstance(this).showOffers(this);
                break;
            case R.id.exchange:
                try {
                    PackageManager packageManager = getPackageManager();
                    Intent intent=new Intent();
                    intent = packageManager.getLaunchIntentForPackage("com.tencent.mm");
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent viewIntent = new
                            Intent("android.intent.action.VIEW", Uri.parse("http://weixin.qq.com/"));
                    startActivity(viewIntent);
                }
                break;
            case R.id.button1:
            case R.id.button3:
                Toast.makeText(this, "功能正在努力开发中.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        AppConnect.getInstance(this).close();
        super.onDestroy();
    }
}

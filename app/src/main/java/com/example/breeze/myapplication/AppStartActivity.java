package com.example.breeze.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

// 广告商接入
import net.youmi.android.AdManager;
import cn.waps.AppConnect;

/**
 * Created by Breeze on 15/8/16.
 */
public class AppStartActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.start, null);
        setContentView(view);

        // 有米
        AdManager.getInstance(this).init("47dd68ac7484b076", "3c9dac355822f41d", false);

        // 万普
        AppConnect.getInstance("78a9c935d121f38c69595007cddc454b", "78a9c935d121f38c69595007cddc454b", this);

        //渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
        aa.setDuration(2000);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationEnd(Animation arg0) {
                redirectTo();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationStart(Animation animation) {}

        });
    }

    /**
     * 跳转到...
     */
    private void redirectTo(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
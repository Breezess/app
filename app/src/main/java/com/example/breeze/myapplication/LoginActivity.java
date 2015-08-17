package com.example.breeze.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Breeze on 15/8/16.
 */
public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 返回箭头（默认不显示）
        getActionBar().setDisplayHomeAsUpEnabled(false);
        // 左侧图标点击事件使能
        getActionBar().setHomeButtonEnabled(true);
        // 使左上角图标(系统)是否显示
        getActionBar().setDisplayShowHomeEnabled(false);
        // 显示标题
        getActionBar().setDisplayShowTitleEnabled(false);
        //显示自定义视图
        getActionBar().setDisplayShowCustomEnabled(true);
        View actionbarLayout = LayoutInflater.from(this).inflate(
                R.layout.login, null);
        getActionBar().setCustomView(actionbarLayout);
    }

}

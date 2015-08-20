package com.example.breeze.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import net.youmi.android.offers.OffersManager;

import cn.waps.AppConnect;

/**
 * Created by sunhq on 2015/8/19.
 */
public class TaskActivity  extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_task);
        setContentView(R.layout.task);

        TableRow button = (TableRow) findViewById(R.id.task1);
        button.setOnClickListener(this);

        TableRow task2 = (TableRow) findViewById(R.id.task2);
        task2.setOnClickListener(this);

        TextView back = (TextView) findViewById(R.id.task_back);
        back.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.task_back:
                finish();
                break;
            case R.id.task1:
                OffersManager.getInstance(this).onAppLaunch();
                OffersManager.getInstance(this).showOffersWall();
                break;
            case R.id.task2:
                AppConnect.getInstance(this).showOffers(this);
                break;
        }
    }

    private void onDestroy(Bundle savedInstanceState) {
        OffersManager.getInstance(this).onAppExit();
    }

    @Override
    protected void onDestroy() {
        AppConnect.getInstance(this).close();
        super.onDestroy();
    }

}

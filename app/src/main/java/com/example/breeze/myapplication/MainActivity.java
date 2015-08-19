package com.example.breeze.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableRow;

import net.youmi.android.offers.OffersManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_main);
        setContentView(R.layout.activity_main);

        ImageButton button = (ImageButton) findViewById(R.id.button2);
        button.setOnClickListener(this);

        TableRow task1 = (TableRow) findViewById(R.id.man_task1);
        task1.setOnClickListener(this);
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
                OffersManager.getInstance(this).onAppLaunch();
                OffersManager.getInstance(this).showOffersWall();
                break;
        }
    }
}

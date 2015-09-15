package com.example.breeze.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.scottyab.aescrypt.AESCrypt;
import com.squareup.picasso.Picasso;
import com.yql.sdk.DRSdk;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.GeneralSecurityException;

import cn.waps.AppConnect;
import im.fir.sdk.FIR;

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

        TableRow task1 = (TableRow) findViewById(R.id.main_task1);
        task1.setOnClickListener(this);
        ImageButton main_task1_image = (ImageButton) findViewById(R.id.main_task1_image);
        main_task1_image.setOnClickListener(this);

        TableRow task2 = (TableRow) findViewById(R.id.main_task2);
        task2.setOnClickListener(this);
        ImageButton main_task2_image = (ImageButton) findViewById(R.id.main_task2_image);
        main_task2_image.setOnClickListener(this);

        TableRow task3 = (TableRow) findViewById(R.id.main_task3);
        task3.setOnClickListener(this);
        ImageButton main_task3_image = (ImageButton) findViewById(R.id.main_task3_image);
        main_task3_image.setOnClickListener(this);

        TextView exchange = (TextView) findViewById(R.id.exchange);
        exchange.setOnClickListener(this);

        ImageButton help = (ImageButton) findViewById(R.id.help);
        help.setOnClickListener(this);

        TextView about = (TextView) findViewById(R.id.about);
        about.setOnClickListener(this);

        ImageButton button1 = (ImageButton) findViewById(R.id.button1);
        button1.setOnClickListener(this);

        ImageButton button3 = (ImageButton) findViewById(R.id.button3);
        button3.setOnClickListener(this);

        SharedPreferences user = getSharedPreferences("userInfo", MODE_PRIVATE);
        int uid = user.getInt("uid", 0);
        TextView uid_id = (TextView) findViewById(R.id.uid);
        uid_id.setText("体验号：" + String.valueOf(uid));

        String nickname = user.getString("nickname", "----");
        TextView nickname_id = (TextView) findViewById(R.id.nickname);
        nickname_id.setText(nickname);

        String headimage = user.getString("headimage", "");
        ImageView i = (ImageView) findViewById(R.id.head_image);
        Picasso.with(this).load(headimage).into(i);

        viewIntegration();

        String openid = user.getString("openid", "");
        // 有米
        OffersManager.getInstance(this).setCustomUserId(openid);
        OffersManager.setUsingServerCallBack(true);
        AdManager.getInstance(this).init("47dd68ac7484b076", "3c9dac355822f41d", false);

        // 万普
        AppConnect.getInstance("78a9c935d121f38c69595007cddc454b", openid, this);

        // 点入
        DRSdk.initialize(this, true, openid);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewIntegration();
    }

    private void viewIntegration() {
        String JSONDataUrl = "http://w.mengzhuanapp.com/hz/a_update_score";
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        String password = "nMHnw4a8mzVFKDJj";
        SharedPreferences user = getSharedPreferences("userInfo", MODE_PRIVATE);

        JSONObject obj = new JSONObject();
        try {
            obj.put("openid", AESCrypt.encrypt(password, user.getString("openid", "")));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, JSONDataUrl, obj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean isok = response.getBoolean("isok");
                    JSONObject data = response.getJSONObject("data");
                    if (isok == false) {
                        String msg = data.getString("msg");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    TextView mbalance = (TextView) findViewById(R.id.mbalance);
                    TextView today_score = (TextView) findViewById(R.id.today_score);
                    mbalance.setText(data.getString("mbalance"));
                    today_score.setText(data.getString("today_score"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("连接失败");
                Toast.makeText(getApplicationContext(), "连接服务器失败", Toast.LENGTH_SHORT).show();
            }

        });

        mRequestQueue.add(jsonObjRequest);
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
            case R.id.main_task1:
            case R.id.main_task1_image:
                OffersManager.getInstance(this).onAppLaunch();
                OffersManager.getInstance(this).showOffersWall();
                break;
            case R.id.main_task2:
            case R.id.main_task2_image:
                AppConnect.getInstance(this).showOffers(this);
                break;
            case R.id.main_task3:
            case R.id.main_task3_image:
                DRSdk.showAdWall(this, DRSdk.DR_OFFER);
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
            case R.id.help:
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        AppConnect.getInstance(this).close();
        super.onDestroy();
    }
}

package com.example.breeze.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Breeze on 15/8/16.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_login);
        setContentView(R.layout.login);
        Button button = (Button)findViewById(R.id.button2);
        button.setOnClickListener(this);
    }

    public void onClick(View v) {

        EditText text = (EditText) findViewById(R.id.code);
        String value = text.getText().toString();
        if(value == null || value.isEmpty()) {
            Toast.makeText(this, "请填写最游验证码", Toast.LENGTH_SHORT).show();
        }
        else {
            ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            pDialog.dismiss();
            finish();
        }
    }


}

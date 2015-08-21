package com.example.breeze.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Breeze on 15/8/16.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private Context mContext;

    // json object response url
    private String urlJsonObj = "http://api.androidhive.info/volley/person_object.json";

    // json array response url
    private String urlJsonArry = "http://api.androidhive.info/volley/person_array.json";

    private static String TAG = MainActivity.class.getSimpleName();

    private EditText text;

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

        text = (EditText) findViewById(R.id.code);
        String value = text.getText().toString();
        if(value == null || value.isEmpty()) {
            Toast.makeText(this, "请填写最游验证码", Toast.LENGTH_SHORT).show();
        }
        else {
            ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            String JSONDataUrl = "http://pipes.yahooapis.com/pipes/pipe.run?_id=giWz8Vc33BG6rQEQo_NLYQ&_render=json";
//            final ProgressDialog progressDialog = ProgressDialog.show(this, "This is title", "...Loading...");
//            mRequestQueue = Volley.newRequestQueue(this);
//            JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, urlJsonObj, (String) null, new Response.Listener<JSONObject>() {
//
//                @Override
//                public void onResponse(JSONObject response) {
//                    System.out.println("response="+response);
//                }
//
//            }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    System.out.println("sorry,Error");
//                }
//
//            });

//            mRequestQueue.add(jsonObjRequest);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            pDialog.dismiss();
            finish();
        }
    }


}

package com.example.breeze.myapplication;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SyncAdapterType;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
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
import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Objects;

/**
 * Created by Breeze on 15/8/16.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static int mNetWorkType;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private Context mContext;

    // json object response url
    private String urlJsonObj = "http://api.androidhive.info/volley/person_object.json";

    // json array response url
    private String urlJsonArry = "http://api.androidhive.info/volley/person_array.json";

    private static String TAG = MainActivity.class.getSimpleName();

    private EditText text;

    /** 没有网络 */
    public static final int NETWORKTYPE_INVALID = 0;
    /** wap网络 */
    public static final int NETWORKTYPE_WAP = 102;
    /** 2G网络 */
    public static final int NETWORKTYPE_2G = 200;
    /** 3G和3G以上网络，或统称为快速网络 */
    public static final int NETWORKTYPE_3G = 300;
    /** wifi网络 */
    public static final int NETWORKTYPE_WIFI = 201;

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
            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            String JSONDataUrl = "http://w.mengzhuanapp.com/hz/a_register";
            mRequestQueue = Volley.newRequestQueue(this);

            JSONObject obj = new JSONObject();
            String password = "nMHnw4a8mzVFKDJj";
            String code = value;

            TelephonyManager mngr = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
            String imei = mngr.getDeviceId();
            if (imei == null) {
                pDialog.dismiss();
                Toast.makeText(this, "系统错误无法登陆", Toast.LENGTH_SHORT).show();
                return;
            }
            String imsi = mngr.getSimSerialNumber();

            String android_id = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);

            int net_type = getNetWorkType(this);

            if (net_type == 0) {
                pDialog.dismiss();
                Toast.makeText(this, "网络错误无法登陆", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                obj.put("code", AESCrypt.encrypt(password, code));
                obj.put("imei", AESCrypt.encrypt(password, imei));
                obj.put("imsi", AESCrypt.encrypt(password, imsi));
                obj.put("device", AESCrypt.encrypt(password, android.os.Build.MODEL + " "
                        + android.os.Build.VERSION.SDK + " "
                        + android.os.Build.VERSION.RELEASE));

//                BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
//                String deviceName = myDevice.getName();
//                System.out.println(deviceName);
//                if (deviceName != "") {
//                    deviceName = AESCrypt.encrypt(password, deviceName);
//                }
                obj.put("device_name", "");
                obj.put("android_id", AESCrypt.encrypt(password, android_id));

                String mac = getMacAddress(this);
                if (mac != null) {
                    mac = AESCrypt.encrypt(password, mac);
                }
                obj.put("mac", mac);
                obj.put("idr", AESCrypt.encrypt(password, this.getPackageName()));
                obj.put("net_type", AESCrypt.encrypt(password, String.valueOf(net_type)));


                if (net_type == NETWORKTYPE_WIFI) {
                    WifiManager wifiManager = (WifiManager) mContext
                            .getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    String bssid = wifiInfo.getBSSID();
                    String ssid = wifiInfo.getSSID();
                    obj.put("bssid", AESCrypt.encrypt(password, bssid));
                    obj.put("ssid", AESCrypt.encrypt(password, ssid));
                }
                else {
                    obj.put("bssid", "");
                    obj.put("ssid", "");
                }

                obj.put("uuid", "");

                obj.put("jb", AESCrypt.encrypt(password, String.valueOf(haveRoot())));


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
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            return;
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    pDialog.dismiss();
                    finish();
                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    System.out.println("连接失败");
                    Toast.makeText(getApplicationContext(), "连接服务器失败", Toast.LENGTH_SHORT).show();
                }


            });

            mRequestQueue.add(jsonObjRequest);


        }
    }


    /**
     * 获取MAC地址
     * @param mContext
     * @return
     */
    public static String getMacAddress(Context mContext) {
        String macStr = "";
        WifiManager wifiManager = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getMacAddress() != null) {
            macStr = wifiInfo.getMacAddress();// MAC地址
        } else {
            macStr = "null";
        }

        return macStr;
    }

    /**
     * 获取网络状态，wifi,wap,2g,3g.
     *
     * @param context 上下文
     * @return int 网络状态 {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G},          *{@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP}* <p>{@link #NETWORKTYPE_WIFI}
     */
    public static int getNetWorkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                mNetWorkType = TextUtils.isEmpty(proxyHost)
                        ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G : NETWORKTYPE_2G)
                        : NETWORKTYPE_WAP;
            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }
        return mNetWorkType;
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    protected static int haveRoot() {

        int i = execRootCmdSilent("echo test"); // 通过执行测试命令来检测
        if (i != -1) {
            return 1;
        }
        return 0;
    }

    protected static int execRootCmdSilent(String paramString) {
        try {
            Process localProcess = Runtime.getRuntime().exec("su");
            Object localObject = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream(
                    (OutputStream) localObject);
            String str = String.valueOf(paramString);
            localObject = str + "\n";
            localDataOutputStream.writeBytes((String) localObject);
            localDataOutputStream.flush();
            localDataOutputStream.writeBytes("exit\n");
            localDataOutputStream.flush();
            localProcess.waitFor();
            int result = localProcess.exitValue();
            return (Integer) result;
        } catch (Exception localException) {
            localException.printStackTrace();
            return -1;
        }
    }

    private String getAppInfo() {
        try {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = this.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return pkName + "   " + versionName + "  " + versionCode;
        } catch (Exception e) {
        }
        return null;
    }

}

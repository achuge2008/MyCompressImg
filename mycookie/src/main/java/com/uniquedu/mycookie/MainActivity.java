package com.uniquedu.mycookie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @InjectView(R.id.button_getpic)
    Button buttonGetpic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.button_getpic)
    public void onClick() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.whjfs.com/mvcwebmis/login/checkcode");
                    CookieManager cookieManager = new CookieManager();
                    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
                    CookieHandler.setDefault(cookieManager);
                    HttpURLConnection httpurlconn = (HttpURLConnection) url.openConnection();
                    httpurlconn.setRequestMethod("GET");
                    //设置编码格式
                    //设置接受的数据类型
                    httpurlconn.setRequestProperty("Accept-Charset", "utf-8");
                    //设置可以序列化的java对象
                    httpurlconn.setRequestProperty("Context-Type", "application/x-www-form-urlencoded");

                    int code = httpurlconn.getResponseCode();
                    httpurlconn.getHeaderFields();
                    CookieStore store = cookieManager.getCookieStore();
                    List<HttpCookie> cookies = store.getCookies();

                    for (HttpCookie cookie : cookies) {
                        String key = cookie.getName();
                        String value = cookie.getValue();
//                        Toast.makeText(MainActivity.this, "获得cookie：key==>" + key + "   value===>" + value, Toast.LENGTH_SHORT).show();
                        Log.d("MainActivity", "获得cookie：key==>" + key + "   value===>" + value);
                        //        BDebug.e(HTTP_COOKIE, cookie.getName() + "---->" + cookie.getDomain() + "------>" + cookie.getPath());
                    }
                    Log.d(TAG, "获得的状态码是：" + code);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}

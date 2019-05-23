package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class registerActivity extends AppCompatActivity {

    private Handler mUI_Handler = new Handler();
    private HandlerThread mThread;
    //繁重執行序用的 (時間超過3秒的)
    private Handler mThreadHandler;;
    public String input_email ="", input_password ="", input_name ="", input_phone ="", self_email, self_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button signup = findViewById(R.id.registerbutton);
        Button registerback = findViewById(R.id.registerbackbutton);

        final EditText reaccount = findViewById(R.id.registeraccount);
        final EditText repassword = findViewById(R.id.registerpwd);
        final EditText rename = findViewById(R.id.registername);
        final EditText rephone = findViewById(R.id.registerphone);
        registerback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                registerActivity.this.finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                input_email = reaccount.getText().toString() ;
                input_password = repassword.getText().toString();
                input_name = rename.getText().toString();
                input_phone = rephone.getText().toString();
                thread();

            }
        });
    }


    private void thread() {
        mThread = new HandlerThread("net");
        mThread.start();
        mThreadHandler = new Handler(mThread.getLooper());
        mThreadHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                // TODO 自動產生的方法 Stub
                final String resuccess = executeQuery(input_email, input_password, input_name, input_phone);
                mUI_Handler.post(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        // TODO 自動產生的方法 Stub
                        if(resuccess.length() >= 5){
                            selfdata(resuccess);
                            Nextactivity();
                        }else {
                            Toast.makeText(registerActivity.this, "註冊失敗或已有相同信箱註冊", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    private  void Nextactivity(){
        Toast.makeText(registerActivity.this, "註冊成功，將在五秒後轉跳頁面", Toast.LENGTH_LONG).show();
        Timer time = new Timer();
        final Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("data", "email:"+self_email+"\nname:"+self_name);
        TimerTask tk = new TimerTask(){
            @Override
            public void run() {
                startActivity(intent);
                registerActivity.this.finish();

            }
        };time.schedule(tk, 5000);
    }
    private String executeQuery(String query0, String query1, String query2, String query3)
    {
        String result = "";
        HttpURLConnection uc = null;
        try {
            URL url = new URL("http://arthurlee.ddns.net/appsignup.php");
            uc = (HttpURLConnection) url.openConnection();
            uc.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(uc.getOutputStream());
            out.write("email=" + query0 + "&" + "name=" + query2 + "&" + "phone=" + query3 + "&" + "pwd=" + query1);
            out.flush();
            InputStream in = new BufferedInputStream(uc.getInputStream());
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            result = builder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("Error1", e.toString());
        }finally {
            uc.disconnect();
        }
        return result;
    }

    public final void selfdata(String input)
    {
        /*
         * SQL 結果有多筆資料時使用JSONArray
         * 只有一筆資料時直接建立JSONObject物件
         * JSONObject jsonData = new JSONObject(result);
         */
        try
        {
            JSONArray jsonArray = new JSONArray(input);
            JSONObject jsonData = jsonArray.getJSONObject(0);
            self_email = jsonData.getString("Email");
            self_name = jsonData.getString("Name");
        }
        catch (JSONException e)
        {
            // TODO 自動產生的 catch 區塊
            e.printStackTrace();
        }
    }


}

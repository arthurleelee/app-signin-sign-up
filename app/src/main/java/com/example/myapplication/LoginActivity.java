package com.example.myapplication;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class LoginActivity extends AppCompatActivity {


    private Handler mUI_Handler = new Handler();
    private HandlerThread mThread;
    //繁重執行序用的 (時間超過3秒的)
    private Handler mThreadHandler;
    public String input_emailname, input_password, self_email, self_name;
    private boolean nextactivity = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login1 = findViewById(R.id.buttonlogin1);
        Button register1 = findViewById(R.id.button1_2);

        final EditText account = findViewById(R.id.account);
        final EditText password = findViewById(R.id.password);
        register1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                registactivity();
            }
        });
        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                input_emailname = account.getText().toString() ;
                input_password = password.getText().toString();
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
                final String jsonString = executeQuery(input_emailname, input_password);
                mUI_Handler.post(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        // TODO 自動產生的方法 Stub
                        selfdata(jsonString);
                        if(self_email != null){
                            nextactivity = true;
                            Nextactivity();
                        }
                    }
                });
            }
        });

    }
    private  void registactivity(){
        Intent intent = new Intent(this,registerActivity.class);
        startActivity(intent);
    }
    private  void Nextactivity(){
        if(nextactivity){
            nextactivity = false;
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("data", "email:"+self_email+"\nname:"+self_name);
            startActivity(intent);
            LoginActivity.this.finish();
        }
    }
    private String executeQuery(String query0, String query1)
    {
        String result = "";
        HttpURLConnection uc = null;
        try {
            URL url = new URL("http://arthurlee.ddns.net/app.php");
            uc = (HttpURLConnection) url.openConnection();
            uc.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(uc.getOutputStream());
            out.write("email=" + query0 + "&" + "name=" + query0 + "&" + "phone=" + query0 + "&" + "pwd=" + query1);
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

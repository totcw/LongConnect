package com.example.lyf.longconnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private Button btn,btn2;
    private MyBroadcase broadcase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        btn = (Button) findViewById(R.id.btn);
        btn2 = (Button) findViewById(R.id.btn2);


        IntentFilter filter = new IntentFilter(ConnectManager.BROADCAST_ACTION);
        broadcase = new MyBroadcase();
        registerReceiver(broadcase, filter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManager.getmInstance().writeToServer("123");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ConnectService.class);
                startService(intent);
            }
        });
    }





    public class MyBroadcase extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            tv.setText(intent.getStringExtra("message"));
        }
    }



}

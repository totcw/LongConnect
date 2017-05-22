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
    private MyBroadcaseRedceiver mBroadcaseRedceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        btn = (Button) findViewById(R.id.btn);
        btn2 = (Button) findViewById(R.id.btn2);


        IntentFilter filter = new IntentFilter(ConnectManager.BROADCAST_ACTION);
        mBroadcaseRedceiver = new MyBroadcaseRedceiver();
        registerReceiver(mBroadcaseRedceiver, filter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManager.getmInstance().writeToServer("发给服务器的消息 ");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开启服务
                Intent intent = new Intent(MainActivity.this, ConnectService.class);
                startService(intent);
            }
        });
    }





    public class MyBroadcaseRedceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            tv.setText(intent.getStringExtra(ConnectManager.MESSAGE));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcaseRedceiver);
    }
}

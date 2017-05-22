package com.example.lyf.longconnect;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * 开启长连接的服务
 * Created by lyf on 2017/5/20.
 */

public class ConnectService extends Service {
    private ConnectThred connectThred;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //使用子线程开启连接
        connectThred = new ConnectThred("mina",getApplicationContext());
        connectThred.start();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        connectThred.disConnection();
        connectThred=null;
    }

    /**
     * 负责调用connectmanager类来完成与服务器的连接
     */
    class ConnectThred extends HandlerThread {

        boolean isConnection;
        ConnectManager mManager;
        public ConnectThred(String name,Context context) {
            super(name);
            ConnectConfig config = new ConnectConfig.Builder(context)
                    .setIp("192.168.0.113")
                    .setPort(9123)
                    .setReadBufferSize(10240)
                    .setConnectionTimeout(10000)
                    .bulid();
            //创建连接的管理类
            mManager = new ConnectManager(config);
        }

        @Override
        protected void onLooperPrepared() {
            //利用循环请求连接
            while (true) {
                isConnection = mManager.connect();
                if (isConnection) {
                    //当请求成功的时候,跳出循环
                    break;
                }
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {

                }
            }
        }

        /**
         *
         */
        public void disConnection(){
            mManager.disConnect();
        }

    }


}

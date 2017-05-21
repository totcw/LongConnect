package com.example.lyf.longconnect;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
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
        private Context context;
        boolean isConnection;
        ConnectManager mManager;
        public ConnectThred(String name,Context context) {
            super(name);
            this.context = context;
            ConnectConfig config = new ConnectConfig.Builder(context)
                    .setIp("")
                    .setPort(9123)
                    .setReadBufferSize(10240)
                    .setConnectionTimeout(10000)
                    .bulid();
        }

        @Override
        protected void onLooperPrepared() {
            while (true) {
                isConnection = mManager.connect();
                if (isConnection) {
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

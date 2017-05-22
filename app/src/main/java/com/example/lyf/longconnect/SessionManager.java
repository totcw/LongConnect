package com.example.lyf.longconnect;

import org.apache.mina.core.session.IoSession;

/**
 * session管理类,通过ioSession与服务器通信
 * Created by lyf on 2017/5/21.
 */

public class SessionManager {
    private static  SessionManager mInstance = null;

    private IoSession ioSession;//最终与服务器 通信的对象

    public static SessionManager getmInstance(){
        if (mInstance == null) {
            synchronized (SessionManager.class) {
                if (mInstance == null) {
                    mInstance = new SessionManager();
                }
            }
        }
        return mInstance;
    }
    private SessionManager(){

    }

    public void setIoSession(IoSession ioSession) {
        this.ioSession = ioSession;
    }

    /**
     * 将对象写到服务器
     */
    public void writeToServer(Object msg) {
        if (ioSession != null) {
            ioSession.write(msg);
        }
    }

    /**
     * 关闭连接
     */
    public void closeSession() {
        if (ioSession != null) {
            ioSession.closeOnFlush();
        }
    }

    public void removeSession() {
        ioSession = null;
    }


}

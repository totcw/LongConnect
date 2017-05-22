package com.example.lyf.longconnect;

import android.content.Context;
import android.content.Intent;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;

/**
 * 连接的管理类
 * Created by lyf on 2017/5/20.
 */

public class ConnectManager {
    public static final String BROADCAST_ACTION="com.example.lyf.longconnect";
    public static final String MESSAGE="message";

    private ConnectConfig mConfig;//配置文件
    private WeakReference<Context> mContext;
    private NioSocketConnector mConnection;
    private IoSession mSessioin;
    private InetSocketAddress mAddress;

    public ConnectManager(ConnectConfig mConfig) {
        this.mConfig = mConfig;
        this.mContext = new WeakReference<Context>(mConfig.getmContext());
        init();
    }

    private void init() {
        mAddress = new InetSocketAddress(mConfig.getIp(),mConfig.getPort());
        //创建连接对象
        mConnection = new NioSocketConnector();
        //设置连接地址
        mConnection.setDefaultRemoteAddress(mAddress);
        mConnection.getSessionConfig().setReadBufferSize(mConfig.getReadBufferSize());
        //设置过滤
        mConnection.getFilterChain().addLast("logger",new LoggingFilter());
        mConnection.getFilterChain().addLast("codec",new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        //设置连接监听
        mConnection.setHandler(new DefaultHandler(mContext.get()));
    }

    private static class DefaultHandler extends IoHandlerAdapter{
        private Context context;

        public DefaultHandler(Context context) {
            this.context = context;
        }

        /**
         * 连接成功时回调的方法
         * @param session
         * @throws Exception
         */
        @Override
        public void sessionOpened(IoSession session) throws Exception {
            //当与服务器连接成功时,将我们的session保存到我们的sesscionmanager类中,从而可以发送消息到服务器
            SessionManager.getmInstance().setIoSession(session);
        }

        /**
         * 接收到消息时回调的方法
         * @param session
         * @param message
         * @throws Exception
         */
        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {

            if (context != null) {
                //将接收到的消息利用广播发送出去
                Intent intent = new Intent(BROADCAST_ACTION);
                intent.putExtra(MESSAGE,message.toString());
                context.sendBroadcast(intent);
            }
        }
    }

    /**
     * 与服务器连接的方法
     * @return
     */
    public boolean connect(){
       try{
           ConnectFuture future =mConnection.connect();
           future.awaitUninterruptibly();
           mSessioin = future.getSession();
       }catch (Exception e){
           e.printStackTrace();
            return false;
       }
        return mSessioin==null?false:true;
    }

    /**
     * 断开连接的方法
     */
    public void disConnect(){
        mConnection.dispose();
        mConnection=null;
        mSessioin=null;
        mAddress=null;
        mContext=null;
    }


}

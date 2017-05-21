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
 * Created by lyf on 2017/5/20.
 */

public class ConnectManager {
    public static final String BROADCAST_ACTION="com.example.lyf.longconnect";
    private static final String MESSAGE="message";

    private ConnectConfig mConfig;
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
        mConnection = new NioSocketConnector();
        mConnection.getSessionConfig().setReadBufferSize(mConfig.getReadBufferSize());
        mConnection.getFilterChain().addLast("logging",new LoggingFilter());
        mConnection.getFilterChain().addLast("codec",new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        mConnection.setHandler(new DefaultHandler(mContext.get()));
    }

    private static class DefaultHandler extends IoHandlerAdapter{
        private Context context;

        public DefaultHandler(Context context) {
            this.context = context;
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            //将我们的session保存到我们的sesscionmanager类中,从而可以发送消息到服务器
        }

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

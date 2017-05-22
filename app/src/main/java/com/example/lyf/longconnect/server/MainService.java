package com.example.lyf.longconnect.server;

import java.net.InetSocketAddress;
import java.util.Date;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * mina服务端
 */
public class MainService {
	
	public static void main(String[] args) {
		IoAcceptor acceptor = new NioSocketAcceptor();
		//添加日志过滤
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		//设置回调监听
		acceptor.setHandler(new DemoServerHandler());
		//设置读取大小
		acceptor.getSessionConfig().setReadBufferSize(2048);
		//设置超时时间
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		
		try {
			//开启监听
			acceptor.bind(new InetSocketAddress(9123));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private static class  DemoServerHandler extends  IoHandlerAdapter{
		
		@Override
		public void sessionCreated(IoSession session) throws Exception {
			// TODO Auto-generated method stub
			super.sessionCreated(session);


		}

		/**
		 * 一个客户端连接时回调的方法
		 * @param session
		 * @throws Exception
         */
		@Override
		public void sessionOpened(IoSession session) throws Exception {
			// TODO Auto-generated method stub
			super.sessionOpened(session);

		}

		/**
		 * 接收到消息时回调的方法
		 * @param session
		 * @param message
         * @throws Exception
         */
		@Override
		public void messageReceived(IoSession session, Object message)
				throws Exception {
			// TODO Auto-generated method stub
			super.messageReceived(session, message);
			//接收客户端的消息
			String msg = message.toString();
			Date date = new Date();
			//发送消息给客户端
			session.write(msg+date.toString());
		}

		/**
		 * 发送消息时回调的方法
		 * @param session
		 * @param message
         * @throws Exception
         */
		@Override
		public void messageSent(IoSession session, Object message)
				throws Exception {
			// TODO Auto-generated method stub
			super.messageSent(session, message);
		}

		/**
		 * 一个客户端断开时回调的方法
		 * @param session
		 * @throws Exception
         */
		@Override
		public void sessionClosed(IoSession session) throws Exception {
			// TODO Auto-generated method stub
			super.sessionClosed(session);

		}
		
	}
	
}

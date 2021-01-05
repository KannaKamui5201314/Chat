package com.tw.login.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tw.login.proto.CsLogin.CSLoginReq;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SocketServerHandler extends SimpleChannelInboundHandler<String> {
	private static final Log logger = LogFactory.getLog(LoginSocketServer.class);
	private String data;
 
	@Override
	public void exceptionCaught(ChannelHandlerContext arg0, Throwable arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}
 
	@Override
	public void channelRead(ChannelHandlerContext arg0, Object msg) throws Exception {
	    // TODO Auto-generated method stub
	    CSLoginReq clientReq = (CSLoginReq)msg;
	    String user_name = clientReq.getLoginInfo().getUserName();
	    String pass_word = clientReq.getLoginInfo().getPassword();
	    logger.info("数据内容：UserName="+user_name+",Password="+pass_word);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, String data) throws Exception {
		// TODO Auto-generated method stub
		logger.info("数据内容：data="+data);
	}
}

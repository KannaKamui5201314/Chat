package com.tw.login.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tw.login.proto.CsLogin.CSLoginInfo;
import com.tw.login.proto.CsLogin.CSLoginReq;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SocketClientHandler extends SimpleChannelInboundHandler<String> {
	private static final Log logger = LogFactory.getLog(LoginSocketClient.class);
 
	@Override
	public void exceptionCaught(ChannelHandlerContext arg0, Throwable arg1) throws Exception {
		// TODO Auto-generated method stub
	}
 
	@Override
	public void channelRead(ChannelHandlerContext arg0, Object msg) throws Exception {
		// TODO Auto-generated method stub
		String data = msg.toString();
		logger.info("数据内容：data="+data);
	}
 
	@Override
	protected void channelRead0(ChannelHandlerContext arg0, String data) throws Exception {
		// TODO Auto-generated method stub
		logger.info("数据内容：data="+data);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
	    CSLoginReq.Builder req_builder = CSLoginReq.newBuilder();
	    CSLoginInfo.Builder info_builder = CSLoginInfo.newBuilder();
	    info_builder.setUserName("linshuhe");
	    info_builder.setPassword("123456");
	    CSLoginInfo info = info_builder.build();
	    req_builder.setLoginInfo(info);
	    CSLoginReq req = req_builder.build();
	    ctx.writeAndFlush(req);
	}

}
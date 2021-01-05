package com.tw.login.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tw.login.proto.CsLogin;
import com.tw.login.tools.PackDecoder;
import com.tw.login.tools.PackEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class LoginSocketClient {

private static final Log logger = LogFactory.getLog(LoginSocketClient.class);
	private static final String IP = "120.77.250.134";
	private static final int PORT = 7861;
	
	private static final EventLoopGroup group = new NioEventLoopGroup();
	
	@SuppressWarnings("rawtypes")
	protected static void  run() throws Exception {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.handler(new ChannelInitializer() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				// TODO Auto-generated method stub
				ChannelPipeline pipeline = ch.pipeline();
				// 协议数据的编解码器
				pipeline.addLast("frameDecoder",new ProtobufVarint32FrameDecoder());
				pipeline.addLast("protobufDecoder",new PackDecoder());
				pipeline.addLast("frameEncoder",new ProtobufVarint32LengthFieldPrepender());
				pipeline.addLast("protobufEncoder", new PackEncoder());
				pipeline.addLast("handler",new SocketClientHandler());
				
			}
			
		});
		
		// 连接服务端
        Channel ch = bootstrap.connect(IP,PORT).sync().channel();
        
        
        ch.writeAndFlush("客户端数据"+"\r\n");
		
		logger.info("向Socket服务器发送数据:"+"客户端数据"+"\r\n");
	}
	
	public static void  main(String[] args){
		logger.info("开始连接Socket服务器...");
		try {
			run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			group.shutdownGracefully();
		}
	}
}

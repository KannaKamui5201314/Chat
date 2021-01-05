package com.tw.login.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tw.login.proto.CsLogin;
import com.tw.login.tools.PackDecoder;
import com.tw.login.tools.PackEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class LoginSocketServer {
	private static final Log logger = LogFactory.getLog(LoginSocketServer.class);
	private static final String IP = "172.18.255.236";
	private static final int PORT = 7861;
	
	//�������ڴ���ҵ����߳�������
	protected static final int BisGroupSize = Runtime.getRuntime().availableProcessors()*2;
	//ÿ���߳������̵߳�����
	protected static final int worGroupSize = 4;
	
	private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BisGroupSize);
	private static final EventLoopGroup workerGroup = new NioEventLoopGroup(worGroupSize);
	
	protected static void  run() throws Exception {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				// Э�����ݵı������
				pipeline.addLast("frameDecoder",new ProtobufVarint32FrameDecoder());
				pipeline.addLast("protobufDecoder",new PackDecoder());
				pipeline.addLast("frameEncoder",new ProtobufVarint32LengthFieldPrepender());
				pipeline.addLast("protobufEncoder", new PackEncoder());
				pipeline.addLast("handler",new SocketServerHandler());
				
			}
		});
		bootstrap.bind(IP,PORT).sync();
		logger.info("Socket���������������");
	}
	
	protected static void shutdown() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}
	
	public static void  main(String[] args) throws Exception {
		logger.info("��ʼ����Socket������...");
		run();
	}
	
}

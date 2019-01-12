package com.hqs.zk.register.chat.server;

import com.hqs.zk.register.chat.protocol.RequestProto;
import com.hqs.zk.register.config.AppConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ProtoBufServer {

    @Autowired
    private AppConfig appConfig;

    public void bind(int port) throws Exception {
        //配置服务器端NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b  = new ServerBootstrap();
            b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                    socketChannel.pipeline().addLast(new ProtobufDecoder(RequestProto.ReqProtocol.getDefaultInstance())).
                            addLast(new ProtobufVarint32LengthFieldPrepender()).addLast(new ProtobufEncoder());
                    socketChannel.pipeline().addLast(new ProBufServerHandler());
                }
            });
            //绑定端口，同步等待
            ChannelFuture f = b.bind(port).sync();
            if (f.isSuccess()) {
                System.out.println("启动 server 成功");
            }

        } catch (Exception  e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void start() throws Exception {
        bind(appConfig.getNettyPort());
    }
}

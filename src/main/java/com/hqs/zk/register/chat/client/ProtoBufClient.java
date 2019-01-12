package com.hqs.zk.register.chat.client;

import com.hqs.zk.register.chat.protocol.RequestProto;
import com.hqs.zk.register.config.AppConfig;
import com.hqs.zk.register.constants.Constants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
@DependsOn("protoBufServer")
public class ProtoBufClient {

    @Autowired
    private AppConfig appConfig;

    private SocketChannel socketChannel;
    //客户端线程组
    private EventLoopGroup group = new NioEventLoopGroup();

    public void connection(String host, int port) throws Exception {


        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new ProtobufVarint32FrameDecoder())
                                    .addLast(new ProtobufDecoder(RequestProto.ReqProtocol.getDefaultInstance()))
                                    .addLast(new ProtobufVarint32LengthFieldPrepender())
                                    .addLast(new ProtobufEncoder())
                                    .addLast(new ProtoBufClientHandler());
                        }
                    });

            //发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();
            socketChannel = (SocketChannel) f.channel();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PostConstruct
    public void start() throws Exception{
        connection(appConfig.getNettyServer(), appConfig.getNettyPort());
        for(int i = 1; i <= 1; i++) {
            int j = i;
            Runnable runnable = () -> {
                try {
                    sendMesgToServer(j);
                    sendMesgToGroup(j);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            };

            new Thread(runnable).start();

        }
    }

    private void sendMesgToServer(int i) throws Exception{
        TimeUnit.SECONDS.sleep(3);
        if(socketChannel != null) {
            RequestProto.ReqProtocol req = RequestProto
                    .ReqProtocol
                    .newBuilder()
                    .setRequestId(1L)
                    .setType(Constants.CommandType.SERVER)
                    .setReqMsg("hello Server:" + i)
                    .build();
            ChannelFuture future = socketChannel.writeAndFlush(req);
            future.addListener((ChannelFutureListener) ChannelFuture -> System.out.println("1L : send message to server successful!"));
        }

    }

    private void sendMesgToGroup(int i) throws Exception{
        TimeUnit.SECONDS.sleep(5);
        if(socketChannel != null) {
            RequestProto.ReqProtocol req = RequestProto
                    .ReqProtocol
                    .newBuilder()
                    .setRequestId(1L)
                    .setType(Constants.CommandType.GROUP)
                    .setReqMsg("hello peoole in group")
                    .build();
            ChannelFuture future = socketChannel.writeAndFlush(req);
            future.addListener((ChannelFutureListener) ChannelFuture -> System.out.println(""));
            while(!future.isDone()) {}
        }

    }

    private void close() {
        //优雅的退出线程，释放资源
        group.shutdownGracefully();
    }



}

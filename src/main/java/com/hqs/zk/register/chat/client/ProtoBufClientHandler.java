package com.hqs.zk.register.chat.client;

import com.hqs.zk.register.chat.protocol.RequestProto;
import com.hqs.zk.register.constants.Constants;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ChannelHandler.Sharable
public class ProtoBufClientHandler extends SimpleChannelInboundHandler<RequestProto.ReqProtocol>{

    public static final Map<Long, SocketChannel> CHANNEL_MAP = new ConcurrentHashMap<>(16);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("=============");
//        RequestProto.ReqProtocol req = RequestProto.ReqProtocol.newBuilder().setRequestId(12341234).setType(123).setReqMsg("hello server").build();
//        ctx.writeAndFlush(req);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RequestProto.ReqProtocol reqProtocol) throws Exception {
        RequestProto.ReqProtocol req = reqProtocol;
        if(req.getType() == Constants.CommandType.SERVER) {
            System.out.println("get Msg from Server: " + req.getRequestId() + ":" + req.getReqMsg());
        } else if(req.getType() == Constants.CommandType.GROUP) {
            System.out.println("get Msg from Group: " + req.getRequestId() + ":" + req.getReqMsg());
        }

    }

    public void handleReq(long requestId) {
        SocketChannel nioSocketChannel = CHANNEL_MAP.get(requestId);
        if(nioSocketChannel != null) {
            RequestProto.ReqProtocol req = RequestProto.ReqProtocol.newBuilder().setRequestId(12341234).setType(123).setReqMsg("hello Server").build();
            ChannelFuture future = nioSocketChannel.writeAndFlush(req);
            future.addListener((ChannelFutureListener) ChannelFuture -> System.out.println("send message back:" + req.toString()));
        }

    }
}

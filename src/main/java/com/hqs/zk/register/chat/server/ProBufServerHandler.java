package com.hqs.zk.register.chat.server;

import com.hqs.zk.register.chat.protocol.RequestProto;
import com.hqs.zk.register.constants.Constants;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProBufServerHandler extends SimpleChannelInboundHandler<RequestProto.ReqProtocol> {

    public static final Map<Long, NioSocketChannel> CHANNEL_MAP = new ConcurrentHashMap<>(2);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NioSocketChannel nioSocketChannel = (NioSocketChannel) ctx.channel();
        for (Map.Entry<Long, NioSocketChannel> entry : CHANNEL_MAP.entrySet()) {
            NioSocketChannel value = entry.getValue();
            if (nioSocketChannel == value){
                Long key = entry.getKey();
                System.out.println("client is offline : " + key);
                CHANNEL_MAP.remove(key);
            }
        }

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RequestProto.ReqProtocol reqProtocol) throws Exception {
        RequestProto.ReqProtocol req = reqProtocol;
        CHANNEL_MAP.putIfAbsent(req.getRequestId(), (NioSocketChannel)channelHandlerContext.channel());
//        System.out.println("get Msg from Client:" + req.getReqMsg());
        handleReq(req);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }

    public void handleReq(RequestProto.ReqProtocol req) {
        Long originRequestId = req.getRequestId();

        if(req.getType() == Constants.CommandType.SERVER) {

            NioSocketChannel nioSocketChannel = CHANNEL_MAP.get(req.getRequestId());
            sendMsg(nioSocketChannel, originRequestId, originRequestId, Constants.CommandType.SERVER, "hello client");

        } else if(req.getType() == Constants.CommandType.GROUP) {
            for(Map.Entry<Long, NioSocketChannel> entry : CHANNEL_MAP.entrySet()) {
                //过滤自己收消息

                if(entry.getKey() == originRequestId) {
                    continue;
                }
                sendMsg(entry.getValue(), originRequestId, entry.getKey(), Constants.CommandType.GROUP, req.getReqMsg());
            }
        }


    }

    private void sendMsg(NioSocketChannel nioSocketChannel,Long originReqId, Long reqId, int type, String msg) {
        if(nioSocketChannel != null) {

            RequestProto.ReqProtocol res = RequestProto.ReqProtocol.newBuilder()
                    .setRequestId(originReqId)
                    .setType(type)
                    .setReqMsg(msg)
                    .build();

            ChannelFuture future = nioSocketChannel.writeAndFlush(res);
            future.addListener((ChannelFutureListener) ChannelFuture ->
                    System.out.println("received id:" + originReqId + "- send to id:" + reqId));
        }
    }

}

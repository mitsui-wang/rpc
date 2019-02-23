package com.mitsui.rpc.transport;


import com.mitsui.rpc.exception.RpcException;
import com.mitsui.rpc.exchange.ReferExchangeHandle;
import com.mitsui.rpc.exchange.ServiceExchangeHandle;
import com.mitsui.rpc.msg.RpcRequest;
import com.mitsui.rpc.msg.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by admin on 2018/11/14.
 */
@ChannelHandler.Sharable
public class ReferTransportHandler extends
        SimpleChannelInboundHandler<RpcResponse> {

    private ReferExchangeHandle handle;
    private static volatile ReferTransportHandler referTransportHandler;
    private ChannelHandlerContext channelHandlerContext;

    private ReferTransportHandler(ReferExchangeHandle handle) {
        this.handle = handle;
    }

    public static ReferTransportHandler getInstance(ReferExchangeHandle handle) {
        if (referTransportHandler == null) {
            synchronized (ServiceExchangeHandle.class) {
                if (referTransportHandler == null) {
                    referTransportHandler = new ReferTransportHandler(handle);
                }
            }
        }

        return referTransportHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.channelHandlerContext = ctx;
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("service leave: "+ctx.channel().remoteAddress().toString());
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        if (ctx.channel().isActive()) {
            handle.messageReceive(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        System.out.println(cause.getStackTrace());
        ctx.close();
    }

    public void invoke(RpcRequest rpcRequest) throws Exception {
        if (channelHandlerContext.channel() != null && channelHandlerContext.channel().isActive()) {
            ChannelFuture channelFuture = channelHandlerContext.writeAndFlush(rpcRequest).sync();
            if (!channelFuture.isSuccess()) {
                throw new RpcException(channelFuture.cause().getMessage());
            }
        } else {
            Transports.closeRefer();
            Transports.refer(referTransportHandler);
            if (channelHandlerContext.channel() != null && channelHandlerContext.channel().isActive()) {
                ChannelFuture channelFuture = channelHandlerContext.writeAndFlush(rpcRequest).sync();
                if (!channelFuture.isSuccess()) {
                    throw new RpcException(channelFuture.cause().getMessage());
                }
            }
        }
    }


}

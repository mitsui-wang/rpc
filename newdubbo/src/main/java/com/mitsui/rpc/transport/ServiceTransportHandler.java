package com.mitsui.rpc.transport;

import com.mitsui.rpc.exchange.ServiceExchangeHandle;
import com.mitsui.rpc.msg.RpcRequest;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by admin on 2018/11/14.
 */
@ChannelHandler.Sharable
public class ServiceTransportHandler extends
        SimpleChannelInboundHandler<RpcRequest> {

    private ServiceExchangeHandle handle;
    private static volatile ServiceTransportHandler serviceTransportHandler;

    private ServiceTransportHandler(ServiceExchangeHandle handle) {
        this.handle = handle;
    }

    public static ServiceTransportHandler getInstance(ServiceExchangeHandle handle) {
        if (serviceTransportHandler == null) {
            synchronized (ServiceExchangeHandle.class) {
                if (serviceTransportHandler == null) {
                    serviceTransportHandler = new ServiceTransportHandler(handle);
                }
            }
        }

        return serviceTransportHandler;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client leave: "+ctx.channel().remoteAddress().toString());
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        if (ctx.channel().isActive()) {
            handle.messageReceive(ctx, msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        ctx.close();
    }

}

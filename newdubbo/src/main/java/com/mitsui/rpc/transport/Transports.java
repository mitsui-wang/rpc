package com.mitsui.rpc.transport;

import com.mitsui.rpc.exception.RpcException;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.Data;

import java.net.InetAddress;
import java.net.InetSocketAddress;


/**
 * Created by mitsui on 2018/11/25.
 */
@Data
public class Transports {
    private static volatile boolean isPublish;
    private static volatile boolean isConnect;
    private static int port = 8002;

    private static EventLoopGroup group = new NioEventLoopGroup();
    private static NioEventLoopGroup boss = new NioEventLoopGroup();
    private static NioEventLoopGroup worker = new NioEventLoopGroup();

    public static class TransportsChannelInitializer extends ChannelInitializer<SocketChannel> {

        private ServiceTransportHandler serviceTransportHandler;
        private ReferTransportHandler referTransportHandler;

        public TransportsChannelInitializer(ServiceTransportHandler serviceTransportHandler,
                                            ReferTransportHandler referTransportHandler) {
            this.serviceTransportHandler = serviceTransportHandler;
            this.referTransportHandler = referTransportHandler;
        }

        @Override
        public void initChannel(SocketChannel ch) {
            ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
            ch.pipeline().addLast(new ObjectEncoder());
            if (serviceTransportHandler != null) {
                ch.pipeline().addLast(serviceTransportHandler);
            }
            if (referTransportHandler != null) {
                ch.pipeline().addLast(referTransportHandler);
            }
        }
    }

    public static void export(ServiceTransportHandler serviceTransportHandler) throws Exception {
        if (!isPublish) {
            synchronized (Transports.class) {
                if (!isPublish) {

                    try {
                        TransportsChannelInitializer transportsChannelInitializer = new TransportsChannelInitializer(serviceTransportHandler, null);
                        ServerBootstrap b = new ServerBootstrap();
                        b.group(boss, worker)
                                .channel(NioServerSocketChannel.class)
                                .localAddress(new InetSocketAddress(InetAddress.getLocalHost(),port))
                                .childHandler(transportsChannelInitializer);

                        ChannelFuture f = b.bind().sync();
                        if (!f.isSuccess()) {
                            throw new RpcException(f.cause().getMessage());
                        }
                        isPublish = true;
                    } finally {
                    }

                }
            }

        }
    }

    public static void refer(ReferTransportHandler referTransportHandler) throws Exception {
        if (!isConnect) {
            synchronized (Transports.class) {
                if (!isConnect) {
                    TransportsChannelInitializer transportsChannelInitializer = new TransportsChannelInitializer(null, referTransportHandler);
                    try {
                        Bootstrap b = new Bootstrap();
                        b.group(group)
                                .channel(NioSocketChannel.class)
                                .remoteAddress(new InetSocketAddress(InetAddress.getLocalHost(), port))
                                .handler(transportsChannelInitializer);

                        ChannelFuture f = b.connect().sync();
                        if (!f.isSuccess()) {
                            throw new RpcException(f.cause().getMessage());
                        } else {
                            System.out.println("connect success");
                        }
                        isConnect = true;
                    } finally {
                    }
                }
            }
        }
    }

    public static void closeRefer() {
        isConnect = false;
    }



}

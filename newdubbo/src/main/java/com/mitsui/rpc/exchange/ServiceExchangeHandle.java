package com.mitsui.rpc.exchange;

import com.mitsui.rpc.RpcServiceBean;
import com.mitsui.rpc.msg.RpcRequest;
import com.mitsui.rpc.msg.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mitsui on 2018/11/25.
 */
@Data
public class ServiceExchangeHandle {

    private static volatile ServiceExchangeHandle serviceExchangeHandle;
    private Map<String, RpcServiceBean> servers = new HashMap<String, RpcServiceBean>();
    private ServiceExchangeHandle() {

    }

    public static ServiceExchangeHandle getInstance() {
        if (serviceExchangeHandle == null) {
            synchronized (ServiceExchangeHandle.class) {
                if (serviceExchangeHandle == null) {
                    serviceExchangeHandle = new ServiceExchangeHandle();
                }
            }
        }

        return serviceExchangeHandle;
    }

    public void addService(RpcServiceBean rpcServiceBean) {
        this.servers.put(rpcServiceBean.getInterfaceName(), rpcServiceBean);
    }

    public void messageReceive(ChannelHandlerContext ctx, RpcRequest rpcRequest) {
        RpcServiceBean rpcServiceBean = servers.get(rpcRequest.getInterfaceName());
        if (rpcServiceBean != null) {
            try {
                Object result = rpcServiceBean.invoke(rpcRequest);
                RpcResponse rpcResponse = new RpcResponse();
                rpcResponse.setSuccess(1);
                rpcResponse.setId(rpcRequest.getId());
                rpcResponse.setResult(result);
                ctx.writeAndFlush(rpcResponse);
            } catch (Exception e) {
                e.printStackTrace();
                RpcResponse rpcResponse = new RpcResponse();
                rpcResponse.setSuccess(0);
                rpcResponse.setId(rpcRequest.getId());
                rpcResponse.setResult(e);
                ctx.writeAndFlush(rpcResponse);
            }
        }
    }
}

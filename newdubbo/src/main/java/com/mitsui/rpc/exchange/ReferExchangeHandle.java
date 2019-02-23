package com.mitsui.rpc.exchange;

import com.mitsui.rpc.msg.RpcRequest;
import com.mitsui.rpc.msg.RpcResponse;
import com.mitsui.rpc.msg.ThreadResponse;
import com.mitsui.rpc.transport.ReferTransportHandler;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created by mitsui on 2018/11/25.
 */
@Data
public class ReferExchangeHandle {

    private static volatile ReferExchangeHandle referExchangeHandle;
    private Map<Long, ThreadResponse> waitMap = new ConcurrentHashMap<Long, ThreadResponse>();

    private ReferExchangeHandle() {

    }

    public void removeMap(Long id) {
        waitMap.remove(id);
    }

    public static ReferExchangeHandle getInstance() {
        if (referExchangeHandle == null) {
            synchronized (ReferExchangeHandle.class) {
                if (referExchangeHandle == null) {
                    referExchangeHandle = new ReferExchangeHandle();
                }
            }
        }

        return referExchangeHandle;
    }

    public void messageReceive(RpcResponse rpcResponse) {
        ThreadResponse threadResponse = waitMap.get(rpcResponse.getId());
        if (threadResponse != null) {
            threadResponse.setRpcResponse(rpcResponse);
            threadResponse.getCountDownLatch().countDown();
        }
    }

    public void invoke(RpcRequest rpcRequest, CountDownLatch countDownLatch) throws Exception{
        ThreadResponse threadResponse = new ThreadResponse();
        threadResponse.setCountDownLatch(countDownLatch);
        waitMap.put(rpcRequest.getId(), threadResponse);
        ReferTransportHandler referTransportHandler = ReferTransportHandler.getInstance(this);
        referTransportHandler.invoke(rpcRequest);

    }
}
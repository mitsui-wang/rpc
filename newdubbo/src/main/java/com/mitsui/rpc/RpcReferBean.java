package com.mitsui.rpc;

import com.mitsui.rpc.exception.RpcException;
import com.mitsui.rpc.exchange.Exchanges;
import com.mitsui.rpc.exchange.ReferExchangeHandle;
import com.mitsui.rpc.msg.RpcRequest;
import com.mitsui.rpc.msg.RpcResponse;
import com.mitsui.rpc.msg.ThreadResponse;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by mitsui on 2018/11/25.
 */
@Data
public class RpcReferBean {
    RpcConfig rpcConfig;
    String interfaceName;

    public void refer() throws Exception{
        ReferExchangeHandle referExchangeHandle = ReferExchangeHandle.getInstance();
        Exchanges.refer(referExchangeHandle);
    }

    public Object invoke(Method method, Object[] args) throws Exception {

        int count = 0;
        while (true) {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setMethod(method.getName());
            if (args != null) {
                rpcRequest.setArgs(Arrays.asList(args));
            }
            rpcRequest.setId(RpcUtils.getRequestId());
            rpcRequest.setInterfaceName(this.getInterfaceName());
            ReferExchangeHandle referExchangeHandle = ReferExchangeHandle.getInstance();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            try {
                referExchangeHandle.invoke(rpcRequest, countDownLatch);
                countDownLatch.await(1, TimeUnit.SECONDS);
                ThreadResponse threadResponse = referExchangeHandle.getWaitMap().get(rpcRequest.getId());
                RpcResponse rpcResponse = threadResponse.getRpcResponse();
                if (rpcResponse != null) {
                    if (rpcResponse.getSuccess() == 1) {
                        return rpcResponse.getResult();
                    } else {
                        throw (Exception) rpcResponse.getResult();
                    }
                } else {
                    count++;
                    if (count > 2) {
                        throw new RpcException("try three times fail");
                    }
                    referExchangeHandle.removeMap(rpcRequest.getId());
                }

            } catch (Exception e) {
                count++;
                if (count > 2) {
                    throw e;
                }
                referExchangeHandle.removeMap(rpcRequest.getId());
            }


        }


    }


}

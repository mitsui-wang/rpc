package com.mitsui.rpc;

import com.mitsui.rpc.exchange.Exchanges;
import com.mitsui.rpc.exchange.ServiceExchangeHandle;
import com.mitsui.rpc.msg.RpcRequest;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by mitsui on 2018/11/25.
 */
@Data
public class RpcServiceBean {
    RpcConfig rpcConfig;
    String interfaceName;
    Object instance;
    List<Method> methods;

    public void export() throws Exception{
        ServiceExchangeHandle serviceExchangeHandle = ServiceExchangeHandle.getInstance();
        serviceExchangeHandle.addService(this);
        Exchanges.export(serviceExchangeHandle);
    }


    public Object invoke(RpcRequest rpcRequest) throws Exception {
        for (Method method : methods) {
            if (method.getName().equals(rpcRequest.getMethod())) {
                if (rpcRequest.getArgs() != null) {
                    return method.invoke(instance, rpcRequest.getArgs().toArray());
                } else {
                    return method.invoke(instance);
                }

            }
        }
        return null;
    }

}

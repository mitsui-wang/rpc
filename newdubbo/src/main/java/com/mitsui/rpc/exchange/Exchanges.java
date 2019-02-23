package com.mitsui.rpc.exchange;

import com.mitsui.rpc.transport.ReferTransportHandler;
import com.mitsui.rpc.transport.ServiceTransportHandler;
import com.mitsui.rpc.transport.Transports;
import lombok.Data;

/**
 * Created by mitsui on 2018/11/25.
 */
@Data
public class Exchanges {
    public static void export(ServiceExchangeHandle serviceExchangeHandle) throws Exception {
        ServiceTransportHandler serviceTransportHandler = ServiceTransportHandler.getInstance(serviceExchangeHandle);
        Transports.export(serviceTransportHandler);
    }

    public static void refer(ReferExchangeHandle referExchangeHandle) throws Exception {
        ReferTransportHandler referTransportHandler = ReferTransportHandler.getInstance(referExchangeHandle);
        Transports.refer(referTransportHandler);

    }
}

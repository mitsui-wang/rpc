package com.mitsui.rpc.msg;

import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

/**
 * Created by mitsui on 2018/11/25.
 */
@Data
public class ThreadResponse implements Serializable {
    private static final long serialVersionUID = 7590999461767050473L;
    CountDownLatch countDownLatch;
    RpcResponse rpcResponse;
}

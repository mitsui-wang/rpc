package com.mitsui.rpc;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by mitsui on 2018/11/25.
 */
public class RpcUtils {

    static AtomicLong atomicLong = new AtomicLong();
    public static Long getRequestId() {
        return atomicLong.addAndGet(1);
    }
}

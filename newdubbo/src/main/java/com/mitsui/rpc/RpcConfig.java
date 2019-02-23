package com.mitsui.rpc;

import lombok.Data;

/**
 * Created by mitsui on 2018/11/25.
 */
@Data
public class RpcConfig {
    private int port;
    private volatile boolean isPublish;

}

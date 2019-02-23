package com.mitsui.rpc.msg;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by mitsui on 2018/11/25.
 */
@Data
public class RpcResponse implements Serializable {

    private static final long serialVersionUID = 7590999461767050473L;
    int success;
    Long id;
    Object result;

}

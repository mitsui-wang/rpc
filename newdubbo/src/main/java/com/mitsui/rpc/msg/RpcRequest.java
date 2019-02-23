package com.mitsui.rpc.msg;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mitsui on 2018/11/25.
 */
@Data
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 7590999461767050473L;
    int type;
    Long id;
    String interfaceName;
    String method;
    List<Object> args;
}

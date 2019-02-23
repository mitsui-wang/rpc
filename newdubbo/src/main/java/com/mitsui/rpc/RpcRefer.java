package com.mitsui.rpc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * Created by mitsui on 2018/11/25.
 */
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcRefer {
    int retryTime = 1;
}

package com.mitsui.rpc;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by mitsui on 2018/11/25.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RpcServer {
}

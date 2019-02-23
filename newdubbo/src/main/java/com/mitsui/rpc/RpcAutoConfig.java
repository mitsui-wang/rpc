package com.mitsui.rpc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mitsui on 2018/11/25.
 */

@Configuration
public class RpcAutoConfig {

    @Bean
    @ConditionalOnMissingBean(RpcBeanPostProcess.class)
    public RpcBeanPostProcess rpcBeanPostProcess() {
        return new RpcBeanPostProcess();
    }

    @Bean
    @ConditionalOnMissingBean(RpcReferProxyProcess.class)
    public RpcReferProxyProcess rpcReferProxyProcess() {
        return new RpcReferProxyProcess();
    }

}

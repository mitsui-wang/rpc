package com.mitsui.rpc;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Arrays;

/**
 * Created by mitsui on 2018/11/25.
 */
@Data
public class RpcBeanPostProcess implements BeanPostProcessor {
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        boolean isExist = bean.getClass().isAnnotationPresent(RpcServer.class);
        if (isExist) {
            try {
                processRpcBean(bean);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BeansException(e.getMessage()) {

                };
            }
        }
        return bean;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private void processRpcBean(Object bean) throws Exception{
        RpcServiceBean rpcServiceBean = new RpcServiceBean();
        rpcServiceBean.setInstance(bean);
        Class<?>[] interfaces = bean.getClass().getInterfaces();
        rpcServiceBean.setInterfaceName(interfaces[0].getName());
        rpcServiceBean.setMethods(Arrays.asList(bean.getClass().getMethods()));
        rpcServiceBean.export();
    }


}

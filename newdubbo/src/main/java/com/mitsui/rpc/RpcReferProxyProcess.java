package com.mitsui.rpc;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by mitsui on 2018/11/25.
 */
@Data
public class RpcReferProxyProcess implements BeanPostProcessor {

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            boolean isExist = field.isAnnotationPresent(RpcRefer.class);
            if (isExist) {
                try {
                    field.setAccessible(true);
                    Object proxy = createProxy(field);
                    field.set(bean, proxy);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new BeansException(e.getMessage()) {};
                }
            }
        }
        return bean;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private Object createProxy(Field field) throws Exception {
        final RpcReferBean rpcReferBean = new RpcReferBean();
        try {
            rpcReferBean.refer();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("fail connect server on init");
        }
        String name = field.getType().getName();
        rpcReferBean.setInterfaceName(name);
        Class[] temp = {field.getType()};
        Object proxyInstance = Proxy.newProxyInstance(field.getType().getClassLoader(),
                temp, new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Object result = rpcReferBean.invoke(method, args);
                        return result;
                    }
                });
        return proxyInstance;
    }


}

package com.mitsui.rpc.impl;

import com.mitsui.rpc.RpcServer;
import com.mitsui.rpc.api.NameService;
import com.mitsui.rpc.bo.Person;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2017/9/23.
 */



@RpcServer
@Service
public class NameServiceImpl implements NameService {
    @Override
    public String getName() {
        return "NameServiceImpl";
    }

    @Override
    public Integer getAge(Integer i) {
        return i;
    }

    @Override
    public Person getPerson() {
        Person person = new Person();
        person.setAge(1);
        person.setName("mitsui");
        return person;
    }
}

package com.mitsui.rpc.api;

import com.mitsui.rpc.bo.Person;

/**
 * Created by admin on 2017/9/23.
 */
public interface NameService {
    String getName();

    Integer getAge(Integer i);

    Person getPerson();


}

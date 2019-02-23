package com.mitsui.rpc.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by mitsui on 2019/2/23.
 */
@Data
public class Person implements Serializable {
    private static final long serialVersionUID = -1857679526676434388L;
    private String name;
    private int age;
}

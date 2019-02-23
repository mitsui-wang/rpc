package com.mitsui.rpc.controller;

import com.mitsui.rpc.RpcRefer;
import com.mitsui.rpc.api.NameService;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by admin on 2017/9/23.
 */
@Controller
@Data
public class NameController{
    @RpcRefer
    NameService nameService;

    @RequestMapping("/get1")
    @ResponseBody
    public String get1(){
        return nameService.getName();
    }

    @RequestMapping("/get2")
    @ResponseBody
    public String str(){
        return nameService.getPerson().toString();
    }
}

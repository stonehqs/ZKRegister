package com.hqs.zk.register.controller;

import com.hqs.zk.register.util.ZKUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangqingshi on 2019/1/8.
 */
@RestController
public class ZKController {

    @Autowired
    ZKUtil zkUtil;

    @RequestMapping(value="/hello", method = RequestMethod.GET)
    public String hello(){
        return "hello this is my test";
    }

    /**
     * 获取所有路由节点
     * @return
     */
    @ApiOperation("获取所有路由节点")
    @RequestMapping(value = "getAllRoute",method = RequestMethod.POST)
    @ResponseBody()
    public List<String> getAllRoute(){
        List<String> allNode = zkUtil.getAllNode();
        List<String> result = new ArrayList<>();
        for (String node : allNode) {
            String key = node.split("-")[1];
            result.add(key);
        }
        return result ;
    }

}

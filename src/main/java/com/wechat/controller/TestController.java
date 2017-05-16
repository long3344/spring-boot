package com.wechat.controller;

import com.wechat.model.Admin;
import com.wechat.service.redis.impl.RedisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/5/16
 */

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedisServiceImpl redisService;

    @RequestMapping("/testAdd")
    @ResponseBody
    public void testAdd(){
        System.out.println("开始添加redis数据");

        for (int i = 0; i <4 ; i++) {

            Admin admin=new Admin();
            admin.setUsername("小明"+i);
            admin.setPassword("20170516_"+i);
            admin.setPhone("1234567890"+i);
            admin.setQq("1070445500"+i);
            redisService.put("AAA"+i,admin,-1);
        }
    }

    //查询所有对象
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    public Object getAll() {
        return redisService.getAll();
    }


    //查询所有key
    @RequestMapping(value = "/getKeys", method = RequestMethod.GET)
    @ResponseBody
    public Object getKeys() {
        return redisService.getKeys();
    }

    //根据key查询
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public Object get() {
        return redisService.get("AAA2");
    }
}

package com.wechat.controller;

import com.wechat.dto.Order;
import com.wechat.dto.ReturnDto;
import com.wechat.service.order.impl.OrderServiceImlp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

/**
 * 描述：测试rocket发送消息
 * 作者: TWL
 * 创建日期: 2017/12/27
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderServiceImlp orderService;

    @RequestMapping("/createOrder")
    @ResponseBody
    public ReturnDto createOrder(){

        Order order=new Order();
        order.setOrderNo("1001521513066323439");
        order.setPrice(200);
        order.setOrderStatus(210);
        order.setCreateTime(new Date());
        ReturnDto result=orderService.createOrder(order);
        return result;
    }


}

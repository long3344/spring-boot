package com.wechat.service.order;

import com.wechat.dto.Order;
import com.wechat.dto.ReturnDto;

/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/12/27
 */
public interface OrderService {

    public ReturnDto createOrder(Order order);
}

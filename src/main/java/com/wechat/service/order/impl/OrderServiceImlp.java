package com.wechat.service.order.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.wechat.dao.WebpageMapper;
import com.wechat.dto.Order;
import com.wechat.dto.ReturnDto;
import com.wechat.job.SendOrder;
import com.wechat.model.Webpage;
import com.wechat.mq.producer.DefaultProducer;
import com.wechat.service.order.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/12/27
 */
@Service
public class OrderServiceImlp implements OrderService{

    private static final Logger logger= LoggerFactory.getLogger(OrderServiceImlp.class);

    @Autowired
    private DefaultProducer defaultProducer;

    @Autowired
    private WebpageMapper webpageMapper;

    /**
     * 创建订单
     * @param order
     * @return
     */
    public ReturnDto createOrder(Order order) {
        logger.info("开始创建订单！");
        logger.info("开始发送订单信息到队列！");

        List<Map> list =webpageMapper.selectWebPageList();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderNo",order.getOrderNo());
        jsonObject.put("price",order.getPrice());
        jsonObject.put("createTime", order.getCreateTime());
        jsonObject.put("orderStatus", order.getOrderStatus());

            Message message=new Message();
            message.setTopic(SendOrder.Order_Topic);
            message.setTags(SendOrder.Order_Tag);
            message.setBody(jsonObject.toString().getBytes());
            try {
                SendResult result=defaultProducer.send(message);

                if (result.getSendStatus() == SendStatus.SEND_OK) {
                    logger.info("订单成功加入队列 brokerName:{} topic:{} queueId:{} offset:{}",
                            result.getMessageQueue().getBrokerName(),
                            result.getMessageQueue().getTopic(),
                            result.getMessageQueue().getQueueId(),
                            result.getQueueOffset()
                    );
//                    return ReturnDto.buildSuccessReturnDto("创建订单成功！");
                }
            } catch (Exception e) {
                logger.error("添加消息队列出现异常，异常信息：{}",e);
            }
        return ReturnDto.buildFailedReturnDto("订单创建失败！");
    }

}

package com.wechat.job;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.wechat.annotation.MsgConsumer;
import com.wechat.mq.MsgConsumeException;
import com.wechat.mq.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@MsgConsumer(
        topicName = SendOrder.Order_Topic,
        tagName = SendOrder.Order_Tag,
        consumeType = MsgConsumer.ConsumeType.Orderly,
        consumerGroup = "OrderConsumerGroup"
)
public class SendOrder implements Consumer {
    private static Logger logger = LoggerFactory.getLogger(SendOrder.class);
    public static final String Order_Tag = "SendOrderTagName";
    public static final String Order_Topic = "SendOrderTopName";

    @Override
    public void consume(List<MessageExt> msgs) throws MsgConsumeException {

        for(MessageExt ext :msgs){
            logger.info("接收队列消息：{}",ext.getMsgId()+","+new String(ext.getBody()));
            logger.info("订单创建成功，已发送消息给用户！");
            //下面写发送短消息的逻辑
        }
    }
}

package com.wechat.mq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.wechat.SpringBoot;
import com.wechat.mq.producer.DefaultProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 文件名:com.jk51.mq.ProducerMsgTest
 * 描述: 消息发送测试
 * 作者:
 * 创建日期: 2017-02-10
 * 修改记录:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBoot.class)
@ActiveProfiles("dev")
public class ProducerMsgTest {

    private static Logger logger = LoggerFactory.getLogger(ProducerMsgTest.class);

    @Autowired
    private DefaultProducer producer;
  /*  @Autowired
    private DemoConsumer demoConsumer;*/

    @Test
    public void sendMsg() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Message message = new Message();
        message.setTopic("OrderTopic");
        message.setTags("AAA");
        message.setBody("*********这是测试数据**********".getBytes());
        SendResult result = producer.send(message);
        logger.info("消息发送结果:{}",result);
    }

    @Test
    public void getMsg(){
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("PushConsumer");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        try{
            consumer.subscribe("OrderTopic","AAA");
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.registerMessageListener(
                    new MessageListenerConcurrently() {
                        @Override
                        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                           Message msg = msgs.get(0);
                           logger.info("消息消费结果：{}",msg.toString());
                           return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                    }
            );
            consumer.start();
        }catch (Exception e){
            logger.error("mq消费消息异常",e);
        }
    }
}

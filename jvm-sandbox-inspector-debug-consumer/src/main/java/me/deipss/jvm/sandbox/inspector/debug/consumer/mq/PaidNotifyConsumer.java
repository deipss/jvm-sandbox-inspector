package me.deipss.jvm.sandbox.inspector.debug.consumer.mq;

import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.debug.api.dto.PaidNotify;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RocketMQMessageListener(topic = PaidNotify.TOPIC,
        consumerGroup = "jvm-sandbox-inspector-debug-consumer"
)
public class PaidNotifyConsumer implements RocketMQListener<PaidNotify> {
    @Override
    public void onMessage(PaidNotify message) {
        log.info("PaidNotifyConsumer 获取MQ消息={}", message);
    }
}
package me.deipss.jvm.sandbox.inspector.debug.provider.mq;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.debug.api.dto.PaidNotify;
import me.deipss.jvm.sandbox.inspector.debug.api.util.IdUtil;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class MqTemplateProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendWithTags(){
        PaidNotify paidNotify = new PaidNotify();
        paidNotify.setOrderID(IdUtil.id("ORDER") );
        paidNotify.setSubOrderID(Lists.newArrayList(IdUtil.id("SUB_ORDER") ,IdUtil.id("SUB_ORDER") ,IdUtil.id("SUB_ORDER") ));
        paidNotify.setPaymentID(IdUtil.id("PAID"));
        paidNotify.setPaidStatus("ERROR");
        rocketMQTemplate.convertAndSend(PaidNotify.TOPIC + ":" + PaidNotify.TAG,paidNotify);
    }

}
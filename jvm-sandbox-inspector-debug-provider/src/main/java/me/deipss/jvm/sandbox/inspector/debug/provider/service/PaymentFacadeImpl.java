package me.deipss.jvm.sandbox.inspector.debug.provider.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.debug.api.dto.PaymentDTO;
import me.deipss.jvm.sandbox.inspector.debug.api.facade.PaymentFacade;
import me.deipss.jvm.sandbox.inspector.debug.api.request.PaymentRequest;
import me.deipss.jvm.sandbox.inspector.debug.api.response.PaymentResponse;
import me.deipss.jvm.sandbox.inspector.debug.api.util.IdUtil;
import me.deipss.jvm.sandbox.inspector.debug.provider.dal.classicmodel.CustomersMapper;
import me.deipss.jvm.sandbox.inspector.debug.provider.mq.MqTemplateProducer;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;

@Slf4j
@DubboService
public class PaymentFacadeImpl implements PaymentFacade {

    @Autowired
    private MqTemplateProducer mqTemplateProducer;

    @Autowired
    private CustomersMapper customersMapper;
    @Override
    public PaymentResponse payment(PaymentRequest request) {
        log.info("createOrder request = {}", JSON.toJSONString(request));
        PaymentResponse paymentResponse = new PaymentResponse();
        String orderId = IdUtil.id("ORDER");
        for (PaymentDTO paymentDTO : request.getOrderList()) {
            paymentDTO.setOrderID(orderId);
            paymentDTO.setSubOrderId(IdUtil.id("SUB_PAY"));
        }
        paymentResponse.setOrderList(request.getOrderList());
        paymentResponse.setCode(200);
        paymentResponse.setMsg("success");
        paymentResponse.setOrderID(orderId);

        mqTemplateProducer.sendWithTags();

        Long l = customersMapper.selectCount(null);
        log.info("count customers ={}",l);

        return paymentResponse;

    }
}


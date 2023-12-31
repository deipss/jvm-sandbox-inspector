package me.deipss.jvm.sandbox.inspector.debug.consumer.controller;

import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.debug.api.facade.PaymentFacade;
import me.deipss.jvm.sandbox.inspector.debug.api.facade.TradeFacade;
import me.deipss.jvm.sandbox.inspector.debug.api.request.PaymentRequest;
import me.deipss.jvm.sandbox.inspector.debug.api.request.TradeCreateRequest;
import me.deipss.jvm.sandbox.inspector.debug.api.response.PaymentResponse;
import me.deipss.jvm.sandbox.inspector.debug.api.response.TradeCreateResponse;
import me.deipss.jvm.sandbox.inspector.debug.consumer.dal.classicmodel.CustomersMapper;
import me.deipss.jvm.sandbox.inspector.debug.consumer.dal.classicmodel.EmployeesMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/debug")
public class DebugController {


    @DubboReference
    private PaymentFacade paymentFacade;

    @DubboReference
    private TradeFacade tradeFacade;

    @Autowired
    private CustomersMapper customersMapper;

    @Autowired
    private EmployeesMapper employeesMapper;

    @PostMapping("/pay")
    @ResponseBody
    public PaymentResponse pay(@RequestBody PaymentRequest request) {
        Long l = customersMapper.selectCount(null);
        log.info("pay before count={}",l );
        return paymentFacade.payment(request);
    }


    @PostMapping("/orderCreate")
    @ResponseBody
    public TradeCreateResponse orderCreate(@RequestBody TradeCreateRequest request) {
        log.info("orderCreate before count={}",employeesMapper.selectCount(null) );
        return tradeFacade.createOrder(request);
    }


}

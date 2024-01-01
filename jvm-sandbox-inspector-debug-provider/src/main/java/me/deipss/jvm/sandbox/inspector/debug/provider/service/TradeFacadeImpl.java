package me.deipss.jvm.sandbox.inspector.debug.provider.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.debug.api.dto.TradeCreateDTO;
import me.deipss.jvm.sandbox.inspector.debug.api.facade.TradeFacade;
import me.deipss.jvm.sandbox.inspector.debug.api.request.TradeCreateRequest;
import me.deipss.jvm.sandbox.inspector.debug.api.response.TradeCreateResponse;
import me.deipss.jvm.sandbox.inspector.debug.api.util.IdUtil;
import me.deipss.jvm.sandbox.inspector.debug.provider.dal.classicmodel.EmployeesMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
@DubboService
public class TradeFacadeImpl implements TradeFacade {

    @Autowired
    private EmployeesMapper employeesMapper;

    @Override
    public TradeCreateResponse createOrder(TradeCreateRequest request) {
        log.info("createOrder request = {}", JSON.toJSONString(request));
        TradeCreateResponse tradeCreateResponse = new TradeCreateResponse();
        String orderId = IdUtil.id("ORDER");
        for (TradeCreateDTO tradeCreateDTO : request.getOrderList()) {
            tradeCreateDTO.setOrderID(orderId);
            tradeCreateDTO.setSubOrderId(IdUtil.id("SUB"));
        }
        tradeCreateResponse.setOrderList(request.getOrderList());
        tradeCreateResponse.setCode(200);
        tradeCreateResponse.setMsg("success");
        tradeCreateResponse.setOrderID(orderId);

        log.info("count employee ={}", employeesMapper.selectCount(null));
        employeesMapper.selectAllByEmail("abc.qq@.com");
        return tradeCreateResponse;
    }
}

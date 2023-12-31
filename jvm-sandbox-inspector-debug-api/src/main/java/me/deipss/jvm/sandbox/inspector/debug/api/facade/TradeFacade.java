package me.deipss.jvm.sandbox.inspector.debug.api.facade;

import me.deipss.jvm.sandbox.inspector.debug.api.request.TradeCreateRequest;
import me.deipss.jvm.sandbox.inspector.debug.api.response.TradeCreateResponse;

public interface TradeFacade {

    TradeCreateResponse createOrder(TradeCreateRequest request);
}

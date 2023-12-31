package me.deipss.jvm.sandbox.inspector.debug.api.facade;

import me.deipss.jvm.sandbox.inspector.debug.api.request.PaymentRequest;
import me.deipss.jvm.sandbox.inspector.debug.api.response.PaymentResponse;

public interface PaymentFacade {

    PaymentResponse payment(PaymentRequest request);

}

package me.deipss.jvm.sandbox.inspector.debug.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDTO implements Serializable {
    private static  final long serialVersionUID  =-1;

    private String orderID;
    private String subOrderId;
    private String paymentId;
    private String subPaymentId;
    private String userID;
    private String storeID;
    private String merchantID;
    private String phone;
    private String spu;
    private String sku;
    private Integer skuCount;
    private Integer amount;
    private Integer storeServiceFee;
    private Integer platformServiceFee;
    private Integer merchantFee;
    private Integer platformMarketingFee;
}

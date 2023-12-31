package me.deipss.jvm.sandbox.inspector.debug.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.deipss.jvm.sandbox.inspector.debug.api.dto.PaymentDTO;
import me.deipss.jvm.sandbox.inspector.debug.api.dto.TradeCreateDTO;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest implements Serializable {

    private static  final long serialVersionUID  =-1;


    private String paymentId;

    private List<PaymentDTO> orderList;

    private Integer amount;
    private Integer storeServiceFee;
    private Integer platformServiceFee;
    private Integer merchantFee;
    private Integer platformMarketingFee;
}

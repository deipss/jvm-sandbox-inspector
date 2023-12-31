package me.deipss.jvm.sandbox.inspector.debug.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeCreateDTO implements Serializable {
    private static  final long serialVersionUID  =-1;

    private String orderID;
    private String subOrderId;

    private String userID;
    private String storeID;
    private String phone;
    private String spu;
    private String sku;
    private Integer skuCount;
    private Integer xIndex;
    private Integer yIndex;

}

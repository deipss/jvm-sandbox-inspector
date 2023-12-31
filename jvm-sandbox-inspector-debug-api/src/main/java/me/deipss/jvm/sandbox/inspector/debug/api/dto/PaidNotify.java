package me.deipss.jvm.sandbox.inspector.debug.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class PaidNotify implements Serializable {
    private static  final long serialVersionUID  =-1;
    public static final String TOPIC="DEBUG_PAID_NOTIFY_TOPIC";
    public static final String TAG="PAID";
    private String orderID;
    private List<String> subOrderID;
    private String paymentID;
    private String paidStatus;
}

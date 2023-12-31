package me.deipss.jvm.sandbox.inspector.debug.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.deipss.jvm.sandbox.inspector.debug.api.dto.TradeCreateDTO;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeCreateResponse implements Serializable {
    private static  final long serialVersionUID  =-1;

    private List<TradeCreateDTO> orderList;
    private Integer code;
    private String msg;
    private String orderID;

}

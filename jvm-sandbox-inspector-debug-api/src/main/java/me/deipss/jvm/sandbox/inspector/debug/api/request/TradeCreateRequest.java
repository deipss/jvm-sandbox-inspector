package me.deipss.jvm.sandbox.inspector.debug.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.deipss.jvm.sandbox.inspector.debug.api.dto.TradeCreateDTO;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeCreateRequest implements Serializable {
    private static  final long serialVersionUID  =-1;

    private List<TradeCreateDTO> orderList;
}

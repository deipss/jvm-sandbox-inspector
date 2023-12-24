package me.deipss.jvm.sandbox.inspector.web.test.agent.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryDTO {
    private String name;
    private String orderId;
    private String userId;
    private Date startTime;

}

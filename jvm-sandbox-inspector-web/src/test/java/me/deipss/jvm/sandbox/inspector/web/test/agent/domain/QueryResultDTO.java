package me.deipss.jvm.sandbox.inspector.web.test.agent.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class QueryResultDTO {
    private String name;
    private String orderId;
    private String userId;
    private Date startTime;

}

package me.deipss.jvm.sandbox.inspector.agent.api.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HttpResult {

    private int httpStatus;

    private boolean success;

    private String data;

}

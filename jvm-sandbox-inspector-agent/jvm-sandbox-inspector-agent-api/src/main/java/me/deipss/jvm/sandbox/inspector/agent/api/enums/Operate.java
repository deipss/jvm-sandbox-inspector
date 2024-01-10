package me.deipss.jvm.sandbox.inspector.agent.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Operate{

    STOP("停用"),
    ADD_ALL("添加所有"),
    VIEW("展示所有"),
    ;
    private final String description;


}
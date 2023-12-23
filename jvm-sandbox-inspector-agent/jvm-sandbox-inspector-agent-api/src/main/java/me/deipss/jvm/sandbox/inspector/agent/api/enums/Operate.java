package me.deipss.jvm.sandbox.inspector.agent.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
enum Operate{

    STOP(""),
    ADD(""),
    VIEW(""),


    ;
    private final String description;


}
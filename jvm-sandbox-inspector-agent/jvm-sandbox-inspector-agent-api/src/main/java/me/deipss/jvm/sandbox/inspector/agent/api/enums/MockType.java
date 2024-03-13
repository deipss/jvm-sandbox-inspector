package me.deipss.jvm.sandbox.inspector.agent.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MockType {

    RETURN_BEFORE_EXE("方法执行前返回"),

    THROW_BEFORE_EXE("方法执行前发生异常"),

    ;
    private final String description;

}

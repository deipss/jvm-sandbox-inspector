package me.deipss.jvm.sandbox.inspector.web.service.impl;

import me.deipss.jvm.sandbox.inspector.web.domain.response.BaseResponse;

public interface ReplayService {

    BaseResponse<?> invokeHttp();


    BaseResponse<?> invokeDubbo();


    BaseResponse<?> invokeRocketMQ();


    BaseResponse<?> page();

}

package me.deipss.jvm.sandbox.inspector.web.service.impl;

import me.deipss.jvm.sandbox.inspector.web.domain.page.PageResponse;
import me.deipss.jvm.sandbox.inspector.web.domain.response.BaseResponse;

public interface MockService {

    BaseResponse<?> mock();

    PageResponse<?> page();

}

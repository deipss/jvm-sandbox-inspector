package me.deipss.jvm.sandbox.inspector.agent.core.plugins.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import com.alibaba.jvm.sandbox.api.event.ReturnEvent;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Span;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.core.plugins.BaseEventListener;
import me.deipss.jvm.sandbox.inspector.agent.core.plugins.http.copier.HttpServletResponseCopier;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.InvocationCache;
import org.apache.commons.lang3.reflect.MethodUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class HttpEventListener extends BaseEventListener {
    public HttpEventListener(boolean entrance, String protocol, InvocationSendService invocationSendService) {
        super(entrance, protocol, invocationSendService);
    }

    @Override
    public Invocation initInvocation(BeforeEvent event) {
        HttpInvocation invocation = new HttpInvocation();
        doInitInvocation(event, invocation);
        log.info("http initInvocation done");
        return invocation;
    }


    @Override
    public Span extractSpan(BeforeEvent event) {
        try {
            Object obj = event.argumentArray[0];
            Object spanObj = MethodUtils.invokeMethod(obj, "getHeader", Span.SPAN);
            if (spanObj == null) {
                return null;
            }
            String header = spanObj.toString();
            if (Strings.isNullOrEmpty(header)) {
                return null;
            }
            return JSON.parseObject(header, Span.class);
        } catch (Exception e) {
            log.info("http extract span ,error", e);
        }
        return null;
    }

    @Override
    public void assembleRequest(BeforeEvent event, Invocation invocation) {
        // void service(HttpServletRequest req, HttpServletResponse resp)
        HttpInvocation httpInvocation =  (HttpInvocation) invocation;
        try {
            if (!(event.argumentArray[0] instanceof HttpServletRequest)) {
                return;
            }
            HttpServletRequest req = (HttpServletRequest) event.argumentArray[0];
            HttpServletResponse response = (HttpServletResponse) event.argumentArray[1];
            httpInvocation.setHttpServletResponseCopier(new HttpServletResponseCopier(response));
            httpInvocation.setUri(req.getRequestURI());
            httpInvocation.setUrl(new String(req.getRequestURL()));
            httpInvocation.setMethodName(req.getMethod());
            httpInvocation.setContentType(req.getContentType());
            httpInvocation.setHttpPort(req.getLocalPort());
            setupHeaders(httpInvocation, req);
            setupParameters(httpInvocation, req);
            setupBody(httpInvocation, req);
        } catch (Exception e) {
            log.error("http assembleRequest error", e);
        }
    }

    @Override
    public void assembleResponse(ReturnEvent event, Invocation invocation) {
        HttpInvocation originInvocation = (HttpInvocation) InvocationCache.get(invocation.getInvokeId());
        if (Objects.isNull(originInvocation)) {
            log.error("assembleResponse error ,can not get Invocation");
        }
        try {
            HttpServletResponseCopier httpServletResponseCopier = originInvocation.getHttpServletResponseCopier();
            String responseData = new String(httpServletResponseCopier.getResponseData(), StandardCharsets.UTF_8);
            originInvocation.setResponseJson(responseData);
        } catch (IOException e) {
            log.error("assembleResponse error,",e);
        }
    }

    private void setupHeaders(Invocation invocation, HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headers = new HashMap<String, String>(2);
        while (headerNames != null && headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            headers.put(header, request.getHeader(header));
        }
        invocation.setHttpHeaders(headers);
    }


    private void setupParameters(Invocation invocation, HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String[]> parameterHolder = new HashMap<String, String[]>(2);
        if (parameterMap != null && !parameterMap.isEmpty()) {
            parameterHolder.putAll(parameterMap);
        }
        invocation.setHttpParameters(parameterHolder);
    }

    private void setupBody(Invocation invocation, HttpServletRequest request) {

        if (null!=request.getContentType() && !request.getContentType().contains("application/json")) {
            return;
        }
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (Objects.isNull(inputStream)) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            char[] charBuffer = new char[256];
            int bytesRead;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
            invocation.setHttpBody(stringBuilder.toString());

        } catch (IOException e) {
            log.error("setupBody error", e);
        } finally {
            if (Objects.nonNull(bufferedReader)) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    log.error("setup body bufferedReader close error", e);
                }
            }
        }

    }

    @Override
    public void toJson(Invocation invocation) {
    }

}

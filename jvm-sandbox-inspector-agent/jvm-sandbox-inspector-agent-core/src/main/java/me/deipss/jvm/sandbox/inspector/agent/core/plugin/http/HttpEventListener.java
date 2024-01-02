package me.deipss.jvm.sandbox.inspector.agent.core.plugin.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import com.alibaba.jvm.sandbox.api.event.ReturnEvent;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Span;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BaseEventListener;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.InvocationCache;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.Tracer;
import org.apache.commons.io.CopyUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Slf4j
public class HttpEventListener extends BaseEventListener {
    public HttpEventListener(boolean entrance, String protocol) {
        super(entrance, protocol);
    }

    @Override
    public Invocation initInvocation(BeforeEvent event) {
        HttpInvocation invocation = new HttpInvocation();
        invocation.setInnerEntrance(entrance);
        invocation.setProtocol(protocol);
        invocation.setUk(Tracer.initUk(event.invokeId));
        invocation.setPreUk(entrance ? Tracer.getOverMachineUk() : Tracer.getPreUk(event.invokeId, protocol));
        invocation.setIp(Tracer.getLocalIp());
        invocation.setTraceId(Tracer.getTraceId());
        log.info("http initInvocation done");
        return invocation;
    }

    @Override
    public void transportSpan(BeforeEvent event) {
    }

    @Override
    public Span extractSpan(BeforeEvent event) {
        try {
            Object obj = event.argumentArray[0];
            if (obj instanceof HttpServletRequest) {
                HttpServletRequest req = (HttpServletRequest) obj;
                String header = req.getHeader(Span.SPAN);
                if (Strings.isNullOrEmpty(header)) {
                    return null;
                }
                return JSON.parseObject(header, Span.class);
            }

        } catch (Exception e) {
            log.info("http extract span ,error", e);
        }
        return null;
    }

    @Override
    public void assembleRequest(BeforeEvent event, Invocation invocation) {
        // void service(HttpServletRequest req, HttpServletResponse resp)
        if (!(event.argumentArray[0] instanceof HttpServletRequest)) {
            return;
        }
        HttpServletRequest req = (HttpServletRequest) event.argumentArray[0];
        invocation.setUri(req.getRequestURI());
        invocation.setUrl(new String(req.getRequestURL()));
        invocation.setMethodName(req.getMethod());
        invocation.setContentType(req.getContentType());
        invocation.setHttpPort(req.getLocalPort());
        setupHeaders(invocation, req);
        setupParameters(invocation, req);
        setupBody(invocation, req);
    }

    @Override
    public void assembleResponse(ReturnEvent event, Invocation invocation) {
        Invocation originInvocation = InvocationCache.get(invocation.getInvokeId());
        if(Objects.isNull(originInvocation)){
            log.error("assembleResponse error ,can not get Invocation" );
        }
        // todo
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

        if (!request.getContentType().contains("application/json")) {
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
                    log.error("setupboyd bufferedReader close error", e);
                }
            }
        }

    }

}

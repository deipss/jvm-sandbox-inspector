package me.deipss.jvm.sandbox.inspector.web.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.HeartBeat;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.web.dal.entity.HeartBeatDO;
import me.deipss.jvm.sandbox.inspector.web.dal.entity.InvocationDO;
import me.deipss.jvm.sandbox.inspector.web.dal.mapper.HeartBeatMapper;
import me.deipss.jvm.sandbox.inspector.web.dal.mapper.InvocationMapper;
import me.deipss.jvm.sandbox.inspector.web.domain.requset.HeartBeatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("inspector")
@Slf4j
public class InspectorController {



        @Autowired
        private HeartBeatMapper heartBeatMapper;

        @Autowired
        private InvocationMapper invocationMapper;

        @GetMapping("/heartBeat")
        public void heartBeat(@ModelAttribute HeartBeatRequest heartBeat) {
                heartBeatMapper.insert(toHeartBeatDO(heartBeat));
        }


        @PostMapping("/dataSync")
        public void dataSync(@RequestBody Invocation invocation) {
                invocationMapper.insertSelective(toInvocationDO(invocation));
        }

        HeartBeatDO toHeartBeatDO(HeartBeatRequest heartBeat){
            HeartBeatDO heartBeatDO = new HeartBeatDO();
            heartBeatDO.setApp(heartBeat.getApp());
            heartBeatDO.setIp(heartBeat.getIp());
            heartBeatDO.setVersion(heartBeat.getVersion());
            heartBeatDO.setBeatTime(new Date(Long.parseLong(heartBeat.getBeatTime())));
            heartBeatDO.setEnvTag(heartBeat.getEnvTag());
            heartBeatDO.setPort(heartBeat.getPort());
            heartBeatDO.setModuleStatus(heartBeat.getModuleStatus());
            return heartBeatDO;
        }

        InvocationDO toInvocationDO(Invocation invocation){
            InvocationDO invocationDO = new InvocationDO();
            invocationDO.setProtocol(invocation.getProtocol());
            invocationDO.setTraceId(invocation.getTraceId());
            invocationDO.setUk(invocation.getUk());
            invocationDO.setPreUk(invocation.getPreUk());
            invocationDO.setInvokeId(invocation.getInvokeId());
            invocationDO.setInnerEntrance(invocation.isInnerEntrance());
            invocationDO.setOuterEntrance(invocation.isOuterEntrance());
            invocationDO.setRequestJson(invocation.getRequestJson());
            invocationDO.setClassName(invocation.getClassName());
            invocationDO.setMethodName(invocation.getMethodName());
            invocationDO.setRpcContext(JSON.toJSONString(invocation.getRpcContext()));
            invocationDO.setResponseJson(invocation.getResponseJson());
            invocationDO.setThrowableMsg(invocation.getThrowableMsg());
            invocationDO.setThrowableClass(invocation.getThrowableClass());
            invocationDO.setSql(invocation.getSql());
            invocationDO.setTopic(invocation.getTopic());
            invocationDO.setMsgId(invocation.getMsgId());
            invocationDO.setTags(invocation.getTags());
            invocationDO.setMqBody(invocation.getMqBody());
            invocationDO.setStart(invocation.getStart());
            invocationDO.setEnd(invocation.getEnd());
            invocationDO.setStartTime(new Date(invocation.getStart()));
            invocationDO.setEndTime(new Date(invocation.getEnd()));
            invocationDO.setIp(invocation.getIp());
            invocationDO.setHttpPort(invocation.getHttpPort());
            invocationDO.setUri(invocation.getUri());
            invocationDO.setUrl(invocation.getUrl());
            invocationDO.setContentType(invocation.getContentType());
            invocationDO.setHttpBody(invocation.getHttpBody());
            invocationDO.setHttpHeaders(JSON.toJSONString(invocation.getHttpHeaders()));
            invocationDO.setHttpParameters(JSON.toJSONString(invocation.getHttpParameters()));
            return invocationDO;

        }
}

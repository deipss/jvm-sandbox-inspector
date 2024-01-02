package me.deipss.jvm.sandbox.inspector.agent.core.impl;

import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.HeartBeat;
import me.deipss.jvm.sandbox.inspector.agent.api.service.HeartBeatService;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.Tracer;
import me.deipss.jvm.sandbox.inspector.agent.core.util.ConfigUtil;
import me.deipss.jvm.sandbox.inspector.agent.core.util.HttpUtil;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HeartBeatServiceImpl implements HeartBeatService {


    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("heartbeat-pool-%d").daemon(true).build());


    @Override
    public void syncHeartBeat() {
        HeartBeat heartBeat = new HeartBeat();
        heartBeat.setApp("default");
        heartBeat.setIp(Tracer.getLocalIp());
        heartBeat.setVersion(Constant.version);
        heartBeat.setBeatTime(new Date());
        heartBeat.setEnvTag("default");
        heartBeat.setPort("default");
        heartBeat.setModuleStatus("ACTIVE");
        HashMap<String, String> heartBeatMap = new HashMap<>();
        heartBeatMap.put("app",heartBeat.getApp());
        heartBeatMap.put("ip",heartBeat.getIp());
        heartBeatMap.put("version",heartBeat.getVersion());
        heartBeatMap.put("beatTime",heartBeat.getBeatTime().toString());
        heartBeatMap.put("envTag",heartBeat.getEnvTag());
        heartBeatMap.put("port",heartBeat.getPort());
        heartBeatMap.put("moduleStatus",heartBeat.getPort());
        HttpUtil.get(ConfigUtil.getHeartUrl(),heartBeatMap,null);
    }


    public synchronized void start() {
        log.info("heart start");
        executorService.scheduleAtFixedRate(() -> {
            try {
                syncHeartBeat();
            } catch (Exception e) {
                log.error("error occurred when report heartbeat", e);
            }
        }, 5, 3, TimeUnit.SECONDS);
    }

    public void stop() {
        executorService.shutdown();
    }

}

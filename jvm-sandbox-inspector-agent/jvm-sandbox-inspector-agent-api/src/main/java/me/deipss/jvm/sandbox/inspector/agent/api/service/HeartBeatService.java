package me.deipss.jvm.sandbox.inspector.agent.api.service;

public interface HeartBeatService {

    void syncHeartBeat();

    void start();

    void stop();
}

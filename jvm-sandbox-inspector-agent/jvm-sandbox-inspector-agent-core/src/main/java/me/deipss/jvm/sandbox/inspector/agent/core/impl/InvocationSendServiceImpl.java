package me.deipss.jvm.sandbox.inspector.agent.core.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.core.util.ConfigUtil;
import me.deipss.jvm.sandbox.inspector.agent.core.util.HttpUtil;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;


@Slf4j
public class InvocationSendServiceImpl implements InvocationSendService {

    private final ConcurrentLinkedQueue<Invocation> queue = new ConcurrentLinkedQueue<>();

    private final ExecutorService queueConsumerTaskExecutor = new ThreadPoolExecutor(4, 4,
            2L, TimeUnit.MINUTES, new LinkedBlockingDeque<>(128),
            new BasicThreadFactory.Builder().namingPattern("invocation-queue-pool-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 在构造函数中
     * 向队列消费的线程池，提交消费的任务
     */
    public InvocationSendServiceImpl() {
        log.info("send start");
        int consumerThreadNum = 4;
        for (int i = 0; i < consumerThreadNum; i++) {
            queueConsumerTaskExecutor.submit(new QueueConsumerTask());
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!queueConsumerTaskExecutor.isShutdown()) {
                log.warn("queueConsumerTaskExecutor losing {}", queueConsumerTaskExecutor.shutdownNow().size());
            }

            log.info("invocationSendExecutor close");
        }));
    }


    @Override
    public void send(Invocation invocation) {
        final int size = queue.size();
        if (size >= 4096) {
            log.info("can't offer queue cause size limit,aboard this record;current={},max={}", size, 4096);
            return;
        }
        queue.offer(invocation);
    }


    class QueueConsumerTask implements Runnable {
        private boolean working = true;

        @Override
        public void run() {
            while (working) {
                try {
                    final Invocation record = queue.poll();
                    if (record != null) {
                        HttpUtil.post(ConfigUtil.getDataSendUrl(), JSON.toJSONString(record), null);
                    } else {
                        Thread.sleep(50);
                    }
                } catch (Throwable throwable) {
                    log.error("uncaught exception occurred in queue consumer thread : {};stop this job",
                            Thread.currentThread().getName(), throwable);
                    working = false;
                }
            }
        }
    }
}

package me.deipss.jvm.sandbox.inspector.agent.core.test;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Span;
import me.deipss.jvm.sandbox.inspector.agent.core.test.bo.User;
import me.deipss.jvm.sandbox.inspector.agent.core.test.plugin.*;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcInvocation;
import org.apache.dubbo.rpc.filter.ConsumerContextFilter;
import org.apache.dubbo.rpc.filter.ContextFilter;
import org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class InspectorAgentDebug {


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            String s = in.nextLine();

            if (s.equals("concurrent")) {
                concurrent();
            }

            if (s.equals("jdbc")) {
                jdbc();
            }
            if (s.equals("rocketMqConsumer")) {
                rocketMqConsumer();
            }
            if (s.equals("rocketMqProvider")) {
                rocketMqProvider();
            }
            if (s.equals("dubboConsumer")) {
                dubboConsumer();
            }
            if (s.equals("dubboProvider")) {
                dubboProvider();
            }

            if (s.equals("http")) {
                http();
            }

            if (s.equals("stop")) {
                return;
            }

            if (s.equals("all")) {
                all();
            }
            System.out.println(s);
        }

    }



    @SneakyThrows
    public static void dubboProvider() {
        ContextFilter contextFilter = new ContextFilter();
        Invoker<?> invoker = new DubboMockInvoker();
        Invocation invocation = new RpcInvocation(User.class.getDeclaredMethod("queryUser",String.class, Integer.class,String.class),new Object[]{"name",12,"wave wave"});
        contextFilter.invoke(invoker,invocation);
    }



    @SneakyThrows
    public static void dubboConsumer() {
        ConsumerContextFilter consumerContextFilter = new ConsumerContextFilter();
        Invoker<?> invoker = new DubboMockInvoker();
        Invocation invocation = new RpcInvocation(User.class.getMethod("queryUser", String.class, Integer.class,String.class),new Object[]{"name",12,"wave wave"});
        Result invoke = consumerContextFilter.invoke(invoker,invocation);
    }

    @SneakyThrows
    public static void rocketMqProvider() {
        DefaultMQProducerImpl defaultMQProducer = new DefaultMQProducerImpl(new DefaultMQProducer());
        Message messageExt = new Message();
        messageExt.setBody(JSON.toJSONBytes(initUser()));
        messageExt.setTopic("TESTER_CONSUMER_TOPIC");
        messageExt.setTags("TAG1");
        messageExt.putUserProperty(Span.SPAN,JSON.toJSONString(initSpan()));
        defaultMQProducer.send(messageExt,  new SendCallback(){

            @Override
            public void onSuccess(SendResult sendResult) {

            }

            @Override
            public void onException(Throwable e) {

            }
        },1000);
    }


    @SneakyThrows
    public static void rocketMqConsumer() {
        RocketMqConsumerImpl rocketMqConsumer = new RocketMqConsumerImpl();
        List<MessageExt> msgs = new ArrayList<>();
        MessageExt messageExt = new MessageExt();
        messageExt.setBody(JSON.toJSONBytes(initUser()));
        messageExt.setTopic("TESTER_CONSUMER_TOPIC");
        messageExt.setTags("TAG1");
        messageExt.setMsgId("TEST"+LocalDateTime.now().getNano());
        messageExt.putUserProperty(Span.SPAN,JSON.toJSONString(initSpan()));
        msgs.add(messageExt);
        rocketMqConsumer.consumeMessage(msgs,null);
    }

    @SneakyThrows
    public static void jdbc() {
        JdbcImpl jdbc = new JdbcImpl();
        jdbc.execute("select * from dual limit 1");
    }


    @SneakyThrows
    public static void all() {
        http();
        concurrent();
        jdbc();
        dubboConsumer();
        rocketMqProvider();
    }


    @SneakyThrows
    public static void concurrent() {

        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        for (int i = 0; i < 10; i++) {
            scheduledThreadPoolExecutor.submit(() -> {
                System.out.println("concurrent"+LocalDateTime.now().getNano());
            });
        }

    }

    @SneakyThrows
    public static void http() {
        HttpServletImpl httpServlet = new HttpServletImpl();
        ServletRequest servletRequest = new HttpServletRequestImpl();
        HttpServletResponse mockHttpServletResponse = new HttpServletResponseImpl();
        servletRequest.setAttribute("test","test");
        httpServlet.service(servletRequest,mockHttpServletResponse);
    }


    public static User initUser(){
        User user = new User();
        user.setName("Tester");
        user.setAge(LocalDateTime.now().getMinute());
        user.setAddress("wave wave mountain");
        return user;
    }


    public static Span initSpan(){
        Span span = new Span();
        span.setTraceId("trace_"+LocalDateTime.now().getNano());
        span.setOverMachineUk("uk_"+LocalDateTime.now().getNano());
        return span;

    }

}

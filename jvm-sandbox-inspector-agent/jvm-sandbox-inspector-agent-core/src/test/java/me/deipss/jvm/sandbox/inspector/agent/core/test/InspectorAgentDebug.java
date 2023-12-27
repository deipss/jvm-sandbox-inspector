package me.deipss.jvm.sandbox.inspector.agent.core.test;

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

            if (s.equals("stop")) {
                return;
            }
            System.out.println(s);
        }

    }


    public static void dubboProvider() {

    }


    public static void dubboConsumer() {

    }

    public static void rocketMqProvider() {

    }


    public static void rocketMqConsumer() {

    }

    public static void jdbc() {

    }

    public static void concurrent() {

        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        for (int i = 0; i < 10; i++) {
            scheduledThreadPoolExecutor.submit(() -> {
                System.out.println("concurrent");
            });
        }

    }

}

package me.deipss.jvm.sandbox.inspector.agent.core.test;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                System.out.println("concurrent");
            });
        }

    }

}

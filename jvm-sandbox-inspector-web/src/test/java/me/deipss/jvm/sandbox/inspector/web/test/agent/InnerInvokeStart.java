package me.deipss.jvm.sandbox.inspector.web.test.agent;

import java.util.Date;
import java.util.Scanner;

import me.deipss.jvm.sandbox.inspector.web.test.agent.domain.QueryDTO;

public class InnerInvokeStart {

    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入您的姓名：");
        while (true) {
            String name = scanner.nextLine();
            if (name.contains("stop")) {
                return;
            } else {
                InnerInvokeQuery innerInvokeQuery = new InnerInvokeQuery();
                QueryDTO queryDTO = new QueryDTO();
                queryDTO.setName(name);
                queryDTO.setOrderId("1");
                queryDTO.setUserId("1");
                queryDTO.setStartTime(new Date());
                innerInvokeQuery.query(queryDTO);
            }

        }


    }


}

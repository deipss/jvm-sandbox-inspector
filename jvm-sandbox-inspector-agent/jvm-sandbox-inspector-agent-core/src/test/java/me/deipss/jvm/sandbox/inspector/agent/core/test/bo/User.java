package me.deipss.jvm.sandbox.inspector.agent.core.test.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private String name;
    private int age;

    private String address;
}

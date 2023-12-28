package me.deipss.jvm.sandbox.inspector.agent.core.test.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private String name;
    private int age;

    private String address;


    public User queryUser(String name, Integer age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
        return this;
    }
}

package com.github.xiaoma.sniper.core;

import java.io.Serializable;

/**
 * Created by machunxiao on 17/1/18.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 7398915348647401990L;

    int age;
    String name;

    public User() {
    }

    public User(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}

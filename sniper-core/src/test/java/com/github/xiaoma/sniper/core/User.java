package com.github.xiaoma.sniper.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by machunxiao on 17/1/18.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 7398915348647401990L;

    private long id;
    private int age;
    private String name;

    private List<Address> addresses;

    private Map<String, Object> names;

    private List<String> phones;

    public User() {
    }

    public User(long id, int age, String name, List<Address> addresses, Map<String, Object> names, List<String> phones) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.addresses = addresses;
        this.names = names;
        this.phones = phones;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Map<String, Object> getNames() {
        return names;
    }

    public void setNames(Map<String, Object> names) {
        this.names = names;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", addresses=" + addresses +
                ", names=" + names +
                ", phones=" + phones +
                '}';
    }
}

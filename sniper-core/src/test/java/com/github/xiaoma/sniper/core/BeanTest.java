package com.github.xiaoma.sniper.core;

import org.junit.Test;
import org.springframework.beans.BeanUtils;

/**
 * Created by machunxiao on 16/12/27.
 */
public class BeanTest {
    @Test
    public void testCopy() {
        B b = new B();
        b.setId(1);
        b.setAddr("b1");
        b.setName("bn1");
        b.setNick("bc1");
        A a = new A();

        try {
            BeanUtils.copyProperties(b, a);
            System.out.println(a);
            System.out.println(b);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class A {
        int id;
        String name;
        String addr;
        String email;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    class B {
        int id;
        String name;
        String addr;
        String nick;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }
    }
}

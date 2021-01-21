package com.lt.recyclerdemo;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTestJava {
    interface Subject{
        void hello(String msg);

        String execyte();
    }
    class SubjectImpl implements Subject{
        @Override
        public void hello(String msg) {
            System.out.println("SubjectImpl" + msg);
        }

        @Override
        public String execyte() {
            return "SubjectImpl";
        }
    }

    @Test
    public void test(){
        Subject s = new SubjectImpl();
        Subject p = (Subject) Proxy.newProxyInstance(Subject.class.getClassLoader(), new Class<?>[]{Subject.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                System.out.println("Proxy invoke " + method.getName());
                method.invoke(s,objects);
                return "SubjectProxy";
            }
        });
        p.hello("proxy");
        String r = p.execyte();
        System.out.println(r);
    }
}

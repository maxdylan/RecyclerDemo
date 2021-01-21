package com.lt.recyclerdemo

import org.junit.Test
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class ProxyTest {

    interface Subject{
        @SuppressWarnings
        fun hello(msg: String):Object
    }

    class Object(val msg:String){
        fun say(){
            println("hello $msg")
        }
    }

    class SubjectInvocationHandler : InvocationHandler{
        override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {
            println("SubjectInvocationHandler invoke ${method.name}")
            return invoke(method)
        }

        private fun invoke(method: Method):Object {
            println("method name: ${method.name}")
            println("method annotation: ${method.annotations}")
            return Object("proxy")
        }

    }

    @Test
    fun proxyTest(){
        val s = Proxy.newProxyInstance(
            Subject::class.java.classLoader,
            arrayOf(Subject::class.java),
            SubjectInvocationHandler()
        ) as Subject
        val o = s.hello("hhh")
        o.say()

    }
}
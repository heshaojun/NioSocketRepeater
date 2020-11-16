package org.heath.test;

/**
 * @author heshaojun
 * @date 2020/11/16
 * @description TODO
 */
public class Test {
    public static void main(String[] args) {
        ITestInterface testInterface = new SubClass();
        testInterface.test();
    }
}

package org.heath.test;

/**
 * @author heshaojun
 * @date 2020/11/16
 * @description TODO
 */
public interface ITestInterface {
    default void test() {
        System.out.println(this);
    }
}

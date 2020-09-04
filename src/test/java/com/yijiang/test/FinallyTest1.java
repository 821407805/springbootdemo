package com.yijiang.test;

import org.springframework.jdbc.core.SqlOutParameter;

import static com.yijiang.test.FinallyTest1.test11;

/**
 * @ClassName: FinallyTest1
 * @Description: try中有return时，会执行finally后再返回
 * @Author Jason
 * @Date 2020/9/3 17:35
 */
public class FinallyTest1 {
    public static void main(String[] args) {

        test11();
        System.out.println("结束");
    }

    public static void test11() {
        try {
            System.out.println("try block");
            return;
        } finally {
            System.out.println("finally block");
        }
    }

}

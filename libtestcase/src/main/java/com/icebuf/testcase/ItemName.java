package com.icebuf.testcase;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/10/12
 * E-mail：bflyff@hotmail.com
 */
public @interface ItemName {

    String value() default "";

    int id() default 0;
}

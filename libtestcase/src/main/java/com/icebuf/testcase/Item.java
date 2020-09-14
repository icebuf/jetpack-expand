package com.icebuf.testcase;

public @interface Item {

    int index() default 0;

    String name();

    String description() default "";

    int destination();
}

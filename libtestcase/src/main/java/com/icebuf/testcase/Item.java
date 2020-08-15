package com.icebuf.testcase;

public @interface Item {

    int index();

    String name();

    String description() default "";

    int destination();
}

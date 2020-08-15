package com.icebuf.testcase;

public @interface ResItem {

    int index() default 0;

    int name();

    int description() default 0;

    int destination();
}

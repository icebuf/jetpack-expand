package com.icebuf.testcase;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.CLASS)
@Target(value = {ElementType.TYPE})
@DefaultValue("default")
public @interface ItemGroup {

    String value() default "default";

}

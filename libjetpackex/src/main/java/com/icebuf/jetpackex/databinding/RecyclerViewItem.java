package com.icebuf.jetpackex.databinding;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/7
 * E-mail：bflyff@hotmail.com
 *
 * 通过注解方式提供RecyclerView的Item对象
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RecyclerViewItem {

    /**
     * 布局文件应当支持dataBinding的写法
     * 如下所示：
     * <code>
     *  <layout xmlns:android="http://schemas.android.com/apk/res/android">
     *      <data>
     *          <variable
     *              name="student"
     *              type="com.xxx.Student" />
     *      </data>
     *      <TextView
     *          android:text="@{student.name}"/>
     *  </layout>
     * </code>
     * @return xml布局文件的id
     */
    @LayoutRes
    int layoutId();

    /**
     * 对应布局文件中的variable
     *  一般会自动构建生成在此类
     * 直接引用即可。 如果此值大于0，则优先使用此值，同时{@link #variable)}的值将不会被使用
     * @return dataBinding生成的布局文件种variable对应的id
     * @see androidx.databinding.library.baseAdapters.BR
     */
    int variableId() default 0;

    /**
     * 对应布局文件中的<code>variable</code>标签的<code>name</code>字段
     *
     * @return 布局文件中variable的名称
     */
    String variable() default "";

    /**
     * 如果item内部会对viewModel操作，请使用此id
     * 这样item的布局内部可以使用viewModel进行操作了
     * @return dataBinding生成的布局文件种variable对应的id
     * @see androidx.databinding.library.baseAdapters.BR
     */
    int viewModelId() default 0;

}

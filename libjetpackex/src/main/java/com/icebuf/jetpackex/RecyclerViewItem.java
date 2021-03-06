package com.icebuf.jetpackex;



import androidx.annotation.LayoutRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通过注解方式提供RecyclerView的Item对象.
 * id的优先级高于名称查找
 *
 * @author IceTang
 * @version 1.0 on 2020/8/7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RecyclerViewItem {

    /**
     * 布局文件应当支持dataBinding的写法.
     * <li>如下所示：
     * <pre>
     * &lt;layout xmlns:android="http://schemas.android.com/apk/res/android"&gt;
     *      &lt;data&gt;
     *          &lt;variable
     *              name="student"
     *              type="com.xxx.Student" /&gt;
     *      &lt;/data&gt;
     *      &lt;TextView
     *          android:text="@{student.name}"/&gt;
     *  &lt;/layout&gt;
     * </pre></li>
     *
     * @return xml布局文件的id
     */
    @LayoutRes
    int layoutId() default 0;

    /**
     * 布局的名称.
     * 因在module中无法使用变量的BR文件的字段，可使用此名称动态查找
     *
     * @return 布局的名称.
     */
    String layout() default "";

    /**
     * 对应布局文件中的variable.
     *  一般会自动构建生成在此类
     * 直接引用即可。 如果此值大于0，则优先使用此值，同时{@link #variable}的值将不会被使用
     *
     * @see androidx.databinding.library.baseAdapters.BR
     *
     * @return dataBinding生成的布局文件种variable对应的id
     */
    int variableId() default 0;

    /**
     * 对应布局文件中的<code>variable</code>标签的<code>name</code>字段.
     * 因在module中无法使用变量的BR文件的字段，可使用此名称动态查找
     *
     * @return 布局文件中variable的名称
     */
    String variable() default "";

    /**
     * 如果item内部会对viewModel操作，请使用此id.
     * 这样item的布局内部可以使用viewModel进行操作了
     *
     * @return dataBinding生成的布局文件中variable对应的id
     * @see androidx.databinding.library.baseAdapters.BR
     */
    int tagId() default 0;

    /**
     * 放入item布局文件中的自定义对象的variable名称.
     * 因在module中无法使用变量的BR文件的字段，可使用此名称动态查找
     *
     * @return 自定义对象的variable名称
     */
    String tag() default "";

}

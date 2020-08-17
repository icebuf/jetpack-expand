# jetpack-expand
基于安卓Jetpack框架组件，在安卓应用开发上的一些辅助类和工具

- ## RadioGroup和RadioButton

使用DataBinding中BindingAdapter的方式对RadioGroup和RadioButton
功能做了拓展，并且避免了通过自定义RadioGroup和RadioButton类进行功能扩展
使得RadioButton支持自定义类型的数据绑定，而且开发者自定义的RadioButton子类
依然享有该功能

通过颜色值绑定
```xml
<layout>
    <data>
        <variable name="vm"
            type="xxx.xxx.ViewModel" />
    </data>
    <RadioGroup
        app:checkedValue="@{@color/red}"
        app:onValueChanged="@{(view, value)->vm.onColorChanged(view, value)}">
        <RadioButton
            app:value="@{@color/red}"
            android:text="Red" />
        <RadioButton
            app:value="@{@color/Blue}"
            android:text="Blue" />    
    </RadioGroup>   
</layout>
```
通过枚举绑定
```xml
<layout>
    <data>
        <import type="xxx.xxx.Sex" />
        <variable name="vm"
            type="xxx.xxx.ViewModel" />
    </data>
    <RadioGroup
        app:checkedValue="@{Sex.MAM}"
        app:onValueChanged="@{(view, value)->vm.onSexChanged(view, value)}">
        <RadioButton
            app:value="@{Sex.MAM}"
            android:text="Man" />
        <RadioButton
            app:value="@{Sex.FEMALE}"
            android:text="Female" />    
    </RadioGroup>  
</layout>
```
注意：同一个RadioGroup中所有RadioButton的绑定`app:value`的类型必须一致

- ## NestedRadioGroup

NestedRadioGroup是一个自定义ViewGroup，它解决了RadioGroup无法在嵌套子布局
中寻找可用的RadioButton的问题，使得RadioGroup容器内，所有RadioButton的布局
方式可以达到任意的排列效果，并且不会影响`checkedChanged`事件的回调

注意：如果NestedRadioGroup的子布局中又出现了一个NestedRadioGroup或者是RadioGroup
时，在检查RadioButton时会直接将其跳过，因为这个子布局可能有自己的`checkedChanged`事件的回调

演示如下：

package com.example.testbutterknife;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: lyc
 * @CreateDate: 2021-01-25 13:28
 * @Description: 类描述
 */
//　     1.CONSTRUCTOR:用于描述构造器
//　　　　2.FIELD:用于描述域
//　　　　3.LOCAL_VARIABLE:用于描述局部变量
//　　　　4.METHOD:用于描述方法
//　　　　5.PACKAGE:用于描述包
//　　　　6.PARAMETER:用于描述参数
//　　　　7.TYPE:用于描述类、接口(包括注解类型) 或enum声明
@Target(ElementType.METHOD)

//      1、RetentionPolicy.SOURCE：注解只保留在源文件，当Java文件编译成class文件的时候，注解被遗弃；
//      2、RetentionPolicy.CLASS：注解被保留到class文件，但jvm加载class文件时候被遗弃，这是默认的生命周期；
//      3、RetentionPolicy.RUNTIME：注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；
@Retention(RetentionPolicy.RUNTIME)

//  bt2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//  onLongClick 同样修改下边是哪个参数即可
@BaseClickInject(listenerSetter = "setOnClickListener",
                listnerType = View.OnClickListener.class,
                callbackMethod = "onClick" )
public @interface ClickInject {
    int[] value() default -1;
}

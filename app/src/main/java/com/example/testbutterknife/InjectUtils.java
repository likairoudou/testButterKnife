package com.example.testbutterknife;

import android.content.Context;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: lyc
 * @CreateDate: 2021-01-25 13:53
 * @Description: 类描述
 */
public class InjectUtils {
    public static void ject(Context context){
        injectLayout(context);// 获取setContentView 方法

        injectView(context); //  获取每个view

        injectClick(context); // 获取view点击交互事件
    }

    private static void injectClick(Context context) {
        //为了处理onClick 、onLongClick。。等等事件，需要一个统一方法

        Class<?> clazz=context.getClass();//获取当前activity的类

        //getMethods 方法取得所有public方法 包括继承的方法

        //getDeclaredMethods 取得所有自己声明的方法 包括 public protected default private

        Method []methods=clazz.getDeclaredMethods();

        //遍历当前类中方法，查出有加入注解的方法

        for (int i = 0; i <methods.length ; i++) {

            Method method=methods[i];


            //遍历当前这个方法的所有注解
            Annotation[] annotations= method.getAnnotations();

            for (Annotation annotation:annotations) {
                Class<?> clazzAnnotation=annotation.annotationType();

                //为什么不用ClickInject，因为可能还要LongClickInject等其他交互方法，所以要判断他们的父注解信息
                BaseClickInject baseClickInject=clazzAnnotation.getAnnotation(BaseClickInject.class);
                if(baseClickInject!=null){ //判断当前这个注解的方法，是不是我们要处理的

                    method.setAccessible(true);//防止有人自己定义的点击方法私有

                    String listenerSetter=baseClickInject.listenerSetter();

                    Class<?> baseClickInjectClazz=baseClickInject.listnerType();


                    try {
                        Method valueMethod=clazzAnnotation.getDeclaredMethod("value"); //获取注解上的value方法


                        int[] values= (int[]) valueMethod.invoke(annotation); //获取当前所有的button对象id值了，没有参数类型

                        for (int value:values) {//遍历当前所有的button对象，通过反射根据view的id值获取view

                           Method findViewById= context.getClass().getMethod("findViewById",int.class);

                           View view= (View) findViewById.invoke(context,value);//根据反射出的方法，拿到当前view 即button

                           if(view!=null){
                              // activity对应的是context    myClick或者myLongClick对应的是我们自己定义的method 通过代理去执行我们自己定义的点击方法

                               MyInvokationHandler myInvokationHandler=new MyInvokationHandler(context,method);

                               //代理类   new View.OnClickListener()对象

                               //    /**
                               //     * 动态代理 能代理实现相同接口的方法 所以他代理其实就是把OnClickListener这个具体实现类代理出来
                               //     */
                               //         public interface OnClickListener {
                               //        /**
                               //         * Called when a view has been clicked.
                               //         *
                               //         * @param v The view that was clicked.
                               //         */
                               //        void onClick(View v);
                               //    }

                               Object proxy= Proxy.newProxyInstance(baseClickInjectClazz.getClassLoader()
                                       ,new Class[] {baseClickInjectClazz}
                                       ,myInvokationHandler);

                               //执行  让proxy执行的onClick()
                               //参数1  setOnClickListener（）
                               //参数2  new View.OnClickListener()对象
                               //   view.setOnClickListener（new View.OnClickListener()）其实就是  反射view上setOnClickListener方法

                               Method onClick=view.getClass().getMethod(listenerSetter,baseClickInjectClazz);
                               onClick.invoke(view,proxy);

                            }

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }




                }

            }


        }
    }

    private static void injectView(Context context) {
        Class<?> clazz=context.getClass();//获取当前activity的类



        Field[] fields=clazz.getDeclaredFields();//获取当前所有

        for (int i = 0; i < fields.length; i++) {//遍历当前所有的成员方法，查看有无注解信息，如果有取出来

            ViewInject viewInject=fields[i].getAnnotation(ViewInject.class); //获取ContentViewInject注解信息

            if(viewInject!=null){
                try {
                    Method findViewById=clazz.getMethod("findViewById",int.class); //通过反射，获取setContentView方法,有参数需要带着int类型
                    fields[i].setAccessible(true);
                    View view=(View)findViewById.invoke(context,viewInject.value()); //相当于 调用findViewById,把当前的id，转换成view
                    fields[i].setAccessible(true);//防止用户设置私有
                    fields[i].set(context,view); //通过反射使用当前字段，把我们findViewById粗来的view，交给fields[i]，即我们自己定义的那个bt1，  private Button bt1;

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

    }

    private static void injectLayout(Context context) {
        int id;//当前activity的layout布局id

        Class<?> clazz=context.getClass();//获取当前activity的类

        ContentViewInject contentViewInject=clazz.getAnnotation(ContentViewInject.class); //获取ContentViewInject注解信息

        id=contentViewInject.value();//将获取到的当前注解中的layout的id赋值

        try {

            Method setContentView=clazz.getMethod("setContentView",int.class); //通过反射，获取setContentView方法,有参数需要带着int类型
            setContentView.invoke(context,id); //执行setContentView方法 第一个被反射类，第二个具体方法参数

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

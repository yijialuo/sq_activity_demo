package com.sq.demo.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtil {
    public static List<String> getAllDeclareField(Object object){
        List<String> list=new ArrayList<>();

        Class clz=object.getClass();
        Field[] fields = clz.getDeclaredFields();
        for (int i=1;i<fields.length;i++){//遍历
            try {
                //得到属性
                Field field = fields[i];
                //打开私有访问
                field.setAccessible(true);
                //获取属性
                String name = field.getName();
                //获取属性值
                Object value = field.get(object);
                //一个个赋值
                list.add(value==null?"":value.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}

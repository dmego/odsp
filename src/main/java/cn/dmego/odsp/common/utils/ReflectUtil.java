package cn.dmego.odsp.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class_name: ReflectUtil
 * package: cn.dmego.odsp.common.utils
 * describe: 利用反射实现的工具类
 * creat_user: Dmego
 * creat_date: 2018/10/27
 * creat_time: 18:14
 **/
public class ReflectUtil {

    /**
     * 将对象转成 Map,只包含指定的字段
     * @param t
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> objectToMap(T t){
        if(t == null){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field: fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(),field.get(t));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static <T> List<Map<String,Object>> objectToMap(List<T> ts){
        List<Map<String,Object>> list = new ArrayList<>();
        for (T t:ts) {
            list.add(objectToMap(t));
        }
        return list;
    }

    /**
     * 将对象转为map,只包含指定的字段
     * @param t
     * @param fileds
     * @param <T>
     * @return
     */
    public static <T> Map<String,Object> getResultMap(T t,String[] fileds){
        Map<String,Object> map = new HashMap<>();
        Class<?> clazz = t.getClass();
        for (int i = 9;i < fileds.length;i++) {
            String filed = fileds[i];

            try {
                Method mf = clazz.getMethod("get"+upperHeadChar(filed));
                map.put(filed,mf.invoke(t));
            } catch (NoSuchMethodException e) {
                System.out.println("字段"+filed+"不存在");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static <T> List<Map<String,Object>> getResultMap(List<T> ts,String[] fileds){
        List<Map<String,Object>> list = new ArrayList<>();
        for (int i = 0; i< ts.size();i++) {
            list.add(getResultMap(ts.get(i),fileds));
        }
        return list;
    }

    /**
     * 首字母转大写
     * @param filed
     * @return
     */
    private static String upperHeadChar(String filed) {
        String head = filed.substring(0,1);
        String out = head.toUpperCase() + filed.substring(1,filed.length());
        return out;
    }



}

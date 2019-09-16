package com.rwz.hook.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 反射工具类
 */
public class ReflectUtil {

    /**
     * 获取对象的属性值
     */
    public static Object getDeclaredField(Object obj, String fieldName) {
        return obj == null ? null : getDeclaredField(obj.getClass(), obj, fieldName);
    }

    /**
     * 获取父类对象的属性值
     * @param obj null表示静态变量
     */
    public static Object getDeclaredField(Class cl, @Nullable Object obj, String fieldName) {
        if (cl == null || TextUtils.isEmpty(fieldName)) {
            return null;
        }
        try {
            Field field = cl.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用某个方法
     */
    public static void invoke(Object target, String methodName, Object... args) {
        if(target == null)
            return;
        invoke(target.getClass(), target, methodName, args);
    }

    /**
     * 调用父类某个方法
     * @param obj : null表示静态方法
     */
    public static void invoke(Class cls, @Nullable Object obj, String methodName, Object... args) {
        try {
            Method method = cls.getDeclaredMethod(methodName, object2Class(args));
            method.setAccessible(true);
            method.invoke(obj, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Class[] object2Class(Object... args) {
        if(args == null || args.length == 0)
            return null;
        Class[] result = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            result[i] = args[i].getClass();
        }
        return result;
    }

    /**
     * 打印所有属性(不包含父类的属性)
     */
    public static void printAllFiled(Object obj, String TAG) {
        if (obj != null) {
            String text = getAllFiled(obj.getClass().getDeclaredFields(), obj, TAG);
            LogUtil.d(TAG, text);
        }
    }

    /**
     * 打印父类cl的属性
     * @param cl 父类
     * @param obj 对象
     */
    public static void printAllFiled(Class cl, Object obj, String TAG) {
        if (cl != null) {
            String text = getAllFiled(cl.getDeclaredFields(), obj, TAG);
            LogUtil.d(TAG, text);
        }
    }

    /**
     *  将对象属性转化为字符串
     */
    public static String getAllFiled(Object obj, String TAG) {
        return obj == null ? "" : getAllFiled(obj.getClass().getDeclaredFields(), obj, TAG);
    }

    private static String getAllFiled(Field[] fields, Object obj, String TAG) {
        if (fields == null || fields.length == 0 || obj == null) {
            return obj + "";
        }
        StringBuilder sb = new StringBuilder();
        for (Field f : fields) {
            f.setAccessible(true);
            Object object = null;
            try {
                object = f.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(TAG)) {
                sb.append(TAG).append(",");
            }
            sb.append(f.getName()).append(" = ").append(parse(object)).append("\n");
        }
        return "length = " + fields.length + "\n" + sb.toString();
    }

    private static String parse(Object obj) {
        if (obj == null)
            return "null";
        if(obj instanceof Object[])
            return Arrays.toString((Object[]) obj);
        if(obj instanceof int[])
            return Arrays.toString((int[]) obj);
        if(obj instanceof boolean[])
            return Arrays.toString((boolean[]) obj);
        if(obj instanceof byte[])
            return Arrays.toString((byte[]) obj);
        if(obj instanceof char[])
            return new String((char[]) obj);
        return obj.toString();
    }

    public static void printArgs(String TAG, Object params) {
        if (params == null) {
            LogUtil.d(TAG, "params = null");
            return;
        } else if (!(params instanceof Object[])) {
            LogUtil.d(TAG, "params = " + params);
        } else {
            LogUtil.d(TAG, "params = " + Arrays.toString((Object[]) params));
        }
    }



}

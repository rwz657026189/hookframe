package com.rwz.hook.utils;

import android.content.ContentValues;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ReflectUtil {

    public static Object getDeclaredField(Object obj, String fieldName) {
        return obj == null ? null : getDeclaredField(obj.getClass(), obj, fieldName);
    }

    public static Object getDeclaredField(Class cl, Object obj, String fieldName) {
        if (cl == null || obj == null || TextUtils.isEmpty(fieldName)) {
            return null;
        }
        try {
            Field field = cl.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 打印所有属性
     * @param obj
     * @param TAG
     */
    public static void printAllFiled(Object obj, String TAG) {
        if(obj != null)
            printAllFiled(obj.getClass().getDeclaredFields(), obj, TAG);
    }
    public static void printAllFiled(Class cl, Object obj, String TAG) {
        if(cl != null)
            printAllFiled(cl.getDeclaredFields(), obj, TAG);
    }
    public static void printAllFiled(Field[] fields, Object obj, String TAG) {
        if (fields == null || fields.length == 0 || obj == null) {
            LogUtil.d(TAG + " = " + obj);
            return;
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
            sb.append(TAG).append(",").append(f.getName()).append(" = ").append(parse(object)).append("\n");
        }
        LogUtil.d(TAG, "length = " + fields.length + "\n" + sb.toString());
    }

    private static void read(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            StringBuilder sb = new StringBuilder();
            byte[] buff = new byte[1024 * 8];
            int len;
            while ((len = fis.read(buff)) >= 0) {
                sb.append(new String(buff, 0, len, "utf-8"));
            }
            System.out.println("content = " + sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static String parse(Object obj) {
        if (obj == null)
            return "null";
        if(obj instanceof byte[]){
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            byte[] buff = (byte[]) obj;
            for (byte b : buff) {
                sb.append(b).append(",");
            }
            if(buff.length > 0)
                sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
            String s = sb.toString();
            return s;
        }
        if(obj instanceof char[])
            return new String((char[]) obj);
        return obj.toString();
    }

    public static void printArgs(String TAG, Object params) {
        if(params == null)
            return;
        if (!(params instanceof Object[])) {
            LogUtil.d(TAG, "params = " + params);
        } else {
            Object[] obj = (Object[]) params;
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (Object o : obj) {
                sb.append(o).append(",");
            }
            if(obj.length > 0)
                sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
            LogUtil.d(TAG, "params = " + sb.toString());
        }
    }


    public interface IAPI {
        @Deprecated
        public void getData(int pageCount, int pageIndex);
    }

    public static void main(String[] args) {
        IAPI iApi = (IAPI) Proxy.newProxyInstance(IAPI.class.getClassLoader(), new Class<?>[]{IAPI.class}, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Integer pageCount = (Integer) args[0];
                Integer pageIndex = (Integer) args[1];
                System.out.println("参数: " + pageCount + "," + pageIndex);
                System.out.println("方法名: " + method.getName());

                Annotation[] annotations = method.getAnnotations();
                System.out.println("注解：" + annotations[0].toString());

                return null;
            }
        });
        iApi.getData(5, 8);
    }



}

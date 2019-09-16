package com.rwz.hook.entity;

import com.rwz.hook.core.Constance;

/**
 * date： 2019/9/16 16:33
 * author： rwz
 * description：
 **/
public class LogEntity {

    private int Code;
    private String Message;

    public LogEntity(int code, String message) {
        Code = code;
        Message = message;
    }

    public static LogEntity createError(String message) {
        return new LogEntity(Constance.LOG_ERROR, message);
    }

    public static LogEntity create(String message) {
        return new LogEntity(Constance.LOG_NORMAL, message);
    }

}

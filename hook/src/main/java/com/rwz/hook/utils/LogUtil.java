package com.rwz.hook.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by rwz on 2016/12/9.
 */

public class LogUtil {

    private static final String TAG = "rwz";
    private static final String OkHttp = "OkHttp";
    private static final String TAG_CLASS; //类名
    private static final String TAG_METHOD;//方法名
    private static final String TAG_LINE; //行数
    private static final String SPACE = "   ";
    private static final String E_MSG_BEF = "【";
    private static final String E_MSG_AFT = " 】";

    public static boolean isDebug = true;

    static {
        TAG_CLASS = new Throwable().getStackTrace()[1].getFileName();
        TAG_METHOD = new Throwable().getStackTrace()[1].getMethodName();
        TAG_LINE = new Throwable().getStackTrace()[1].getLineNumber() + "";
    }

    public static void tag(String tag, Object... msg) {
        if (isDebug) {
            StringBuffer sb = new StringBuffer();
            if (msg != null) {
                for (Object o : msg) {
                    sb.append(o).append(",");
                }
            }
            if (!TextUtils.isEmpty(sb) && sb.length() > 0) {
//                printTop(tag);
                String print = sb.toString().substring(0, sb.length() - 1);
                Log.d(tag, "║" + SPACE + print);
//                printBottom(tag);
            }
        }
    }

    public static void ok(Object... msg) {
        tag(OkHttp, msg);
    }

    public static void d(Object... msg) {
        tag(TAG, msg);
    }
    public static void e(Object tips) {
        if (isDebug) {
//            printTop(TAG);
            Log.d(TAG, "║" + SPACE + getMethodNames() +
                    "\n║" + SPACE + tips);
//            printBottom(TAG);
        }
    }

    public static void e(Object tips , Object... msg) {
        if (isDebug) {
            StringBuilder sb = new StringBuilder();
            if (msg != null) {
                for (Object o : msg) {
                    sb.append(o).append(", ");
                }
            }
            String s = sb.toString();
            if (!TextUtils.isEmpty(s) && s.length() > 0) {
//                printTop(TAG);
//                Log.e(TAG, "║" + tips + E_MSG_BEF + s.substring(0, s.length() - 1) + E_MSG_AFT);
                String print = tips + E_MSG_BEF + s.substring(0, s.length() - 1) + E_MSG_AFT;
                Log.d(TAG, "║" + SPACE + getMethodNames() +
                        "\n║" + tips + SPACE + print);

//                printBottom(TAG);
            }
        }
    }

    private static void printTop(String TAG) {
        Log.d(TAG,"         ");
        Log.d(TAG, "╔════════════════════════════════════════════════════════════════════════════════════════");
    }

    private static void printBottom(String TAG) {
        Log.d(TAG, "╚════════════════════════════════════════════════════════════════════════════════════════");
        Log.d(TAG,"         ");
    }

    public static void dd(Object tips , Object... msg) {
        if (isDebug) {
            StringBuffer sb = new StringBuffer();
            if (msg != null) {
                for (Object o : msg) {
                    sb.append(o).append(",");
                }
            }
            if (!TextUtils.isEmpty(sb) && sb.length() > 0) {
                Log.d(TAG, tips + SPACE + sb.toString().substring(0,sb.toString().length() - 1));
            }
        }
    }

    private static String getTurnMsg() {
        StackTraceElement[] stackTraceElement = Thread.currentThread()
                .getStackTrace();
        int currentIndex = -1;
        for (int i = 0; i < stackTraceElement.length; i++) {
            if (stackTraceElement[i].getMethodName().compareTo("showLogCat") == 0)
            {
                currentIndex = i + 1;
                break;
            }
        }
        if (currentIndex == -1) {
            return "";
        }
        String fullClassName = stackTraceElement[currentIndex].getClassName();
        String className = fullClassName.substring(fullClassName
                .lastIndexOf(".") + 1);
        String methodName = stackTraceElement[currentIndex].getMethodName();
        String lineNumber = String
                .valueOf(stackTraceElement[currentIndex].getLineNumber());

        return "【 " + fullClassName + "." + methodName + "("
                + className + ".java:" + lineNumber + ") 】";
    }

    private static String getMethodNames() {
       /* String result = "at 【";
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        result += thisMethodStack.getClassName()+ "."; //  当前的类名（全名）
        result += thisMethodStack.getMethodName();
        result += "(" + thisMethodStack.getFileName();
        result += ":" + thisMethodStack.getLineNumber() + ") 】 ";
        return result;*/
        return "              ";
    }

    public static void stackTraces(String tag) {
        stackTraces(tag, 15, 3);
    }

    public static void stackTraces(String tag, int methodCount, int methodOffset) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String level = "";
        Log.d(TAG, SPACE + tag + "--------- logStackTraces start ----------");
        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + methodOffset;
            if (stackIndex >= trace.length) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append("|")
                    .append(' ')
                    .append(level)
                    .append(trace[stackIndex].getClassName())
                    .append(".")
                    .append(trace[stackIndex].getMethodName())
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].getFileName())
                    .append(":")
                    .append(trace[stackIndex].getLineNumber())
                    .append(")");
            level += "   ";
            Log.d(TAG, SPACE  + tag + builder.toString());
        }
        Log.d(TAG, SPACE  + tag + "--------- logStackTraces end ----------");
    }

}

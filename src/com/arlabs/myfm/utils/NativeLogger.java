package com.arlabs.myfm.utils;

/**
 *
 * @author AR-LABS
 */
public class NativeLogger {
    private static String currentClassName;
    
    public static void setClassName(Class<?> loggingClass) {
        currentClassName = loggingClass.getName();
    }
    
    public static void logInfo(String message) {
        System.out.printf("[%s] %s\n", NativeLogger.currentClassName, message);
    }
}

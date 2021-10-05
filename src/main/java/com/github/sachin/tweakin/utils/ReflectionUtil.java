package com.github.sachin.tweakin.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class ReflectionUtil {

    private static final Table<Class<?>,String,Field> fieldCache = HashBasedTable.create();
    private static final Table<Class<?>,String,Method> methodCache = HashBasedTable.create();
   
    
    public static Field getFieldCached(final Class<?> clazz, final String fieldName) {
        if (fieldCache.contains(clazz, fieldName)) {
            return fieldCache.get(clazz, fieldName);
        }
        try {
            final Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            fieldCache.put(clazz, fieldName, field);
            return field;
        } catch (final NoSuchFieldException e) {
            return null;
        }
    }


    public static Method getMethodCached(final Class<?> clazz,final String methodName){
        if(methodCache.contains(clazz, methodName)){
            return methodCache.get(clazz, methodName);
        }
        try {
            final Method method = clazz.getDeclaredMethod(methodName);
            method.setAccessible(true);
            methodCache.put(clazz, methodName, method);
            return method;
        } catch (final NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getMethodCached(final Class<?> clazz,final String methodName, boolean looping,Class<?>... parameterTypes){
        if(methodCache.contains(clazz, methodName)){
            return methodCache.get(clazz, methodName);
        }
        try {
            Method method = null;
            if(looping){
                for(Method m : clazz.getDeclaredMethods()){
                    if(m.getName().equals(methodName) && m.getParameterTypes().equals(parameterTypes)){
                        method = m;
                        method.setAccessible(true);
                        methodCache.put(clazz, methodName, method);
                        break;
                    }
                }
                return method;
            }
            else{
                method = clazz.getDeclaredMethod(methodName,parameterTypes);
                method.setAccessible(true);
                methodCache.put(clazz, methodName, method);
                return method;
            }
        } catch (final NoSuchMethodException e) {
            return null;
        }
    }


}

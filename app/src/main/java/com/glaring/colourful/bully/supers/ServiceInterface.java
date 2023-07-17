package com.glaring.colourful.bully.supers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法定义 Object ***(Object source, Method method, Object[] args) throws Throwable
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ServiceInterface {
    /**
     * 方法名称
     *
     * @return
     */
    String value();
}


package com.example.utils;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * 公共工具类
 * @author cenkang
 * @date 2020/4/5 - 11:37
 */
public class CommonUtils {

    /**
     * 复制一个对象的属性到另一个对象中去
     * @param dest 目标对象
     * @param original 原始对象
     */
    public static void copyProperties(Object dest, Object original) {
        if (original != null) {
            try {
                PropertyUtils.copyProperties(dest, original);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.example.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 公共工具类
 *
 * @author cenkang
 * @date 2020/4/5 - 11:37
 */
public class CommonUtils {

    /**
     * 复制一个对象的属性到另一个对象中去
     *
     * @param dest     目标对象
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

    /**
     * 设置cookie
     *
     * @param response HttpServletResponse响应
     * @param name     名称
     * @param value    值
     * @param path     路径
     *                 如果设置路径，这个路径即该工程下都可以访问该cookie。
     *                 不设置路径,在访问子路径时，会包含其父路径的Cookie，而在访问父路径时，不包含子路径的Cookie。
     * @return
     * @maxAge cookie的生命周期  以秒为单位
     */
    public static void addcookie(HttpServletResponse response, String name, String value, String path, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        if (StringUtils.isEmpty(path)) {
            cookie.setPath("/");
        } else {
            cookie.setPath(path);
        }
        if (maxAge > 0) cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 根据名字获取cookie
     *
     * @param request HttpServletRequest请求
     * @param name    键key
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        return cookieMap.containsKey(name) ? cookieMap.get(name) : null;
    }

    /**
     * 将cookie封装到Map里面
     *
     * @param request HttpServletRequest
     * @return
     */
    private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
}

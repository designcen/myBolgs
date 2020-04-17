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
     * @maxAge 生命周期（秒） 1.当expiry大于0时（一般设置为1），浏览器不仅会把cookie保存在浏览器内存中，还会把cookie保存到硬盘上，
     * 例：cookie.setMaxAge(60*60)，表示cookie将存活一个小时，无论是否重启浏览器、或是系统。
     * 2.expiry < 0：当expiry小于0时（一般设置为-1），表示只在浏览器内存中存活。一旦关闭浏览器窗口，那么cookie就会消失。
     * 3.expiry = 0：当expiry等于0时，表示cookie即不在内存中存活，也不在硬盘上存活，这样的cookie设置只有一个目的，
     * 那就是覆盖客户端原来的这个cookie，使其作废。
     */
    public static void addCookie(HttpServletResponse response, String name, String value, String path, int maxAge, Cookie cookie) {
        if (cookie == null) {
            cookie = new Cookie(name, value);
        }else{
            cookie.setValue(value);
        }
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

package com.example.shiro;

import cn.hutool.json.JSONUtil;
import com.example.common.lang.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 异步请求登录过滤器
 */
public class AuthFilter extends UserFilter {

    @Override
    protected void redirectToLogin(ServletRequest servletRequest, ServletResponse response) throws IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // 判断是否是异步请求，如果是要先登录
        String header = request.getHeader("X-Requested-With");
        if (header != null && "XMLHttpRequest".equals(header)) {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.isAuthenticated()) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().print(JSONUtil.toJsonStr(Result.fail("请先登录!")));
            }
        } else {

            super.redirectToLogin(request, response);
        }
    }
}

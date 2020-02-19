package com.example.shiro;

import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定义自己实现的realm域,并配置凭证匹配器
 * @author cenkang
 * @Date 2020/1/3 9:58
 */
@Slf4j
@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        AccountProfile principal = (AccountProfile) principalCollection.getPrimaryPrincipal();

        // 硬编码
        if(principal.getUsername().equals("admin") || principal.getId() == 1){
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.addRole("admin");
            return info;
        }

        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 注意token.getUsername()是指email
        AccountProfile profile = userService.login(token.getUsername(),String.valueOf(token.getPassword()));
        log.info("======================================>进入认证步骤");
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(profile,token.getCredentials(),getName());
        return info;
    }
}

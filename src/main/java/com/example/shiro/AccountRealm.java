package com.example.shiro;

import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
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

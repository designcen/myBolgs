package com.example.shiro;

import com.example.service.UserService;
import com.example.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
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
        AccountProfile principal = new AccountProfile();
        // 没有直接向下转型，原因是devtools类加载器与IDE的不同
        CommonUtils.copyProperties(principal,principalCollection.getPrimaryPrincipal());
        SimpleAuthorizationInfo info = null;
        // 根据博客项目的业务需要，只需要有一个管理员角色即可，所以本项目为了节省开发时间，省去了角色的维护，管理员是操作数据库得到的
        // 由于本项目只有无角色和admin角色两个角色，所以省去角色配置，此处采用硬编码设置角色
        // 规定昵称为admin且id为1的用户为管理员
        if(principal.getUsername().equals("admin") && principal.getId() == 1){
            info = new SimpleAuthorizationInfo();
            info.addRole("admin");
        }

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 注意token.getUsername()是指email
        AccountProfile profile = userService.login(token.getUsername(),String.valueOf(token.getPassword()));
        // 将用户信息放到session可供页面获取（注意脱敏）
        SecurityUtils.getSubject().getSession().setAttribute("profile", profile);
        log.info("======================================>进入认证步骤");
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(profile,token.getCredentials(),this.getName());
        return info;
    }
}

package com.example.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.common.lang.Result;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shiro.AccountProfile;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Result register(User user) {
        if (StringUtils.isEmpty(user.getEmail()) || StringUtils.isEmpty(user.getPassword()) || StringUtils.isEmpty(user.getUsername())) {
            return Result.fail("必要字段不能为空");
        }
        // 查看数据库中是否存在该邮箱
        User databaseUser = this.getOne(new QueryWrapper<User>().eq("email",user.getEmail()));
        if (databaseUser != null) {
            return Result.fail("邮箱已被注册");
        }
        String passMd5 = SecureUtil.md5(user.getPassword());
        databaseUser = new User();
        databaseUser.setEmail(user.getEmail());
        databaseUser.setPassword(passMd5);
        databaseUser.setCreated(new Date());
        databaseUser.setUsername(user.getUsername());
        databaseUser.setAvatar("/res/images/avatar/default.png");
        databaseUser.setPoint(0);

        return this.save(databaseUser) ? Result.succ("") : Result.fail("注册失败");
    }

    @Override
    public AccountProfile login(String username, String password) {
        log.info("======================================>进入用户登陆判断，获取用户信息步骤");
        User user = this.getOne(new QueryWrapper<User>().eq("email", username));
        if (user == null) {
            throw new UnknownAccountException("账户不存在");
        }
        if (!user.getPassword().equals(password)) {
            throw new IncorrectCredentialsException("密码错误");
        }
        //更新最后登录时间
        user.setLasted(new Date());
        this.updateById(user);
        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user,profile);
        return profile;
    }
}

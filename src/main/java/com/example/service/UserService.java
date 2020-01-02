package com.example.service;

import com.example.common.lang.Result;
import com.example.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
public interface UserService extends IService<User> {

    Result register(User user);
}

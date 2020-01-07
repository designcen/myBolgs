package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Post;
import com.example.entity.UserCollection;
import com.example.mapper.UserCollectionMapper;
import com.example.service.UserCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
@Service
public class UserCollectionServiceImpl extends ServiceImpl<UserCollectionMapper, UserCollection> implements UserCollectionService {

    @Autowired
    UserCollectionMapper userCollectionMapper;
    @Override
    public IPage<Post> paging(Page page, QueryWrapper queryWrapper) {
        return userCollectionMapper.selectPosts(page,queryWrapper);
    }
}

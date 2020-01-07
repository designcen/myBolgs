package com.example.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Post;
import com.example.entity.UserCollection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
public interface UserCollectionMapper extends BaseMapper<UserCollection> {

    IPage<Post> selectPosts(Page page,@Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}

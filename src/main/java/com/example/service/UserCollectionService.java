package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.UserCollection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vo.CollectionVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
public interface UserCollectionService extends IService<UserCollection> {

    IPage<CollectionVo> paging(Page page, QueryWrapper queryWrapper);
}

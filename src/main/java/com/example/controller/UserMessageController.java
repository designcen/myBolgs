package com.example.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.UserMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
@RestController
@RequestMapping("/message")
public class UserMessageController extends BaseController {

    @PostMapping("/nums/")
    public Object messageNums() {
        int count = userMessageService.count(new QueryWrapper<UserMessage>()
                .eq("to_user_id",getProfile())
                .eq("status",0));
        return MapUtil.builder().put("status",0).put("count",count).build();
    }
}

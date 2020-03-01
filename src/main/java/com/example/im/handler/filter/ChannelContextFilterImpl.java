package com.example.im.handler.filter;

import lombok.Data;
import org.tio.core.ChannelContext;
import org.tio.core.ChannelContextFilter;

/**
 * 通道过滤器
 * @author cenkang
 * @date 2020/2/29 - 20:33
 */
@Data
public class ChannelContextFilterImpl implements ChannelContextFilter {

    private ChannelContext currentContext;

    /**
     * 过滤掉自己，不需要发送给自己
     * @param channelContext
     * @return
     */
    @Override
    public boolean filter(ChannelContext channelContext) {
        if(currentContext.userid.equals(channelContext.userid)) {
            return false;
        }
        return true;
    }
}

<#include "../inc/layout.ftl"/>
<#include "../inc/centerBanner.ftl"/>
<#include "../inc/page.ftl"/>
<@layout "用户中心">

<div class="layui-container fly-marginTop fly-user-main">
    <@centerBanner 3></@centerBanner>

    <div class="fly-panel fly-panel-user" pad20>
        <div class="layui-tab layui-tab-brief" lay-filter="user" id="LAY_msg" style="margin-top: 15px;">
            <button class="layui-btn layui-btn-danger" id="LAY_delallmsg">清空全部消息</button>
            <div  id="LAY_minemsg" style="margin-top: 10px;">
                <!--<div class="fly-none">您暂时没有最新消息</div>-->
                <ul class="mine-msg">
                    <#list pageData.records as mess>
                    <li data-id="${mess.id}">
                    <blockquote class="layui-elem-quote">

                        <#if mess.type == 1>
                            <a href="/user/${mess.fromUserId}" target="_blank"><cite>${mess.fromUserName}</cite></a>评论了您的文章<a target="_blank" href="${base}/post/${mess.postId}"><cite>${mess.postTitle}</cite></a>
                        </#if>
                        <#if mess.type == 2>
                            系统消息：${mess.content}
                        </#if>
                        <#if mess.type == 3>
                            <a href="/user/${mess.fromUserId}" target="_blank"><cite>${mess.fromUserName}</cite></a>回复了您的评论<a target="_blank" href="${base}/post/${mess.postId}"><cite>${mess.postTitle}</cite></a>
                        </#if>
                        </blockquote>
                        <p><span>${mess.created?string("yyyy-MM-dd")}</span><a href="javascript:;" class="layui-btn layui-btn-small layui-btn-danger fly-delete">删除</a></p>
                    </li>
                    </#list>
                </ul>
                <div id="LAY_page">
                    <div style="text-align: center">
                            <@page pageData></@page>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

</@layout>
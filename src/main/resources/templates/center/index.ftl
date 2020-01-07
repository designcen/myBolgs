<#include "../inc/layout.ftl"/>
<#include "../inc/centerBanner.ftl"/>
<#include "../inc/page.ftl"/>

<@layout "用户中心">


<div class="layui-container fly-marginTop fly-user-main">
    <@centerBanner 1></@centerBanner>

    <div class="fly-panel fly-panel-user" pad20>
        <div class="layui-tab layui-tab-brief" lay-filter="user">
            <ul class="layui-tab-title" id="LAY_mine">
                <li data-type="mine-jie" lay-id="index" class="layui-this">我发的帖（<span>${pageData.total}</span>）</li>
                <li data-type="collection" data-url="/collection/find/" lay-id="collection">
                    <a href="${base}/center/collection">我收藏的帖</a>
                </li>
            </ul>
            <div class="layui-tab-content" style="padding: 20px 0;">
                <div class="layui-tab-item layui-show">
                    <ul class="mine-view jie-row">
                        <#list pageData.records as post>
                            <li>
                                <a class="jie-title" href="${base}/post/${post.id}" target="_blank">${post.title}</a>
                                <i>${post.created?string('yyyy-MM-dd')}</i>
                                <a class="mine-edit" href="${base}/post/edit/?id=${post.id}">编辑</a>
                                <em>${post.viewCount}阅/${post.commentCount}答</em>
                            </li>
                        </#list>
                    </ul>

                    <div id="LAY_page">
                        <div style="text-align: center">
                            <@page pageData></@page>
                        </div>
                    </div>
                </div>
                <div class="layui-tab-item">

                </div>
            </div>
        </div>
    </div>
</div>
</@layout>
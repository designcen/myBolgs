<#include "/inc/layout.ftl"/>

<@layout "用户中心">


<div class="layui-container fly-marginTop fly-user-main">

    <@centerBanner 1></@centerBanner>


    <div class="fly-panel fly-panel-user" pad20>
        <div class="layui-tab layui-tab-brief" lay-filter="user">
            <ul class="layui-tab-title" id="LAY_mine">
                <li data-type="mine-jie" lay-id="index">
                    <a href="${base}/center">我发的帖</a>
                </li>
                <li data-type="collection" data-url="/collection/find/" lay-id="collection" class="layui-this">我收藏的帖（<span>${pageData.total}</span>）</li>
            </ul>
            <div class="layui-tab-content" style="padding: 20px 0;">
                <div class="layui-tab-item">

                </div>
                <div class="layui-tab-item  layui-show">
                    <ul class="mine-view jie-row">
                        <#list pageData.records as post>
                        <li>
                            <a class="jie-title" href="${base}/post/${post.id}" target="_blank">${post.title}</a>
                            <i>${post.created?string('yyyy-MM-dd')}</i>
                        </li>
                        </#list>
                    </ul>
                    <div id="LAY_page1">
                        <div style="text-align: center">
                            <@page pageData></@page>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</@layout>
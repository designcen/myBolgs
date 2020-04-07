<#include "/inc/layout.ftl"/>
<@layout "用户中心">
<div class="layui-container fly-marginTop fly-user-main">
    <@centerBanner 1></@centerBanner>

    <div class="fly-panel fly-panel-user" pad20>
        <div class="layui-tab layui-tab-brief" lay-filter="user">
            <ul class="layui-tab-title" id="LAY_mine">
                <li data-type="mine-jie" lay-id="index" class="layui-this">我发的帖（<span>${postCount!}</span>）</li>
                <li data-type="collection" lay-id="collection"><a href="/user/find">我收藏的帖（<span>${collectionCount!}</span>）</a></li>
            </ul>
            <div class="layui-tab-content" style="padding: 20px 0;">
                <div class="layui-tab-item">
                    <ul class="mine-view jie-row">
                        <#list pageDate.records as post>
                            <li>
                                <#if post.recommend>
                                    <span class="fly-jing">精</span>
                                </#if>
                                <a href="${base}/post/${post.id}" class="jie-title"> ${post.title}</a>
                                <i>${post.created?string('yyyy-MM-dd')}</i>
                                <em class="layui-hide-xs">${post.viewCount}阅/${post.commentCount}答</em>
                            </li>
                        </#list>
                    </ul>
                    <div id="LAY_page"></div>
                </div>
            </div>
        </div>
    </div>
</div>

</@layout>
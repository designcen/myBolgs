<#include "/inc/layout.ftl"/>
<@layout "用户中心">
<div class="layui-container fly-marginTop fly-user-main">
    <#include "/inc/user/right.ftl" />

    <div class="fly-panel fly-panel-user" pad20>
        <div class="layui-tab layui-tab-brief" lay-filter="user">
            <ul class="layui-tab-title" id="LAY_mine">
                <li data-type="mine-jie" lay-id="index"><a href="/user/index">我发的帖（<span>${postCount!}</span>）</a></li>
                <li data-type="collection"  lay-id="collection" class="layui-this">我收藏的帖（<span>${collectionCount!}</span>）</li>
            </ul>
            <div class="layui-tab-content" style="padding: 20px 0;">
                <div class="layui-tab-item">
                    <ul class="mine-view jie-row">
                        <#list pageDate.records as post>
                            <li>
                                <a class="jie-title" href="../jie/detail.html" target="_blank">${post.title}</a>
                                <i>收藏于 ${post.collectionCreated?string("yyyy-MM-dd HH:mm:ss")}</i>
                            </li>
                        </#list>
                    </ul>
                    <div id="LAY_page1"></div>
                </div>
            </div>
        </div>
    </div>
</div>

</@layout>
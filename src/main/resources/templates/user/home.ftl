<#include "/inc/layout.ftl"/>

<@layout "我的主页">

<div class="fly-home fly-panel" style="background-image: url();">
    <img src="${user.avatar!}" alt="${user.username!}">
    <i class="iconfont icon-renzheng" title="Fly社区认证"></i>
    <h1>
    ${user.username!}
        <#if user.gender! == "0">
            <i class="iconfont icon-nan"></i>
        <#else>
            <i class="iconfont icon-nv"></i>
        </#if>
        <i class="layui-badge fly-badge-vip">VIP${user.vipLevel!}</i>
    </h1>

    <#--<p style="padding: 10px 0; color: #5FB878;">认证信息：layui 作者</p>-->

    <p class="fly-home-info">
        <i class="iconfont icon-kiss" title="飞吻"></i><span style="color: #FF7200;">${user.point!}飞吻</span>
        <i class="iconfont icon-shijian"></i><span>${user.created?string("yyyy-MM-dd")} 加入</span>
        <i class="iconfont icon-chengshi"></i><span>${user.city!}</span>
    </p>

    <p class="fly-home-sign">${user.sign!}</p>
</div>

<div class="layui-container">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md6 fly-home-jie">
            <div class="fly-panel">
                <h2 class="fly-panel-title">最近30天发表的文章</h2>
                <ul class="jie-row">
                    <#if pageData.records?? && (pageData.records?size > 0) >
                        <#list pageData.records as post>
                            <li>
                                <#if post.recommend!>
                                    <span class="fly-jing">精</span>
                                </#if>
                                <a href="${base}/post/${post.id}" class="jie-title"> ${post.title}</a>
                                <i>${post.created?string("yyyy-MM-dd")}</i>
                                <em class="layui-hide-xs">${post.viewCount}阅/${post.commentCount}答</em>
                            </li>
                        </#list>
                    <#else>
                        <div class="fly-none" style="min-height: 50px; padding:30px 0; height:auto;">
                            <i style="font-size:14px;">最近没有发表任何文章</i>
                        </div>
                    </#if>
                </ul>
                <div style="text-align: center">
                    <@page pageData></@page>
                </div>
            </div>
        </div>

    <#--请自行完成-->
        <div class="layui-col-md6 fly-home-da">
            <div class="fly-panel">
                <h3 class="fly-panel-title">最近的回答</h3>
                <ul class="home-jieda">
                    <#if pageData.records?? && (pageData.records?size > 0)>
                        <#list pageData.records as comment>
                            <li>
                                <p>
                                    <span>${comment.created?string("yyyy-MM-dd HH:mm:ss")}</span>在<a href="/post/${comment.postId!}" target="_blank">${comment.title!}</a>中回答：
                                </p>
                                <div class="home-dacontent">${comment.content!}</div>
                            </li>
                        </#list>
                    <#else >
                        <div class="fly-none" style="min-height: 50px; padding:30px 0; height:auto;">
                            <span>没有回答任何问题</span>
                        </div>
                    </#if>
                </ul>
                <div style="text-align: center">
                    <#-- 分页 -->
                    <@page pageData></@page>
                </div>
            </div>
        </div>
    </div>
</div>
</@layout>
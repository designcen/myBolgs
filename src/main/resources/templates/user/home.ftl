<#include "/inc/layout.ftl"/>

<@layout "我的主页">

<div class="fly-home fly-panel" style="background-image: url();">
    <img src="${user.avatar!}" alt="${user.username!}">
    <i class="iconfont icon-renzheng" title="Fly社区认证"></i>
    <h1>
    ${user.username!}
        <#if user.gender! == "1">
            <i class="iconfont icon-nan"></i>
        <#else>
            <i class="iconfont icon-nv"></i>
        </#if>
        <i class="layui-badge fly-badge-vip">VIP${user.vipLevel!}</i>
    </h1>

    <p style="padding: 10px 0; color: #5FB878;">认证信息：layui 作者</p>

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
                    <#if posts?? && (posts?size > 0) >
                        <#list posts as post>
                            <li>
                                <#if post.recommend>
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
            </div>
        </div>

    <#--请自行完成-->
        <div class="layui-col-md6 fly-home-da">
            <div class="fly-panel">
                <h3 class="fly-panel-title">最近的回答</h3>
                <ul class="home-jieda">
                    <li>
                        <p>
                            <span>1分钟前</span>
                            在<a href="" target="_blank">tips能同时渲染多个吗?</a>中回答：
                        </p>
                        <div class="home-dacontent">
                            尝试给layer.photos加上这个属性试试：
                            <pre>
full: true
</pre>
                            文档没有提及
                        </div>
                    </li>
                    <li>
                        <p>
                            <span>5分钟前</span>
                            在<a href="" target="_blank">在Fly社区用的是什么系统啊?</a>中回答：
                        </p>
                        <div class="home-dacontent">
                            Fly社区采用的是NodeJS。分享出来的只是前端模版
                        </div>
                    </li>

                    <!-- <div class="fly-none" style="min-height: 50px; padding:30px 0; height:auto;"><span>没有回答任何问题</span></div> -->
                </ul>
            </div>
        </div>
    </div>
</div>

</@layout>
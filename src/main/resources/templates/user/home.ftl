<#include "../inc/layout.ftl"/>

<@layout "用户中心">

<div class="fly-home fly-panel" style="background-image: url();">
    <img src="${user.avatar}" alt="${user.username}">
    <i class="iconfont icon-renzheng" title="Fly社区认证"></i>
    <h1>
        ${user.username}
        <i class="iconfont icon-nan"></i>
        <i class="layui-badge fly-badge-vip">VIP${user.vipLevel}</i>
    </h1>

    <p style="padding: 10px 0; color: #5FB878;">认证信息：layui 作者</p>

    <p class="fly-home-info">
        <i class="iconfont icon-kiss" title="飞吻"></i><span style="color: #FF7200;">66666 飞吻</span>
        <i class="iconfont icon-shijian"></i><span>${user.created?string("yyyy-MM-dd")} 加入</span>
        <i class="iconfont icon-chengshi"></i><span>来自杭州</span>
    </p>

    <p class="fly-home-sign">（${user.sign}）</p>

    <div class="fly-sns" data-user="">
        <a href="javascript:;" class="layui-btn layui-btn-primary fly-imActive" data-type="addFriend">加为好友</a>
        <a href="javascript:;" class="layui-btn layui-btn-normal fly-imActive" data-type="chat">发起会话</a>
    </div>

</div>

<div class="layui-container">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md6 fly-home-jie">
            <div class="fly-panel">
                <h2 class="fly-panel-title">${user.username} 最近发表的文章</h2>
                <ul class="jie-row">
                    <#list posts as post>
                    <li>
                        <#if post.recommend><span class="fly-jing">精</span></#if>
                        <a href="${base}/post/${post.id}" class="jie-title"> ${post.title}</a>
                        <i>${post.created?string("yyyy-MM-dd")}</i>
                        <em class="layui-hide-xs">${post.viewCount}阅/${post.commentCount}答</em>
                    </li>
                    </#list>
                    <!-- <div class="fly-none" style="min-height: 50px; padding:30px 0; height:auto;"><i style="font-size:14px;">没有发表任何求解</i></div> -->
                </ul>
            </div>
        </div>

    <#--请自行完成-->
        <div class="layui-col-md6 fly-home-da">
            <div class="fly-panel">
                <h3 class="fly-panel-title">贤心 最近的回答</h3>
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
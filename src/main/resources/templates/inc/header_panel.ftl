<#-- 头部导航栏-->
<div class="fly-panel fly-column">
    <div class="layui-container">
        <ul class="layui-clear">
            <li class="${(0 == currentCategoryId)?string('layui-hide-xs layui-this', '')}">
                <a href="/">首页</a>
            </li>
            <#list categorys as category>
                <li class="${(category.id == currentCategoryId)?string('layui-hide-xs layui-this', '')}">
                    <a href="${base}/category/${category.id}">${category.name}</a>
                </li>
            </#list>
            <li class="layui-hide-xs layui-hide-sm layui-show-md-inline-block"><span class="fly-mid"></span></li>
            <@shiro.user>
                <!-- 用户登入后显示 -->
                <li class="layui-hide-xs layui-hide-sm layui-show-md-inline-block"><a href="/user/index">我发表的贴</a></li>
                <li class="layui-hide-xs layui-hide-sm layui-show-md-inline-block"><a href="/user/find">我收藏的贴</a></li>
            </@shiro.user>
        </ul>

        <div class="fly-column-right layui-hide-xs">
            <span class="fly-search"><i class="layui-icon"></i></span>
            <a href="/post/add" class="layui-btn">发表新帖</a>
        </div>
        <@shiro.hasRole name="admin">
            <div class="fly-column-right layui-hide-xs">
                <span class="fly-search"><i class="layui-icon"></i></span>
                <a href="/admin/addCategory" class="layui-btn">添加专栏</a>
            </div>
        </@shiro.hasRole>
    </div>
</div>
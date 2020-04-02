<#include "/inc/layout.ftl"/>
<@layout "个人博客">
<div class="layui-container">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md8">
            <!-- 内容-->
            <div class="fly-panel" style="margin-bottom: 0;">
                <!-- 文章分类导航栏 -->
                <div class="fly-panel-title fly-filter">
                    <a href="" class="layui-this">综合</a>
                    <span class="fly-mid"></span>
                    <a href="">精华</a>
                    <span class="fly-mid"></span>
                    <a href="">置顶</a>
                    <span class="fly-filter-right layui-hide-xs">
                        <a href="" class="layui-this">按最新</a>
                        <span class="fly-mid"></span>
                        <a href="">按热议</a>
                    </span>
                </div>
            <#-- 文章信息，标题，阅读量等 -->
                <ul class="fly-list">
                   <#list pageData.records as post>
                       <@listing post></@listing>
                   </#list>
                </ul>
                <div style="text-align: center">
                <#-- 分页 -->
                    <@page pageData></@page>
                </div>
            </div>
        </div>
        <!--侧边栏的签到等-->
        <#include "/inc/right.ftl" />
    </div>
</div>
</@layout>
<#include "/inc/layout.ftl"/>
<@layout "首页">
<div class="layui-container">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md8">
            <!-- 置顶-->
            <div class="fly-panel">
                <div class="fly-panel-title fly-filter">
                    <a>置顶</a>
                    <a href="#signin" class="layui-hide-sm layui-show-xs-block fly-right" id="LAY_goSignin" style="color: #FF5722;">去签到</a>
                </div>
                <@posts pn=1 size=5 categoryId=1>
                     <ul class="fly-list">
                         <#list results.records as post>
                             <@listing post></@listing>
                         </#list>
                     </ul>
                </@posts>
            </div>
            <!-- 内容-->
            <div class="fly-panel" style="margin-bottom: 0;">
                <!-- 文章分类导航栏 -->
                <div class="fly-panel-title fly-filter">
                    <a href="" class="layui-this">综合</a>
                    <span class="fly-mid"></span>
                    <a href="">未结</a>
                    <span class="fly-mid"></span>
                    <a href="">已结</a>
                    <span class="fly-mid"></span>
                    <a href="">精华</a>
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
<#include "/inc/layout.ftl"/>

<@layout "搜索-${q}">

<#include "/inc/header-panel.ftl" />

<div class="layui-container">
  <div class="layui-row layui-col-space15">
    <div class="layui-col-md8">
      <div class="fly-panel" style="margin-bottom: 0;">

          <div class="fly-panel-title fly-filter">
              <span>共检索到关键字“${q}” - ${pageData.total}条记录</span>
          </div>

        <ul class="fly-list">
          <#list pageData.records as post>
            <@listing post></@listing>
          </#list>
        </ul>

        <#if !pageData.records??>
            <div class="fly-none">没有相关数据</div>
        </#if>
    
        <div style="text-align: center">
          <@page pageData></@page>
        </div>

      </div>
    </div>

    <#include "/inc/right.ftl" />

  </div>
</div>


</@layout>
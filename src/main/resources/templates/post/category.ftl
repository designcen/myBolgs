<#include "../inc/listing.ftl">
<#include "../inc/page.ftl">
<ul class="fly-list">
  <#list pageData.records as post>
    <@listing post></@listing>
  </#list>
</ul>

<!-- <div class="fly-none">没有相关数据</div> -->

<div style="text-align: center">
  <@page pageData></@page>
</div>
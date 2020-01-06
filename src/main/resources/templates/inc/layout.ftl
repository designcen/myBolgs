<#-- Layout 全局页面-->
<#macro layout title>
<!DOCTYPE html>
<html lang="zh-CN">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!--[if IE]>
        <meta http-equiv='X-UA-Compatible' content='IE=edge,chrome=1'/>
        <![endif]-->

        <link rel="stylesheet" href="${base}/res/layui/css/layui.css">
        <link rel="stylesheet" href="${base}/res/css/global.css">
        <#-- 全局css\js-->
        <script src="${base}/res/layui/layui.js"></script>
        <script src="${base}/res/js/jquery.min.js"></script>
        <title>${title?default('个人博客')}</title>
    </head>
    <body>
    <#-- 头-->
    <#include "header.ftl" />
    <#-- 头部导航栏-->
    <#include "header_panel.ftl" />
    <#-- 中间部分-->
    <#nested>
    <#-- 尾-->
    <#include "footer.ftl" />
    </body>
</html>
</#macro>
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
        <#-- 宏引用-->
        <#include "/inc/common.ftl" />
        <#-- 头-->
        <#include "/inc/header.ftl" />
        <#-- 头部导航栏-->
        <#include "/inc/header_panel.ftl" />
        <#-- 中间部分-->
        <#nested>
        <#-- 尾-->
        <#include "/inc/footer.ftl" />
    </body>
</html>
<script>
    layui.cache.page = 'jie';
    layui.cache.user = {
        username: '${profile.username!"游客"}'
        , uid: ${profile.id!'-1'}
        , avatar: '${profile.avatar!"/res/images/avatar/00.jpg"}'
        , experience: 0
        , sex: '${profile.sex!'未知'}'
    };
    layui.config({
        version: "3.0.0"
        , base: '/res/mods/'
    }).extend({
        fly: 'index'
    }).use('fly').use('jie').use('user');
</script>

<script>
    function showTips(count) {
        var msg = $('<a class="fly-nav-msg" href="javascript:;">' + count + '</a>');

        var elemUser = $('.fly-nav-user');
        elemUser.append(msg);
        msg.on('click', function () {
            location.href = '/center/message/';
        });
        layer.tips('你有 ' + count + ' 条未读消息', msg, {
            tips: 3
            , tipsMore: true
            , fixed: true
        });
        msg.on('mouseenter', function () {
            layer.closeAll('tips');
        })
    }

    $(function () {
        var elemUser = $('.fly-nav-user');

        if (layui.cache.user.uid !== -1 && elemUser[0]) {
            // 建立端点链接
            var socket = new SockJS("/websocket");
            // 切换成stomp文本传输协议传输内容
            stompClient = Stomp.over(socket);
            // 建立连接触发的方法
            stompClient.connect({}, function (frame) {
                //subscribe订阅这个消息队列
                // 当后端往/user/{userId}/messCount里面发送消息时候，当前用户就能接收到消息
                stompClient.subscribe('/user/' + ${profile.id} +'/messCount', function (res) {
                    // 渲染新消息通知的样式
                    // res.body是返回的内容
                    showTips(res.body);
                })
            })
        }
    });
</script>

</#macro>
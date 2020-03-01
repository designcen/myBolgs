layui.use('layim', function (layim) {

    var $ = layui.jquery;

    //初始化layim
    layim.config({
        brief: true //是否简约模式（如果true则不显示主面板）
        ,voice: false
        ,members: {
            url: '/chat/getMembers'
        },
        chatLog: layui.cache.dir + 'css/modules/layim/html/chatlog.html'
    });

    //建立ws链接，监听消息
    var tiows = new tio.ws($, layim);

    //初始化群聊离线信息
    tiows.initHistroyMess();

    //打开群聊窗口并初始化个人信息
    tiows.openChatWindow();

    tiows.connect();

    //发送消息
    layim.on('sendMessage', function(res){
        tiows.sendChatMessage(res);
    });
});




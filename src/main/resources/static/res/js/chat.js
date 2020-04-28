layui.use('layim', function (layim) {

    var $ = layui.jquery;

    // 初始化layim
    layim.config({
        // 是否简约模式（如果true则不显示主面板）
        brief: true
        // 设定消息提醒的声音文件（所在目录：./layui/css/modules/layim/voice/）若不开启，设置 false 即可
        ,voice: false
        // 用于设定主面板右偏移量。该参数可避免遮盖你页面右下角已经的bar
        ,right: "100"
        // 查看群员接口
        ,members: {
            url: '/chat/getMembers'
        }
        // 聊天记录页面地址
        ,chatLog: layui.cache.dir + 'css/modules/layim/html/chatlog.html'
    });

    //建立ws链接，监听消息
    var tiows = new tio.ws($, layim);

    // 初始化群聊离线信息
    tiows.initHistroyMess();

    // 打开群聊窗口并初始化个人信息
    tiows.openChatWindow();

    // 建立WebSocket通讯
    tiows.connect();

    //发送消息
    layim.on('sendMessage', function(res){
        tiows.sendChatMessage(res);
    });
});




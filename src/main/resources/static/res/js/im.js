if (typeof(tio) == "undefined") {
    tio = {};
}
tio.ws = {};
tio.ws = function ($, layim) {

    this.heartbeatTimeout = 5000; // 心跳超时时间，单位：毫秒
    this.heartbeatSendInterval = this.heartbeatTimeout / 2

    var self = this;
    var params;
    this.connect = function() {
        var socket = new WebSocket('ws://localhost:9326?' + params);
        self.socket = socket;

        socket.onopen = function (res) {
            console.log("open-----------> res");
            self.lastInteractionTime(new Date().getTime());

            //建立心跳
            self.ping();
        }

        socket.onmessage = function (res) {
            //emit即为发出的事件名，用于区分不同的消息
            console.log("收到信息啦------>" + res.data);

            var msgBody = eval('(' + res.data + ')');

            if(msgBody.emit === 'chatMessage'){
                layim.getMessage(msgBody.mess);
            }
            self.lastInteractionTime(new Date().getTime());
        }

        socket.onclose = function (res) {
            console.log("close-----------> res");
            //尝试重连
            self.reconn();
        }
        return self;
    }

    this.lastInteractionTime = function () {
        // debugger;
        if (arguments.length == 1) {
            this.lastInteractionTimeValue = arguments[0]
        }
        return this.lastInteractionTimeValue
    }

    this.ping = function () {
        console.log("------------->准备心跳中~");


        //建立一个定时器，定时心跳
        self.pingIntervalId = setInterval(function () {
            var iv = new Date().getTime() - self.lastInteractionTime(); // 已经多久没发消息了

            // debugger;

            // 单位：秒
            if ((self.heartbeatSendInterval + iv) >= self.heartbeatTimeout) {
                self.socket.send(JSON.stringify({
                    type: 'pingMessage'
                    ,data: 'ping'
                }))
                console.log("------------->心跳中~")
            }
        }, self.heartbeatSendInterval)
    };

    this.reconn = function () {
        // 先删除心跳定时器
        clearInterval(self.pingIntervalId);

        // 然后尝试重连
        self.connect();
    };


    this.sendChatMessage = function(res) {
        //监听到上述消息后，就可以轻松地发送socket了
        this.socket.send(JSON.stringify({
            type: 'chatMessage' //随便定义，用于在服务端区分消息类型
            ,data: res
        }));
    }

    var mine;
    var group;
    this.openChatWindow = function () {
        this.initChatData();
        var cache = layui.layim.cache();
        cache.mine = mine;
        layim.chat(group);
        layim.setChatMin(); //收缩聊天面板
    }




    this.initChatData = function () {
        $.ajax({
            url: '/chat/getMineAndGroupData',
            async: false,
            success: function (data) {
                mine = data.data.mine;
                group = data.data.group;
                params = 'userId=' + mine.id + '&username=' + mine.username + '&avatar=' + mine.avatar;
            }
        });
    }

    this.initHistroyMess = function () {
        localStorage.clear();

        $.ajax({
            url: '/chat/getGroupHistoryMsg',
            success: function (res) {
                var data = res.data;
                if(data.length < 1) {
                    return;
                }

                for (var i in data){
                    layim.getMessage(data[i]);
                }
            }
        });
    }

}
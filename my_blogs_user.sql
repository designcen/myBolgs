CREATE TABLE my_blogs.user
(
    id bigint(32) PRIMARY KEY NOT NULL COMMENT '主键ID' AUTO_INCREMENT,
    username varchar(128) NOT NULL COMMENT '昵称',
    password varchar(128) NOT NULL COMMENT '密码',
    email varchar(64) COMMENT '邮件',
    mobile varchar(32) COMMENT '手机电话',
    point int(11) unsigned DEFAULT '0' NOT NULL COMMENT '积分',
    sign varchar(255) COMMENT '个性签名',
    gender varchar(16) COMMENT '性别',
    wechat varchar(32) COMMENT '微信号',
    vip_level int(32) COMMENT 'vip等级',
    birthday datetime COMMENT '生日',
    avatar varchar(256) NOT NULL COMMENT '头像',
    post_count int(11) unsigned DEFAULT '0' NOT NULL COMMENT '内容数量',
    comment_count int(11) unsigned DEFAULT '0' NOT NULL COMMENT '评论数量',
    status tinyint(2) DEFAULT '0' NOT NULL COMMENT '状态',
    lasted datetime COMMENT '最后的登陆时间',
    created datetime NOT NULL COMMENT '创建日期',
    modified datetime COMMENT '最后修改时间',
    city varchar(50) COMMENT '城市'
);
CREATE UNIQUE INDEX username ON my_blogs.user (username);
CREATE UNIQUE INDEX email ON my_blogs.user (email);
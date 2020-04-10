CREATE TABLE my_blogs.user_message
(
    id bigint(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    from_user_id bigint(20) NOT NULL COMMENT '发送消息的用户ID',
    to_user_id bigint(20) NOT NULL COMMENT '接收消息的用户ID',
    post_id bigint(20) COMMENT '消息可能关联的帖子',
    comment_id bigint(20) COMMENT '消息可能关联的评论',
    content text COMMENT '消息内容',
    type tinyint(2) COMMENT '消息类型',
    created datetime NOT NULL COMMENT '创建时间',
    modified datetime COMMENT '修改时间',
    status int(11) COMMENT '状态'
);
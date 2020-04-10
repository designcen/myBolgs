CREATE TABLE my_blogs.user_action
(
    id varchar(32) DEFAULT '' PRIMARY KEY NOT NULL,
    user_id varchar(32) COMMENT '用户ID',
    action varchar(32) COMMENT '动作类型',
    point int(11) COMMENT '得分',
    post_id varchar(32) COMMENT '关联的帖子ID',
    comment_id varchar(32) COMMENT '关联的评论ID',
    created datetime COMMENT '创建时间',
    modified datetime COMMENT '修改时间'
);
CREATE TABLE my_blogs.comment
(
    id bigint(32) PRIMARY KEY NOT NULL COMMENT '主键ID' AUTO_INCREMENT,
    content longtext NOT NULL COMMENT '评论的内容',
    parent_id bigint(32) COMMENT '回复的评论ID',
    post_id bigint(32) NOT NULL COMMENT '评论的文章ID',
    user_id bigint(32) NOT NULL COMMENT '评论的用户ID',
    vote_up int(11) unsigned DEFAULT '0' NOT NULL COMMENT '“顶”的数量',
    vote_down int(11) unsigned DEFAULT '0' NOT NULL COMMENT '“踩”的数量',
    level tinyint(2) unsigned DEFAULT '0' NOT NULL COMMENT '置顶等级',
    status tinyint(2) COMMENT '评论的状态',
    created datetime NOT NULL COMMENT '评论的时间',
    modified datetime COMMENT '评论的更新时间'
);
CREATE TABLE my_blogs.post
(
    id bigint(32) PRIMARY KEY NOT NULL COMMENT '主键ID' AUTO_INCREMENT,
    title varchar(128) NOT NULL COMMENT '标题',
    content longtext NOT NULL COMMENT '内容',
    edit_mode varchar(32) DEFAULT '0' NOT NULL COMMENT '编辑模式：html可视化，markdown ..',
    category_id bigint(32) COMMENT '标签栏分类id',
    user_id bigint(32) COMMENT '用户ID',
    vote_up int(11) unsigned DEFAULT '0' NOT NULL COMMENT '支持人数',
    vote_down int(11) unsigned DEFAULT '0' NOT NULL COMMENT '反对人数',
    view_count int(11) unsigned DEFAULT '0' NOT NULL COMMENT '访问量',
    comment_count int(11) DEFAULT '0' NOT NULL COMMENT '评论数量',
    recommend tinyint(1) COMMENT '是否为精华',
    level tinyint(2) DEFAULT '0' NOT NULL COMMENT '置顶等级',
    status tinyint(2) COMMENT '状态',
    created datetime COMMENT '创建日期',
    modified datetime COMMENT '最后更新日期'
);
CREATE TABLE my_blogs.user_collection
(
    id bigint(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_id bigint(20) NOT NULL COMMENT '收藏者的id',
    post_id bigint(20) NOT NULL COMMENT '文章id',
    post_user_id bigint(20) NOT NULL COMMENT '文章作者id',
    created datetime NOT NULL COMMENT '创建时间',
    modified datetime NOT NULL COMMENT '修改时间'
);
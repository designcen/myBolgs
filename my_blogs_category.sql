CREATE TABLE my_blogs.category
(
    id bigint(32) PRIMARY KEY NOT NULL COMMENT '主键ID' AUTO_INCREMENT,
    name varchar(512) NOT NULL COMMENT '标题',
    content text COMMENT '内容描述',
    summary text COMMENT '摘要',
    icon varchar(128) COMMENT '图标',
    post_count int(11) unsigned DEFAULT '0' COMMENT '该分类的内容数量',
    order_num int(11) COMMENT '排序编码',
    parent_id bigint(32) unsigned COMMENT '父级分类的ID',
    meta_keywords varchar(256) COMMENT 'SEO关键字',
    meta_description varchar(256) COMMENT 'SEO描述内容',
    created datetime COMMENT '创建日期',
    modified datetime COMMENT '更新日期'
);
INSERT INTO my_blogs.category (id, name, content, summary, icon, post_count, order_num, parent_id, meta_keywords, meta_description, created, modified) VALUES (1, '文章', '', null, null, 0, null, null, null, null, null, null);
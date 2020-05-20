package com.example.search.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * @author cenkang
 * @Date 2020/2/19 11:04
 */
@Document(indexName = "post", type = "post")
@Data
public class PostDocument implements Serializable {
    @Id
    private Long id;

    // 中文分词器 -> https://github.com/medcl/elasticsearch-analysis-ik
    // analyzer:索引时的策略，即插入数据时创建索引的策略
    // searchAnalyzer:搜索时的策略，即查询数据时
    // ik_max_word:细粒度
    // ik_smart:粗粒度
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;

    private Long authorId;

    @Field(type = FieldType.Text)
    private String authorName;

    private String authorVip;

    private String authorAvatar;

    private Long categoryId;

    @Field(type = FieldType.Keyword)
    private String categoryName;

    private Boolean recommend;

    private Integer level;

    @Field(type = FieldType.Text)
    private String tags;

    private Integer commentCount;

    private Integer viewCount;

    @Field(type = FieldType.Date)
    private Date created;

}

package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Post;
import com.example.search.common.IndexKey;
import com.example.search.common.PostMqIndexMessage;
import com.example.search.model.PostDocument;
import com.example.search.repository.PostRepository;
import com.example.service.PostService;
import com.example.service.SearchService;
import com.example.vo.PostVo;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PostService postService;

    /**
     * 两种方式使用elasticsearch
     * 1、spring jpa的repository
     * 2、spring的template
     */
    @Autowired
    ElasticsearchTemplate esTemplate;

    @Override
    public Page<PostDocument> query(Pageable pageable, String keyword) {

        //多个字段匹配，只要满足一个即可返回结果
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyword,
                IndexKey.POST_TITLE,
                IndexKey.POST_DESCRIPTION,
                IndexKey.POST_AUTHOR,
                IndexKey.POST_CATEGORY,
                IndexKey.POST_TAGS
        );

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQueryBuilder)
                .withPageable(pageable)
                .build();

        Page<PostDocument> page = postRepository.search(searchQuery);

        log.info("查询 - {} - 的得到结果如下-------------> {}个查询结果，一共{}页",
                keyword, page.getTotalElements(), page.getTotalPages());

        return page;
    }


    /**
     * 异步创建或者更新
     */
    public void createOrUpdateIndex(PostMqIndexMessage message) {
        long postId = message.getPostId();

        PostVo postVo = postService.selectOne(new QueryWrapper<Post>().eq("p.id", postId));

        log.info("需要更新的post --------> {}",  postVo.toString());

        if(PostMqIndexMessage.CREATE.equals(message.getType())) {
            if(postRepository.existsById(postId)) {
                this.removeIndex(message);
            }
        }

        PostDocument postDocument = new PostDocument();
        modelMapper.map(postVo, postDocument);

        PostDocument saveDoc = postRepository.save(postDocument);

        log.info("es 索引更新成功！ --> {}" , saveDoc.toString());

    }

    @Override
    public void removeIndex(PostMqIndexMessage message) {
        long postId = message.getPostId();

        postRepository.deleteById(postId);

        log.info("es 索引删除成功！ --> {}" , message.toString());
    }

    @Override
    public int initEsIndex(List<PostVo> datas) {
        if(datas == null || datas.isEmpty()) return 0;

        List<PostDocument> docs = new ArrayList<>();

        for(PostVo vo : datas) {
            PostDocument doc = modelMapper.map(vo, PostDocument.class);
            docs.add(doc);
        }

        //批量保存
        postRepository.saveAll(docs);

        return docs.size();
    }

}

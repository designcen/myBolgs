package com.example.search.repository;

import com.example.search.model.PostDocument;
import org.springframework.boot.actuate.autoconfigure.metrics.export.elastic.ElasticProperties;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author cenkang
 * @Date 2020/2/19 11:02
 */
public interface PostRepository extends ElasticsearchRepository<PostDocument,Long> {
}

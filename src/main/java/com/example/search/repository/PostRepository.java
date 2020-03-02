package com.example.search.repository;

import com.example.search.model.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cenkang
 * @Date 2020/2/19 11:02
 */
@Repository
public interface PostRepository extends ElasticsearchRepository<PostDocument,Long> {
}

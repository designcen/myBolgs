package com.example.service;

import com.example.search.common.PostMqIndexMessage;
import com.example.search.model.PostDocument;
import com.example.vo.PostVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchService {

    Page<PostDocument> query(Pageable pageable, String keyword);

    void removeIndex(PostMqIndexMessage message);

    void createOrUpdateIndex(PostMqIndexMessage message);

    int initEsIndex(List<PostVo> data);
}

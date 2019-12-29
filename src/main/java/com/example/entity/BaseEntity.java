package com.example.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author cenkang
 * @date 2019/12/26 - 22:17
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Date created;
    private Date modified;
}

package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @author cenkang
 * @date 2019/12/26 - 22:18
 */
public class BaseController {

    @Autowired
    HttpServletRequest req;
}

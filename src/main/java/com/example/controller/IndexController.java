package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author cenkang
 * @date 2019/12/26 - 23:42
 */
@Controller
public class IndexController extends BaseController {

    @RequestMapping({"","/","/index"})
    public String index(){
        return "index";
    }
}

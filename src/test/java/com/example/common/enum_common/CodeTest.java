package com.example.common.enum_common;

import cn.hutool.crypto.SecureUtil;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author cenkang
 * @date 2019/12/28 - 15:40
 */
public class CodeTest {

    public static void main(String[] args) {
        System.out.println(SecureUtil.md5("#@&"));
    }
}

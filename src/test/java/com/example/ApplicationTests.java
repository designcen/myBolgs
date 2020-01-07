package com.example;

import cn.hutool.crypto.SecureUtil;
import com.example.entity.User;
import com.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class ApplicationTests {

	@Autowired
	UserService userService;

	@Test
	public void contextLoads() {

	}
}

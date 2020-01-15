package com.example.controller;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.lang.Result;
import com.example.entity.User;
import com.google.code.kaptcha.Producer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 首页
 * @author cenkang
 * @date 2019/12/26 - 23:42
 */
@Controller
public class IndexController extends BaseController {
    private static final String KAPTCHA_SESSION_KEY = "KAPTCHA_SESSION_KEY";

    @Autowired
    private Producer producer;

    /**
     * 首页跳转
     * @return
     */
    @RequestMapping({"","/","/index"})
    public String index(){
        IPage pageData = postService.paging(getPage(),null,null,null,null,"created");
        req.setAttribute("pageData", pageData);
        return "index";
    }

    /**
     * 生成验证码
     * @param response http响应
     * @throws IOException
     */
    @GetMapping("/captcha.jpg")
    public void captcha(HttpServletResponse response) throws IOException{
        response.setHeader("Cache-Control", "no-store, no-cache");
        // 设置响应为图片形式
        response.setContentType("image/jpeg");
        // 生成文字验证码
        String text = producer.createText();
        // 生成图片验证码
        BufferedImage image = producer.createImage(text);
        // 把验证码存到shiro的session中，等待验证时使用
        SecurityUtils.getSubject().getSession().setAttribute(KAPTCHA_SESSION_KEY,text);
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image,"jpg",outputStream);
    }

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(){
        return "auth/register";
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:/";
    }

    @PostMapping("/login")
    @ResponseBody
    public Result doLogin(String email,String password){
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            return Result.fail("用户名或密码不能为空");
        }
        AuthenticationToken token = new UsernamePasswordToken(email, SecureUtil.md5(password));
        try {
            // 尝试登陆，将会调用realm的认证方法
            SecurityUtils.getSubject().login(token);

        }catch (AuthenticationException e){
            if (e instanceof UnknownAccountException) {
                return  Result.fail("用户不存在");
            }else if (e instanceof LockedAccountException) {
                return Result.fail("用户被禁用");
            }else if (e instanceof IncorrectCredentialsException) {
                return Result.fail("密码错误");
            }else {
                return Result.fail("用户认证失败");
            }
        }
        return Result.succ("登陆成功",null,"/");
    }

    @PostMapping("/register")
    @ResponseBody
    public Result doRegister (User user,String captcha,String repassword){
        String kaptcha = (String)SecurityUtils.getSubject().getSession().getAttribute(KAPTCHA_SESSION_KEY);
        if (!kaptcha.equalsIgnoreCase(captcha)) {
            return Result.fail("验证码不正确");
        }
        if (repassword == null || !repassword.equals(user.getPassword())) {
            return Result.fail("两次输入密码不一致");
        }
        Result result = userService.register(user);
        result.setAction("/login"); // 注册成功后跳转的页面
        return result;
    }

}

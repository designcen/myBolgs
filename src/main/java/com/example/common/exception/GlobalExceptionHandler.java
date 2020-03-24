package com.example.common.exception;

import cn.hutool.json.JSONUtil;
import com.example.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 定义全局异常处理
 * ，@ControllerAdvice 表示定义全局控制器异常处理
 * ，@ExceptionHandler 表示针对性异常处理，可对每种异常针对性处理。
 *
 * @author cenkang
 * @date 2019/12/28 - 13:35
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler (HttpServletRequest request, HttpServletResponse response,Exception e){
        log.error("===================>捕捉到全局异常",e);
        // 判断是否是ajax请求
        if (request.getHeader("accept").contains("application/json")
                || (request.getHeader("X-Requested-With") != null
                && request.getHeader("X-Requested-With").contains("XMLHttpRequest"))) {
            try {
                System.out.println(e.getMessage());
                Result result = Result.fail(e.getMessage(),"some error data");
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSONUtil.toJsonStr(result));
                writer.flush();
            }catch (IOException i){
                i.printStackTrace();
            }
            return null;
        }
        if (e instanceof MyException){
            // ....
            System.out.println("=======================================================");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception",e);
        modelAndView.addObject("message",e.getMessage());
        modelAndView.addObject("url",request.getRequestURL());
        modelAndView.setViewName("error");
        return modelAndView;
    }

    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public Result jsonErrorHandler (HttpServletRequest request, MyException e){
        return Result.fail(e.getMessage(),"some error data about MyExcetion");
    }
}

package com.example.common.exception;

import com.example.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 定义全局异常处理，@ControllerAdvice表示定义全局控制器异常处理
 * ，@ExceptionHandler表示针对性异常处理，可对每种异常针对性处理。
 *
 * @author cenkang
 * @date 2019/12/28 - 13:35
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler (HttpServletRequest request,Exception e){
        log.error("===================>捕捉到全局异常",e);

        if (e instanceof MyException){
            // ....
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
        return Result.fail(e.getMessage(),"some error data");
    }
}

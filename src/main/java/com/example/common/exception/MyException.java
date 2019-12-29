package com.example.common.exception;

import com.example.common.enum_common.Code;

/**
 * 自定义异常
 * 需要继承RuntimeException，这样涉及到事务时候才会有回滚。
 * MyException将作为我们系统catch到错误时候报出来的异常。
 * @author cenkang
 * @date 2019/12/28 - 13:36
 */
public class MyException extends RuntimeException {
    // 错误编码
    private Code code;

    public MyException() {
    }

    public MyException(Code code) {
        this.code = code;
    }
    // 父类错误提示信息
    public MyException(String message) {
        super(message);
    }

    public MyException(String message, Code code) {
        super(message);
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }
}

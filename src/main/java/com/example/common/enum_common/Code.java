package com.example.common.enum_common;

/**
 *
 * 公共编码枚举
 * @author cenkang
 * @date 2019/12/28 - 14:06
 */
public enum Code {
    ZERO("零",0),MINUS("负一",-1);
    String message;
    Integer code;
    Code(String mesage,Integer code) {
        this.message = mesage;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }


    public Integer getCode() {
        return code;
    }

}

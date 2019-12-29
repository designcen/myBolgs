package com.example.common.enum_common;

/**
 * @author cenkang
 * @date 2019/12/28 - 15:40
 */
public class CodeTest {

    public static void main (String[] args){
        String message = Code.ZERO.getMessage();
        Integer code = Code.ZERO.getCode();
        System.out.println(message+"     "+code);
    }
}

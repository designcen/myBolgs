package com.example.common.lang;

import com.example.common.enum_common.Code;

import java.io.Serializable;

/**
 * 统一返回结果
 * @author cenkang
 * @Date 2019/12/28 - 13:57
 */
public class Result implements Serializable{
    // 是否成功，可用code表示（如0表示成功，-1表示异常）
    private Code code;
    // 结果消息
    private String msg;
    // 结果数据
    private Object data;
    // 状态码，0表示成功
    private Integer status;
    // action表示路径
    private String action;
    public static Result succ(Object data){
        Result result = new Result();
        result.setCode(Code.ZERO);
        result.setData(data);
        result.setMsg("操作成功");
        return result;
    }

    public static Result succ(String mess,Object data){
        Result result = new Result();
        result.setCode(Code.ZERO);
        result.setData(data);
        result.setMsg(mess);
        return result;
    }
    public static Result succ(String mess,Object data,String action){
        Result result = new Result();
        result.setCode(Code.ZERO);
        result.setData(data);
        result.setMsg(mess);
        result.setAction(action);
        return result;
    }

    public static Result fail(String mess){
        Result result = new Result();
        result.setMsg(mess);
        result.setData(null);
        result.setCode(Code.MINUS);
        return result;
    }

    public static Result fail(String mess, Object data){
        Result result = new Result();
        result.setMsg(mess);
        result.setData(data);
        result.setCode(Code.MINUS);
        return result;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

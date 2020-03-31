package com.example.common.lang;

import com.example.common.enum_common.Code;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果
 * @author cenkang
 * @Date 2019/12/28 - 13:57
 */
@Data
public class Result implements Serializable {

    private Integer code;
    private Integer status;
    private String msg;
    private Object data;
    private String action;

    public static Result succ(Object data) {
        return succ("操作成功", data);
    }

    public static Result succ(String mess, Object data) {
        return succ(mess, data, null);
    }

    public static Result succ(String mess, Object data, String action) {
        Result m = new Result();
        m.setCode(0);
        m.setStatus(0);
        m.setData(data);
        m.setMsg(mess);
        m.setAction(action);

        return m;
    }

    public static Result fail(String mess) {
        return fail(mess, null);
    }

    public static Result fail(String mess, Object data) {
        Result m = new Result();
        m.setCode(-1);
        m.setStatus(-1);
        m.setData(data);
        m.setMsg(mess);

        return m;
    }
}

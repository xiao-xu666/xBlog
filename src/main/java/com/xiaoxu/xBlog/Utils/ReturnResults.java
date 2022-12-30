package com.xiaoxu.xBlog.Utils;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用的返回结果类
 *
 * @param <T>
 */
@Data
public class ReturnResults<T> implements Serializable {
    private Integer code; //编码：0成功，其它数字为失败
    private String msg; //错误信息
    private T data; //数据

    public static <T> ReturnResults<T> success(T object) {
        ReturnResults<T> rr = new ReturnResults<T>();
        rr.data = object;
        rr.code = 0;
        return rr;
    }
    public static <T> ReturnResults<T> error(String msg) {
        ReturnResults rr = new ReturnResults();
        rr.msg = msg;
        rr.code = 1;
        return rr;
    }
}

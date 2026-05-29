package com.qianyu.util;

import lombok.Data;

/**
 * 全局统一返回结果
 */
@Data
public class Result<T> {
    // 响应码（200成功，500失败）
    private Integer code;
    // 响应消息
    private String msg;
    // 响应数据
    private T data;

    // 成功响应（无数据）
    public static <T> Result<T> success(String msg) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg(msg);
        return result;
    }

    // 成功响应（有数据）
    public static <T> Result<T> success(String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    // 失败响应
    public static <T> Result<T> fail(String msg) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }
}
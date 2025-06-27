package com.example.backend.common;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {
    private int code;// 0 means success 1 means fail
    private T data;
    private String message;

    public Result(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }

}

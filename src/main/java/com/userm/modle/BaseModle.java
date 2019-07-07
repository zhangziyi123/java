package com.userm.modle;

/*
* json封装的基类，其他json的实体类必须继承该类
* */

public class BaseModle {

    public int code;
    public String msg;
    public int count;

    public BaseModle() {}

    public BaseModle(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseModle(int code, String msg, int count) {
        this.code = code;
        this.msg = msg;
        this.count = count;
    }
}

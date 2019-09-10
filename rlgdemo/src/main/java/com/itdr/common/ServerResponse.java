package com.itdr.common;

import java.io.Serializable;

public class ServerResponse<T> implements Serializable {

    //状态码
    private Integer status;
    //数据
    private T data;
    //返回的错误信息
    private String msg;



    /**
     * 获取成功的对象,包括成功状态码和数据
     */
    private ServerResponse(T data){
        //固定的状态码
        this.status = 200;
        this.data = data;
    }

    /**
     * 获取成功的对象,包括成功状态码和数据
     */
    private ServerResponse(Integer status,T data){
        this.status = status;
        this.data = data;
    }

    /**
     * 获取成功的对象,包括成功状态码，数据，状态信息
     */
    private ServerResponse(Integer status,T data,String msg){
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    /**
     * 获取失败的对象,包括失败状态码和数据
     */
    private ServerResponse(Integer status,String msg){
        this.status = status;
        this.msg = msg;
    }

    /**
     * 获取失败的对象,包括失败数据
     */
    private ServerResponse(String msg){
        //固定的状态码
        this.status = 100;
        this.msg = msg;
    }




    //成功的时候返回状态码和成功获取的数据
    public static<T> ServerResponse successRS(Integer status,T data){
        return new ServerResponse(status,data);
    }
    //只传入数据
    public static<T> ServerResponse successRS(T data){
        return new ServerResponse(data);
    }
    //传入状态码，数据，信息
    public static<T> ServerResponse successRS(Integer status,T data,String msg){
        return new ServerResponse(status,data,msg);
    }


    //失败的时候返回状态码和失败的信息
    public static<T> ServerResponse defeateRS(Integer status,String msg){
        return new ServerResponse(status,msg);
    }
    //状态码
    public static<T> ServerResponse defeateRS(String msg){
        return new ServerResponse(msg);
    }
}

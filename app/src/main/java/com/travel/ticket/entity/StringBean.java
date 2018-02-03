package com.travel.ticket.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 李小凡 on 2018/2/1.
 */

public class StringBean implements Serializable {

    /**
     * code : string
     * msg : string
     */

    @SerializedName("code")
    private String code;
    @SerializedName("msg")
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

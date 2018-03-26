package com.travel.ticket.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lixiaofan on 2018/2/13.
 */

public class MineResult implements Serializable{

    /**
     * userId : test02
     * userName : 344
     * status : enable
     * mobile : 4443
     * operatorType : admin
     * portCoporationId : 10000000
     * role : sys_port
     * portCoporation : {"id":"10000000","name":"东北凯亚"}
     */

    @SerializedName("userId")
    private String userId;
    @SerializedName("userName")
    private String userName;
    @SerializedName("status")
    private String status;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("operatorType")
    private String operatorType;
    @SerializedName("portCoporationId")
    private String portCoporationId;
    @SerializedName("role")
    private String role;
    @SerializedName("portCoporation")
    private PortCoporationBean portCoporation;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getPortCoporationId() {
        return portCoporationId;
    }

    public void setPortCoporationId(String portCoporationId) {
        this.portCoporationId = portCoporationId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public PortCoporationBean getPortCoporation() {
        return portCoporation;
    }

    public void setPortCoporation(PortCoporationBean portCoporation) {
        this.portCoporation = portCoporation;
    }

    public static class PortCoporationBean {
        /**
         * id : 10000000
         * name : 东北凯亚
         */

        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

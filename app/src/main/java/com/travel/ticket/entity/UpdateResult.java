package com.travel.ticket.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lixiaofan on 2018/2/13.
 */

public class UpdateResult implements Serializable{

    /**
     * appName : string
     * createTime : 2018-02-13T08:50:23.033Z
     * upgradeURL : string
     * versionNo : string
     */

    @SerializedName("appName")
    private String appName;
    @SerializedName("createTime")
    private String createTime;
    @SerializedName("upgradeURL")
    private String upgradeURL;
    @SerializedName("versionNo")
    private String versionNo;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpgradeURL() {
        return upgradeURL;
    }

    public void setUpgradeURL(String upgradeURL) {
        this.upgradeURL = upgradeURL;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }
}

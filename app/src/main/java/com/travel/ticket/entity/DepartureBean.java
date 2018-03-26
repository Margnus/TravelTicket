package com.travel.ticket.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 李小凡 on 2018/2/1.
 */

public class DepartureBean implements Serializable{

    /**
     * "adultCheckIn": 0,
     "capacity": 0,
     "carrierShipId": "string",
     "checkIn": 0,
     "childCheckIn": 0,
     * capacity : 0
     * carrierShipId : string
     * checkIn : 0
     * cruise : {"id":"string","name":"string"}
     * cruisePlanId : string
     * departureDate : 2018-02-01T02:27:15.015Z
     * departureTimeFrom : 2018-02-01T02:27:15.015Z
     * departureTimeTo : 2018-02-01T02:27:15.015Z
     * docker : {"id":"string","name":"string"}
     * leaveAt : 2018-02-01T02:27:15.015Z
     * loop : true
     * operator : string
     * sailingStatus : sailing
     * sold : 0
     */

    @SerializedName("capacity")
    private int capacity;
    @SerializedName("carrierShipId")
    private String carrierShipId;
    @SerializedName("checkIn")
    private int checkIn;
    @SerializedName("cruise")
    private CruiseBean cruise;
    @SerializedName("cruisePlanId")
    private String cruisePlanId;
    @SerializedName("departureDate")
    private String departureDate;
    @SerializedName("departureTimeFrom")
    private String departureTimeFrom;
    @SerializedName("departureTimeTo")
    private String departureTimeTo;
    @SerializedName("docker")
    private DockerBean docker;
    @SerializedName("leaveAt")
    private String leaveAt;
    @SerializedName("loop")
    private boolean loop;
    @SerializedName("operator")
    private String operator;
    @SerializedName("sailingStatus")
    private String sailingStatus;
    @SerializedName("sold")
    private int sold;
    @SerializedName("adultCheckIn")
    private int adultCheckIn;
    @SerializedName("childCheckIn")
    private int childCheckIn;

    public int getAdultCheckIn() {
        return adultCheckIn;
    }

    public void setAdultCheckIn(int adultCheckIn) {
        this.adultCheckIn = adultCheckIn;
    }

    public int getChildCheckIn() {
        return childCheckIn;
    }

    public void setChildCheckIn(int childCheckIn) {
        this.childCheckIn = childCheckIn;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getCarrierShipId() {
        return carrierShipId;
    }

    public void setCarrierShipId(String carrierShipId) {
        this.carrierShipId = carrierShipId;
    }

    public int getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(int checkIn) {
        this.checkIn = checkIn;
    }

    public CruiseBean getCruise() {
        return cruise;
    }

    public void setCruise(CruiseBean cruise) {
        this.cruise = cruise;
    }

    public String getCruisePlanId() {
        return cruisePlanId;
    }

    public void setCruisePlanId(String cruisePlanId) {
        this.cruisePlanId = cruisePlanId;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureTimeFrom() {
        return departureTimeFrom;
    }

    public void setDepartureTimeFrom(String departureTimeFrom) {
        this.departureTimeFrom = departureTimeFrom;
    }

    public String getDepartureTimeTo() {
        return departureTimeTo;
    }

    public void setDepartureTimeTo(String departureTimeTo) {
        this.departureTimeTo = departureTimeTo;
    }

    public DockerBean getDocker() {
        return docker;
    }

    public void setDocker(DockerBean docker) {
        this.docker = docker;
    }

    public String getLeaveAt() {
        return leaveAt;
    }

    public void setLeaveAt(String leaveAt) {
        this.leaveAt = leaveAt;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getSailingStatus() {
        return sailingStatus;
    }

    public void setSailingStatus(String sailingStatus) {
        this.sailingStatus = sailingStatus;
    }

    public static class CruiseBean {
        /**
         * id : string
         * name : string
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

    public static class DockerBean {
        /**
         * id : string
         * name : string
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

package com.travel.ticket.entity;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 李小凡 on 2018/3/26.
 */

public class MyCorpsResult {

    /**
     * corps : {"id":"string","name":"string"}
     * workDate : 2018-03-26
     */

    @SerializedName("corps")
    private CorpsBean corps;
    @SerializedName("workDate")
    private String workDate;
    private DateFormat dateFormat = new SimpleDateFormat("MM月dd日 EE");
    private DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

    public CorpsBean getCorps() {
        return corps;
    }

    public void setCorps(CorpsBean corps) {
        this.corps = corps;
    }

    public String getWorkDate() {
        Date dateTrans = new Date();
        try {
            dateTrans = format1.parse(workDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat.format(dateTrans);
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public static class CorpsBean {
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

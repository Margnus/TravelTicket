package com.travel.ticket.util;

import com.example.http.HttpUtils;
import com.travel.ticket.entity.DepartureBean;
import com.travel.ticket.entity.PortResult;
import com.travel.ticket.entity.StringBean;
import com.travel.ticket.entity.TokenBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface HttpClient {

    class Builder {
        public static HttpClient getTravelService() {
            return HttpUtils.getInstance().getTravelServer(HttpClient.class);
        }
    }

    /**
     * 登录
     *
     * @param clientId
     * @param secret
     * @param type
     * @param id
     * @param pwd
     * @return
     */
    @FormUrlEncoded
    @POST("oauth/sys/oauth/token")
    Observable<TokenBean> login(@Field("client_id") String clientId, @Field("client_secret") String secret,
                                @Field("grant_type") String type, @Field("scope") String scope, @Field("username") String id,
                                @Field("password") String pwd);


    /**
     * 获取今日出发查验航班
     *
     * @param dockerId
     * @param cruiseId
     * @param from
     * @param to
     * @param corporationId
     * @param cruisePlanId
     * @return
     */
    @GET("api/sys/checking/today/departure")
    Observable<List<DepartureBean>> getDeparture();

    /**
     * 获取今日出发查验航班
     *
     * @param dockerId
     * @param cruiseId
     * @param cruisePlanId
     * @return
     */
    @FormUrlEncoded
    @POST("api/sys/checking/today/departure")
    Observable<List<DepartureBean>> postDeparture(@Field("dockerId") String dockerId, @Field("cruiseId") String cruiseId,
                                                  @Field("cruisePlanId") String cruisePlanId);

    /**
     * 开始查验
     *
     * @param ship
     * @return
     */
    @FormUrlEncoded
    @POST("api/sys/checking/{carrierOfShip}/begin/checkin")
    Observable<StringBean> checkIn(@Path("carrierOfShip") String ship);

    /**
     * 发航
     *
     * @param ship
     * @return
     */
    @FormUrlEncoded
    @POST("api/sys/checking/{carrierOfShip}/begin/sailing")
    Observable<StringBean> sailing(@Path("carrierOfShip") String ship);

    /**
     * 取消查验
     *
     * @param ship
     * @return
     */
    @FormUrlEncoded
    @POST("api/sys/checking/{carrierOfShip}/cancel/checkin")
    Observable<StringBean> cancelCheckin(@Path("carrierOfShip") String ship);


    /**
     * 取消发航
     *
     * @param ship
     * @return
     */
    @FormUrlEncoded
    @POST("api/sys/checking/{carrierOfShip}/cancel/sailing")
    Observable<StringBean> cancelSailing(@Path("carrierOfShip") String ship);


    /**
     * 发航
     *
     * @param ship
     * @return
     */
    @FormUrlEncoded
    @POST("api/sys/checking/{ticketNo}/ticket")
    Observable<StringBean> ticketNo(@Path("ticketNo") String ship);

    /**
     * 获取全部码头id
     *
     * @return
     */
    @GET("api/sys/checking/my/docker")
    Observable<List<PortResult>> docker();

}
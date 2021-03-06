package com.travel.ticket.util;

import android.app.Activity;
import android.content.Intent;

import com.travel.ticket.MainActivity;
import com.travel.ticket.entity.TokenBean;

/**
 * Created by 李小凡 on 2018/2/5.
 */

public class AccountUtil {
    public static void login(TokenBean result) {
        if (result != null) {
            PreferenceUtil preferenceUtil = PreferenceUtil.getInstance();
            preferenceUtil.saveString(TokenBean.ACCESS_TOKEN, result.getAccessToken());
            preferenceUtil.saveString(TokenBean.REFRESH_TOKEN, result.getRefreshToken());
            preferenceUtil.saveString(TokenBean.TOKEN_TYPE, result.getTokenType());
            preferenceUtil.saveInt(TokenBean.EXPIRES_IN, result.getExpiresIn());
        }
    }

    public static void logout() {
        PreferenceUtil preferenceUtil = PreferenceUtil.getInstance();
        preferenceUtil.remove(TokenBean.ACCESS_TOKEN);
        preferenceUtil.remove(TokenBean.TOKEN_TYPE);
        preferenceUtil.remove(TokenBean.EXPIRES_IN);
    }

    public static void reLogin(Activity activity) {
        PreferenceUtil preferenceUtil = PreferenceUtil.getInstance();
        preferenceUtil.remove(TokenBean.ACCESS_TOKEN);
        preferenceUtil.remove(TokenBean.TOKEN_TYPE);
        preferenceUtil.remove(TokenBean.EXPIRES_IN);

        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }
}

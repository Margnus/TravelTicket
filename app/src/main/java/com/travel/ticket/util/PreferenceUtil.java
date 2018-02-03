package com.travel.ticket.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {

    private static PreferenceUtil mInstance;

    private PreferenceUtil(Context context) {
        init(context);
    }

    public static PreferenceUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (PreferenceUtil.class) {
                if (mInstance == null) {
                    mInstance = new PreferenceUtil(context);
                }
            }
            mInstance.init(context);
        }
        return mInstance;
    }

    private SharedPreferences sp;

    private SharedPreferences.Editor ed;


    public void init(Context context) {
        if (sp == null || ed == null) {
            try {
                sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
                ed = sp.edit();
            } catch (Exception e) {
            }
        }
    }

    public void saveLong(String key, long l) {
        ed.putLong(key, l);
        ed.commit();
    }

    public long getLong(String key, long defaultlong) {
        return sp.getLong(key, defaultlong);
    }

    public void saveBoolean(String key, boolean value) {
        ed.putBoolean(key, value);
        ed.commit();
    }

    public boolean getBoolean(String key, boolean defaultboolean) {
        return sp.getBoolean(key, defaultboolean);
    }

    public void saveInt(String key, int value) {
        if (ed != null) {
            ed.putInt(key, value);
            ed.commit();
        }
    }

    public int getInt(String key, int defaultInt) {
        return sp.getInt(key, defaultInt);
    }

    public String getString(String key, String defaultInt) {
        return sp.getString(key, defaultInt);
    }

    public String getString(Context context, String key, String defaultValue) {
        if (sp == null || ed == null) {
            sp = context.getSharedPreferences("sp", 0);
            ed = sp.edit();
        }
        if (sp != null) {
            return sp.getString(key, defaultValue);
        }
        return defaultValue;
    }

    public void saveString(String key, String value) {
        ed.putString(key, value);
        ed.commit();
    }

    public void remove(String key) {
        ed.remove(key);
        ed.commit();
    }

    public void destroy() {
        sp = null;
        ed = null;
        mInstance = null;
    }

}

package com.xinqidian.adcommon.util;
import android.content.Context;
import android.content.SharedPreferences;

import com.xinqidian.adcommon.app.AppConfig;
import com.xinqidian.adcommon.app.LiveBusConfig;
import com.xinqidian.adcommon.bus.LiveDataBus;


/**
 * SharedPreferences的一个工具类，调用setParam就能保存String, Integer, Boolean, Float, Long类型的参数
 * 同样调用getParam就能获取到保存在手机里面的数据
 * @author xiaanming
 *
 */
public class SharedPreferencesUtil {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_date";


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param key
     * @param object
     */
    public static void setParam(String key, Object object){

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = Common1.getApplication().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(key, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(key, (Long)object);
        }

        editor.commit();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = Common1.getApplication().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if("String".equals(type)){
            return sp.getString(key, (String)defaultObject);
        }
        else if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return sp.getFloat(key, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return sp.getLong(key, (Long)defaultObject);
        }

        return null;
    }


    public static void setCount(int i){
        setParam(AppConfig.count,i);
    }

    public static int getCount(){
       return (int) getParam(AppConfig.count,1);
    }



    /**
     * 退出登录
     */
    public static void exitLogin(){
        setParam(AppConfig.cooike,false);
        setParam(AppConfig.vip,false);
        LiveDataBus.get().with(LiveBusConfig.login,Boolean.class).postValue(false);

    }

    /**
     * 退出登录
     */
    public static void exitLoginNotSend(){
        setParam(AppConfig.cooike,false);
        setParam(AppConfig.vip,false);

    }


    /**
     * 支付宝支付成功之后回调
     */
    public static void alipaySuccess(){
        setParam(AppConfig.alipay,true);
        setVip(true);
        LiveDataBus.get().with(LiveBusConfig.alipaySuccess,Boolean.class).postValue(true);

    }


    /**
     * 设置vip
     */
    public static void setVip(boolean vip){
        setParam(AppConfig.vip,vip);
    }


    /**
     * 判断是否是vip
     * @return
     */
    public static boolean isVip(){
        return (boolean) getParam(AppConfig.vip,false);
    }


    /**
     * 登录成功设置
     */
    public static void toLogin(){
        setParam(AppConfig.cooike,true);
        LiveDataBus.get().with(LiveBusConfig.login,Boolean.class).postValue(true);

    }

    /**
     * 设置登录类型
     */
    public static void setLoginType(boolean isWX) {

        setParam(AppConfig.loginType, isWX);

    }

    /**
     * 获取登录类型
     */
    public static boolean getLoginType() {

        boolean param = (boolean) getParam(AppConfig.loginType,false);
        return param;

    }


    /**
     * 判断是否登录
     * @return
     */
    public static boolean isLogin(){
        return (boolean) getParam(AppConfig.cooike,false);
    }


}
